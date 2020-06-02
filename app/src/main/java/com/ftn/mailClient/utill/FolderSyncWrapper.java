package com.ftn.mailClient.utill;

import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;

import java.util.ArrayList;
import java.util.List;

public class FolderSyncWrapper {
    private List<Message> messages = new ArrayList<>();
    private List<Folder> folders = new ArrayList<>();

    public FolderSyncWrapper() {
    }

    public FolderSyncWrapper(List<Message> messages, List<Folder> folders) {
        this.messages = messages;
        this.folders = folders;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
