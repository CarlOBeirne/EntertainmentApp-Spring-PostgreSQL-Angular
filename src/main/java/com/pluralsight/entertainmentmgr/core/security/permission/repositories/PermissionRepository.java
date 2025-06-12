package com.pluralsight.entertainmentmgr.core.security.permission.repositories;

import com.pluralsight.entertainmentmgr.core.security.permission.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
