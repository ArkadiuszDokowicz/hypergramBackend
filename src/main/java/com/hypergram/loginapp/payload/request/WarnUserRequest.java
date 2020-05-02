package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class WarnUserRequest {
    @NotEmpty
    String username;
    @NotEmpty
    String reason;


    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }
}
