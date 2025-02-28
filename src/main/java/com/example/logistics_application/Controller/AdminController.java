package com.example.logistics_application.Controller;

import com.example.logistics_application.ENUM.DriverStatus;
import com.example.logistics_application.ENUM.OrderStatus;
import com.example.logistics_application.ENUM.Role;
import com.example.logistics_application.ENUM.ShipmentStatus;
import com.example.logistics_application.Model.*;
import com.example.logistics_application.Repository.*;
import com.example.logistics_application.Service.AdminService;
import com.example.logistics_application.Service.DriverService;
import com.example.logistics_application.Service.ShipmentService;
import com.example.logistics_application.Service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private  DriverRepo driverRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VechicleRepo vechicleRepo;

    @Autowired
    private ShipmentRepo shipmentRepo;

    BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    @Autowired
    private ShipmentService shipmentService;
    @RequestMapping("/admin/rec-orders")
    public List<Orders> getRecorders()
    {
        return adminService.getRecivedOrders();
    }

    @RequestMapping("/getDrivers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Users> getDrivers()
    {
        return userService.getDrivers(Role.DRIVER);
    }



    @PutMapping("/assignDriverVec/{orderId}")
    public ResponseEntity<String> assignDriverAndVehicle(@PathVariable Long orderId) {
        try {
            return shipmentService.assignDriverAndVehicle(orderId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found: " + orderId);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while assigning a driver.");
        }
    }



    @Transactional
    @PostMapping("/addDV")
    public ResponseEntity<String> addDriverVehicle(@RequestBody Users user) {
        if (user == null || user.getDriver() == null) {
            return ResponseEntity.badRequest().body("Invalid request. Driver details are missing.");
        }

        Driver driver = user.getDriver();
        if (driver.getVechicles() == null) {
            return ResponseEntity.badRequest().body("Vehicle details are missing.");
        }

        // Ensure the user is saved first
        if (user.getUid() == null) {
            user.setRole(Role.DRIVER);
            user.setUpass(encoder.encode(user.getUpass()));
            user = userRepo.save(user);
        } else {
            user = userRepo.findById(user.getUid()).orElseThrow(() ->
                    new RuntimeException("User not found"));
        }

        // Save Driver
        driver.setUser(user);
        driver.setStatus(DriverStatus.AVAILABLE);
        Driver savedDriver = driverRepo.save(driver);

        // Save Vehicle
        Vechicles vechicles = driver.getVechicles();
        vechicles.setDriver(savedDriver);
        Vechicles savedVehicle = vechicleRepo.save(vechicles);

        // Link Vehicle to Driver
        savedDriver.setVechicles(savedVehicle);
        driverRepo.save(savedDriver);

        return ResponseEntity.ok("Driver and vehicle added successfully!");
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
