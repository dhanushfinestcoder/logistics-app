package com.example.logistics_application.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "orders")
public class Orders
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;
  private String pickupLoc;
  private String deliveryLoc;
  private double weight;

  // it will be enum
  private String status;


  // one user can place many orders many->one
  @ManyToOne
  @JoinColumn(name = "Uid",nullable = false)
  private Users customer;

  //one order -> one shipment
    @OneToOne(mappedBy = "orders",cascade = CascadeType.ALL)
    private Shipment shipment;




}
