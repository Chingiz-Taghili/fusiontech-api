package com.fusiontech.api.dtos.user;

import com.fusiontech.api.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String email;
    private Gender gender;
    private String imageUrl;
    private String password;
}
