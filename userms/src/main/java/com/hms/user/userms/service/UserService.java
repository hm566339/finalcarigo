package com.hms.user.userms.service;

import com.hms.user.userms.dto.LoginDTO;
import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.exception.HsmException;

public interface UserService {
    public void registerUser(UserDTO userDTO) throws HsmException;

    public UserDTO loginUser(UserDTO userDTO) throws HsmException;

    public UserDTO getUserById(Long id) throws HsmException;

    public void updateUser(UserDTO userDTO);

    public UserDTO getUser(String email) throws HsmException;

    // UserDTO loginUser(LoginDTO loginDTO) throws HsmException;
}
