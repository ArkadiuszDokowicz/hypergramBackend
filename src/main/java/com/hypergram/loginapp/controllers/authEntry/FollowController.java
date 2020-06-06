package com.hypergram.loginapp.controllers.authEntry;

import com.hypergram.loginapp.userFeatures.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @PostMapping("/followUser")
    public ResponseEntity<?> followUser(@RequestParam(name = "username")String username) {
        return followService.followUser(username);
    }

}
