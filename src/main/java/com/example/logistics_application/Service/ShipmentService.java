package com.example.logistics_application.Service;

import com.example.logistics_application.ENUM.DriverStatus;
import com.example.logistics_application.ENUM.OrderStatus;
import com.example.logistics_application.ENUM.ShipmentStatus;
import com.example.logistics_application.ENUM.VechicleStatus;
import com.example.logistics_application.Model.Driver;
import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Model.Shipment;
import com.example.logistics_application.Model.Vechicles;
import com.example.logistics_application.Repository.DriverRepo;
import com.example.logistics_application.Repository.OrdersRepo;
import com.example.logistics_application.Repository.ShipmentRepo;
import com.example.logistics_application.Repository.VechicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ShipmentService {

    private static final Logger LOGGER = Logger.getLogger(ShipmentService.class.getName());

    @Autowired
    private OrdersRepo ordersRepo;

    @Autowired
    private VechicleRepo vechicleRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Autowired
    private EmailService emailService;

    /**
     * Assigns the first available driver to the shipment.
     */
    public Shipment assignDriver(Long id) {
        Orders order = ordersRepo.findById(id).orElseThrow(() -> new RuntimeException("Order Not Found"));

        List<Driver> availableDrivers = driverRepo.findFirstByStatusAndVechiclesIsNotNull(DriverStatus.AVAILABLE);
        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No available drivers with assigned vehicles.");
        }

        Driver selectedDriver = availableDrivers.get(0);
        Vechicles vehicle = selectedDriver.getVechicles();

        Shipment shipment = new Shipment();
        shipment.setDriver(selectedDriver);
        shipment.setOrders(order);
        shipment.setVechicles(vehicle);
        shipment.setStatus(ShipmentStatus.DISPATCHED);

        return shipmentRepo.save(shipment);
    }


    public List<Shipment> getShipments() {
        return shipmentRepo.findAll();
    }

    @Transactional
    public ResponseEntity<String> assignDriverAndVehicle(Long orderId) {
        Orders order = ordersRepo.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId));

        double orderWeight = order.getWeight();

        List<Vechicles> availableVehicles = getAvailableVehicles(orderWeight);

        if (availableVehicles.isEmpty()) {
            LOGGER.warning("No available vehicles for order ID: " + orderId);
            return ResponseEntity.badRequest().body("No available vehicles matching order requirements.");
        }

        Vechicles selectedVehicle = availableVehicles.get(0);
        if (shipmentRepo.existsByVechiclesAndStatus(selectedVehicle, ShipmentStatus.DISPATCHED)) {
            return ResponseEntity.badRequest().body("Selected vehicle is already assigned to an active shipment.");
        }
        List<Driver> availableDrivers = driverRepo.findByStatus(DriverStatus.AVAILABLE).stream()
                .filter(driver -> driver.getVechicles() != null &&
                        driver.getVechicles().getVechicleCap() >= orderWeight)
                .toList();

        if (availableDrivers.isEmpty()) {
            LOGGER.warning("No available drivers with a suitable vehicle for order ID: " + orderId);
            return ResponseEntity.badRequest().body("No available drivers with a vehicle that meets the order weight.");
        }

        Driver selectedDriver = availableDrivers.get(0);

        if (shipmentRepo.existsByDriverAndStatus(selectedDriver, ShipmentStatus.DISPATCHED)) {
            return ResponseEntity.badRequest().body("Selected driver is already assigned to an active shipment.");
        }

        order.setStatus(OrderStatus.DISPATCHED);
        selectedVehicle.setVechicleStatus(VechicleStatus.IN_USE);
        selectedDriver.setStatus(DriverStatus.UNAVAILABLE);
        String otp = emailService.generateOtp();
        emailService.sendOtpEmail(order.getRecieverEmail(), otp);
        Shipment shipment = new Shipment();
        shipment.setOrders(order);
        shipment.setVechicles(selectedVehicle);
        shipment.setDriver(selectedDriver);
        shipment.setOtp(otp);
        shipment.setStatus(ShipmentStatus.DISPATCHED);
        shipmentRepo.save(shipment);
        vechicleRepo.save(selectedVehicle);
        driverRepo.save(selectedDriver);
        LOGGER.info("Driver and vehicle assigned successfully for order ID: " + orderId);
        return ResponseEntity.ok("Driver and vehicle assigned successfully!");
    }

    private List<Vechicles> getAvailableVehicles(double orderWeight) {
        if (orderWeight <= 25) {
            return vechicleRepo.findByVechiclesTypeAndStatus("Bike", VechicleStatus.AVAILABLE);
        } else if (orderWeight <= 500) {
            return vechicleRepo.findByVechiclesTypeAndStatus("Van", VechicleStatus.AVAILABLE);
        } else {
            return vechicleRepo.findByVechiclesTypeAndStatus("Truck", VechicleStatus.AVAILABLE);
        }
    }



}
