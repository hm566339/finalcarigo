package com.hms.user.userms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hms.user.userms.dto.LoginDTO;
import com.hms.user.userms.dto.ResponseDTO;
import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.dto.UserResponseDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.jwt.CustomUserDetail;
import com.hms.user.userms.jwt.JwtUtil;
import com.hms.user.userms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) throws HsmException {
        userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO("USER_REGISTERED_SUCCESSFULLY"));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws HsmException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()));
        } catch (AuthenticationException e) {
            throw new HsmException("INVALID_CREDENTIALS");
        }

        // MUST BE CAST TO CustomUserDetail
        CustomUserDetail user = (CustomUserDetail) userDetailService.loadUserByUsername(loginDTO.getEmail());

        // Generate RSA JWT
        final String jwt = jwtUtil.generateToken(user);

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long id) {
        boolean exists = userService.userExists(id);
        return ResponseEntity.ok(exists);
    }

}
