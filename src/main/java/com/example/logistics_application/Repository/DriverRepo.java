package com.example.logistics_application.Repository;

import com.example.logistics_application.ENUM.DriverStatus;
import com.example.logistics_application.Model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepo extends JpaRepository<Driver,Long> {
    List<Driver> findFirstByStatusAndVechiclesIsNotNull(DriverStatus status);
    List<Driver> findByStatus(DriverStatus driverStatus);
}
