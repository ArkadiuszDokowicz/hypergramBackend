package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PasswordQuestionAddRequest {
    @NotEmpty
    String username;
    @NotEmpty
    String password;
    @NotEmpty
    @Size(min = 6, max = 60)
    String question;
    @NotEmpty
    @Size(min = 6, max = 60)
    String answer;
    @NotEmpty
    @Size(min = 6, max = 60)
    String answer2;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "PasswordQuestionRequest{" +
                "username='" + username + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
