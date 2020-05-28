package com.ftn.mailClient.model;


public class Account extends Identifiable {
    private String smtpAdress;
    private IncomingMailProtocol incomingMailProtocol;
    private String incomingMailAddress;
    private String username;
    private String password;

    public  Account(){
        this(null,null,null,null,null,null);
    }

    public Account(Integer id, String smtpAdress, IncomingMailProtocol incomingMailProtocol, String incomingMailAddress, String username, String password) {
        super(id);
        this.smtpAdress = smtpAdress;
        this.incomingMailProtocol = incomingMailProtocol;
        this.incomingMailAddress = incomingMailAddress;
        this.username = username;
        this.password = password;
    }


    public String getSmtpAdress() {
        return smtpAdress;
    }

    public void setSmtpAdress(String smtpAdress) {
        this.smtpAdress = smtpAdress;
    }

    public IncomingMailProtocol getIncomingMailProtocol() {
        return incomingMailProtocol;
    }

    public void setIncomingMailProtocol(IncomingMailProtocol incomingMailProtocol) {
        this.incomingMailProtocol = incomingMailProtocol;
    }

    public String getIncomingMailAddress() {
        return incomingMailAddress;
    }

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
