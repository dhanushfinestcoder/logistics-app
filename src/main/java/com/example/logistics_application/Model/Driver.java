package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.DriverStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    @JsonManagedReference("driver-shipments")
    private List<Shipment> shipment;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "vechicle_id")
    @JsonManagedReference("driver-vechicle")
    private Vechicles vechicles;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonManagedReference("user-driver")
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
