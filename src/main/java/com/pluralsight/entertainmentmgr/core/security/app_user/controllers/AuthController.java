package com.pluralsight.entertainmentmgr.core.security.app_user.controllers;

import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import com.pluralsight.entertainmentmgr.core.security.app_user.models.LoginRequest;
import com.pluralsight.entertainmentmgr.core.security.app_user.models.RegistrationRequest;
import com.pluralsight.entertainmentmgr.core.security.app_user.repositories.AppUserRepository;
import com.pluralsight.entertainmentmgr.core.security.jwt.JwtUtil;
import com.pluralsight.entertainmentmgr.core.security.permission.entities.Permission;
import com.pluralsight.entertainmentmgr.core.security.permission.repositories.PermissionRepository;
import com.pluralsight.entertainmentmgr.core.security.role.entities.Role;
import com.pluralsight.entertainmentmgr.core.security.role.repositories.RoleRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        if (appUserRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        Set<Role> defaultRoles = new HashSet<>();
        Set<Permission> defaultPermissions = new HashSet<>();
        roleRepository.findByName("USER").ifPresent(defaultRoles::add);
        permissionRepository.findByName("LISTENER").ifPresent(defaultPermissions::add);
        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(defaultRoles)
                .permissions(defaultPermissions)
                .build();
        appUserRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getUsername());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/{id}/roles/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addRole(@NonNull @PathVariable Long id,
                                          @NonNull @NotEmpty @RequestBody Set<Role> roles) {
        try {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
            if (optionalAppUser.isPresent()) {
                AppUser appUser = optionalAppUser.get();
                appUser.getRoles().addAll(roles);
                appUserRepository.save(appUser);
                return ResponseEntity.status(HttpStatus.OK).body("Permission added successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User could not be found");
        } catch (Exception e) {
            log.error("Error occurred while attempting to update authorities", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred");
        }
    }

    @PostMapping("/{id}/roles/remove")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> removeRole(@NonNull @PathVariable Long id,
                                             @NonNull @NotEmpty @RequestBody Set<Role> roles) {
        try {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
            if (optionalAppUser.isPresent()) {
                AppUser appUser = optionalAppUser.get();
                appUser.getRoles().removeAll(roles);
                appUserRepository.save(appUser);
                return ResponseEntity.status(HttpStatus.OK).body("Permission removed successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User could not be found");
        } catch (Exception e) {
            log.error("Error occurred while attempting to update authorities", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred");
        }
    }

    @PostMapping("/{id}/permissions/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<String> addPermission(@NonNull @PathVariable Long id,
                                                @NonNull @NotEmpty @RequestBody Set<Permission> permissions) {
        try {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
            if (optionalAppUser.isPresent()) {
                AppUser appUser = optionalAppUser.get();
                appUser.getPermissions().addAll(permissions);
                appUserRepository.save(appUser);
                return ResponseEntity.status(HttpStatus.OK).body("Permission added successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User could not be found");
        } catch (Exception e) {
            log.error("Error occurred while attempting to update authorities", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred");
        }
    }

    @PostMapping("/{id}/permissions/remove")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<String> removePermission(@NonNull @PathVariable Long id,
                                                @NonNull @NotEmpty @RequestBody Set<Permission> permissions) {
        try {
            Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
            if (optionalAppUser.isPresent()) {
                AppUser appUser = optionalAppUser.get();
                appUser.getPermissions().removeAll(permissions);
                appUserRepository.save(appUser);
                return ResponseEntity.status(HttpStatus.OK).body("Permission removed successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User could not be found");
        } catch (Exception e) {
            log.error("Error occurred while attempting to update authorities", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error occurred");
        }
    }

}
