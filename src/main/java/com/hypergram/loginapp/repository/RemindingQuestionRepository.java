package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.RemindingQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RemindingQuestionRepository extends MongoRepository<RemindingQuestion,String> {
}
