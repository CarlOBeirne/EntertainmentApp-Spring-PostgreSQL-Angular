package com.pluralsight.entertainmentmgr.core.security.role.repositories;

import com.pluralsight.entertainmentmgr.core.security.role.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
