package com.example.logistics_application.Repository;

import com.example.logistics_application.ENUM.VechicleStatus;
import com.example.logistics_application.Model.Vechicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VechicleRepo extends JpaRepository<Vechicles,Long>
{
    @Query("SELECT v FROM Vechicles v WHERE v.vechicleType = :type AND v.vechicleStatus = 'AVAILABLE'")
    List<Vechicles> findByVechiclesTypeAndStatus(@Param("type") String type, @Param("status") VechicleStatus status);

}
