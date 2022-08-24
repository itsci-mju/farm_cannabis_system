package com.itsci.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PaymentModel2 {
    private Payment payment;

    public PaymentModel2() {
        payment = new Payment();
    }

    public PaymentModel2(String jsonResponse) {
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
        String amount;
        String imgPayment;
        OrderModel2.Order order;

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

        public OrderModel2.Order getOrder() {
            return order;
        }

        public void setOrder(OrderModel2.Order order) {
            this.order = order;
        }
    }
}
