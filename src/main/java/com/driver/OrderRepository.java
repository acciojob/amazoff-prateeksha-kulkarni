package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId, partner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            // Add order to partner's order list
            HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
            orders.add(orderId);
            partnerToOrderMap.put(partnerId, orders);

            // Map order to partner
            orderToPartnerMap.put(orderId, partnerId);

            // Update partner's order count
            DeliveryPartner partner = partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
        }
    }

    public Order findOrderById(String orderId) {
        return orderMap.getOrDefault(orderId, null);
    }

    public DeliveryPartner findPartnerById(String partnerId) {
        return partnerMap.getOrDefault(partnerId, null);
    }

    public Integer findOrderCountByPartnerId(String partnerId) {
        if (partnerMap.containsKey(partnerId)) {
            return partnerMap.get(partnerId).getNumberOfOrders();
        }
        return 0;
    }

    public List<String> findOrdersByPartnerId(String partnerId) {
        List<String> orders = new ArrayList<>();
        if (partnerToOrderMap.containsKey(partnerId)) {
            orders.addAll(partnerToOrderMap.get(partnerId));
        }
        return orders;
    }

    public List<String> findAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId) {
        if (partnerMap.containsKey(partnerId)) {
            // Get all orders assigned to this partner
            HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());

            // Remove the partner-order mapping for these orders
            for (String orderId : orders) {
                orderToPartnerMap.remove(orderId);
            }

            // Remove the partner from maps
            partnerToOrderMap.remove(partnerId);
            partnerMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId) {
        if (orderMap.containsKey(orderId)) {
            // Check if order is assigned to any partner
            if (orderToPartnerMap.containsKey(orderId)) {
                String partnerId = orderToPartnerMap.get(orderId);

                // Remove order from partner's order list
                HashSet<String> orders = partnerToOrderMap.get(partnerId);
                orders.remove(orderId);

                // Update partner's order count
                DeliveryPartner partner = partnerMap.get(partnerId);
                partner.setNumberOfOrders(partner.getNumberOfOrders() - 1);

                // Remove order-partner mapping
                orderToPartnerMap.remove(orderId);
            }

            // Remove order from orderMap
            orderMap.remove(orderId);
        }
    }

    public Integer findCountOfUnassignedOrders() {
        return orderMap.size() - orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {
        // Convert given time to minutes
        String[] time = timeString.split(":");
        int givenTime = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);

        int count = 0;
        if (partnerToOrderMap.containsKey(partnerId)) {
            for (String orderId : partnerToOrderMap.get(partnerId)) {
                Order order = orderMap.get(orderId);
                if (order.getDeliveryTime() > givenTime) {
                    count++;
                }
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        int latestTime = 0;

        if (partnerToOrderMap.containsKey(partnerId)) {
            for (String orderId : partnerToOrderMap.get(partnerId)) {
                Order order = orderMap.get(orderId);
                latestTime = Math.max(latestTime, order.getDeliveryTime());
            }
        }

        // Convert minutes back to HH:MM format
        int hours = latestTime / 60;
        int minutes = latestTime % 60;

        String hoursStr = (hours < 10) ? "0" + hours : String.valueOf(hours);
        String minutesStr = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);

        return hoursStr + ":" + minutesStr;
    }
}