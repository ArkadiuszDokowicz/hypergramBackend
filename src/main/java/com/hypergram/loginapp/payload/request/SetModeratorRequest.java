package com.hypergram.loginapp.payload.request;

import javax.validation.constraints.NotEmpty;

public class SetModeratorRequest {
    @NotEmpty
    String promotedUser;
    @NotEmpty
    String password1;
    @NotEmpty
    String password2;

    public String getPromotedUser() {
        return promotedUser;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }
}
