package com.pm.productMail.batch.processors;

import com.pm.productMail.entities.Product;
import com.pm.productMail.entities.State;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ProductProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String date1 = format.format(item.getExpirationDate());
        String date2 = format.format(new Date());

        java.sql.Date sqlDate1 = java.sql.Date.valueOf(date1);
        java.sql.Date sqlDate2 = java.sql.Date.valueOf(date2);
        LocalDate ld1 = sqlDate1.toLocalDate();
        LocalDate ld2 = sqlDate2.toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(ld1, ld2);


        boolean isExpired = ((date1.compareTo(date2) == 0)) ? true : false;
        boolean isAlmostExpired = ((daysBetween == 5)) ? true : false;

        if(isExpired){
            item.setState((State.EXPIRED).toString());
            return item;
        }

        if(isAlmostExpired){
            item.setState((State.ALMOSTEXPIRED).toString());
            return item;
        }
        return null;
    }
}
