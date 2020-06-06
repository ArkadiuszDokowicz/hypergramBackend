package com.hypergram.loginapp.controllers.authEntry;

import com.hypergram.loginapp.payload.request.AddWarnRequest;
import com.hypergram.loginapp.payload.request.BanRequest;
import com.hypergram.loginapp.payload.response.WarnListResponse;
import com.hypergram.loginapp.security.services.WarnAndBanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/warn")
public class WarnBanController {

    @Autowired
    WarnAndBanService warnAndBanService;

    @PreAuthorize("(hasRole('MODERATOR') or hasRole('ADMIN'))")
    @PostMapping("/addWarn")
    public ResponseEntity<?> addWarn (@Valid @RequestBody AddWarnRequest addWarnRequest){
        boolean status = warnAndBanService.addWarnForUser(addWarnRequest);
        if(status){
            return ResponseEntity.ok("warn added");
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No user with: "+addWarnRequest.getUsername()+" username");

        }
    }
    @GetMapping("/myWarns/{username:.+}")
    public WarnListResponse addWarn (@PathVariable String username){

       return warnAndBanService.findActiveWarnByUsername(username);

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @PostMapping("/banUser")
    public ResponseEntity<?> banUser(@Valid @RequestBody BanRequest banRequest){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        if(username.equals(banRequest.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "You can not ban yourself");
        }
        else{
             warnAndBanService.banUser(banRequest);
            return ResponseEntity.ok("user banned");
        }
    }


}
