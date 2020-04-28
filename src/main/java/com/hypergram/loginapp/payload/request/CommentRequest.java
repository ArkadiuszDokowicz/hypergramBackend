package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class CommentRequest {
    @NotEmpty
    String commentId;

    @NotEmpty
    String username;

    public String getUsername() {
        return username;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
