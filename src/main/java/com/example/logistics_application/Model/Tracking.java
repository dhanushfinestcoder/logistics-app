package com.example.logistics_application.Model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name="Tracking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Tracking
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long trackingId;

    @ManyToOne
    @JoinColumn(name = "shipmentId",nullable = false)
    private Shipment shipment;
    @Column(nullable = false)
    private String currLoc;
    @Column(nullable = false)
    private Timestamp timestamp;

}
