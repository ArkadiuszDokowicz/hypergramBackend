package com.hypergram.loginapp;

import com.hypergram.loginapp.service.FilesStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class LoginappApplication implements CommandLineRunner{
    @Resource
    FilesStorageService storageService;
    public static void main(String[] args) {
        SpringApplication.run(LoginappApplication.class, args);
    }
    @Override
    public void run(String... arg) throws Exception {
        storageService.deleteAll();
        storageService.init();
    }
}