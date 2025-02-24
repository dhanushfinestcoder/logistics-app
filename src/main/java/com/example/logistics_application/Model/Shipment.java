package com.example.logistics_application.Model;


import com.example.logistics_application.ENUM.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;
    @ManyToOne
    @JoinColumn(name = "driver_id",nullable = false)
    @JsonBackReference("driver-shipments")
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "vechicle_id",nullable = false)
    @JsonBackReference("vechicle-shipments")
    private  Vechicles vechicles;
    @Column(name = "otp")
    private String otp;
    @OneToOne
    @JoinColumn(name = "order_id",nullable = false)
    @JsonBackReference("order-shipment")
    private Orders orders;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;




}
