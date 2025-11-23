package com.hms.user.userms.service;

import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.dto.UserResponseDTO;
import com.hms.user.userms.exception.HsmException;

public interface UserService {
    public void registerUser(UserDTO userDTO) throws HsmException;

    public UserDTO loginUser(UserDTO userDTO) throws HsmException;

    public UserResponseDTO getUserById(Long id) throws HsmException;

    public void updateUser(Long id, UserDTO userDTO);

    public UserDTO getUser(String email) throws HsmException;

    public void deleteUser(Long id) throws HsmException;

    public boolean userExists(Long id);
}
