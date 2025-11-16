package com.hms.profile.model;

import java.time.LocalDate;
import com.hms.profile.dto.BloodGroup;
import com.hms.profile.dto.OwnnerDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ownner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private LocalDate dob;
    private String phone;

    private String address;

    @Column(unique = true)
    private String adharNo;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    public OwnnerDTO toEntity() {
        return new OwnnerDTO(
                this.id,
                this.name,
                this.email,
                this.dob,
                this.phone,
                this.address,
                this.adharNo,
                this.bloodGroup);
    }
}
