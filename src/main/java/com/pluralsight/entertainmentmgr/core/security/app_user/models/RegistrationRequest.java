package com.pluralsight.entertainmentmgr.core.security.app_user.models;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

}
