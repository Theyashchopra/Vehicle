package com.indiaactive.vehicle.datamodels;

public class ProfileOwnerData {

    private int id;
    private String email;
    private String name;
    private String mobile;
    private Boolean status;
    private String ipAddress;
    private String subscription_name;

    private int alloted_vehicles;
    private String pin_code;
    private String full_address;
    private String pan;
    private String tan;
    private String gst;
    private String gumasta;
    private String mobile2;
    private String state;
    private String city;

    public ProfileOwnerData(int id, String email, String name, String mobile, Boolean status, String ipAddress, String subscriptionName, int allotedVehicles, String pinCode, String fullAddress, String pan, String tan, String gst, String gumasta, String mobile2, String state, String city) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.status = status;
        this.ipAddress = ipAddress;
        this.subscription_name = subscriptionName;
        this.alloted_vehicles = allotedVehicles;
        this.pin_code = pinCode;
        this.full_address = fullAddress;
        this.pan = pan;
        this.tan = tan;
        this.gst = gst;
        this.gumasta = gumasta;
        this.mobile2 = mobile2;
        this.state = state;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubscriptionName() {
        return subscription_name;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscription_name = subscriptionName;
    }

    public int getAllotedVehicles() {
        return alloted_vehicles;
    }

    public void setAllotedVehicles(int allotedVehicles) {
        this.alloted_vehicles = allotedVehicles;
    }

    public String getPinCode() {
        return pin_code;
    }

    public void setPinCode(String pinCode) {
        this.pin_code = pinCode;
    }

    public String getFullAddress() {
        return full_address;
    }

    public void setFullAddress(String fullAddress) {
        this.full_address = fullAddress;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTan() {
        return tan;
    }

    public void setTan(String tan) {
        this.tan = tan;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getGumasta() {
        return gumasta;
    }

    public void setGumasta(String gumasta) {
        this.gumasta = gumasta;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
