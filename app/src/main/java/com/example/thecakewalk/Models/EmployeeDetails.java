package com.example.thecakewalk.Models;

public class EmployeeDetails {

    String id;
    String name;
    String contact;
    String nic;
    String address;
    String position;

    public EmployeeDetails() {
    }

    public EmployeeDetails(String id, String name, String contact, String nic, String address, String position) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.nic = nic;
        this.address = address;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
