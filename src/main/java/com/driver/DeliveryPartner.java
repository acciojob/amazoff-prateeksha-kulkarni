package com.driver;


import java.util.ArrayList;
import java.util.List;

public class DeliveryPartner {
    private String partnerId;
    private List<String> orders;

    public DeliveryPartner() {
        this.orders = new ArrayList<>();
    }

    public DeliveryPartner(String partnerId) {
        this.partnerId = partnerId;
        this.orders = new ArrayList<>();
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public void addOrder(String orderId) {
        this.orders.add(orderId);
    }

    public void removeOrder(String orderId) {
        this.orders.remove(orderId);
    }

    public int getNumberOfOrders() {
        return this.orders.size();
    }
}