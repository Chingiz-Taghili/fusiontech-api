package com.fusiontech.api.config;

import com.fusiontech.api.models.Role;
import com.fusiontech.api.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<String> roles = List.of(Constants.ROLE_ADMIN, Constants.ROLE_USER);

        roles.forEach(this::createRole);
    }

    private void createRole(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}
