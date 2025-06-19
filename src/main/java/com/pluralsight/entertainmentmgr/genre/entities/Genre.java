package com.pluralsight.entertainmentmgr.genre.entities;

import com.pluralsight.entertainmentmgr.core.auditable.entity.BaseEntity;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genre")
public class Genre extends BaseEntity {

    private String name;

    private String description;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<Track> tracks = new HashSet<>();
}
