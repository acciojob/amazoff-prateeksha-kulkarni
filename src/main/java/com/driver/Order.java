package com.driver;

import java.util.Arrays;
import java.util.List;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String orderId, String time) {
        this.id=id;

        this.deliveryTime=convertTimeToMinutes(time);

    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

    private  int convertTimeToMinutes(String timeStr){
        int colonIndex = timeStr.indexOf(':');
        int hours = Integer.parseInt(timeStr.substring(0, colonIndex));
        int minutes = Integer.parseInt(timeStr.substring(colonIndex + 1));
        return hours * 60 + minutes;
    }



}