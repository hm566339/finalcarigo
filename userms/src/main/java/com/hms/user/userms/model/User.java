package com.hms.user.userms.model;

import com.hms.user.userms.dto.UserDTO;
import com.hms.user.userms.helper.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private Roles role;

    @Column(nullable = false)
    private boolean blocked = false;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = generateRandomId();
        }
    }

    private Long generateRandomId() {
        long min = 100000;
        long max = 999999;
        return min + (long) (Math.random() * (max - min));
    }

    public UserDTO toDTO() {
        return new UserDTO(this.id, this.name, this.email, this.password, this.role, this.blocked);
    }
}
