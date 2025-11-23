package com.hms.user.userms.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.user.userms.client.ProfileClient;
import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.dto.UserResponseDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.helper.Roles;
import com.hms.user.userms.model.User;
import com.hms.user.userms.repository.UserRepository;
import com.hms.user.userms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public void registerUser(UserDTO userDTO) throws HsmException {

        // 1. Check email already exists
        Optional<User> opt = userRepository.findByEmail(userDTO.getEmail());
        if (opt.isPresent()) {
            throw new HsmException("USER_ALREADY_EXISTS");
        }

        // 2. Encode password
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // 3. Save user FIRST
        User savedUser = userRepository.save(userDTO.toEntity());

        // 4. NOW userId exists — set it back to DTO
        userDTO.setId(savedUser.getId());

        // 5. NOW call ProfileMS with userId
        if (userDTO.getRole().equals(Roles.OWNER)) {
            profileClient.saveInProfileOwner(userDTO);
        } else {
            profileClient.saveInProfileRenters(userDTO);
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
    public void deleteUser(Long id) throws HsmException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"));
        userRepository.delete(user);
    }

    @Override
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}
