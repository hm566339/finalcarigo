package com.hms.user.userms.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.model.User;
import com.hms.user.userms.repository.UserRepository;
import com.hms.user.userms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDTO userDTO) throws HsmException {
        Optional<User> opt = userRepository.findByEmail(userDTO.getEmail());
        if (opt.isPresent()) {
            throw new HsmException("USER_ALREADY_EXISTS");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO.toEntity());
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
    public UserDTO getUserById(Long id) throws HsmException {
        return userRepository.findById(id)
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"))
                .toDTO();
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public UserDTO getUser(String email) throws HsmException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new HsmException("USER_NOT_FOUND"));
        return user.toDTO();
    }

}
