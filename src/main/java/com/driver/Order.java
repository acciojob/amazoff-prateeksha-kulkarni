package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        this.id = id;
        this.deliveryTime = convertTimeToMinutes(deliveryTime);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    private int convertTimeToMinutes(String timeStr) {
        int colonIndex = timeStr.indexOf(':');
        int hours = Integer.parseInt(timeStr.substring(0, colonIndex));
        int minutes = Integer.parseInt(timeStr.substring(colonIndex + 1));
        return hours * 60 + minutes;
    }
}