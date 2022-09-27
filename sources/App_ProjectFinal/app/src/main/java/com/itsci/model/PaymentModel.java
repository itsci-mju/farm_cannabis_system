package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PaymentModel {
    private Payment payment;

    public PaymentModel() {
        payment = new Payment();
    }

    public PaymentModel(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        payment = gson.fromJson(jsonResponse, Payment.class);
    }

    public String toJSONString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.payment);
    }

    public Payment getPayment() {
        return payment;
    }

    public class Payment {
        String paymentID;
        String paydate;
        String paytime;
        String amount;
        String imgPayment;
        String order;

        public String getPaymentID() {
            return paymentID;
        }

        public void setPaymentID(String paymentID) {
            this.paymentID = paymentID;
        }

        public String getPaydate() {
            return paydate;
        }

        public void setPaydate(String paydate) {
            this.paydate = paydate;
        }

        public String getPaytime() {
            return paytime;
        }

        public void setPaytime(String paytime) {
            this.paytime = paytime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getImgPayment() {
            return imgPayment;
        }

        public void setImgPayment(String imgPayment) {
            this.imgPayment = imgPayment;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }
}
