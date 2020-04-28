package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findAllByimageId(String id);
    Optional<Comment> findById(String id);


}