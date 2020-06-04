package com.hypergram.loginapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;


@Document(collection = "warns")
public class Warn {
    @Id
    private String id;
    @DateTimeFormat
    Date date;
    @DBRef
    private User user;
    @NotBlank
    @Size(max = 120)
    private String message;
    @Size(max = 120)
    private String reason;


    public Warn() {

    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public Warn(User user, @NotBlank @Size(max = 120) String message, @Size(max = 120) String reason) {
        this.user = user;
        this.message = message;
        this.reason = reason;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.date=date;
    }

}
