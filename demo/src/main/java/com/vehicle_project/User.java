package com.vehicle_project;

// 2. Enhanced User class with role
public abstract class User {

  
  
    private final String name;
    private final String email;
    private final UserRole role;

    public User(String name, String email, UserRole role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    // Check if user has specific role
    public boolean hasRole(UserRole role) {
        return this.role == role;
    }

      public enum UserRole {  ADMIN, CUSTOMER  }
}