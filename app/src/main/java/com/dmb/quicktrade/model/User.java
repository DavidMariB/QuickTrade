package com.dmb.quicktrade.model;

import java.io.Serializable;

/**
 * Created by davidmari on 18/1/18.
 */

public class User implements Serializable{

    private String username,name,surname,email,address,auth;

    public User(String username, String name, String surname, String email, String address,String auth){

        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.auth = auth;

    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "User{" +
                "Usuario='" + username + '\'' +
                ", nombre='" + name + '\'' +
                ", apellidos='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + address + '\'' +
                '}';
    }
}
