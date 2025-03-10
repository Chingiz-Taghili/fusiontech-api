package com.fusiontech.api.security;


import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Spring Security login olan istifadəçini doğrulamaq üçün default olaraq UserDetailsService-in içindəki
//"loadUserByUsername" metodunu çağırıb onun qaytardığı obyektdən istifadə edir. Bu metod username-ə
//uyğun gələn istifadəçi məlumatlarını "UserDetails" obyekti şəklində qaytarır, heçnə tapmasa Exception
//atır. Məlumatları database-lə yoxlaması üçün metodu override edib konfiqurasiyanı yazırıq.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            User user = new User(
                    optionalUser.get().getEmail(), optionalUser.get().getPassword(),
                    optionalUser.get().isEnabled(),
                    optionalUser.get().isAccountNonExpired(),
                    optionalUser.get().isCredentialsNonExpired(),
                    optionalUser.get().isAccountNonLocked(),
                    optionalUser.get().getAuthorities());
            return user;
        } else {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }
    }
}
