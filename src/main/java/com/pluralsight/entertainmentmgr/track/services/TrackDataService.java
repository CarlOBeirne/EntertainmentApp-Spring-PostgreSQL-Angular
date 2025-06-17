package com.pluralsight.entertainmentmgr.track.services;

import com.pluralsight.entertainmentmgr.artist.entities.Artist;
import com.pluralsight.entertainmentmgr.artist.mapper.ArtistMapper;
import com.pluralsight.entertainmentmgr.artist.repositories.ArtistRepository;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidTrackException;
import com.pluralsight.entertainmentmgr.genre.entities.Genre;
import com.pluralsight.entertainmentmgr.genre.mapper.GenreMapper;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import com.pluralsight.entertainmentmgr.genre.service.GenreDataService;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import com.pluralsight.entertainmentmgr.track.mapper.TrackMapper;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.repositories.TrackRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TrackDataService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    private final ArtistRepository artistRepository;

    private final GenreMapper genreMapper;
    private final GenreDataService genreDataService;
    private final ArtistMapper artistMapper;

    public Optional<TrackDto> findTrackById(@NonNull Long trackId) {
        return trackRepository.findById(trackId)
                .map(trackMapper::toDTO);
    }

    public List<TrackDto> findAllTracks() {
        return trackRepository.findAll().stream()
                .map(trackMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrackDto createTrack(@NonNull TrackDto trackDto) {
        if (trackDto.getId() != null) {
            throw new InvalidTrackException("Track id already exists");
        }
        Track entity = trackMapper.toEntity(trackDto);
        return trackMapper.toDTO(trackRepository.save(entity));
    }

    @Transactional
    public TrackDto updateTrack(@NonNull Long id, @NonNull TrackDto trackDto) {
        if (trackDto.getId() == null) {
            throw new InvalidTrackException("Track ID does not exist.");
        }
        if (!trackDto.getId().equals(id)) {
            throw new InvalidTrackException("Track id and id do not match.");
        }

        Track entity = trackMapper.toEntity(trackDto);
        return trackMapper.toDTO(trackRepository.save(entity));

    }

    @Transactional
    public void deleteTrack(@NonNull Long trackId) {
        trackRepository.deleteById(trackId);
    }

    public List<TrackDto> findTracksByTitle(@NonNull String title) {
        return trackRepository.findAllByTitleIgnoreCase(title).stream()
                .map(trackMapper::toDTO)
                .toList();
    }

    public TrackDto addArtist(@NonNull Long trackId, @NonNull Long artistId) {
        Track track = trackRepository.findById(trackId).orElse(null);
        Artist artist = artistRepository.findById(artistId).orElse(null);

        if(track == null || artist == null) { return null; };

        track.getArtists().add(artist);
        trackRepository.save(track);

        return trackMapper.toDTO(track);
    }

    public TrackDto removeArtist(@NonNull Long trackId, @NonNull Long artistId) {
        Track track = trackRepository.findById(trackId).orElse(null);
        Artist artist = artistRepository.findById(artistId).orElse(null);

        if(track == null || artist == null) { return null; };

        track.getArtists().remove(artist);
        trackRepository.save(track);

        return trackMapper.toDTO(track);
    }

    private Genre addGenre(@NonNull TrackDto trackDto) {
        Optional<GenreDto> genre = genreDataService.findGenreById(trackDto.getGenre().getId());
        return genreMapper.toEntity(genre.get());
    }

    // ALSO SEEMS LIKE THERE IS NO NEED ?
//    private Genre removeGenre(@NonNull TrackDto trackDto) {
//        Optional<GenreDto> genre = genreDataService.findGenreById(trackDto.getGenre().getId());
//        return genreMapper.toEntity(genre.get());
//    }

}
