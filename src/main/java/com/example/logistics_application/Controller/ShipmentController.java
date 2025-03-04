package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Shipment;
import com.example.logistics_application.Service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ship")
public class ShipmentController {
    @Autowired
    private ShipmentService shipmentService;

    @PostMapping("/assign/{id}")
    public Shipment assignDriver(@PathVariable Long id)
    {
        return shipmentService.assignDriver(id);
    }

    @GetMapping("/shipments")
    public List<Shipment>getAllShipments()
    {
        return shipmentService.getShipments();
    }

    @GetMapping("/otp")
    public List<Shipment> getShip(){
        return shipmentService.getInships();
    }

    @GetMapping("/getShipById/{id}")
    public List<Shipment> getShipById(@PathVariable Long id)
    {
        return shipmentService.getByShipId(id);
    }
}
