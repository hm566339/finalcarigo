package com.hms.user.userms.dto;

import com.hms.user.userms.helper.Roles;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Roles role;

}
