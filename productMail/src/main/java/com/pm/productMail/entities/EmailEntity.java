package com.pm.productMail.entities;


import javax.persistence.*;
import java.util.Date;


@Entity()
@Table(name = "EMAIL")
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    private String fromAddr;
    private String toAddr;
    private String content;
    private Date sendingDate;

    @ManyToOne
    private Client client;

    public EmailEntity() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String from) {
        this.fromAddr = from;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String to) {
        this.toAddr = to;
    }

    public Date getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(Date sendingDate) {
        this.sendingDate = sendingDate;
    }
}
