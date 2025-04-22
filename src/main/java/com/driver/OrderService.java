package com.driver;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    public OrderService() {
        this.repository = new OrderRepository();
    }


    public void addOrder(Order order) {
        repository.saveOrder(order);
    }

    public Order getOrderById(String orderId) {
        return repository.findOrderById(orderId);
    }

    public List<String> getAllOrders() {
        return repository.findAllOrders();
    }

    public void deleteOrder(String orderId) {
        repository.deleteOrder(orderId);
    }

    public void addPartner(String partnerId) {
        System.out.println("Registering new partner");
        repository.savePartner(partnerId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        String trimmedId = partnerId.trim(); // Preserving important behavior
        System.out.println("Service: Looking for delivery partner: '" + trimmedId + "'");
        return repository.findPartnerById(trimmedId);
    }

    public void deletePartner(String partnerId) {
        repository.deletePartner(partnerId);
    }

    public void createOrderPartnerPair(String orderId, String partnerId) {
        repository.saveOrderPartnerMap(orderId, partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return repository.findOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return repository.findOrdersByPartnerId(partnerId);
    }

    public Integer getCountOfUnassignedOrders() {
        return repository.findCountOfUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        return repository.findOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        return repository.findLastDeliveryTimeByPartnerId(partnerId);
    }
}