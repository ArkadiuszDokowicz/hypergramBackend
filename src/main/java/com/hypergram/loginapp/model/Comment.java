package com.hypergram.loginapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "comments")
public class Comment {
    @Id
    String id;
    @NotEmpty
    String imageId;
    @NotEmpty
    String comment;
    @NotEmpty
    String username;
    @DateTimeFormat
    Date date;

    public Comment(String imageId, String comment, String username) {
        this.imageId=imageId;
        this.comment = comment;
        this.username = username;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.date=date;
    }

    public String getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public Date getDate() {
        return date;
    }
}
