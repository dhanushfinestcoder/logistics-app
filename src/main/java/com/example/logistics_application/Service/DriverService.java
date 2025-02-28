package com.example.logistics_application.Service;

import com.example.logistics_application.ENUM.DriverStatus;
import com.example.logistics_application.ENUM.Role;
import com.example.logistics_application.Model.Driver;
import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Model.Vechicles;
import com.example.logistics_application.Repository.DriverRepo;
import com.example.logistics_application.Repository.UserRepo;
import com.example.logistics_application.Repository.VechicleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private UserRepo userRepo;



    @Autowired
    private VechicleRepo vechicleRepo;

    public List<Driver> getDrivers(DriverStatus driverStatus) {
        return driverRepo.findByStatus(driverStatus);
    }

    public Driver addDriver(Driver driver) {

        return driverRepo.save(driver);
    }
    @Transactional
    public Driver addDriverVehicle(Users user, Vechicles vechicles) {
        // Validate input
        if (user == null || vechicles == null) {
            throw new IllegalArgumentException("User and Vehicle details must not be null.");
        }


        if (userRepo.existsByUemailId(user.getUemailId())) {
            throw new RuntimeException("User with email " + user.getUemailId() + " already exists.");
        }

        user.setRole(Role.DRIVER);
        Users savedUser = userRepo.save(user);
        System.out.println("User Saved: " + savedUser);

        // Check if driver already exists for this user
        if (driverRepo.existsByUser(savedUser)) {
            throw new RuntimeException("Driver with email " + savedUser.getUemailId() + " already exists.");
        }

        Driver driver = new Driver();
        driver.setUser(savedUser);
        driver.setStatus(DriverStatus.AVAILABLE);

        // Save the driver
        Driver savedDriver = driverRepo.save(driver);
        System.out.println("Driver Saved: " + savedDriver);

        // Associate vehicle with driver
        vechicles.setDriver(savedDriver);
        Vechicles savedVec = vechicleRepo.save(vechicles);
        System.out.println("Vehicle Saved: " + savedVec);

        // Link vehicle to driver and save
        savedDriver.setVechicles(savedVec);
        driverRepo.save(savedDriver);

        return savedDriver;
    }


    public List<Driver> findAvaiableDrivers(DriverStatus driverStatus) {
        return  driverRepo.findByStatus(driverStatus);
    }
}