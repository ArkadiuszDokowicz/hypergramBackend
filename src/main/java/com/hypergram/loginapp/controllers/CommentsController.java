package com.hypergram.loginapp.controllers;

import com.hypergram.loginapp.payload.request.CommentRequest;
import com.hypergram.loginapp.payload.request.GetCommentRequest;
import com.hypergram.loginapp.payload.request.NewCommentRequest;
import com.hypergram.loginapp.userFeatures.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/comment")
public class CommentsController {

    @Autowired
    CommentService commentService;

    @PreAuthorize("(hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')) and #commentRequest.username == authentication.principal.username")
    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@Valid @RequestBody NewCommentRequest commentRequest){
        return commentService.addComment(commentRequest);
    }
    @PreAuthorize("#commentRequest.username == authentication.principal.username")
    @PostMapping("/removeComment")
    public ResponseEntity<?> removeComment(@Valid @RequestBody CommentRequest commentRequest){
        return  commentService.removeComment(commentRequest);
    }
    @PreAuthorize("#getCommentRequest.username == authentication.principal.username")
    @GetMapping("/getComments")
    public ResponseEntity<?> getCommentList(@Valid @RequestBody GetCommentRequest getCommentRequest){
        return commentService.getComments(getCommentRequest);
    }
}
