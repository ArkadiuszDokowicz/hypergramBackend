package com.hypergram.loginapp;

import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.security.jwt.AuthTokenFilter;
import com.hypergram.loginapp.security.services.InitialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class LoginappApplication implements CommandLineRunner{

    @Autowired
    InitialService initialService;
    @Resource
    FilesStorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(LoginappApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LoginappApplication.class, args);
    }
    @Override
    public void run(String... arg) throws Exception {
        logger.info("try to create admin account");
        String initialCreateAdminStatus =initialService.createAdmin();
        logger.info(initialCreateAdminStatus);
        /*
        storageService.deleteAll();
        storageService.init();
        */
    }
}