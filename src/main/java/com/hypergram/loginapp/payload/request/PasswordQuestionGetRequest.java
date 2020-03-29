package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class PasswordQuestionGetRequest {
    @NotEmpty
    String username;
    @NotEmpty
    String password;

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
