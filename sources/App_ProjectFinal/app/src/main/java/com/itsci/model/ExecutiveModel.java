package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExecutiveModel {
    private Executive executive;

    public ExecutiveModel(){
        executive = new Executive();
    }

    public ExecutiveModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        executive = gson.fromJson(jsonResponse, Executive.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.executive);
    }

    public Executive getExecutive() {
        return executive;
    }

    public class Executive {
        String executiveid;
        String mobileno;
        String user;

        public String getExecutiveid() {
            return executiveid;
        }

        public void setExecutiveid(String executiveid) {
            this.executiveid = executiveid;
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
