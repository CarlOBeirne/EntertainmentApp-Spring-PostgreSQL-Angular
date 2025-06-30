package com.pluralsight.entertainmentmgr.playlist.entities;

import com.pluralsight.entertainmentmgr.playlist.entities.Playlist;
import com.pluralsight.entertainmentmgr.playlist.entities.PlaylistTrackId;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "playlist_track")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistTrack {

    @EmbeddedId
    private PlaylistTrackId id = new PlaylistTrackId();

    @ManyToOne
    @MapsId("playlistId")
    private Playlist playlist;

    @ManyToOne
    @MapsId("trackId")
    private Track track;

    private int trackOrder;

    public PlaylistTrack(Playlist playlist, Track track, int trackOrder) {
        this.playlist = playlist;
        this.track = track;
        this.trackOrder = trackOrder;
        this.id = new PlaylistTrackId(playlist.getId(), track.getId());
    }
}
