package com.hms.user.userms.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.user.userms.client.OwnerClient;
import com.hms.user.userms.client.RenterClient;
import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.dto.UserResponseDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.helper.Roles;
import com.hms.user.userms.jwt.JwtDecoder;
import com.hms.user.userms.model.User;
import com.hms.user.userms.repository.UserRepository;
import com.hms.user.userms.service.UserService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final OwnerClient ownerClient;
    private final RenterClient renterClient;
    private final JwtDecoder jwtDecoder;

    @Transactional
    @Override
    public void registerUser(UserDTO userDTO) {

        log.info("Register API received for email: {}", userDTO.getEmail());

        Optional<User> opt = userRepository.findByEmail(userDTO.getEmail());
        if (opt.isPresent()) {
            log.warn("User already exists: {}", userDTO.getEmail());
            throw new RuntimeException("USER_ALREADY_EXISTS");
        }

        // Encode password
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Save user in DB and get saved entity with ID
        User savedUser = userRepository.save(userDTO.toEntity());
        log.info("User successfully registered: {}", userDTO.getEmail());

        // Convert to response DTO so we can call profile service
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setName(savedUser.getName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());

        // Call Owner/Profile microservice
        try {
            switch (savedUser.getRole().name()) {

                case "OWNER":
                    ownerClient.createOwnerProfile(responseDTO);
                    log.info("Owner profile created for user ID: {}", savedUser.getId());
                    break;

                case "REANT": // ✔ Correct spelling
                    renterClient.createRenterProfile(responseDTO);
                    log.info("Renter profile created for user ID: {}", savedUser.getId());
                    break;

                default:
                    log.info("No profile creation needed for role: {}", savedUser.getRole().name());
            }

        } catch (Exception e) {
            log.error("Profile service failed: {}", e.getMessage());
            throw new RuntimeException("PROFILE_SERVICE_ERROR"); // ✔ rollback
        }

        // If role is ADMIN (no profile created)
        if (savedUser.getRole() == Roles.ADMIN) {
            userRepository.save(savedUser); // Just to be explicit
            log.info("Admin registered. No profile service call needed for ID: {}", savedUser.getId());
        }
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) throws HsmException {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"));
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new HsmException("INVALID_CREDENTIALS");
        }
        user.setPassword(null);
        return user.toDTO();
    }

    @Override
    public UserResponseDTO getUserById(Long id) throws HsmException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"));

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());

        return responseDTO;
    }

    @Override
    public void updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setRole(userDTO.getRole());

        userRepository.save(user);

    }

    @Override
    public UserDTO getUser(String email) throws HsmException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"));
        return user.toDTO();
    }

    @Override
    public void deleteUser(Long id, String token) throws HsmException {

        if (token == null || token.isBlank()) {
            throw new HsmException("MISSING_AUTH_TOKEN");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"));

        String role = jwtDecoder.extractRole(token).trim().toUpperCase();

        log.info("Deleting user {} with role {}", id, role);

        try {

            switch (role) {
                case "OWNER":
                    ownerClient.deleteOwner(id);
                    log.info("Deleted associated owner data for user {}", id);
                    break;

                case "REANT":
                    renterClient.deleteRenter(id);
                    log.info("Deleted associated renter data for user {}", id);
                    break;
                case "ADMIN":
                    if (user.getRole().equals(Roles.OWNER.name())) {
                        ownerClient.deleteOwner(id);
                        log.info("Admin deleted associated owner data for user {}", id);
                    } else if (user.getRole().equals(Roles.REANT.name())) {
                        renterClient.deleteRenter(id);
                        log.info("Admin deleted associated renter data for user {}", id);

                    }
                    log.info("Admin user deletion - no associated data to delete for user {}", id);
                    break;

                default:
                    log.warn("No associated data to delete for role: {}", role);
            }

            // user delete
            userRepository.deleteById(id);
            log.info("User {} deleted successfully", id);

        } catch (Exception e) {

            log.error("Error deleting associated data for user {}: {}", id, e.getMessage());
            throw new HsmException("FAILED_TO_DELETE_ASSOCIATED_DATA");

        }
    }

    @Override
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll().stream().map(user -> {
            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(user.getId());
            responseDTO.setName(user.getName());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setRole(user.getRole());
            return responseDTO;
        }).toList();
    }

}
