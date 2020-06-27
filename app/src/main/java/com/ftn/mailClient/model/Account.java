package com.ftn.mailClient.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ftn.mailClient.utill.converters.FolderListTypeConverter;
import com.ftn.mailClient.utill.converters.FolderTypeConverter;
import com.ftn.mailClient.utill.converters.IncomingMailProtocolTypeConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Account extends Identifiable {
    private String smtpAddress;
    @TypeConverters(IncomingMailProtocolTypeConverter.class)
    private IncomingMailProtocol incomingMailProtocol;
    private String incomingMailAddress;
    private String username;
    private String password;
    @Ignore
    private List<FolderMetadata> accountFolders;

    @Ignore
    public  Account(){
        this(null,null,null,null,null,null, new ArrayList<>());
    }

    @Ignore
    public Account(Long id, String smtpAddress, IncomingMailProtocol incomingMailProtocol, String incomingMailAddress, String username, String password, List<FolderMetadata> accountFolders) {
        super(id);
        this.smtpAddress = smtpAddress;
        this.incomingMailProtocol = incomingMailProtocol;
        this.incomingMailAddress = incomingMailAddress;
        this.username = username;
        this.password = password;
        this.accountFolders = accountFolders;
    }

    public Account(Long id, String smtpAddress, IncomingMailProtocol incomingMailProtocol, String incomingMailAddress, String username, String password){
        super(id);
        this.smtpAddress = smtpAddress;
        this.incomingMailProtocol = incomingMailProtocol;
        this.incomingMailAddress = incomingMailAddress;
        this.username = username;
        this.password = password;
        this.accountFolders = new ArrayList<>();
    }

    @JsonProperty("smtp")
    public String getSmtpAddress() {
        return smtpAddress;
    }

    @JsonSetter("smtp")
    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    @JsonProperty("inServerType")
    public IncomingMailProtocol getIncomingMailProtocol() {
        return incomingMailProtocol;
    }

    @JsonSetter("inServerType")
    public void setIncomingMailProtocol(IncomingMailProtocol incomingMailProtocol) {
        this.incomingMailProtocol = incomingMailProtocol;
    }

    @JsonProperty("inServer")
    public String getIncomingMailAddress() {
        return incomingMailAddress;
    }
    @JsonSetter("inServer")
    public void setIncomingMailAddress(String incomingMailAddress) {
        this.incomingMailAddress = incomingMailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FolderMetadata> getAccountFolders() {
        return accountFolders;
    }

    public void setAccountFolders(List<FolderMetadata> accountFolders) {
        this.accountFolders = accountFolders;
    }

    @NonNull
    @Override
    public String toString() {
        return username != null ? username : "";
    }
}
