package com.ftn.mailClient.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ftn.mailClient.utill.converters.FolderListTypeConverter;
import com.ftn.mailClient.utill.converters.FolderTypeConverter;
import com.ftn.mailClient.utill.converters.MessageListTypeConverter;
import com.ftn.mailClient.utill.deserializer.InnerFoldersDeserializer;
import com.ftn.mailClient.utill.deserializer.ParentFolderDeserializer;
import com.ftn.mailClient.utill.serializer.InnerFoldersSerializer;
import com.ftn.mailClient.utill.serializer.ParentFolderSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Folder extends Identifiable {
    private String name;
    @TypeConverters(FolderTypeConverter.class)
    private FolderMetadata parentFolder;
    @TypeConverters(FolderListTypeConverter.class)
    private List<FolderMetadata> folders;
    @TypeConverters(MessageListTypeConverter.class)
    private List<Message> messages;

    @Ignore
    public Folder() {
        this(null, null, null, new ArrayList<>(), null);
    }

    public Folder(Long id, String name, FolderMetadata parentFolder, List<FolderMetadata> folders, List<Message> messages) {
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


    public FolderMetadata getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderMetadata parentFolder) {
        this.parentFolder = parentFolder;
    }


    public List<FolderMetadata> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderMetadata> folders) {
        this.folders = folders;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
