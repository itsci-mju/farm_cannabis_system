package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CustomerModel2 {
    private Customer customer;

    public CustomerModel2(){
        customer = new Customer();
    }

    public CustomerModel2(String jsonResponse) {
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
        RequestRegisterModel.RequestRegister reqreg;
        UserModel.User user;

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

        public RequestRegisterModel.RequestRegister getReqreg() {
            return reqreg;
        }

        public void setReqreg(RequestRegisterModel.RequestRegister reqreg) {
            this.reqreg = reqreg;
        }

        public UserModel.User getUser() {
            return user;
        }

        public void setUser(UserModel.User user) {
            this.user = user;
        }
    }
}
