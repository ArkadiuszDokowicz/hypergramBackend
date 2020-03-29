package com.hypergram.loginapp.security.services;

import com.hypergram.loginapp.model.Question;
import com.hypergram.loginapp.model.RemindingQuestion;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.repository.RemindingQuestionRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class RemindingPasswordService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RemindingQuestionRepository remindingQuestionRepository;
    @Autowired
    PasswordEncoder encoder;


    public Question getRandomQuestion(String username){
        Optional<User> user= userRepository.findByUsername(username);
        try {
            List<RemindingQuestion> questionsList = remindingQuestionRepository.findAllByUser(user.get());
            SecureRandom secureRandom = new SecureRandom();
            RemindingQuestion rq = questionsList.get(secureRandom.nextInt(questionsList.size()));
        return new Question(rq.getId(),rq.getQuestion());
        }catch(Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "you haven't set any question");
        }
    }
    public Boolean validateQuestion(String username,String id,String answer) throws NoSuchAlgorithmException {
        Optional<User> user= userRepository.findByUsername(username);
        Optional<RemindingQuestion> question = remindingQuestionRepository.findById(id);
        return getHashAnswer(answer).equals(question.get().getAnswer());
    }
    public String getHashAnswer(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));
        return sha256hex;
    }
}
