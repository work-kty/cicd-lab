package com.cicdlab.app.model;

public class User {
    private final String username;
    private final String password;   // 실습용 평문. 실무는 반드시 해시(BCrypt)!
    private final String nickname;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
}