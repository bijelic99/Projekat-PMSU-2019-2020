package com.ftn.mailClient.model;

import androidx.room.Ignore;

import java.io.Serializable;

public class FolderMetadata implements Serializable {
    private Long id;
    private String name;
    private Integer numberOfFolders;
    private Integer numberOfMessages;

    @Ignore
    public FolderMetadata(){

    }

    public FolderMetadata(Long id, String name, Integer numberOfFolders, Integer numberOfMessages) {
        this.id = id;
        this.name = name;
        this.numberOfFolders = numberOfFolders;
        this.numberOfMessages = numberOfMessages;
    }

    @Ignore
    public FolderMetadata(Folder folder){
        this.id = folder.getId();
        this.name = folder.getName();
        this.numberOfFolders = folder.getFolders().size();
        this.numberOfMessages = folder.getMessages().size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfFolders() {
        return numberOfFolders;
    }

    public void setNumberOfFolders(Integer numberOfFolders) {
        this.numberOfFolders = numberOfFolders;
    }

    public Integer getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(Integer numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }
}
