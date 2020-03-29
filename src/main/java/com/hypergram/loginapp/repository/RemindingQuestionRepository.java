package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.RemindingQuestion;
import com.hypergram.loginapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RemindingQuestionRepository extends MongoRepository<RemindingQuestion,String> {

    Optional<RemindingQuestion> findByQuestion();
    Optional<RemindingQuestion> findByUser();
    List<RemindingQuestion> findAllByUser(User user);
    Optional<RemindingQuestion> findById(String id);

    @Override
    void delete(RemindingQuestion remindingQuestion);
}
