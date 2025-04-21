package com.driver;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {


    HashMap<String, Order> orderDb = new HashMap<>();

    HashMap<String, DeliveryPartner> partnerDb = new HashMap<>();

    HashMap<String, List<String>> orderPartnerPair = new HashMap<>();

    HashSet<String> isOrderAssigned = new HashSet<>();


    public void addOrder(Order order){
        String key = order.getId();
        orderDb.put(key,order);
        isOrderAssigned.add(order.getId());
    }

    public void addPartner(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        List<String> list = orderPartnerPair.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);

        DeliveryPartner partner = partnerDb.get(partnerId);
        int noOfOrders = partner.getNumberOfOrders();
        partner.setNumberOfOrders(noOfOrders+1);

        orderPartnerPair.put(partnerId, list);
        isOrderAssigned.remove(orderId);
    }

    public Order getOrderById(String orderId){
        Order order = orderDb.get(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId){
        DeliveryPartner partner = partnerDb.get(partnerId);
        return partner;
    }

    public int getOrderCountByPartnerId(String partnerId){

        int orderCount = orderPartnerPair.get(partnerId).size();
        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> list = orderPartnerPair.getOrDefault(partnerId, new ArrayList<>());
        return list;

    }

    public List<String> getAllOrders(){
        List<String> list = new ArrayList<>();

        for(String orderId: orderDb.keySet()){
            list.add(orderId);
        }

        return list;
    }


    public int getCountOfUnassignedOrders(){
//        int count = 0;
//
//        for(String orderId : orderDb.keySet()){
//            boolean contains = false;
//            for(String partnerId : orderPartnerPair.keySet()){
//                List<String> list = orderPartnerPair.get(partnerId);
//                if(list.contains(orderId)){
//                    contains = true;
//                }
//            }
//            if(contains == true){
//                count++;
//            }
//        }
//        return count;
        return isOrderAssigned.size();
    }


    public int getOrdersLeftAfterGivenTimeByPartnerId(Integer time, String partnerId){
        List<String> list = orderPartnerPair.getOrDefault(partnerId, new ArrayList<>());

        int count = 0;

        for(String orderId : list){
            Order order = orderDb.get(orderId);
            if(order.getDeliveryTime() > time){
                count++;
            }
        }
        return count;
    }


    public int getLastDeliveryTimeByPartnerId(String partnerId){
        List<String> list = orderPartnerPair.getOrDefault(partnerId, new ArrayList<>());

        int timeInt = 0;
        for(String orderId : list){
            Order order = orderDb.get(orderId);
            if(order.getDeliveryTime() > timeInt){
                timeInt = order.getDeliveryTime();
            }
        }
        return timeInt;
    }


    public void deletePartnerById(String partnerId){
        if(!orderPartnerPair.isEmpty()){
            isOrderAssigned.addAll(orderPartnerPair.get(partnerId));
        }
        orderPartnerPair.remove(partnerId);
        partnerDb.remove(partnerId);
    }


    public void deleteOrderById(String orderId){

//        for(String partnerId : orderPartnerPair.keySet()){
//            List<String> list = orderPartnerPair.get(partnerId);
//            if(list.contains(orderId)){
//                list.remove(orderId);
//                orderPartnerPair.put(partnerId, list);
//
////                updating number of orders of the delivery partner
//                DeliveryPartner partner = partnerDb.get(partnerId);
//                int noOfOrder = partner.getNumberOfOrders();
//                partner.setNumberOfOrders(noOfOrder-1);
//
//                break;
//            }
//        }

        if(orderDb.containsKey(orderId)){
            if(isOrderAssigned.contains(orderId)){
                isOrderAssigned.remove(orderId);
            }
            else{
                for(String partnerId : orderPartnerPair.keySet()){
                    List<String> list = orderPartnerPair.get(partnerId);
                    if(list.contains(orderId)){
                        list.remove(orderId);
                    }
                }
            }
        }

    }
}