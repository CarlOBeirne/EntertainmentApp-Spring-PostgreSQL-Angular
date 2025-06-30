package com.pluralsight.entertainmentmgr.playlist.services;

import com.pluralsight.entertainmentmgr.playlist.entities.Playlist;
import com.pluralsight.entertainmentmgr.playlist.entities.PlaylistTrack;
import com.pluralsight.entertainmentmgr.playlist.entities.PlaylistTrackId;
import com.pluralsight.entertainmentmgr.playlist.mapper.PlaylistMapper;
import com.pluralsight.entertainmentmgr.playlist.models.PlaylistDto;
import com.pluralsight.entertainmentmgr.playlist.repositories.PlaylistRepository;
import com.pluralsight.entertainmentmgr.playlist.repositories.PlaylistTrackRepository;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import com.pluralsight.entertainmentmgr.track.mapper.TrackMapper;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.repositories.TrackRepository;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidPlaylistException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlaylistDataService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final TrackRepository trackRepository;

    private final PlaylistMapper playlistMapper;
    private final TrackMapper trackMapper;

    public Optional<Playlist> getPlaylistEntity(Long id) {
        return playlistRepository.findById(id);
    }

    public Optional<PlaylistDto> getPlaylist(Long id) {
        return playlistRepository.findById(id)
                .map(playlistMapper::toDTO);
    }

    public List<TrackDto> getTracksInPlaylist(Long playlistId) {
        return playlistTrackRepository.findAllByPlaylistIdOrderByTrackOrder(playlistId).stream()
                .map(PlaylistTrack::getTrack)
                .map(trackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PlaylistDto createPlaylist(PlaylistDto dto) {
        Playlist playlist = playlistMapper.toEntity(dto);
        Playlist saved = playlistRepository.save(playlist);
        return playlistMapper.toDTO(saved);
    }

    @Transactional
    public void addTrackToPlaylist(Long playlistId, Long trackId, int order) {
        if (playlistTrackRepository.existsByPlaylistIdAndTrackId(playlistId, trackId)) {
            log.warn("Track already exists in PL.");
            return;
        }

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("PL not found"));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        PlaylistTrack pt = new PlaylistTrack();
        pt.setId(new PlaylistTrackId(playlistId, trackId));
        pt.setPlaylist(playlist);
        pt.setTrack(track);
        pt.setTrackOrder(order);

        playlistTrackRepository.save(pt);
    }

    @Transactional
    public void removeTrackFromPlaylist(Long playlistId, Long trackId) {
        if (!playlistTrackRepository.existsByPlaylistIdAndTrackId(playlistId, trackId)) {
            log.warn("Track not found in PL.");
            return;
        }

        playlistTrackRepository.deleteByPlaylistIdAndTrackId(playlistId, trackId);
    }

    //@Transactional
    //public PlaylistDto updatePlaylist(Long id, PlaylistDto dto) {
    //    if (id == null || dto == null) {
    //        throw new NullPointerException("Playlist ID and DTO must not be null");
    //    }
    //    if (!id.equals(dto.getId())) {
    //        throw new InvalidPlaylistException("Playlist ID mismatch");
    //    }
//
    //    Playlist playlist = playlistRepository.findById(id)
    //            .orElseThrow(() -> new InvalidPlaylistException("Playlist not found"));
//
    //    playlistMapper.updateEntityFromDto(dto, playlist);
    //    Playlist saved = playlistRepository.save(playlist);
    //    return playlistMapper.toDTO(saved);
    //}

    @Transactional
    public PlaylistDto updatePlaylist(Long id, PlaylistDto dto) {
        if (id == null || dto == null) {
            throw new NullPointerException("PL ID and DTO must not be null");
        }
        if (!id.equals(dto.getId())) {
            throw new InvalidPlaylistException("PL ID mismatch");
        }

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new InvalidPlaylistException("PL not found"));

        playlistMapper.updateEntityFromDto(dto, playlist);

        //if (dto.getTracks() != null) {
        //    //playlist.getTracks().clear();
//
        //    List<PlaylistTrack> updatedPlaylistTracks = dto.getTracks().stream()
        //            .map(dtoTrack -> {
        //                //PlaylistTrack pt = new PlaylistTrack();
        //                //pt.setTrackOrder(dtoTrack.getTrackOrder());
//
        //                Long trackId = dtoTrack.getId();
        //                Track trackEntity = trackRepository.findById(trackId)
        //                        .orElseThrow(() -> new RuntimeException("Track not found with id " + trackId));
//
        //               //pt.setTrack(trackEntity);
        //               //pt.setPlaylist(playlist);
        //               return Playlis;
        //            })
        //            .collect(Collectors.toList());
//
        //    playlist.getTracks().addAll(updatedPlaylistTracks);
//
        //} else {
        //    //Testfix?
        //    //for (PlaylistTrack pt : playlist.getPlaylistTracks()) {
        //    //    pt.setPlaylist(playlist);
        //    //}
//
        //    if (playlist.getPlaylistTracks() != null) {
        //        for (PlaylistTrack pt : playlist.getPlaylistTracks()) {
        //            pt.setPlaylist(playlist);
        //        }
        //    }
        //}

        Playlist saved = playlistRepository.save(playlist);
        return playlistMapper.toDTO(saved);
    }

    public void deletePlaylist(Long id) {
        if (id == null) {
            throw new NullPointerException("Playlist ID must not be null");
        }
        playlistRepository.deleteById(id);
    }

    public List<PlaylistDto> findAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }
}
