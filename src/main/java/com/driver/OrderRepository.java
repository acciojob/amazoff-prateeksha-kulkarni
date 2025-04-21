package com.driver;



import com.driver.DeliveryPartner;
import com.driver.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    // Map to store orders with orderId as key
    private Map<String, Order> orderMap;

    // Map to store delivery partners with partnerId as key
    private Map<String, DeliveryPartner> partnerMap;

    // Map to store order-partner pairs with orderId as key and partnerId as value
    private Map<String, String> orderPartnerMap;

    // Map to store partner-orders pairs with partnerId as key and list of orderIds as value
    private Map<String, List<String>> partnerOrderMap;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.orderPartnerMap = new HashMap<>();
        this.partnerOrderMap = new HashMap<>();
    }

    public void addOrder(Order order) {
        orderMap.put(order.getOrderId(), order);
    }

    public void addPartner(String partnerId) {
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
        partnerOrderMap.put(partnerId, new ArrayList<>());
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        // Add orderId to partner's order list
        partnerOrderMap.get(partnerId).add(orderId);

        // Update order-partner mapping
        orderPartnerMap.put(orderId, partnerId);

        // Update order's partnerId field
        orderMap.get(orderId).setPartnerId(partnerId);

        // Update partner's orders list
        partnerMap.get(partnerId).addOrder(orderId);
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return partnerOrderMap.getOrDefault(partnerId, new ArrayList<>()).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public int getCountOfUnassignedOrders() {
        int totalOrders = orderMap.size();
        int assignedOrders = orderPartnerMap.size();
        return totalOrders - assignedOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int count = 0;
        List<String> orderIds = partnerOrderMap.getOrDefault(partnerId, new ArrayList<>());

        for (String orderId : orderIds) {
            Order order = orderMap.get(orderId);
            if (order != null && order.getDeliveryTime().compareTo(time) > 0) {
                count++;
            }
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String lastTime = "00:00";
        List<String> orderIds = partnerOrderMap.getOrDefault(partnerId, new ArrayList<>());

        for (String orderId : orderIds) {
            Order order = orderMap.get(orderId);
            if (order != null && order.getDeliveryTime().compareTo(lastTime) > 0) {
                lastTime = order.getDeliveryTime();
            }
        }

        return lastTime;
    }

    public void deletePartnerById(String partnerId) {
        // Get all orders assigned to this partner
        List<String> orderIds = partnerOrderMap.getOrDefault(partnerId, new ArrayList<>());

        // Unassign all orders from the partner
        for (String orderId : orderIds) {
            orderMap.get(orderId).setPartnerId(null);
            orderPartnerMap.remove(orderId);
        }

        // Remove partner from maps
        partnerMap.remove(partnerId);
        partnerOrderMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        // Check if order is assigned to any partner
        String partnerId = orderPartnerMap.get(orderId);

        if (partnerId != null) {
            // Remove order from partner's list
            partnerOrderMap.get(partnerId).remove(orderId);
            partnerMap.get(partnerId).removeOrder(orderId);
        }

        // Remove order from maps
        orderMap.remove(orderId);
        orderPartnerMap.remove(orderId);
    }
}