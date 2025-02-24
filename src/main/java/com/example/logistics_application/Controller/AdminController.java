package com.example.logistics_application.Controller;

import com.example.logistics_application.ENUM.OrderStatus;
import com.example.logistics_application.ENUM.Role;
import com.example.logistics_application.ENUM.ShipmentStatus;
import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Model.Shipment;
import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Repository.OrdersRepo;
import com.example.logistics_application.Repository.ShipmentRepo;
import com.example.logistics_application.Service.AdminService;
import com.example.logistics_application.Service.ShipmentService;
import com.example.logistics_application.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController
{
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;



    @Autowired
    private ShipmentRepo shipmentRepo;

    @Autowired
    private ShipmentService shipmentService;
    @RequestMapping("/admin/rec-orders")
    public List<Orders> getRecorders()
    {
        return adminService.getRecivedOrders();
    }

    @RequestMapping("/getDrivers")
    public List<Users> getDrivers()
    {
        return userService.getDrivers(Role.DRIVER);
    }

    @PutMapping("/assignDriverVec/{orderid}")
    public ResponseEntity<String>assignDV(@PathVariable Long orderid)
    {
        return shipmentService.assignDriverAndVehicle(orderid);
    }


    @PutMapping("/mark-in-transit/{shipmentId}")
    public ResponseEntity<String> markInTransit(@PathVariable Long shipmentId) {
        Optional<Shipment> optionalShipment = shipmentRepo.findById(shipmentId);

        if (optionalShipment.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found!");
        }

        Shipment shipment = optionalShipment.get();  // Extract the Shipment object

        if (shipment.getStatus() != ShipmentStatus.DISPATCHED) {
            return ResponseEntity.badRequest().body("Shipment is not dispatched yet!");
        }

        shipment.setStatus(ShipmentStatus.IN_TRANSIT);

       // shipment.setInTransitAt(LocalDateTime.now());
        shipmentRepo.save(shipment);

        return ResponseEntity.ok("Shipment is now in transit.");
    }

}
