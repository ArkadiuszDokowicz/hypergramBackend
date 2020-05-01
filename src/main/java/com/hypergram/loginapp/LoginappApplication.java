package com.hypergram.loginapp;

import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.security.services.InitialService;
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
    public static void main(String[] args) {
        SpringApplication.run(LoginappApplication.class, args);
    }
    @Override
    public void run(String... arg) throws Exception {
        initialService.createAdmin();
        /*
        storageService.deleteAll();
        storageService.init();
        */
    }
}