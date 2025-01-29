package com.example.smartspine;

public class PostureData {

    private String posture;
    private String timestamp;

    public PostureData(String posture, String timestamp) {
        this.posture = posture;
        this.timestamp = timestamp;
    }

    public String getPosture() {
        return posture;
    }

    public void setPosture(String posture) {
        this.posture = posture;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
