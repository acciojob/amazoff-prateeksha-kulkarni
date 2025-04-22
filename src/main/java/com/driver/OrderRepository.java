package com.driver;

import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();
    private final Map<String, DeliveryPartner> partners = new HashMap<>();
    private final Map<String, List<String>> partnerOrders = new HashMap<>();
    private final Set<String> unassignedOrders = new HashSet<>();

    public void addOrder(Order order) {
        String orderId = order.getId();
        orders.put(orderId, order);
        unassignedOrders.add(orderId);
    }

    public void addPartner(String partnerId) {
        partners.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (!orders.containsKey(orderId) || !partners.containsKey(partnerId)) return;

        partnerOrders.computeIfAbsent(partnerId, k -> new ArrayList<>()).add(orderId);
        partners.get(partnerId).setNumberOfOrders(partnerOrders.get(partnerId).size());
        unassignedOrders.remove(orderId);
    }

    public Order getOrderById(String orderId) {
        return orders.getOrDefault(orderId, null);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partners.getOrDefault(partnerId, null);
    }

    public int getOrderCountByPartnerId(String partnerId) {
        return partnerOrders.getOrDefault(partnerId, Collections.emptyList()).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return new ArrayList<>(partnerOrders.getOrDefault(partnerId, new ArrayList<>()));
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orders.keySet());
    }

    public int getCountOfUnassignedOrders() {
        return unassignedOrders.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(Integer time, String partnerId) {
        List<String> assignedOrders = partnerOrders.getOrDefault(partnerId, new ArrayList<>());
        int count = 0;
        for (String orderId : assignedOrders) {
            Order order = orders.get(orderId);
            if (order != null && order.getDeliveryTime() > time) {
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderIds = partnerOrders.getOrDefault(partnerId, new ArrayList<>());
        int lastTime = 0;
        for (String id : orderIds) {
            Order order = orders.get(id);
            if (order != null) {
                lastTime = Math.max(lastTime, order.getDeliveryTime());
            }
        }
        return lastTime;
    }

    public void deletePartnerById(String partnerId) {
        List<String> assignedOrders = partnerOrders.getOrDefault(partnerId, new ArrayList<>());
        unassignedOrders.addAll(assignedOrders);

        partnerOrders.remove(partnerId);
        partners.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orders.remove(orderId);

        if (unassignedOrders.contains(orderId)) {
            unassignedOrders.remove(orderId);
        } else {
            for (Map.Entry<String, List<String>> entry : partnerOrders.entrySet()) {
                List<String> orderList = entry.getValue();
                if (orderList.remove(orderId)) {
                    String partnerId = entry.getKey();
                    partners.get(partnerId).setNumberOfOrders(orderList.size());
                    break;
                }
            }
        }
    }
}
