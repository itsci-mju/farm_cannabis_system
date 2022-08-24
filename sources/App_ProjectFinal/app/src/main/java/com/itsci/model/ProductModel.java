package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProductModel {
    private Product product;

    public ProductModel(){
        product = new Product();
    }

    public ProductModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        product = gson.fromJson(jsonResponse, Product.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.product);
    }

    public Product getProduct() {
        return product;
    }

    public class Product {
        String productid;
        String productname;
        String price;

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }

        public String getProductname() {
            return productname;
        }

        public void setProductname(String productname) {
            this.productname = productname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
