package com.pluralsight.entertainmentmgr.playlist.models;

import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistTrackDto {
    private List<PlaylistTrackDto> playlistTracks;
    private int trackOrder;
    private TrackDto track;
}