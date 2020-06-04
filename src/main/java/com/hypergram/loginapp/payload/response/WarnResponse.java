package com.hypergram.loginapp.payload.response;

import java.util.Date;

public class WarnResponse {
    private Date date;
    private String message;
    private String reason;

    public WarnResponse(Date date, String message, String reason) {
        this.date = date;
        this.message = message;
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
