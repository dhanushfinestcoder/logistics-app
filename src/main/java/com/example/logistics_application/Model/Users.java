package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Uid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String UemailId;
    @Column(unique = true, nullable = false)
    private String Upass;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //one to many for customer
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Orders> ordersList;

    //one to many for
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    private List<Shipment>shipments;





}
