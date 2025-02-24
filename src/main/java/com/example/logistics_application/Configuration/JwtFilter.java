//package com.example.logistics_application.Configuration;
//
//import com.example.logistics_application.Service.JWTservice;
//import com.example.logistics_application.Service.OwnUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter
//{
//
//    @Autowired
//    private JWTservice jwTservice;
//
//    @Autowired
//    ApplicationContext context;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
//    {
//       String authHeader=request.getHeader("Authorization");
//       String token="";
//       String Username=null;
//       if(authHeader!=null && authHeader.startsWith("Bearer"))
//        {
//            token=authHeader.substring(7);
//            Username=jwTservice.extractUsername(token);
//            //System.out.println(token+" "+Username);
//        }
//        if(Username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
//        {
//            UserDetails userDetails=context.getBean(OwnUserDetailsService.class).loadUserByUsername(Username);
//            if(jwTservice.validateToken(token,userDetails)){
//                UsernamePasswordAuthenticationToken authtoken=
//                        new UsernamePasswordAuthenticationToken(userDetails,"null",userDetails.getAuthorities());
//                authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authtoken);
//
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}
