package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class BanRequest {
    @NotEmpty
    String username;
    @NotEmpty
    String reason;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
