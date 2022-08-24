package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OfficerModel {

    private Officer officer;

    public OfficerModel(){
        officer = new Officer();
    }

    public OfficerModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        officer = gson.fromJson(jsonResponse, Officer.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.officer);
    }

    public Officer getOfficer() {
        return officer;
    }

    public class Officer {
        String officerid;
        String address;
        String mobileno;
        String user;

        public String getOfficerid() {
            return officerid;
        }

        public void setOfficerid(String officerid) {
            this.officerid = officerid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobileno() {
            return mobileno;
        }

        public void setMobileno(String mobileno) {
            this.mobileno = mobileno;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
