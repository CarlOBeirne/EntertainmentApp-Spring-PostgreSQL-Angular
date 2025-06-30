package com.pluralsight.entertainmentmgr.playlist.models;

import com.pluralsight.entertainmentmgr.core.auditable.models.BaseDto;
//import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlaylistDto extends BaseDto {

    private String name;
    private String description;
    private List<TrackDto> tracks;
    //private List<PlaylistTrackDto> playlistTracks;
}