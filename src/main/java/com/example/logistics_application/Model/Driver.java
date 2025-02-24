package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.DriverStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "driver")
public class Driver
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.AVAILABLE; // Available by default

    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    @JsonIgnore
    private Shipment shipment;

    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    @JsonIgnore
    private Vechicles vechicles;


}
