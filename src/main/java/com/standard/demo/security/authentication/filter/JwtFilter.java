package com.standard.demo.security.authentication.filter;

import com.standard.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService service;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader =  request.getHeader("Authorization");
        String token=null;
        String username=null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = service.getUsername(token);
        }

        if(username!= null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails details = userDetailsService.loadUserByUsername(username);
            if(service.validateToken(token,details)){

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    details,
                        null,
                        details.getAuthorities()
                );

               authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        filterChain.doFilter(request,response);
    }
}
