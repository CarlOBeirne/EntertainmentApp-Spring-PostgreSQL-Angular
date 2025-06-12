package com.pluralsight.entertainmentmgr.artist.entities;

import com.pluralsight.entertainmentmgr.artist.enums.ArtistType;
import com.pluralsight.entertainmentmgr.core.auditable.entity.BaseEntity;
import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist")
public class Artist extends BaseEntity {

    private String name;

    private ArtistType artistType;

    private String biography;

    private String nationality;

    private int yearFounded;

    @ManyToMany(mappedBy = "artists", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Track> tracks = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    private AppUser appUser;

}
