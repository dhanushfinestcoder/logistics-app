package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.VechicleStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@ToString
@Table(name = "vechicles")
public class Vechicles
{

    @Id
    @SequenceGenerator(name = "vid",initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "vid")
    private Long vechicleId;

    private String vechicleType;

    private double vechicleCap;

    @Enumerated(EnumType.STRING)
    private VechicleStatus vechicleStatus;

    @OneToMany(mappedBy = "vechicles",cascade = CascadeType.ALL)
    @JsonBackReference("vechicle-shipment")
    private List<Shipment> shipmentList;

    @OneToOne
    @JoinColumn(name = "driver_id",unique = true)
    @JsonBackReference("driver-vechicle")
    private Driver driver;

    public double getVechicleCap() {
        return vechicleCap;
    }
}
