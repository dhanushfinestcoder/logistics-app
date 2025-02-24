package com.example.logistics_application.Service;

import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Repository.OrdersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private OrdersRepo ordersRepo;
    public List<Orders> getRecivedOrders() {
        return ordersRepo.findAll();
    }
}
