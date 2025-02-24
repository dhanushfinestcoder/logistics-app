package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Repository.UserRepo;
import com.example.logistics_application.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController
{
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserRepo userRepo;



    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody Orders order) {
        if (order.getCustomer() == null || order.getCustomer().getUid() == null) {
            return ResponseEntity.badRequest().body("Customer ID is required!");
        }
        //System.out.println("Order is Placing.........");
        Users existingCustomer = userRepo.findById(order.getCustomer().getUid())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + order.getCustomer().getUid()));
       // System.out.println("Customer "+existingCustomer);
        System.out.println(order.getRecieverEmail());
        order.setCustomer(existingCustomer);
        Orders savedOrder = ordersService.save(order);
       // System.out.println("New Order Received "+savedOrder);
        return ResponseEntity.ok(savedOrder);
    }
    @GetMapping("/getOrders")
    public List<Orders> getOrders()
    {
        return ordersService.getOrders();
    }
}
