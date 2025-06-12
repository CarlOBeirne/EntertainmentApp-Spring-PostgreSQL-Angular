package com.pluralsight.entertainmentmgr.track.models;

import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
import com.pluralsight.entertainmentmgr.core.auditable.models.BaseDto;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TrackDto extends BaseDto {
    private String title;
    private int durationSeconds;
    private int yearReleased;
    private int beatsPerMinute;
    private GenreDto genre;
    private Set<ArtistDto> artists;
}
