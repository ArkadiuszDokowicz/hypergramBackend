package com.hypergram.loginapp.model;

public class FileInfo {
    private String name;
    private String url;
    private String fileOwner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileOwner() {
        return fileOwner;
    }

    public void setFileOwner(String fileOwner) {
        this.fileOwner = fileOwner;
    }

    public FileInfo(String name, String url, String fileOwner) {
        this.name = name;
        this.url = url;
        this.fileOwner = fileOwner;
    }
}