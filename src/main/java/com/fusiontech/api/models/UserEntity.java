package com.fusiontech.api.models;

import com.fusiontech.api.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

//Spring Security ilə birləşdirilmiş bir model. Sistemə istifadəçi məlumatlarını təqdim etmək üçün nəzərdə
//tutulub. UserDetails Spring Security-də istifadəçinin təfərrüatlarını təmsil edən interfeysdir. Onunla
//Spring Security-ə istifadəçi ilə bağlı məlumat təqdim olunur. Beləliklə, UserDetails interfeysini
//implement etməklə Spring Security-yə öz yazdığımız istifadəçi modelini təqdim etmiş oluruq.
@Data
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String imageUrl;
    @Column(name = "email", nullable = false)
    private String email;
    private String password;
    private Gender gender;
    //private Boolean emailConfirmed;
    //private String confirmationToken;
    //private Integer loginAttempt;
    //private Integer otp;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    //İstifadəçinin sahib olduğu rolların siyahısını qaytarır.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities =
                this.roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).toList();
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    //Hesabın vaxtının bitib-bitmədiyini yoxlayır. Vaxtı bitibsə, giriş etmək mümkün olmayacaq.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Hesabın bloklanıb-bloklanmadığını yoxlayır. Əgər hesab bloklanıbsa, giriş mümkün olmayacaq.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Parolun vaxtının bitib-bitmədiyini yoxlayır.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Hesabın aktiv olub-olmadığını yoxlayır. Əgər aktiv deyilsə, giriş mümkün olmayacaq.
    @Override
    public boolean isEnabled() {
        return true;
    }
}
