package com._agents.library.entity;

import com._agents.library.exception.RequiredDataMissingException;
import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {

    @Id
    private String username;
    private String email;
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void validateRequiredAttributes(){
        // Check for required data
        if (this.getEmail()== null){
            throw new RequiredDataMissingException("email");
        }
        if (this.getAddress()== null){
            throw new RequiredDataMissingException("address");
        }
    }
}