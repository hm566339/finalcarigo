package com.hms.user.userms.service;

import java.util.List;

import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.dto.UserResponseDTO;
import com.hms.user.userms.exception.HsmException;
import com.hms.user.userms.model.User;

public interface UserService {
    void registerUser(UserDTO userDTO) throws HsmException;

    UserDTO loginUser(UserDTO userDTO) throws HsmException;

    UserResponseDTO getUserById(Long id) throws HsmException;

    String updateUser(Long id, UserDTO userDTO);

    UserDTO getUser(String email) throws HsmException;

    void deleteUser(Long id, String token) throws HsmException;

    boolean userExists(Long id);

    List<UserResponseDTO> getAllUsers();

    long countAllUsers();

    long countBlockedUsers();
}
