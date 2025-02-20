package com.example.logistics_application.Controller;

import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController
{
    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public Users register(@RequestBody Users user)
    {
        return userService.register(user);
    }
   @PostMapping("/login")
   public String login(@RequestBody Users user)
   {
      return userService.verify(user);
   }
    @GetMapping("/users")
    public List<Users> getAllUsers()
    {
        return userService.getAllUsers();
    }

}
