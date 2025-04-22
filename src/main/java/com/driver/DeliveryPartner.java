package com.driver;

public class DeliveryPartner {
    private int totalOrders;
    private String partnerId;

    public DeliveryPartner(String partnerId) {
        this.partnerId = partnerId;
        this.totalOrders = 0;
    }

    public String getId() {
        return partnerId;
    }

    public int getNumberOfOrders() {
        return totalOrders;
    }

    public void setNumberOfOrders(Integer orderCount) {
        this.totalOrders = orderCount;
    }
}