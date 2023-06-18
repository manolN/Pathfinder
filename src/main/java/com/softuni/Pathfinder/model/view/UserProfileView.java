package com.softuni.Pathfinder.model.view;

public class UserProfileView {

    private String username;
    private String fullName;
    private Integer age;

    public String getUsername() {
        return username;
    }

    public UserProfileView setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserProfileView setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserProfileView setAge(Integer age) {
        this.age = age;
        return this;
    }
}
