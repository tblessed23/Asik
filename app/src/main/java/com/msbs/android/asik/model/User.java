package com.msbs.android.asik.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName  = "user", indices = {@Index(value = {"userId"},
        unique = true)})
public class User implements Serializable {

    @PrimaryKey(autoGenerate =true)
    public int primaryId;
    @NonNull
    public String userId;
    public String email;
    public String firstname;
    private String lastname;
    private String city;
    private String state;
    private String country;
    private String phone;
    private String displayname;
    private String displayemail;

    @Ignore
    public boolean isNew;

    /**
     * No args constructor for use in serialization
     *
     * @param user
     */
    public User(String user) {
    }

    // Regular Constructor
    @Ignore
    public User(String userId, String email, String firstname, String lastname, String city, String state, String country, String phone, String displayname, String displayemail) {
        this.userId = userId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.city= city;
        this.state= state;
        this.country = country;
        this.phone = phone;
        this.displayname = displayname;
        this.displayemail = displayemail;

    }


    //Id Constructor

    public User(int primaryId, String userId, String email, String firstname, String lastname, String city, String state, String country, String phone, String displayname, String displayemail) {
        this.primaryId = primaryId;
        this.userId = userId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phone = phone;
        this.displayname = displayname;
        this.displayemail = displayemail;

    }


    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname= firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone) {
        this.phone = phone;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDisplayemail() {
        return displayemail;
    }

    public void setDisplayemailname(String displayemail) {
        this.displayemail = displayemail;
    }

}
