package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Vechicles;
import com.example.logistics_application.Service.VechicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/vec")
public class VechicleController {

    @Autowired
    private VechicleService vechicleService;
    @RequestMapping("/getVechicles")
    public List<Vechicles> getVechicles()
    {
        return vechicleService.getVechicles();
    }


}
