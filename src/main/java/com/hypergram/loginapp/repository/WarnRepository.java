package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.model.Warn;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WarnRepository extends MongoRepository<Warn,String> {

    Optional<Warn> findById(String id);

    Optional<List<Warn>> findAllByUser(User user);


}
