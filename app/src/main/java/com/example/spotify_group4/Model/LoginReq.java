package com.example.spotify_group4.Model;

public class LoginReq {
    private String status;
    private String message;
    private String email;

    public LoginReq(String status, String message, String email) {
        this.status = status;
        this.message = message;
        this.email = email;
    }
    public LoginReq(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginReq{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
