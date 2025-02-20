package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.VechicleStatus;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vechicleId;


    private String vechicleType;

    private double vechicleCap;

    @Enumerated(EnumType.STRING)
    private VechicleStatus vechicleStatus;

    @OneToMany(mappedBy = "vechicles",cascade = CascadeType.ALL)
    private List<Shipment> shipmentList;
}
