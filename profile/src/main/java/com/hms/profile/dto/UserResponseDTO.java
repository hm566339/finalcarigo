package com.hms.profile.dto;

import com.hms.profile.helper.Roles;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;

}
