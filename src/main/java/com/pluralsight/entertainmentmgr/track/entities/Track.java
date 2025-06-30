package com.pluralsight.entertainmentmgr.track.entities;

import com.pluralsight.entertainmentmgr.artist.entities.Artist;
import com.pluralsight.entertainmentmgr.core.auditable.entity.BaseEntity;
import com.pluralsight.entertainmentmgr.genre.entities.Genre;
import com.pluralsight.entertainmentmgr.playlist.entities.Playlist;
import jakarta.persistence.*;
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
@Table(name = "track")
public class Track extends BaseEntity {

    @EqualsAndHashCode.Include
    private String title;

    @EqualsAndHashCode.Include
    private int durationSeconds;

    @EqualsAndHashCode.Include
    private int yearReleased;

    @EqualsAndHashCode.Include
    private int beatsPerMinute;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "track_artist",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists = new HashSet<>();

    @ManyToMany(mappedBy = "tracks")
    private Set<Playlist> playlists = new HashSet<>();

    public void addArtist(Artist artist) {
        if (artist == null) {
            artists = new HashSet<>();
        }
        artists.add(artist);
    }


}
