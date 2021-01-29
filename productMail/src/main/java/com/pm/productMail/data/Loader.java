package com.pm.productMail.data;

import com.pm.productMail.dao.ClientRepository;
import com.pm.productMail.dao.EmailRepository;
import com.pm.productMail.dao.ProductRepository;
import com.pm.productMail.entities.Client;
import com.pm.productMail.entities.EmailEntity;
import com.pm.productMail.entities.Product;
import com.pm.productMail.entities.State;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
@Slf4j
public class Loader implements ApplicationListener<ContextRefreshedEvent> {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final EmailRepository emailRepository;

    public Loader(ClientRepository clientRepository, ProductRepository productRepository, EmailRepository emailRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.emailRepository = emailRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        try {
            this.loadData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws ParseException {
        Client client = new Client();
        client.setEmail("elimanefofana17@gmail.com");
        client.setFirstName("Elimane");
        client.setLastName("Fofana");


        Product product = new Product();
        Product product2 = new Product();
        product.setName("COCACOLA");
        product.setExpirationDate(new Date());
        product.setSerialNumber("1122AZ");
        product.setState((State.EDIBLE).toString());

        product2.setName("SEVENUP");
        product2.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-24"));
        product2.setSerialNumber("1177AR");
        product2.setState((State.EDIBLE).toString());

        product.setClient(client);
        client.getProducts().add(product);

        product2.setClient(client);
        client.getProducts().add(product2);



        this.productRepository.save(product);
        this.productRepository.save(product2);
        this.clientRepository.save(client);
    }
}
