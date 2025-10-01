package com.ayanda.HealthEaseApi.additional;

public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String userType;
    private UserRole userRole;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UserRole getUserRole() {
        return mapRoleToUserRole(this.userType);
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String role) {
        this.userType = role;
    }

    private UserRole mapRoleToUserRole(String role){
        if(role.equals("patient")){
            return UserRole.PATIENT;
        }else if(role.equals("doctor")){
            return UserRole.DOCTOR;
        }else{
            return UserRole.ADMIN;
        }
    }
}
