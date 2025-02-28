package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Repository.UserRepo;
import com.example.logistics_application.Service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/orders")
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
    public ResponseEntity<List<Orders>> getOrders()
    {
        List<Orders>orders=ordersService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getThierOrders")
    public List<Orders> getThierOrders(@RequestParam String name)
    {
        Users users=userRepo.findByName(name);
        if(users==null)
        {
            throw new RuntimeException("Users not found");
        }
        Long id=users.getUid();
        System.out.println(id);
        return  ordersService.getThierOrders(id);
    }

    @GetMapping("/getId/{name}")
    public Long getId(@PathVariable String name)
    {
        Users u=userRepo.findByName(name);
        return u.getUid();
    }

    @GetMapping("/getById/{id}")
    public Optional<Orders> getOrder(@PathVariable Long id)
    {
        return ordersService.getById(id);
    }
}
