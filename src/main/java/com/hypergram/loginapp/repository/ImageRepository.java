package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.ImageDB;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ImageRepository extends MongoRepository<ImageDB,String> {
    Optional<ImageDB> findById();
    Optional<ImageDB> findByUser();
}
