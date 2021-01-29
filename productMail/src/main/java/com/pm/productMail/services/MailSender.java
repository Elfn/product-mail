package com.pm.productMail.services;

import com.pm.productMail.entities.EmailEntity;
import com.pm.productMail.entities.Product;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailSender {

    public void send(EmailEntity emailEntity, Product product) throws MessagingException, UnsupportedEncodingException;

}
