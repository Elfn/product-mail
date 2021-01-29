package com.pm.productMail.services.utils;

import com.pm.productMail.services.utils.AbstractTask;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

public class MailSenderTask extends AbstractTask {

    final private JavaMailSender javaMailSender;
    private MimeMessage mimeMessage;

    public MailSenderTask(JavaMailSender javaMailSender, MimeMessage mimeMessage) {
        this.javaMailSender = javaMailSender;
        this.mimeMessage = mimeMessage;
    }

    @Override
    public void send() {
       if(this.mimeMessage != null)
        {
            javaMailSender.send(this.mimeMessage);
        }
    }
}
