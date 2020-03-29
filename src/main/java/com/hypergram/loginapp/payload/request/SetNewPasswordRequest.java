package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class SetNewPasswordRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String questionId;
    @NotEmpty
    private String answer;
    @NotBlank
    private String password1;
    @NotBlank
    private String password2;
    public String getUsername() {
        return username;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
