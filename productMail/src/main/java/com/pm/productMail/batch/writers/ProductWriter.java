package com.pm.productMail.batch.writers;

import com.pm.productMail.entities.EmailEntity;
import com.pm.productMail.entities.Product;
import com.pm.productMail.services.MailSender;
import com.pm.productMail.services.MailSenderImpl;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

public class ProductWriter implements ItemWriter<Product> {






    final  private MailSender mailSender;

    public ProductWriter(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void write(List<? extends Product> products) throws Exception {



        //Pour chaque produit on va generer le contenu du mail
        for(Product product : products)
        {
            EmailEntity emailEntity = new EmailEntity();
            this.mailSender.send(emailEntity,product);
        }

    }
}
