package com.ftn.mailClient.model;

import androidx.room.Entity;
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

import java.util.HashSet;
import java.util.Set;

@Entity
public class Folder extends Identifiable {
    private String name;
    @JsonDeserialize(using = ParentFolderDeserializer.class)
    @JsonSerialize(using = ParentFolderSerializer.class)
    @TypeConverters(FolderTypeConverter.class)
    private Folder parentFolder;
    @JsonDeserialize(using = InnerFoldersDeserializer.class)
    @JsonSerialize(using = InnerFoldersSerializer.class)
    @TypeConverters(FolderListTypeConverter.class)
    private Set<Folder> folders;
    @TypeConverters(MessageListTypeConverter.class)
    private Set<Message> messages;

    public Folder() {
        this(null, null, null, new HashSet<>(), null);
    }

    public Folder(Long id, String name, Folder parentFolder, Set<Folder> folders, Set<Message> messages) {
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


    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }


    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
