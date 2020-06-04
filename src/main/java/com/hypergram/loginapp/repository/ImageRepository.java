package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.ImageDB;
import com.hypergram.loginapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends MongoRepository<ImageDB,String> {
    Optional<ImageDB> findById(String id);
    Optional<ImageDB> findByUser(User user);
    Optional<List<ImageDB>> findAllByUser(User user);
}

