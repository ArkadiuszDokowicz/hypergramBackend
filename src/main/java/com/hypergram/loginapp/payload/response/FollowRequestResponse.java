package com.hypergram.loginapp.payload.response;

import java.util.Date;

public class FollowRequestResponse {
    private String id;
    private String asker;
    private Date date;

    public FollowRequestResponse(String id, String asker, Date date) {
        this.id = id;
        this.asker = asker;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
