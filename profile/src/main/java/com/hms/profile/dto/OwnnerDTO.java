package com.hms.profile.dto;

import java.time.LocalDate;

import com.hms.profile.model.Ownner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnnerDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;
    private String adharNo;
    private BloodGroup bloodGroup;

    public Ownner toEntity() {
        return new Ownner(
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
