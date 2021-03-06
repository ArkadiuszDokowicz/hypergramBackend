package com.hypergram.loginapp.security.services;

import com.hypergram.loginapp.model.Ban;
import com.hypergram.loginapp.model.Role;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.model.Warn;
import com.hypergram.loginapp.payload.request.AddWarnRequest;
import com.hypergram.loginapp.payload.request.BanRequest;
import com.hypergram.loginapp.payload.response.WarnListResponse;
import com.hypergram.loginapp.payload.response.WarnResponse;
import com.hypergram.loginapp.repository.BanRepository;
import com.hypergram.loginapp.repository.UserRepository;
import com.hypergram.loginapp.repository.WarnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WarnAndBanService {


    @Autowired
    BanRepository banRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarnRepository warnRepository;

    public WarnListResponse findActiveWarnByUsername(String username) {

        WarnListResponse warnListResponse = new WarnListResponse();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<List<Warn>> warns = warnRepository.findAllByUser(user.get());
            if(warns.isPresent()){

                List<WarnResponse> warnResponses = new ArrayList<>();
                for(Warn warn :warns.get()){
                        warnResponses.add(new WarnResponse(warn.getDate(),warn.getMessage(),warn.getReason()));
                }
                 warnListResponse.setWarns(warnResponses);
                 warnListResponse.setUsername(username);
                 return warnListResponse;
            }else{ return warnListResponse; }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found");
        }

    }
    public boolean addWarnForUser(AddWarnRequest warnUserRequest){
        Optional<User> user = userRepository.findByUsername(warnUserRequest.getUsername());
        if (user.isPresent()) {
            Warn warn = new Warn(user.get(), warnUserRequest.getMessage(), warnUserRequest.getReason());
            Warn saved = warnRepository.save(warn);
            return saved.equals(warn);
        }else{
        return false;
        }
    }

    public boolean banUser(BanRequest banRequest) {

        Optional<User> user = userRepository.findByUsername(banRequest.getUsername());
        if(user.isPresent()){
            Set<Role> roles = user.get().getRoles();
            Optional<User> banedUser = banRepository.findByUser(user.get());
            if(roles.size()>3){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Other admins can not be banned");
            }else {
                if (!banedUser.isPresent()) {
                    banRepository.save(new Ban(user.get(), banRequest.getReason()));
                    return true;
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.ALREADY_REPORTED,"user already banned");
                }
            }
        }else{
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "No user with username : "+ banRequest.getUsername());
        }
    }
}
