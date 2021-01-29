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

        this.loadData();
    }

    private void loadData()
    {
        Client client = new Client();
        client.setEmail("elimanefofana17@gmail.com");
        client.setFirstName("Elimane");
        client.setLastName("Fofana");


        Product product = new Product();
        product.setName("COCACOLA");
        product.setExpirationDate(new Date());
        product.setSerialNumber("1122AZ");
        product.setState((State.EDIBLE).toString());

        product.setClient(client);
        client.getProducts().add(product);



        this.productRepository.save(product);
        this.clientRepository.save(client);
    }
}
