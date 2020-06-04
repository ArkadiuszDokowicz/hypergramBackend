package com.hypergram.loginapp.controllers.authEntry;

import com.hypergram.loginapp.payload.request.AddWarnRequest;
import com.hypergram.loginapp.payload.response.WarnListResponse;
import com.hypergram.loginapp.security.services.WarnAndBanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok("warn could not be added");
        }
    }
    @GetMapping("/myWarns/{username:.+}")
    public WarnListResponse addWarn (@PathVariable String username){

       return warnAndBanService.findActiveWarnByUsername(username);

    }

}
