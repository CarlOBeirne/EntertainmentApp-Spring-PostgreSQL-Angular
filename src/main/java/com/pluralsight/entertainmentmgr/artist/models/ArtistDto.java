package com.pluralsight.entertainmentmgr.artist.models;

import com.pluralsight.entertainmentmgr.artist.enums.ArtistType;
import com.pluralsight.entertainmentmgr.core.auditable.models.BaseDto;
import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ArtistDto extends BaseDto {
    private String name;
    private ArtistType artistType;
    private String biography;
    private String nationality;
    private int yearFounded;
//    private AppUser appUser;

}
