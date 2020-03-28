package com.hypergram.loginapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document(collection = "questions")
public class RemindingQuestion {
    @Id
    private String id;

    @DBRef
    private User user;

    @NotEmpty
    @Size(max = 50)
    private String question;

    @NotEmpty
    @Size(max = 50)
    private String answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public RemindingQuestion(User user, @NotEmpty @Size(max = 50) String question, @NotEmpty @Size(max = 50) String answer) {
        this.user = user;
        this.question = question;
        this.answer = answer;
    }
}
