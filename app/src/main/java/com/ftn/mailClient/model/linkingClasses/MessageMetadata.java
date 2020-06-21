package com.ftn.mailClient.model.linkingClasses;

import androidx.room.Ignore;

import com.ftn.mailClient.model.Identifiable;
import com.ftn.mailClient.model.Message;

import java.time.LocalDateTime;

public class MessageMetadata extends Identifiable {
    private String subject;
    private Integer numberOfTags;
    private String content;
    private LocalDateTime dateTime;

    @Ignore
    public MessageMetadata(){

    }

    public MessageMetadata(Long id, String subject, Integer numberOfTags, String content, LocalDateTime dateTime){
        this.id = id;
        this.subject = subject;
        this.numberOfTags = numberOfTags;
        this.content = content;
        this.dateTime = dateTime;
    }

    @Ignore
    public MessageMetadata(Message message){
        subject = message.getSubject();
        numberOfTags = message.getTags().size();
        content = message.getContent();
        dateTime = message.getDateTime();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getNumberOfTags() {
        return numberOfTags;
    }

    public void setNumberOfTags(Integer numberOfTags) {
        this.numberOfTags = numberOfTags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
