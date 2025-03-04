package com.example.logistics_application.Repository;

import com.example.logistics_application.ENUM.ShipmentStatus;
import com.example.logistics_application.Model.Driver;
import com.example.logistics_application.Model.Shipment;
import com.example.logistics_application.Model.Vechicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment,Long> {

    boolean existsByVechicles(Vechicles vechicles);
    boolean existsByDriver(Driver driver);

    boolean existsByVechiclesAndStatus(Vechicles selectedVehicle, ShipmentStatus shipmentStatus);

    boolean existsByDriverAndStatus(Driver selectedDriver, ShipmentStatus shipmentStatus);

    List<Shipment> findByStatus(ShipmentStatus shipmentStatus);

    List<Shipment> findAllByDriverId(Long id);
}
