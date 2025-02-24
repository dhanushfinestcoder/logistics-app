package com.example.logistics_application.Service;

import com.example.logistics_application.ENUM.DriverStatus;
import com.example.logistics_application.Model.Driver;
import com.example.logistics_application.Repository.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepo driverRepo;
    public List<Driver> getDrivers(DriverStatus driverStatus) {
        return driverRepo.findByStatus(driverStatus);
    }

    public Driver addDriver(Driver driver) {
      return driverRepo.save(driver);
    }
}
