package com.pm.productMail.services;

import com.pm.productMail.dao.ClientRepository;
import com.pm.productMail.dao.EmailRepository;
import com.pm.productMail.entities.Client;
import com.pm.productMail.entities.EmailEntity;
import com.pm.productMail.entities.Product;
import com.pm.productMail.services.utils.MailSenderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class MailSenderImpl implements MailSender{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Value("${spring.mail.username}")
    private String fromAddr;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    @Override
    public void send(EmailEntity emailEntity, Product product) throws MessagingException, UnsupportedEncodingException {
        //Encapsuler le contenu du mail dans un mimeMessage
        MimeMessage message = javaMailSender.createMimeMessage();

        //Configurer l'envoi du mail avec un mimeMessageHelper
        MimeMessageHelper helper = new MimeMessageHelper(message);



        if(product.getState() == "ALMOSTEXPIRED") {
            Client client = this.clientRepository.findById(product.getClient().getId()).get();
            String mailSubject = client.getFirstName() + " " + client.getLastName() + ", votre alerte d'expiration de produit";
            String mailContent = "<p><b> Sender name: </b> " + client.getFirstName() + " " + client.getLastName() + "</p>";
            mailContent += "<p><b> Sender email: </b> " + fromAddr + "</p>";
            mailContent += "<p><b> Subject: </b> " + mailSubject + "</p>";
            mailContent += "<p><b> Content: </b> " + "Le produit " + product.getName() + " (" + product.getSerialNumber() + ") expire dans 5 jours" + "</p>";

            emailEntity.setSubject(mailSubject);
            emailEntity.setFromAddr(fromAddr);
            emailEntity.setToAddr(client.getEmail());
            emailEntity.setContent(mailContent);
            emailEntity.setSendingDate(new Date());
            emailEntity.setClient(product.getClient());

            helper.setTo(emailEntity.getToAddr());
            helper.setSubject(emailEntity.getSubject());
            helper.setText(emailEntity.getContent());
            helper.setFrom(emailEntity.getFromAddr(),"My");
            helper.setText(emailEntity.getContent(), true);

        }

        if(product.getState() == "EXPIRED") {
            Client client = this.clientRepository.findById(product.getClient().getId()).get();
            String mailSubject = client.getFirstName() + " " + client.getLastName() + ", votre alerte d'expiration de produit";
            String mailContent = "<p><b> Sender name: </b> " + client.getFirstName() + " " + client.getLastName() + "</p>";
            mailContent += "<p><b> Sender email: </b> " + fromAddr + "</p>";
            mailContent += "<p><b> Subject: </b> " + mailSubject + "</p>";
            mailContent += "<p><b> Content: </b> " + "Le produit " + product.getName() + " (" + product.getSerialNumber() + ") vient d'expirer" + "</p>";

            emailEntity.setSubject(mailSubject);
            emailEntity.setFromAddr(fromAddr);
            emailEntity.setToAddr(client.getEmail());
            emailEntity.setContent(mailContent);
            emailEntity.setSendingDate(new Date());
            emailEntity.setClient(product.getClient());

            helper.setTo(emailEntity.getToAddr());
            helper.setSubject(emailEntity.getSubject());
            helper.setText(emailEntity.getContent());
            helper.setFrom(emailEntity.getFromAddr(),"My");
            helper.setText(emailEntity.getContent(), true);

        }




        this.emailRepository.save(emailEntity);

        // Call the thread to send mail
        this.executor.setCorePoolSize(5);
        this.executor.setMaximumPoolSize(10);

        //Here we are sending mail quickly with multi-threading
         this.executor.execute(new MailSenderTask(javaMailSender,message));

    }
}
