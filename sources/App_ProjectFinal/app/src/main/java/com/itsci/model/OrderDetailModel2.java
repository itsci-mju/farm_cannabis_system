package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrderDetailModel2 {
    private OrderDetail orderdetail;

    public OrderDetailModel2(){
        orderdetail = new OrderDetail();
    }

    public OrderDetailModel2(String jsonResponse) {
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
        ProductModel.Product product;
        OrderModel2.Order order;

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

        public ProductModel.Product getProduct() {
            return product;
        }

        public void setProduct(ProductModel.Product product) {
            this.product = product;
        }

        public OrderModel2.Order getOrder() {
            return order;
        }

        public void setOrder(OrderModel2.Order order) {
            this.order = order;
        }
    }
}
