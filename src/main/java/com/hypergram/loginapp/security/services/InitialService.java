package com.hypergram.loginapp.security.services;

import com.hypergram.loginapp.LoginappApplication;
import com.hypergram.loginapp.model.ERole;
import com.hypergram.loginapp.model.Role;
import com.hypergram.loginapp.model.User;
import com.hypergram.loginapp.repository.RoleRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;
@Service
public class InitialService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    public String createAdmin(){
        String initAdminName = "INITIAL_ADMIN_ACCOUNT";
        String initAdminPassword = "INITIALADMINPASSWORD";
        String initAdminEmail = "XYZ@XYZ.com";
        if(!userRepository.existsByUsername(initAdminName)) {
            User user = new User(initAdminName,
                    initAdminEmail,
                    encoder.encode(initAdminPassword));

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);


            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            user.setRoles(roles);
            userRepository.save(user);
            if(userRepository.existsByUsername(initAdminName)){
                return "create admin operation success";
            }else{
                return "create admin operation failed";
            }
        }else{
            return "Admin account already exist";
        }
    }

    public String createUser(){
     //TODO
        return null;
    }
}
