package com.hypergram.loginapp.repository;

import com.hypergram.loginapp.model.ERole;
import com.hypergram.loginapp.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}