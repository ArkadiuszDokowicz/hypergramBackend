package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.Ban;
import com.hypergram.loginapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BanRepository extends MongoRepository<Ban,String> {
    Optional<User> findByUser(User user);
}

