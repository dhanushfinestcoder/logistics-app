package com.example.logistics_application.Repository;

import com.example.logistics_application.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users,Long>
{
    Users findByName(String Uname);
}
