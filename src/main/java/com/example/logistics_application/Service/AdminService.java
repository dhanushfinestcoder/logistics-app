package com.example.logistics_application.Service;

import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Repository.OrdersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private OrdersRepo ordersRepo;
    public List<Orders> getRecivedOrders() {
        return ordersRepo.findAll();
    }

    public Map<String, Long> getOrderanlytics() {
        Map<String,Long> anl=new HashMap<>();
        anl.put("PendingOrders",ordersRepo.countPendingOrders());
        anl.put("DispatchedOrders",ordersRepo.countDispatchedOrders());
        anl.put("DeliveredOrders",ordersRepo.countDeliveredOrders());
        return anl;
    }
}
