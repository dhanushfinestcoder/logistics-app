package com.example.logistics_application.Model;


import com.example.logistics_application.ENUM.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="shipment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Shipment
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long shipmentId;
    @OneToOne
    @JoinColumn(name = "driver_id",nullable = false,unique = true)
    @JsonManagedReference("driver-shipments")
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "vechicle_id",nullable = false)
    @JsonManagedReference("vechicle-shipments")
    private  Vechicles vechicles;
    @Column(name = "otp")
    private String otp;
    @OneToOne
    @JoinColumn(name = "order_id",nullable = false)
    @JsonManagedReference("order-shipment")
    private Orders orders;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;




}
