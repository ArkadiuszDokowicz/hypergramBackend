package com.hypergram.loginapp.payload.response;

import com.hypergram.loginapp.model.Question;
import com.hypergram.loginapp.model.RemindingQuestion;

import java.util.ArrayList;
import java.util.List;

public class RemindingQuestionsListResponse {

    private int questionAmount = 0;
    private List<Question> questions = new ArrayList<>();

    public RemindingQuestionsListResponse(List<RemindingQuestion> dbQuestions){
        for(RemindingQuestion rq:dbQuestions){
            questions.add(new Question(rq.getId(),rq.getQuestion()));
        }
        questionAmount=dbQuestions.size();
    }
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }
}
