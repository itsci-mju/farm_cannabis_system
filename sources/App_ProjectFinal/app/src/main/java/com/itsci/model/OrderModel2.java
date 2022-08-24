package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrderModel2 {
    private Order order;

    public OrderModel2() {
        order = new OrderModel2.Order();
    }

    public OrderModel2(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        order = gson.fromJson(jsonResponse, OrderModel2.Order.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.order);
    }

    public OrderModel2.Order getOrder() {
        return order;
    }

    public class Order {
        String OrderID;
        String orderDate;
        String receiveDate;
        String status;
        CustomerModel2.Customer customer;

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

        public CustomerModel2.Customer getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerModel2.Customer customer) {
            this.customer = customer;
        }
    }
}
