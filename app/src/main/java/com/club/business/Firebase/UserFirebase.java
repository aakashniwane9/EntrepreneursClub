package com.club.business.Firebase;

import java.util.HashMap;
import java.util.Map;

public class UserFirebase {
    //Register
    String username,fullname,email,password,contactnumber,BusinessEmail,uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    //Business Details
    String BusinessName,GSTIN,Industry,Scale,Nature,City,Haves,Wants;


    public long contact_number=0;

    //String BusinessEmail;
    //Getters & Setters
    public UserFirebase() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserFirebase(String username, String fullname, String email, String password, String contactnumber, String businessEmail, String uid, String businessName, String GSTIN, String industry, String scale, String nature, String city, String haves, String wants, long contact_number) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.contactnumber = contactnumber;
        BusinessEmail = businessEmail;
        this.uid = uid;
        BusinessName = businessName;
        this.GSTIN = GSTIN;
        Industry = industry;
        Scale = scale;
        Nature = nature;
        City = city;
        Haves = haves;
        Wants = wants;
        this.contact_number = contact_number;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public String getBusinessEmail() {
        return BusinessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        BusinessEmail = businessEmail;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String nature) {
        Nature = nature;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getHaves() {
        return Haves;
    }

    public void setHaves(String haves) {
        Haves = haves;
    }

    public String getWants() {
        return Wants;
    }

    public void setWants(String wants) {
        Wants = wants;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }


    //MAPS
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("name",fullname);
        result.put("email",email);
        result.put("password",password);
        result.put("contact",contactnumber);
        result.put("Bname",BusinessName);
        result.put("GSTIN",GSTIN);
        result.put("BusinessEmail",BusinessEmail);
        result.put("Industry",Industry);
        result.put("Scale",Scale);
        result.put("Nature",Nature);
        result.put("City",City);
        result.put("Haves",Haves);
        result.put("Wants",Wants);
        return result;
    }
}
