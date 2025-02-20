package com.example.logistics_application.Model;


import com.example.logistics_application.ENUM.ShipmentStatus;
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
    private Users driver;
    @ManyToOne
    @JoinColumn(name = "vechicle_id",nullable = false)
    private  Vechicles vechicles;
    @OneToOne
    @JoinColumn(name = "order_id",nullable = false)
    private Orders orders;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
    @OneToMany(mappedBy = "shipment",cascade = CascadeType.ALL)
    private List<Tracking> trackingList;



}
