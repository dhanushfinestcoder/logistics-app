package com.example.logistics_application.Service;

import com.example.logistics_application.Model.UserPrincipal;
import com.example.logistics_application.Model.Users;
import com.example.logistics_application.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class OwnUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String Uname) throws UsernameNotFoundException {
        Users user=userRepo.findByName(Uname);
        return new UserPrincipal(user);
    }
}
