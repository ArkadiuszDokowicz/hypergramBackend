package com.hypergram.loginapp.userFeatures.services;

import com.hypergram.loginapp.model.FollowRequest;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.repository.FollowRequestsRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class FollowService {

    @Autowired
    UserRepository userRepository;


    @Autowired
    FollowRequestsRepository requestsRepository;

    public ResponseEntity<?> followUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> follower =userRepository.findByUsername(userDetails.getUsername());
        if(follower.isPresent()){
            if(!follower.get().isFollowingUser(username)){
                if(user.isPresent()){
                    if(user.get().isPrivateAccount()){
                        FollowRequest followRequest = new FollowRequest(user.get(),userDetails.getUsername());
                        requestsRepository.save(followRequest);
                        return  ResponseEntity.ok("Your follow request has been send");
                    }else{
                        follower.get().addFollow(username);
                        userRepository.save(follower.get());
                        return  ResponseEntity.ok("Follow added");
                    }
                }else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST," user not found ");
                }
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are following this user");
            }
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
