package com.example.logistics_application.Service;

import com.example.logistics_application.ENUM.DriverStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * Fetch all shipments.
     */
    public List<Shipment> getShipments() {
        return shipmentRepo.findAll();
    }

    /**
     * Assigns a driver and a vehicle to an order based on the order's weight.
     */
    public ResponseEntity<String> assignDriverAndVehicle(Long orderId) {
        Orders order = ordersRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        double orderWeight = order.getWeight();

        List<Vechicles> availableVehicles;
        if (orderWeight <= 25) {
            availableVehicles = vechicleRepo.findByVechicleTypeAndStatus("Bike", VechicleStatus.AVAILABLE);
        } else if (orderWeight <= 500) {
            availableVehicles = vechicleRepo.findByVechicleTypeAndStatus("Van", VechicleStatus.AVAILABLE);
        } else {
            availableVehicles = vechicleRepo.findByVechicleTypeAndStatus("Truck", VechicleStatus.AVAILABLE);
        }

        if (availableVehicles.isEmpty()) {
            LOGGER.warning("No vehicles available for order ID: " + orderId);
            return ResponseEntity.badRequest().body("No vehicles available for this order");
        }

        Vechicles selectedVehicle = availableVehicles.get(0);

        List<Driver> availableDrivers = driverRepo.findByStatus(DriverStatus.AVAILABLE);
        if (availableDrivers.isEmpty()) {
            LOGGER.warning("No available drivers for order ID: " + orderId);
            return ResponseEntity.badRequest().body("No available drivers");
        }

        Driver selectedDriver = availableDrivers.get(0);
        selectedVehicle.setDriver(selectedDriver);
        selectedVehicle.setVechicleStatus(VechicleStatus.IN_USE);

        selectedDriver.setStatus(DriverStatus.UNAVAILABLE);

        String otp= emailService.generateOtp();
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


}
