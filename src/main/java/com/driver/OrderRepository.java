package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();
    private final Map<String, DeliveryPartner> partners = new HashMap<>();
    private final Map<String, Set<String>> assignedOrders = new HashMap<>();
    private final Map<String, String> orderAssignments = new HashMap<>();

    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        partners.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (!orders.containsKey(orderId) || !partners.containsKey(partnerId)) return;

        assignedOrders.computeIfAbsent(partnerId, k -> new HashSet<>()).add(orderId);
        orderAssignments.put(orderId, partnerId);

        DeliveryPartner partner = partners.get(partnerId);
        partner.setNumberOfOrders(assignedOrders.get(partnerId).size());
    }

    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partners.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return assignedOrders.getOrDefault(partnerId, new HashSet<>()).size();
    }

    public List<String>  getOrdersByPartnerId(String partnerId) {
        return new ArrayList<>(assignedOrders.getOrDefault(partnerId, new HashSet<>()));
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orders.keySet());
    }

    public int getCountOfUnassignedOrders() {
        return orders.size() - orderAssignments.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String timeStr, String partnerId) {
        int thresholdTime = convertTimeStringToMinutes(timeStr);

        int count = 0;
        Set<String> partnerOrders = assignedOrders.getOrDefault(partnerId, new HashSet<>());

        for (String orderId : partnerOrders) {
            Order order = orders.get(orderId);
            if (order != null && order.getDeliveryTime() > thresholdTime) {
                count++;
            }
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int latestTime = 0;

        for (String orderId : assignedOrders.getOrDefault(partnerId, new HashSet<>())) {
            Order order = orders.get(orderId);
            if (order != null) {
                latestTime = Math.max(latestTime, order.getDeliveryTime());
            }
        }

        return formatTime(latestTime);
    }

    public void  deletePartnerById(String partnerId) {
        if (assignedOrders.containsKey(partnerId)) {
            for (String orderId : assignedOrders.get(partnerId)) {
                orderAssignments.remove(orderId);
            }
            assignedOrders.remove(partnerId);
        }
        partners.remove(partnerId);
    }

    public void  deleteOrderById(String orderId) {
        if (!orders.containsKey(orderId)) return;

        if (orderAssignments.containsKey(orderId)) {
            String partnerId = orderAssignments.remove(orderId);
            Set<String> ordersList = assignedOrders.getOrDefault(partnerId, new HashSet<>());
            ordersList.remove(orderId);
            DeliveryPartner partner = partners.get(partnerId);
            if (partner != null) {
                partner.setNumberOfOrders(ordersList.size());
            }
        }

        orders.remove(orderId);
    }

    private int convertTimeStringToMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private String formatTime(int totalMinutes) {
        int hrs = totalMinutes / 60;
        int mins = totalMinutes % 60;
        return String.format("%02d:%02d", hrs, mins);
    }
}
