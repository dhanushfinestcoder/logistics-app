package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Vechicles;
import com.example.logistics_application.Service.VechicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VechicleController {

    @Autowired
    private VechicleService vechicleService;
    @RequestMapping("/getVechicles")
    public List<Vechicles> getVechicles()
    {
        return vechicleService.getVechicles();
    }

    @PostMapping("/addV")
    public Vechicles addNewVechcicle(@RequestBody Vechicles vechicles)
    {
        return vechicleService.addVecicle(vechicles);
    }
}
