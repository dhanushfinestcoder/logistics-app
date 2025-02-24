package com.example.logistics_application.Service;

import com.example.logistics_application.ENUM.Role;
import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    private UserRepo userRepo;

//    @Autowired
//    private JWTservice jwTservice;

//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

//    public Users register(Users user)
//    {
//        user.setUpass(encoder.encode(user.getUpass()));
//        userRepo.save(user);
//        return user;
//    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public List<Users> getDrivers(Role role)
    {
        return userRepo.findByRole(role);
    }

//    public String verify(Users user)
//    {
//        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getUpass()));
//        if(authentication.isAuthenticated())
//        {
//            return jwTservice.genrateToken(user.getName());
//        }
//        return "Login Failed";
//    }

}
