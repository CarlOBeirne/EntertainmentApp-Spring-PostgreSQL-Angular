package com.pluralsight.entertainmentmgr.playlist.entities;
import com.pluralsight.entertainmentmgr.core.auditable.entity.BaseEntity;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "playlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Playlist extends BaseEntity {

    private String name;
    private String description;

    //@ManyToMany(mappedBy = "tracks")
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_track",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks = new ArrayList<>();

    //public void addTrack(Track track, int order) {
    //    PlaylistTrack pt = new PlaylistTrack(this, track, order);
    //    playlistTracks.add(pt);
}

//public void removeTrack(Track track) {playlistTracks.removeIf(pt -> pt.getTrack().equals(track));
//}
//}
