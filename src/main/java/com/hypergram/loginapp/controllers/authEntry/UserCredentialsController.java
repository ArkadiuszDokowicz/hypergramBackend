package com.hypergram.loginapp.controllers.authEntry;

import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.model.ImageDB;
import com.hypergram.loginapp.model.RemindingQuestion;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.payload.request.*;
import com.hypergram.loginapp.payload.response.MessageResponse;
import com.hypergram.loginapp.payload.response.RemindingQuestionsListResponse;
import com.hypergram.loginapp.repository.ImageRepository;
import com.hypergram.loginapp.repository.RemindingQuestionRepository;
import com.hypergram.loginapp.repository.UserRepository;
import com.hypergram.loginapp.security.jwt.JwtTokenUtil;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    JwtTokenUtil jwtBuilder;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RemindingQuestionRepository remindingQuestionRepository;

    @Autowired
    RemindingPasswordService remindingPasswordService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FilesStorageService storageService;

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

    @PostMapping("/removeAccount")
    @PreAuthorize("#loginRequest.username == authentication.principal.username")
    public ResponseEntity<?> removeAccount(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        try {
            Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
            Optional<List<ImageDB>> imagesToRemove= imageRepository.findAllByUser(user.get());
            if(imagesToRemove.isPresent()){
                for(ImageDB image : imagesToRemove.get()){
                    String imagePath = image.getId()+".jpg";
                    storageService.deletePicture(imagePath);
                }
            }
            userRepository.delete(user.get());
            String jwt = jwtBuilder.generateJwtToken(authentication);
            return ResponseEntity.ok("ok");
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/setPrivate")
    public ResponseEntity<?> setPrivate(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            user.get().setPrivateAccount(true);
            userRepository.save(user.get());
            return ResponseEntity.ok("Your account has become private ");
        }else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/setPublic")
    public ResponseEntity<?> setPublic(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            user.get().setPrivateAccount(false);
            userRepository.save(user.get());
            return ResponseEntity.ok("Your account has become public");
        }else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/isPrivateAccount")
    public ResponseEntity<?> getPrivacySettings(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
                return ResponseEntity.ok().body(user.get().isPrivateAccount());
        }else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
