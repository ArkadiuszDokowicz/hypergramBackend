package com.hypergram.loginapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "bans")
public class Ban {
    @Id
    private String id;
    @DateTimeFormat
    Date date;
    @DBRef
    private User user;
    @Size(max = 120)
    private String reason;


    public Ban(User user, @Size(max = 120) String reason) {
        this.user = user;
        this.reason = reason;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.date=date;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

