package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.DriverStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @SequenceGenerator(name = "DidGen",allocationSize = 1,initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "DidGen")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.AVAILABLE; // Available by default

    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    @JsonBackReference("driver-shipments")
    private Shipment shipment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vechicle_id")
    private Vechicles vechicles;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

//    public Driver(Users savedUser, Vechicles vechicles, DriverStatus driverStatus) {
//        this.user = savedUser;
//        this.vechicles = vechicles;
//        this.status = driverStatus;
//    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", status=" + status +
                ", shipment=" + shipment +
                ", vechicles=" + vechicles +
                ", user=" + user +
                '}';
    }
}
