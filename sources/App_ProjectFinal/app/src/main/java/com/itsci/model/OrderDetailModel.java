package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrderDetailModel {
    private OrderDetail orderdetail;

    public OrderDetailModel(){
        orderdetail = new OrderDetail();
    }

    public OrderDetailModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        orderdetail = gson.fromJson(jsonResponse, OrderDetail.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.orderdetail);
    }

    public OrderDetail getOrderDetail() {
        return orderdetail;
    }

    public class OrderDetail {
        String orderDetailID;
        String qty;
        String totalprice;
        String product;
        String order;

        public String getOrderDetailID() {
            return orderDetailID;
        }

        public void setOrderDetailID(String orderDetailID) {
            this.orderDetailID = orderDetailID;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getTotalprice() {
            return totalprice;
        }

        public void setTotalprice(String totalprice) {
            this.totalprice = totalprice;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }
}
