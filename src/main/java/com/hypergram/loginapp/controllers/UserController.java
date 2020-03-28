package com.hypergram.loginapp.controllers;

import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.payload.request.ChangePasswdRequest;
import com.hypergram.loginapp.payload.request.PasswordQuestionRequest;
import com.hypergram.loginapp.payload.response.MessageResponse;
import com.hypergram.loginapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @PreAuthorize("#cpRequest.username == authentication.principal.username")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswdRequest cpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(cpRequest.getUsername(), cpRequest.getPassword()));
        Optional<User> user= userRepository.findByUsername(cpRequest.getUsername());
        if(!cpRequest.getPasswordNew1().equals(cpRequest.getPasswordNew2())){
            logger.warn("Bad request passwords are not the same");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Passwords are not the same");
        }
        user.get().setPassword(encoder.encode(cpRequest.getPasswordNew1()));
        userRepository.save(user.get());
        return ResponseEntity.ok(new MessageResponse("PasswordChanged"));
    }
    @PreAuthorize("#pqRequest.username == authentication.principal.username")
    @PostMapping("/addRemindingPasswordQuestion")
    public ResponseEntity<?> addPasswordQuestion(@Valid @RequestBody PasswordQuestionRequest pqRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(pqRequest.getUsername(), pqRequest.getPassword()));
        Optional<User> user= userRepository.findByUsername(pqRequest.getUsername());


}