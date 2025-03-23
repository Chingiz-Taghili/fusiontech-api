package com.fusiontech.api.config;

import com.fusiontech.api.enums.Roles;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Role;
import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.repositories.RoleRepository;
import com.fusiontech.api.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public RoleInitializer(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        ensureRoles();
        ensureSuperAdmin();
    }

    private void ensureRoles() {
        List<String> roles = List.of(Roles.ROLE_SUPER_ADMIN, Roles.ROLE_ADMIN, Roles.ROLE_USER);
        roles.forEach(this::createRole);
    }

    private void createRole(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    private void ensureSuperAdmin() {
        if (!userRepository.existsByEmail("super.admin@mail.com")) {
            Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN).orElseThrow(
                    () -> new ResourceNotFoundException("Role", "name", Roles.ROLE_ADMIN));
            Role superAdminRole = roleRepository.findByName(Roles.ROLE_SUPER_ADMIN).orElseThrow(
                    () -> new ResourceNotFoundException("Role", "name", Roles.ROLE_SUPER_ADMIN));
            UserEntity admin = new UserEntity();
            admin.setName("Admin");
            admin.setEmail("super.admin@mail.com");
            admin.setPassword(encoder.encode("Admin123"));
            admin.setRoles(Set.of(adminRole, superAdminRole));
            userRepository.save(admin);
        }
    }
}
