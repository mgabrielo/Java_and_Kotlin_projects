package com.jwtdev.userrolemanagement.config;

import com.jwtdev.userrolemanagement.service.JwtService;
import com.jwtdev.userrolemanagement.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtUtils jwtUtils;
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
       final String jwtToken ;
       final String userName ;

        if(header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = header.substring(7);
        userName = jwtUtils.getUsernameFromToken(jwtToken);

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails= jwtService.loadUserByUsername(userName);

            if(jwtUtils.validateToken(jwtToken, userDetails)){
               UsernamePasswordAuthenticationToken userAuthToken= new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                userAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
            }
        }
                filterChain.doFilter(request, response);
    }
}
