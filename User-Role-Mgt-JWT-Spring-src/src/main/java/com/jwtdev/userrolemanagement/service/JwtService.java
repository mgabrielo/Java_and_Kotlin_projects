package com.jwtdev.userrolemanagement.service;

import com.jwtdev.userrolemanagement.dao.UserDao;
import com.jwtdev.userrolemanagement.dto.JwtRequest;
import com.jwtdev.userrolemanagement.dto.JwtResponse;
import com.jwtdev.userrolemanagement.entity.User;
import com.jwtdev.userrolemanagement.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    public JwtService(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception{
     String username=    jwtRequest.getUserName();
     String password =  jwtRequest.getUserPassword();
        authenticate(username,password);
       final UserDetails userDetails =  loadUserByUsername(username);

       String newGenToken = jwtUtils.generateToken(userDetails);
       User user = userDao.findById(username).get();

      return new JwtResponse(user, newGenToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userDao.findById(username).get();
      if(user != null){
                return new org.springframework.security.core.userdetails.User(
                        user.getUserName(), user.getUserPassword(), getAuthorities(user)
                );
      }else {
            throw new UsernameNotFoundException("username not valid");
      }
    }

    private Set getAuthorities(User user){
        Set authorities = new HashSet();
        user.getRole().forEach(role-> { authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });
        return authorities;
    }

    private void  authenticate (String username, String password) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e){
            throw new Exception("user is disabled");
        }catch (BadCredentialsException e){
            throw new Exception("bad Credentials from user");
        }
    }



}
