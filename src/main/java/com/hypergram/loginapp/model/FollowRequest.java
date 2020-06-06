package com.hypergram.loginapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document( collection = "followRequests")
public class FollowRequest {
    @Id
    private String id;
    @NotEmpty
    private User user;
    @NotEmpty
    private String asker;
    @DateTimeFormat
    private Date date;

    public FollowRequest(@NotEmpty User user, @NotEmpty String asker) {
        this.user = user;
        this.asker = asker;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.date=date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAsker() {
        return asker;
    }

    public void setAsker(String asker) {
        this.asker = asker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
