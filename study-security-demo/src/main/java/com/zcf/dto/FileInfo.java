package com.zcf.dto;

public class FileInfo {
    public FileInfo(String path){
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;
}
