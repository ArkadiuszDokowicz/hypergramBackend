package com.hypergram.loginapp.payload.response;

import java.util.List;

public class WarnListResponse {
    private List<WarnResponse> warns;
    private String username;

    public WarnListResponse(){}

    public WarnListResponse(List<WarnResponse> warns, String username) {
        this.warns = warns;
        this.username = username;
    }

    public List<WarnResponse> getWarns() {
        return warns;
    }

    public void setWarns(List<WarnResponse> warns) {
        this.warns = warns;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
