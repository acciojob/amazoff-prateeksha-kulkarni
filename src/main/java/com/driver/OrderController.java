package com.driver;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    public OrderController() {
        this.orderService = new OrderService();
    }


    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return new ResponseEntity<>("Order added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order result = orderService.getOrderById(orderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders() {
        List<String> allOrders = orderService.getAllOrders();
        return new ResponseEntity<>(allOrders, HttpStatus.OK);
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Order " + orderId + " successfully removed", HttpStatus.OK);
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId) {
        orderService.addPartner(partnerId);
        return new ResponseEntity<>("Delivery partner registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId) {
        partnerId = partnerId.trim();
        System.out.println("Controller: Processing request for partner: '" + partnerId + "'");

        DeliveryPartner partner = orderService.getPartnerById(partnerId);
        if (partner == null) {
            System.out.println("Controller: Partner not found: " + partnerId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        System.out.println("Controller: Retrieved partner: " + partner.getId());
        return new ResponseEntity<>(partner, HttpStatus.OK);
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId) {
        orderService.deletePartner(partnerId);
        return new ResponseEntity<>("Partner " + partnerId + " successfully removed", HttpStatus.OK);
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(
            @RequestParam String orderId,
            @RequestParam String partnerId) {
        orderService.createOrderPartnerPair(orderId, partnerId);
        return new ResponseEntity<>("Order assigned to partner successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId) {
        Integer count = orderService.getOrderCountByPartnerId(partnerId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId) {
        List<String> partnerOrders = orderService.getOrdersByPartnerId(partnerId);
        return new ResponseEntity<>(partnerOrders, HttpStatus.OK);
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders() {
        Integer unassignedCount = orderService.getCountOfUnassignedOrders();
        return new ResponseEntity<>(unassignedCount, HttpStatus.OK);
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{time}/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(
            @PathVariable String time,
            @PathVariable String partnerId) {
        Integer pendingOrders = orderService.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
        return new ResponseEntity<>(pendingOrders, HttpStatus.OK);
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId) {
        String lastDeliveryTime = orderService.getLastDeliveryTimeByPartnerId(partnerId);
        return new ResponseEntity<>(lastDeliveryTime, HttpStatus.OK);
    }
}