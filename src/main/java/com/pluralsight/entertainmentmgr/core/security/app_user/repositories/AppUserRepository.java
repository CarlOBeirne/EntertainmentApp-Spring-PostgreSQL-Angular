package com.pluralsight.entertainmentmgr.core.security.app_user.repositories;

import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

