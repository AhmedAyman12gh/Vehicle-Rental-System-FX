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


      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Compare users based on email (unique identifier)
        return email != null && email.equalsIgnoreCase(user.email);
    }

    @Override
    public int hashCode() {
        return email != null ? email.toLowerCase().hashCode() : 0;
    }

}