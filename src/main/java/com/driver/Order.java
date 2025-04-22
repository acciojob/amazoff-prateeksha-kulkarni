package com.driver;

public class Order {
    private int timeInMinutes;
    private String orderId;

    public Order(String orderId, String deliveryTime) {
        this.orderId = orderId;
        this.timeInMinutes = parseDeliveryTime(deliveryTime);
    }

    private int parseDeliveryTime(String timeString) {
        String[] hourAndMinute = timeString.split(":");
        int hourComponent = Integer.parseInt(hourAndMinute[0]);
        int minuteComponent = Integer.parseInt(hourAndMinute[1]);

        return hourComponent * 60 + minuteComponent;
    }

    public String getId() {
        return orderId;
    }

    public int getDeliveryTime() {
        return timeInMinutes;
    }
}