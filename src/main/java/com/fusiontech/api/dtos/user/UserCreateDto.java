package com.fusiontech.api.dtos.user;

import com.fusiontech.api.enums.Gender;
import com.fusiontech.api.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String image;
    private Gender gender;
    private String email;
    private String password;
    private Set<Role> roles;
}
