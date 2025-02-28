package com.example.logistics_application.Repository;

import com.example.logistics_application.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepo extends JpaRepository<Orders,Long>
{
     Optional<Orders> findById(Long id);

    List<Orders> findByCustomerUid(Long id);
}
