package com.driver;

public class Order {
    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        this.id = id;

        // Handle delivery time conversion more robustly
        if(deliveryTime != null) {
            String[] time = deliveryTime.split(":");
            if(time.length == 2) {
                int hour = Integer.parseInt(time[0]);
                int minutes = Integer.parseInt(time[1]);
                this.deliveryTime = hour * 60 + minutes;
            }
        }
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}