package com.example.logistics_application.Repository;

import com.example.logistics_application.ENUM.Role;
import com.example.logistics_application.Model.Orders;
import com.example.logistics_application.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Long>
{
    Users findByName(String Uname);
    boolean existsByUemailId(String UemailId);
    List<Users> findByRole(Role role);
}
