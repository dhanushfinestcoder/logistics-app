package com.example.logistics_application.Repository;

import com.example.logistics_application.Model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment,Long> {

}
