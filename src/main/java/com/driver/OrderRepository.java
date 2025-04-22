package com.driver;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private Map<String, Order> orderDatabase;
    private Map<String, DeliveryPartner> partnerDatabase;
    private Map<String, Set<String>> partnerOrdersMap;
    private Map<String, String> orderAssignmentMap;

    public OrderRepository() {
        this.orderDatabase = new HashMap<>();
        this.partnerDatabase = new HashMap<>();
        this.partnerOrdersMap = new HashMap<>();
        this.orderAssignmentMap = new HashMap<>();
    }


    public void saveOrder(Order order) {
        orderDatabase.put(order.getId(), order);
    }

    public void savePartner(String partnerId) {
        System.out.println("Creating partner with ID: " + partnerId);
        partnerDatabase.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId) {
        if (!orderDatabase.containsKey(orderId) || !partnerDatabase.containsKey(partnerId)) {
            System.out.println("Cannot assign: Invalid order or partner ID");
            return;
        }

        System.out.println("Assignment: Pre-count for " + partnerId + ": " +
                (partnerDatabase.get(partnerId) != null ?
                        partnerDatabase.get(partnerId).getNumberOfOrders() : "null"));

        if (!partnerOrdersMap.containsKey(partnerId)) {
            partnerOrdersMap.put(partnerId, new HashSet<>());
        }

        partnerOrdersMap.get(partnerId).add(orderId);
        orderAssignmentMap.put(orderId, partnerId);

        DeliveryPartner partner = partnerDatabase.get(partnerId);
        partner.setNumberOfOrders(partnerOrdersMap.get(partnerId).size());

        System.out.println("Assignment: Updated count for " + partnerId + ": " +
                partner.getNumberOfOrders());
    }

    public Order findOrderById(String orderId) {
        System.out.println("Finding order: " + orderId);
        return orderDatabase.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId) {
        System.out.println("Repository: Searching for partner: '" + partnerId + "'");
        System.out.println("Available partners: " + partnerDatabase.keySet());

        DeliveryPartner partner = partnerDatabase.get(partnerId);
        if (partner != null) {
            System.out.println("Partner located: " + partner.getId());
        } else {
            System.out.println("No partner found with ID: '" + partnerId + "'");
        }
        return partner;
    }

    public List<String> findAllOrders() {
        return new ArrayList<>(orderDatabase.keySet());
    }

    public Integer findOrderCountByPartnerId(String partnerId) {
        if (!partnerOrdersMap.containsKey(partnerId)) {
            return 0;
        }
        return partnerOrdersMap.get(partnerId).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId) {
        if (!partnerOrdersMap.containsKey(partnerId)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(partnerOrdersMap.get(partnerId));
    }

    public Integer findCountOfUnassignedOrders() {
        return orderDatabase.size() - orderAssignmentMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {
        if (!partnerOrdersMap.containsKey(partnerId)) {
            System.out.println("Cannot find orders: Partner " + partnerId + " not registered");
            return 0;
        }

        int targetTimeMinutes = parseTimeToMinutes(timeString);
        System.out.println("Analyzing orders after: " + timeString + " (" + targetTimeMinutes + " minutes)");

        int pendingOrders = 0;
        for (String orderId : partnerOrdersMap.get(partnerId)) {
            Order currentOrder = orderDatabase.get(orderId);
            if (currentOrder != null && currentOrder.getDeliveryTime() > targetTimeMinutes) {
                pendingOrders++;
            }
        }

        System.out.println("Pending orders for partner " + partnerId + ": " + pendingOrders);
        return pendingOrders;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        if (!partnerOrdersMap.containsKey(partnerId) || partnerOrdersMap.get(partnerId).isEmpty()) {
            return "00:00";
        }

        int latestDeliveryTime = 0;
        for (String orderId : partnerOrdersMap.get(partnerId)) {
            Order order = orderDatabase.get(orderId);
            if (order != null) {
                latestDeliveryTime = Math.max(latestDeliveryTime, order.getDeliveryTime());
            }
        }

        int hours = latestDeliveryTime / 60;
        int minutes = latestDeliveryTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public void deletePartner(String partnerId) {
        if (!partnerDatabase.containsKey(partnerId)) {
            return;
        }

        if (partnerOrdersMap.containsKey(partnerId)) {
            for (String orderId : partnerOrdersMap.get(partnerId)) {
                orderAssignmentMap.remove(orderId);
            }
            partnerOrdersMap.remove(partnerId);
        }

        partnerDatabase.remove(partnerId);
    }

    public void deleteOrder(String orderId) {
        if (!orderDatabase.containsKey(orderId)) {
            return;
        }

        if (orderAssignmentMap.containsKey(orderId)) {
            String partnerId = orderAssignmentMap.get(orderId);

            if (partnerOrdersMap.containsKey(partnerId)) {
                partnerOrdersMap.get(partnerId).remove(orderId);

                DeliveryPartner partner = partnerDatabase.get(partnerId);
                if (partner != null) {
                    partner.setNumberOfOrders(partnerOrdersMap.get(partnerId).size());
                }
            }

            orderAssignmentMap.remove(orderId);
        }

        orderDatabase.remove(orderId);
    }

    private int parseTimeToMinutes(String timeString) {
        String[] components = timeString.split(":");
        return Integer.parseInt(components[0]) * 60 + Integer.parseInt(components[1]);
    }
}