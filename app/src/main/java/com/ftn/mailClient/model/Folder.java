package com.ftn.mailClient.model;

import java.util.List;
import java.util.Set;

public class Folder extends Identifiable {
    private String name;
    private Long parentFolder;
    private Set<Long> folders;
    private Set<Message> messages;

    public Folder() {
        this(null, null, null, null, null);
    }

    public Folder(Long id, String name, Long parentFolder, Set<Long> folders, Set<Message> messages) {
        super(id);
        this.name = name;
        this.parentFolder = parentFolder;
        this.folders = folders;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Long parentFolder) {
        this.parentFolder = parentFolder;
    }

    public Set<Long> getFolders() {
        return folders;
    }

    public void setFolders(Set<Long> folders) {
        this.folders = folders;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
