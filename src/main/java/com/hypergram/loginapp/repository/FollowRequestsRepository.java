package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.FollowRequest;
import com.hypergram.loginapp.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
public interface FollowRequestsRepository extends MongoRepository<FollowRequest,String> {

    Optional<List<FollowRequest>> findAllByUser(User user);
    Optional<FollowRequest> findById(String id);
}
