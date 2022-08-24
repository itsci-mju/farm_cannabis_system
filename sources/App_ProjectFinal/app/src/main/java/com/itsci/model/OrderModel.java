package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    private Order order;

    public OrderModel(){
        order = new Order();
    }

    public OrderModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        order = gson.fromJson(jsonResponse, Order.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.order);
    }

    public Order getOrder() {
        return order;
    }

    public class Order {
        String OrderID;
        String orderDate;
        String receiveDate;
        String status;
        String customer;

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String orderID) {
            OrderID = orderID;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getReceiveDate() {
            return receiveDate;
        }

        public void setReceiveDate(String receiveDate) {
            this.receiveDate = receiveDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }
    }
}
