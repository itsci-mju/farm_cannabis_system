package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomerModel {
    private Customer customer;

    public CustomerModel(){
        customer = new Customer();
    }

    public CustomerModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        customer = gson.fromJson(jsonResponse, Customer.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.customer);
    }

    public Customer getCustomer() {
        return customer;
    }

    public class Customer {
        String CustomerID;
        String company;
        String address;
        String mobileNo;
        String imgCertificate;
        String imgIDCard;
        String reqreg;
        String user;

        public String getCustomerID() {
            return CustomerID;
        }

        public void setCustomerID(String customerID) {
            CustomerID = customerID;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getImgCertificate() {
            return imgCertificate;
        }

        public void setImgCertificate(String imgCertificate) {
            this.imgCertificate = imgCertificate;
        }

        public String getImgIDCard() {
            return imgIDCard;
        }

        public void setImgIDCard(String imgIDCard) {
            this.imgIDCard = imgIDCard;
        }

        public String getReqreg() {
            return reqreg;
        }

        public void setReqreg(String reqreg) {
            this.reqreg = reqreg;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
