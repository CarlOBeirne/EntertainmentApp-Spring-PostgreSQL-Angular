package com.pluralsight.entertainmentmgr.core.utils;

import com.pluralsight.entertainmentmgr.core.security.permission.entities.Permission;
import com.pluralsight.entertainmentmgr.core.security.permission.repositories.PermissionRepository;
import com.pluralsight.entertainmentmgr.core.security.role.entities.Role;
import com.pluralsight.entertainmentmgr.core.security.role.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Bean
    public CommandLineRunner initRoles() {
        return args -> {
            Role userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
        };
    }

    @Bean
    public CommandLineRunner initPermissions() {
        return args -> {
            permissionRepository.findByName("CREATOR").orElseGet(() -> permissionRepository.save(Permission.builder().name("CREATOR").build()));
            permissionRepository.findByName("LISTENER").orElseGet(() -> permissionRepository.save(Permission.builder().name("LISTENER").build()));
        };
    }

}
