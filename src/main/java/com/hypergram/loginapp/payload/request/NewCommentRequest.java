package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class NewCommentRequest {
    @NotEmpty
    String imageId;
    @NotEmpty
    @Size(min = 1, max = 60)
    String comment;
    @NotEmpty
    String username;

    public String getUsername() {
        return username;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
