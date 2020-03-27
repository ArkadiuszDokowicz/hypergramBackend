package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotBlank;

public class TestRequest {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
