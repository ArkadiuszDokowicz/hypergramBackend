package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotBlank;

public class ChangePasswdRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordNew1;

    @NotBlank
    private String passwordNew2;


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

    public String getPasswordNew1() {
        return passwordNew1;
    }

    public void setPasswordNew1(String passwordNew1) {
        this.passwordNew1 = passwordNew1;
    }

    public String getPasswordNew2() {
        return passwordNew2;
    }

    public void setPasswordNew2(String passwordNew2) {
        this.passwordNew2 = passwordNew2;
    }

    @Override
    public String toString() {
        return "ChangePasswdRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordNew1='" + passwordNew1 + '\'' +
                ", passwordNew2='" + passwordNew2 + '\'' +
                '}';
    }
}
