package com.example.logistics_application.Model;

import com.example.logistics_application.ENUM.OrderStatus;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderId")
public class Orders
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
   // @Column(name = "order_id")
  private Long orderId;
    //@Column(name = "order_picLoc")
  private String pickupLoc;
   // @Column(name = "order_delLoc")
  private String deliveryLoc;
   // @Column(name = "order_wi8")
  private double weight;

  @NonNull
  @Column(nullable = false)
  private String recieverEmail;
  // it will be enum
  @Enumerated(EnumType.STRING)
 // @Column(name = "order_sts")
  private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid",nullable = false)
    @JsonBackReference(value = "user-orders")
    private Users customer;

    //one order -> one shipment
    @OneToOne(mappedBy = "orders",cascade = CascadeType.ALL)
    @JsonBackReference("order-shipment")
    private Shipment shipment;

//    @Override
//    public String toString() {
//        return "Orders{" +
//                "orderId=" + orderId +
//                ", pickupLoc='" + pickupLoc + '\'' +
//                ", deliveryLoc='" + deliveryLoc + '\'' +
//                ", weight=" + weight +
//                ", status=" + status +
//                ",recieverEmail="+recieverEmail+
//                ", customerId=" + (customer != null ? customer.getUid() : "null") + // Prevents recursion
//                '}';
//    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPickupLoc() {
        return pickupLoc;
    }

    public void setPickupLoc(String pickupLoc) {
        this.pickupLoc = pickupLoc;
    }

    public String getDeliveryLoc() {
        return deliveryLoc;
    }

    public void setDeliveryLoc(String deliveryLoc) {
        this.deliveryLoc = deliveryLoc;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getRecieverEmail() {
        return recieverEmail;
    }

    public void setRecieverEmail(String recieverEmail) {
        this.recieverEmail = recieverEmail;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Users getCustomer() {
        return customer;
    }

    public void setCustomer(Users customer) {
        this.customer = customer;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

// one user can place many orders many->one



}
