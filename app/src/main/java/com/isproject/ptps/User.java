package com.isproject.ptps;


public class User {

    private String firstName;
    private String lastName;
    private String userType;
    private String phoneNumber;

    public User() {}

    public User( String firstName, String lastName, String userType, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
