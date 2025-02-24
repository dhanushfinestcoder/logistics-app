package com.example.logistics_application.Service;

import com.example.logistics_application.Model.Vechicles;
import com.example.logistics_application.Repository.VechicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VechicleService
{

    @Autowired
    private VechicleRepo vechicleRepo;

    public List<Vechicles> getVechicles() {
    return vechicleRepo.findAll();
    }

    public Vechicles addVecicle(Vechicles vechicles) {
        return vechicleRepo.save(vechicles);
    }
}
