package com.hms.user.userms.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hms.user.userms.dto.LoginDTO;
import com.hms.user.userms.dto.ResponseDTO;
import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.dto.UserResponseDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.jwt.CustomUserDetail;
import com.hms.user.userms.jwt.JwtUtil;
import com.hms.user.userms.model.PasswordResetOtp;
import com.hms.user.userms.model.User;
import com.hms.user.userms.repository.PasswordResetOtpRepository;
import com.hms.user.userms.repository.UserRepository;
import com.hms.user.userms.service.EmailService;
import com.hms.user.userms.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    // ---------------- Dependencies ----------------

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordResetOtpRepository otpRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------------- Register API ----------------

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) throws HsmException {

        log.info("Register request received for: {}", userDTO.getEmail());
        userService.registerUser(userDTO);
        log.info("User registered: {}", userDTO.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO("USER_REGISTERED_SUCCESSFULLY"));
    }

    // ---------------- Login API ----------------

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws HsmException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (AuthenticationException e) {
            throw new HsmException("INVALID_CREDENTIALS");
        }

        CustomUserDetail userDetails = (CustomUserDetail) userDetailService.loadUserByUsername(loginDTO.getEmail());

        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);
    }

    // ---------------- Send OTP API ----------------

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setEmail(email);
        resetOtp.setOtp(otp);
        resetOtp.setExpiry(LocalDateTime.now().plusMinutes(10));

        otpRepo.save(resetOtp);

        emailService.sendOtpEmail(email, otp);

        log.info("OTP sent to: {}", email);
        return ResponseEntity.ok("OTP_SENT_TO_EMAIL");
    }

    // ---------------- Verify OTP + Reset Password API ----------------

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String otp = req.get("otp");
        String newPassword = req.get("newPassword");

        PasswordResetOtp savedOtp = otpRepo.findById(email)
                .orElseThrow(() -> new RuntimeException("OTP_NOT_FOUND"));

        if (!savedOtp.getOtp().equals(otp)) {
            throw new RuntimeException("INVALID_OTP");
        }

        if (savedOtp.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP_EXPIRED");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpRepo.delete(savedOtp);

        log.info("Password reset for: {}", email);
        return ResponseEntity.ok("PASSWORD_RESET_SUCCESS");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseDTO> deleteUser(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) throws HsmException {

        log.info("Delete request received for userId: {}", userId);

        userService.deleteUser(userId, token); // <-- FIXED

        log.info("User deleted with userId: {}", userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO("USER_DELETED_SUCCESSFULLY"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> healthCheck() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("update/e&n/{id}")
    public ResponseEntity<String> updateEmail_name(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {

        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countAllUsers() {
        return ResponseEntity.ok(userService.countAllUsers());
    }

    @GetMapping("/count/blocked")
    public ResponseEntity<Long> countBlockedUsers() {
        return ResponseEntity.ok(userService.countBlockedUsers());
    }

}
