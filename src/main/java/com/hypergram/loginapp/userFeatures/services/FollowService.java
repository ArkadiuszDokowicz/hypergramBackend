package com.hypergram.loginapp.userFeatures.services;

import com.hypergram.loginapp.model.FollowRequest;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.payload.response.FollowRequestResponse;
import com.hypergram.loginapp.repository.FollowRequestsRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if(follower.isPresent() && user.isPresent()){
            if(!follower.get().isFollowingUser(username)){
                    if(user.get().isPrivateAccount()){
                        if(!ifRequestHasBeenSend(username)){
                            FollowRequest followRequest = new FollowRequest(user.get(),userDetails.getUsername());
                            requestsRepository.save(followRequest);
                            return  ResponseEntity.ok("Your follow request has been send");
                            }
                        else{
                            return ResponseEntity.ok("You have previously sent follow request");
                        }
                    }else{
                        follower.get().addFollow(username);
                        userRepository.save(follower.get());
                        return  ResponseEntity.ok("Follow added");
                    }
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are following this user");
            }
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> getFollowRequests() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user =userRepository.findByUsername(userDetails.getUsername());
        if(user.isPresent()){
            Optional<List<FollowRequest>> requests =  requestsRepository.findAllByUser(user.get());
            if(requests.isPresent()){
                List<FollowRequestResponse> response = requests.get().stream().map(followRequest ->
                                new FollowRequestResponse(followRequest.getId(),followRequest.getAsker(),followRequest.getDate())
                        ).collect(Collectors.toList());
                return ResponseEntity.ok().body(response);
            }
            else{
                return ResponseEntity.ok("No follow request");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private boolean ifRequestHasBeenSend(String username){
        Optional<User> user = userRepository.findByUsername(username);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> follower =userRepository.findByUsername(userDetails.getUsername());
        if(user.isPresent() && follower.isPresent()) {
            Optional<List<FollowRequest>> currentRequests = requestsRepository.findAllByUser(user.get());
            return currentRequests.map(followRequests -> followRequests.stream().anyMatch(followRequest -> followRequest.getAsker().equals(follower.get().getUsername()))).orElse(false);
        }else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
