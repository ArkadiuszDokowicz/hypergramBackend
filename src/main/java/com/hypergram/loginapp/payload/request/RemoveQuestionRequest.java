package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class RemoveQuestionRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String questionId;

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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
