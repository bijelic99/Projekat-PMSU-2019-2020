package com.ftn.mailClient.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Account extends Identifiable {
    private String smtpAdress;
    private IncomingMailProtocol incomingMailProtocol;
    private String incomingMailAddress;
    private String username;
    private String password;

    public  Account(){
        this(null,null,null,null,null,null);
    }

    public Account(Long id, String smtpAdress, IncomingMailProtocol incomingMailProtocol, String incomingMailAddress, String username, String password) {
        super(id);
        this.smtpAdress = smtpAdress;
        this.incomingMailProtocol = incomingMailProtocol;
        this.incomingMailAddress = incomingMailAddress;
        this.username = username;
        this.password = password;
    }

    @JsonProperty("smtp")
    public String getSmtpAdress() {
        return smtpAdress;
    }

    @JsonSetter("smtp")
    public void setSmtpAdress(String smtpAdress) {
        this.smtpAdress = smtpAdress;
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
}
