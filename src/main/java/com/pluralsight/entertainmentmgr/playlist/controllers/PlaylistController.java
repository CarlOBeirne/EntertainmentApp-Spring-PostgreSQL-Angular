package com.pluralsight.entertainmentmgr.playlist.controllers;

import com.pluralsight.entertainmentmgr.core.exceptions.InvalidPlaylistException;
import com.pluralsight.entertainmentmgr.playlist.models.PlaylistDto;
import com.pluralsight.entertainmentmgr.playlist.services.PlaylistDataService;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlist")
@Log4j2
@CrossOrigin("*")
public class PlaylistController {

    private final PlaylistDataService playlistDataService;

    @PostMapping("/new")
    public ResponseEntity<PlaylistDto> createPlaylist(@NonNull @RequestBody PlaylistDto playlistDto) {
        try {
            if (playlistDto.getId() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(playlistDataService.createPlaylist(playlistDto));
        } catch (Exception e) {
            log.error("Error creating PL", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    public List<PlaylistDto> getAllPlaylists() {
        return playlistDataService.findAllPlaylists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@NonNull @PathVariable Long id) {
        try {
            Optional<PlaylistDto> optional = playlistDataService.getPlaylist(id);
            return optional.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            log.error("Error getting playlist with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<List<TrackDto>> getTracksInPlaylist(@NonNull @PathVariable Long id) {
        try {
            List<TrackDto> tracks = playlistDataService.getTracksInPlaylist(id);
            return tracks.isEmpty() ?
                    ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                    ResponseEntity.ok(tracks);
        } catch (Exception e) {
            log.error("Error getting tracks for playlist with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{playlistId}/add-track/{trackId}")
    public ResponseEntity<Void> addTrackToPlaylist(@PathVariable Long playlistId,
                                                   @PathVariable Long trackId,
                                                   @RequestParam(name = "order", defaultValue = "0") int order) {
        try {
            playlistDataService.addTrackToPlaylist(playlistId, trackId, order);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error adding track {} to playlist {}", trackId, playlistId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{playlistId}/remove-track/{trackId}")
    public ResponseEntity<Void> removeTrackFromPlaylist(@PathVariable Long playlistId,
                                                        @PathVariable Long trackId) {
        try {
            playlistDataService.removeTrackFromPlaylist(playlistId, trackId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("Error removing track {} from playlist {}", trackId, playlistId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        try {
            if (playlistDataService.getPlaylist(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            playlistDataService.deletePlaylist(id);
            return ResponseEntity.ok().build();
            //return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 204's
        } catch (Exception e) {
            log.error("Error deleting playlist {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PlaylistDto> updatePlaylist(@PathVariable Long id, @RequestBody PlaylistDto playlistDto) {
        try {
            if (!id.equals(playlistDto.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            PlaylistDto updated = playlistDataService.updatePlaylist(id, playlistDto);
            return ResponseEntity.ok(updated);
        } catch (InvalidPlaylistException e) {
            log.warn("playlist not found/invalid for id {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error updating playlist {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
