package com.example.logistics_application.Service;

import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Repository.OrdersRepo;
import com.example.logistics_application.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
     @Autowired
    private OrdersRepo ordersRepo;

     @Autowired
     private UserRepo userRepo;

    public Orders placeOrder(Orders orders)
    {

        return ordersRepo.save(orders);
    }

    public List<Orders> getOrders() {

        return ordersRepo.findAll();
    }

    public Orders save(Orders order) {
        return ordersRepo.save(order);
    }

    public List<Orders> getThierOrders(Long id) {
        return ordersRepo.findByCustomerUid(id);
    }

    public Optional<Orders> getById(Long id) {
        return ordersRepo.findById(id);
    }
}
