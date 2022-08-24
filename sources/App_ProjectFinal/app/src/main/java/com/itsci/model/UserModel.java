package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserModel {
    private User user;

    public UserModel(){
        user = new User();
    }

    public UserModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        user = gson.fromJson(jsonResponse, User.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.user);
    }

    public User getUser() {
        return user;
    }

    public class User {
        String fullname;
        String username;
        String password;

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
