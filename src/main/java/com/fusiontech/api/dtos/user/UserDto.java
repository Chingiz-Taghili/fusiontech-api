package com.fusiontech.api.dtos.user;

import com.fusiontech.api.enums.Gender;
import com.fusiontech.api.models.CartItem;
import com.fusiontech.api.models.Favorite;
import com.fusiontech.api.models.Order;
import com.fusiontech.api.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String image;
    private String email;
    private String password;
    private Gender gender;
    private List<Favorite> favorites;
    private List<CartItem> cartItems;
    private List<Order> orders;
    private Set<Role> roles;
}
