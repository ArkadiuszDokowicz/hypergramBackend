package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class GetCommentRequest {
    @NotEmpty
    String imageId;
    @NotEmpty
    String username;

    public String getImageId() {
        return imageId;
    }

    public String getUsername() {
        return username;
    }
}
