package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.Role;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="Users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uid")
public class Users
{
    @SequenceGenerator(name = "uidGen",allocationSize = 1,initialValue = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "uidGen")
    private Long uid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String uemailId;
    @Column(unique = true, nullable = false)
    private String upass;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //one to many for customer
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    //@JsonManagedReference(value = "user-orders")
    @JsonIgnore
    private List<Orders> ordersList;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Driver driver;

    //one to many for

    @Override
    public String toString() {
        return "Users{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", uemailId='" + uemailId + '\'' +
                ", upass='" + upass + '\'' +
                ", role=" + role +
                ", ordersList=" + ordersList +
                '}';
    }
}
