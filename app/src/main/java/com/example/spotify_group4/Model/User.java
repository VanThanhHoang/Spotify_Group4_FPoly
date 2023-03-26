package com.example.spotify_group4.Model;

public class User {
    private int resourceId;
    private String name;
    private String password;
    public User(String name) {
        this.name = name;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public User(int resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean validateEmail(String email) {
        if (email.isEmpty()) {
            return false;
        } else {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            return email.matches(emailPattern);
        }
    }
    public boolean validatePassword(String password) {
        return password.length() >= 6;
    }
    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
