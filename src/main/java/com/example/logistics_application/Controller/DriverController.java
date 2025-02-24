package com.example.logistics_application.Controller;

import com.example.logistics_application.ENUM.DriverStatus;
import com.example.logistics_application.ENUM.OrderStatus;
import com.example.logistics_application.ENUM.ShipmentStatus;
import com.example.logistics_application.ENUM.VechicleStatus;
import com.example.logistics_application.Model.Driver;
import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Model.Shipment;
import com.example.logistics_application.Model.Vechicles;
import com.example.logistics_application.Repository.OrdersRepo;
import com.example.logistics_application.Repository.ShipmentRepo;
import com.example.logistics_application.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DriverController
{
    @Autowired
    private DriverService driverService;


    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private ShipmentRepo shipmentRepo;
    @RequestMapping("/drivers")
    public List<Driver> getDriver()
    {
      return driverService.getDrivers(DriverStatus.AVAILABLE);
    }

    @PostMapping("/addDriver")
    public Driver addDriver(@RequestBody Driver driver)
    {
        return driverService.addDriver(driver);
    }

    @PutMapping("/verify-otp/{shipmentId}")
    public ResponseEntity<String> verifyOtpAndDeliver(@PathVariable Long shipmentId, @RequestParam String otp) {
        Optional<Shipment> optionalShipment = shipmentRepo.findById(shipmentId);



        if (optionalShipment.isEmpty()) {
            return ResponseEntity.badRequest().body("Shipment not found!");
        }

        Shipment shipment = optionalShipment.get();

        if (!shipment.getStatus().equals(ShipmentStatus.IN_TRANSIT)) {
            return ResponseEntity.badRequest().body("Shipment is not in transit!");
        }

        if (!shipment.getOtp().equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP! Please try again.");
        }
        shipment.setStatus(ShipmentStatus.DELIVERED);
        Orders order=shipment.getOrders();
        Vechicles vechicles=shipment.getVechicles();
        vechicles.setVechicleStatus(VechicleStatus.AVAILABLE);
        Driver driver=shipment.getDriver();
        driver.setStatus(DriverStatus.AVAILABLE);
        order.setStatus(OrderStatus.DELIVERED);
        ordersRepo.save(order);
        shipmentRepo.save(shipment);


        return ResponseEntity.ok("Order successfully delivered!");
    }

}
