package com.example.logistics_application.Service;

import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Repository.OrdersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
     @Autowired
    private OrdersRepo ordersRepo;

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
}
