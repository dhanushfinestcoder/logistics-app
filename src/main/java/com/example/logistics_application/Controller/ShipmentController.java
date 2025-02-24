package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Shipment;
import com.example.logistics_application.Service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
}
