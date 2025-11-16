package com.hms.user.userms.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.service.UserService;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserDTO userDTO = userService.getUser(email);
            return new CustomUserDetail(
                    userDTO.getId(),
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    userDTO.getRole(),
                    userDTO.getEmail(),
                    userDTO.getName(),
                    null);
        } catch (HsmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
