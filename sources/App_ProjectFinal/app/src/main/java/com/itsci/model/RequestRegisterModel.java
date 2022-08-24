package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequestRegisterModel {
    private RequestRegister requestregister;

    public RequestRegisterModel(){
        requestregister = new RequestRegister();
    }

    public RequestRegisterModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        requestregister = gson.fromJson(jsonResponse, RequestRegister.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.requestregister);
    }

    public RequestRegister getRequestRegister() {
        return requestregister;
    }

    public class RequestRegister {
        String reqregid;
        String persontype;
        String status;

        public String getReqregid() {
            return reqregid;
        }

        public void setReqregid(String reqregid) {
            this.reqregid = reqregid;
        }

        public String getPersontype() {
            return persontype;
        }

        public void setPersontype(String persontype) {
            this.persontype = persontype;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
