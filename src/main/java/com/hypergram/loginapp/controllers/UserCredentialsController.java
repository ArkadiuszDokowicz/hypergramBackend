package com.hypergram.loginapp.controllers;

import com.hypergram.loginapp.model.RemindingQuestion;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.payload.request.ChangePasswdRequest;
import com.hypergram.loginapp.payload.request.PasswordQuestionAddRequest;
import com.hypergram.loginapp.payload.request.PasswordQuestionGetRequest;
import com.hypergram.loginapp.payload.request.RemoveQuestionRequest;
import com.hypergram.loginapp.payload.response.MessageResponse;
import com.hypergram.loginapp.payload.response.RemindingQuestionsListResponse;
import com.hypergram.loginapp.repository.RemindingQuestionRepository;
import com.hypergram.loginapp.repository.UserRepository;
import com.hypergram.loginapp.security.services.RemindingPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserCredentialsController {

    private static final Logger logger = LoggerFactory.getLogger(UserCredentialsController.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RemindingQuestionRepository remindingQuestionRepository;

    @Autowired
    RemindingPasswordService remindingPasswordService;

    @PreAuthorize("#cpRequest.username == authentication.principal.username")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswdRequest cpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(cpRequest.getUsername(), cpRequest.getPassword()));
        Optional<User> user = userRepository.findByUsername(cpRequest.getUsername());
        if (!cpRequest.getPasswordNew1().equals(cpRequest.getPasswordNew2())) {
            logger.warn("Bad request passwords are not the same");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Passwords are not the same");
        }
        if (cpRequest.getPassword().equals(cpRequest.getPasswordNew1())) {
            logger.warn("Bad request new password is the same");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The new password is no different from the old one");
        }
        user.get().setPassword(encoder.encode(cpRequest.getPasswordNew1()));
        userRepository.save(user.get());
        return ResponseEntity.ok(new MessageResponse("PasswordChanged"));
    }
    @PreAuthorize("#pqRequest.username == authentication.principal.username")
    @PostMapping("/setSecurityQuestion")
    public ResponseEntity<?> addPasswordQuestion(@Valid @RequestBody PasswordQuestionAddRequest pqRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(pqRequest.getUsername(), pqRequest.getPassword()));
        Optional<User> user = userRepository.findByUsername(pqRequest.getUsername());
        try {
            String hashAnswer = remindingPasswordService.getHashAnswer(pqRequest.getAnswer());
            RemindingQuestion remindingQuestion = new RemindingQuestion(user.get(), pqRequest.getQuestion(), hashAnswer);
            remindingQuestionRepository.save(remindingQuestion);
            return ResponseEntity.ok(new MessageResponse("Question Added"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Service not available");
        }
    }
    @PreAuthorize("#pqRequest.username == authentication.principal.username")
    @PostMapping("/getSecurityQuestions")
    public ResponseEntity<?> getQuestions(@Valid @RequestBody PasswordQuestionGetRequest pqRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(pqRequest.getUsername(), pqRequest.getPassword()));
        Optional<User> user= userRepository.findByUsername(pqRequest.getUsername());
        List<RemindingQuestion> questions = remindingQuestionRepository.findAllByUser(user.get());

        return  ResponseEntity.ok().body(new RemindingQuestionsListResponse(questions));
    }

    @PreAuthorize("#rqRequest.username == authentication.principal.username")
    @PostMapping("/deleteSecurityQuestion")
    public ResponseEntity<?> removeQuestion(@Valid @RequestBody RemoveQuestionRequest rqRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(rqRequest.getUsername(), rqRequest.getPassword()));
        Optional<RemindingQuestion> question = remindingQuestionRepository.findById(rqRequest.getQuestionId());
        remindingQuestionRepository.delete(question.get());
        return ResponseEntity.ok(new MessageResponse("QuestionRemoved"));
    }
}
