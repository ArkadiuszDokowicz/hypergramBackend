package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class AddWarnRequest {
    @NotEmpty
    String username;
    @NotEmpty
    String reason;
    @NotEmpty
    String message;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }
}
