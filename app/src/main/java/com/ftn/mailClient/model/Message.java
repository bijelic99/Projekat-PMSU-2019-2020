package com.ftn.mailClient.model;

import androidx.room.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ftn.mailClient.utill.deserializer.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;
@Entity
public class Message extends Identifiable {
    private Long account;
    private Contact from;
    private List<Contact> to;
    private List<Contact> cc;
    private List<Contact> bcc;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;
    private String subject;
    private String content;
    private List<Attachment> attachments;
    private List<Tag> tags;
    private Boolean unread;

    public Message(){
        this(null, null, null, null, null, null, null, null, null, true);
    }

    public Message(Long id, Long account, Contact from, List<Contact> to, List<Contact> cc, List<Contact> bcc, LocalDateTime dateTime, String subject, String content, Boolean unread) {
        super(id);
        this.account = account;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
        this.unread = unread;
    }

    public Message(Long account, Contact from, List<Contact> to, List<Contact> cc, List<Contact> bcc, LocalDateTime dateTime, String subject, String content) {
        super(null);
        this.account = account;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Contact getFrom() {
        return from;
    }

    public void setFrom(Contact from) {
        this.from = from;
    }

    public List<Contact> getTo() {
        return to;
    }

    public void setTo(List<Contact> to) {
        this.to = to;
    }

    public List<Contact> getCc() {
        return cc;
    }

    public void setCc(List<Contact> cc) {
        this.cc = cc;
    }

    public List<Contact> getBcc() {
        return bcc;
    }

    public void setBcc(List<Contact> bcc) {
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }
}
