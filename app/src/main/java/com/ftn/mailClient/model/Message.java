package com.ftn.mailClient.model;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Identifiable {
    private Account from;
    private List<Account> to;
    private List<Account> cc;
    private List<Account> bcc;
    private LocalDateTime dateTime;
    private String subject;
    private String content;
    private List<Attachment> attachments;
    private List<Tag> tags;

    public Message(){
        this(null, null, null, null, null, null, null, null);
    }

    public Message(Account from, List<Account> from1, List<Account> to, List<Account> cc, List<Account> bcc, LocalDateTime dateTime, String subject, String content) {
        this.from = from;
        this.from = from1;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(List<Account> from) {
        this.from = from;
    }

    public List<Account> getTo() {
        return to;
    }

    public void setTo(List<Account> to) {
        this.to = to;
    }

    public List<Account> getCc() {
        return cc;
    }

    public void setCc(List<Account> cc) {
        this.cc = cc;
    }

    public List<Account> getBcc() {
        return bcc;
    }

    public void setBcc(List<Account> bcc) {
        this.bcc = bcc;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public void setFrom(Account from) {
        this.from = from;
    }
}
