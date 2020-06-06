package com.hypergram.loginapp.controllers.authEntry;

import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.model.ERole;
import com.hypergram.loginapp.model.ImageDB;
import com.hypergram.loginapp.model.Role;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.payload.request.SetModeratorRequest;
import com.hypergram.loginapp.payload.response.MessageResponse;
import com.hypergram.loginapp.repository.ImageRepository;
import com.hypergram.loginapp.repository.RoleRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/admin")
public class AdminActionController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FilesStorageService storageService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/setModeratorRole")
    public ResponseEntity<?> setRoleModerator(@Valid @RequestBody SetModeratorRequest request){
        if(request.getPassword1().equals(request.getPassword2())){
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword1()));

            String promotedUsername = request.getPromotedUser();
            if(userRepository.existsByUsername(promotedUsername)){
                Optional<User> user = userRepository.findByUsername(promotedUsername);
                Set<Role> roles = new HashSet<>();
                Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(modRole);
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
                user.get().setRoles(roles);
                userRepository.save(user.get());
                return ResponseEntity.ok(new MessageResponse("Moderator has been added"));
            }else{
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User with "+promotedUsername+" username not exist");
            }
        }else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password are not the same");
        }
    }
    @PostMapping("/removeAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAccount(@RequestParam (name = "username")String username){
        try {
            Optional<User> user = userRepository.findByUsername(username);
            Optional<List<ImageDB>> imagesToRemove= imageRepository.findAllByUser(user.get());
            if(imagesToRemove.isPresent()){
                for(ImageDB image : imagesToRemove.get()){
                    String imagePath = image.getId()+".jpg";
                    storageService.deletePicture(imagePath);
                }
            }
            userRepository.delete(user.get());
            return ResponseEntity.ok("User removed");
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
