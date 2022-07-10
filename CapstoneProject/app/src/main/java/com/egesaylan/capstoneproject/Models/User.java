package com.egesaylan.capstoneproject.Models;

public class User {

    public String fullName, age, email,gender, statusCode, password, livein;
    public int status ;
    public boolean isAdmin; //isPollDone;

    public User(){}

    public User(String fullName, String age, String email, int status, String gender,boolean isAdmin, String statusCode,String password,String livein){
        this.fullName = fullName;//Profilde gösterilicek değer
        this.age = age;//Profilde gösterilicek değer
        this.email = email;
        this.status = status;//Profilde gösterilicek değer
        this.gender = gender;
        this.isAdmin = isAdmin;
        this.statusCode = statusCode;
        this.password = password;
        this.livein = livein;
        //this.isPollDone = isPollDone;
    }

}
