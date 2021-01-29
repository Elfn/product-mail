package com.pm.productMail.batch.configs;

import com.pm.productMail.batch.mappers.ProductRowMapper;
import com.pm.productMail.batch.processors.ProductProcessor;
import com.pm.productMail.batch.writers.ProductWriter;
import com.pm.productMail.entities.Product;
import com.pm.productMail.services.MailSender;
import org.quartz.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig extends QuartzJobBean {

    @Autowired
    MailSender mailSender;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource datasource;

    @Autowired
    public JobExplorer jobExplorer;

    @Autowired
    public JobLauncher jobLauncher;


    //To determine when we execute our job
    @Bean
    public Trigger trigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(20)//To launch our job every thirty seconds
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(BatchConfig.class)
                .storeDurably()
                .build();
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobParameters params =  new JobParametersBuilder(jobExplorer)
                    .getNextJobParameters(job(mailSender))//To auto increment job parameters
                    .toJobParameters();

            this.jobLauncher.run(job(mailSender),params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Bean
    public PagingQueryProvider queryProvider() throws Exception {

        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setSelectClause("select id, expiration_date, name, serial_number, state, client_id");
        factory.setFromClause( "from product");
        factory.setSortKey("id");
        factory.setDataSource(datasource);

        return factory.getObject();

    }

    @Bean
    @StepScope
    public ItemReader<Product> itemReader() throws Exception {

        JdbcPagingItemReader<Product> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(datasource);
        reader.setFetchSize(5);
        reader.setPageSize(5);
        reader.setQueryProvider(queryProvider());
        reader.setRowMapper(new ProductRowMapper());
        reader.afterPropertiesSet();
        return reader;

//        return new JdbcPagingItemReaderBuilder<Product>()
//                .dataSource(datasource)
//                .name("jdbccursoritemreader")
//                .queryProvider(queryProvider())
//                .rowMapper(new ProductRowMapper())
//                .pageSize(10)
//                .build();
    }

    @Bean
    public ItemProcessor<Product,Product> productProcessor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<? super Product> productWriter(MailSender mailSender) {
        return new ProductWriter(mailSender);
    }


    @Bean
    public Step chunkBasedStep(MailSender mailSender) throws Exception {
        return this.stepBuilderFactory.get("chunkbasedstep")
                .<Product, Product>chunk(10)
                .reader(itemReader())
                .processor(productProcessor())
                .writer(productWriter(mailSender))
                .build();
    }



    @Bean
    public Job job(MailSender mailSender) throws Exception {
        return  this.jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())//To increment jobparameters for scheduling with quartz
                .start(chunkBasedStep(mailSender))
                .build();
    }


}
