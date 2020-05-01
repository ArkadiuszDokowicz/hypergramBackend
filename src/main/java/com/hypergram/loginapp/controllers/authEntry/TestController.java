package com.hypergram.loginapp.controllers.authEntry;
import com.hypergram.loginapp.payload.request.TestRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @PreAuthorize("#username == authentication.principal.username")
    @GetMapping("/userTest")
    public String testAccess(@RequestParam(required = true) String username){
        return "Test passed";
    }
    @PreAuthorize("#testrequest.getUsername() == authentication.principal.username")
    @GetMapping("/userTestWithBody")
    public String testAccessWithBody(@RequestBody TestRequest testrequest){
        return "Test passed";
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}