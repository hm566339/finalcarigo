package com.hms.user.userms.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hms.user.userms.helper.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetail implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Roles role;
    private String email;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;

}
