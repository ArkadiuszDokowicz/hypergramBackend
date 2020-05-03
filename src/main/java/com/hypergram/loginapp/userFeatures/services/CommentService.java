package com.hypergram.loginapp.userFeatures.services;

import com.hypergram.loginapp.fileRepository.FilesStorageService;
import com.hypergram.loginapp.model.Comment;
import com.hypergram.loginapp.model.ImageDB;
import com.hypergram.loginapp.model.User;

import java.util.List;
import java.util.Optional;

import com.hypergram.loginapp.payload.request.CommentRequest;
import com.hypergram.loginapp.payload.request.GetCommentRequest;
import com.hypergram.loginapp.payload.request.NewCommentRequest;
import com.hypergram.loginapp.repository.CommentRepository;
import com.hypergram.loginapp.repository.ImageRepository;
import com.hypergram.loginapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FilesStorageService filesStorageService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;

    public ResponseEntity<?> addComment(NewCommentRequest commentRequest){
        if(imageRepository.existsById(commentRequest.getImageId())){
            try{
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Optional<User> user=userRepository.findByUsername(userDetails.getUsername());
                Optional<ImageDB> imageDB = imageRepository.findById(commentRequest.getImageId());
                commentRepository.save(new Comment(imageDB.get().getId(),commentRequest.getComment(),user.get().getUsername()));
                return ResponseEntity.ok("Comment added");
            }
            catch(Exception ex){
                ex.printStackTrace();
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,"Service unavailable");
            }
        }
        else
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No image with id: "+commentRequest.getImageId());

    }
    public ResponseEntity<?> removeComment(CommentRequest commentRequest){
        try{
            Optional<Comment> comment = commentRepository.findById(commentRequest.getCommentId());
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(comment.isPresent() && userDetails.getUsername().equals(comment.get().getUsername())){
            commentRepository.delete(comment.get());
            return ResponseEntity.ok("Comment removed");
            }
            else{

                //TODO REFACTOR
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Transaction unauthorized");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No comment with id:"+commentRequest.getCommentId());
        }
    }

    public ResponseEntity<?> getComments(GetCommentRequest commentRequest) {
        try{
            List<Comment> comments =  commentRepository.findAllByimageId(commentRequest.getImageId());
            return ResponseEntity.status(HttpStatus.OK).body(comments);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No image with id:"+commentRequest.getImageId());
        }
    }
}
