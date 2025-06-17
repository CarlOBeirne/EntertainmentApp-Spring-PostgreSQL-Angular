package com.pluralsight.entertainmentmgr.track.controllers;

import com.pluralsight.entertainmentmgr.core.exceptions.InvalidTrackException;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.services.TrackDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/track")
@Log4j2
@CrossOrigin("*")
public class TrackController {

    private final TrackDataService trackDataService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        try {
            List<TrackDto> allTracks = trackDataService.findAllTracks();
            return allTracks.isEmpty() ?
                    ResponseEntity.noContent().build() : ResponseEntity.ok(allTracks) ;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrackById(@NonNull @PathVariable Long id) {
        try {
            Optional<TrackDto> track = trackDataService.findTrackById(id);
            return track.isPresent() ? ResponseEntity.ok(track.get()) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TrackDto>> getTrackByTitle(@RequestParam("title") String title) {
        if (title.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        try {
            List<TrackDto> allTracksByName = trackDataService.findTracksByTitle(title);
            return allTracksByName.isEmpty() ?
                    ResponseEntity.notFound().build() : ResponseEntity.ok(allTracksByName);
        } catch (NullPointerException e) {
            log.info("Track title is null. ", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATOR')")
    @PostMapping(path = "/new")
    public ResponseEntity<TrackDto> createTrack(@NonNull @RequestBody TrackDto trackDto) {
        try {
            if (trackDto.getId() != null) {
                log.info("Track ID already exists. Expecting ID to be null.");
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return ResponseEntity.ok(trackDataService.createTrack(trackDto));
        } catch (InvalidTrackException e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        catch (Exception e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATOR')")
    @PutMapping("/update/{id}")
    public ResponseEntity<TrackDto> updateTrack(@RequestBody TrackDto trackDto, @PathVariable Long id) {
        try {
            Optional<TrackDto> track = trackDataService.findTrackById(id);
            if (track.isEmpty()) {
                log.info("Could not find track with id {} ", trackDto.getId());
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(trackDataService.updateTrack(id, trackDto));
        } catch (InvalidTrackException e) {
            log.info("Cannot update track. {} ", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTrackById(@PathVariable Long id) {
        try {
            Optional<TrackDto> track = trackDataService.findTrackById(id);
            if (track.isEmpty()) {
                log.info("Track with id {} does not exist. ", id);
                return ResponseEntity.notFound().build();
            }
            trackDataService.deleteTrack(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/{trackId}/add-artist/{artistId}")
    public ResponseEntity<TrackDto> addArtistToTrack(@PathVariable Long trackId, @PathVariable Long artistId) {
        TrackDto updatedTrackDto = trackDataService.addArtist(trackId, artistId);
        return updatedTrackDto != null ? ResponseEntity.ok(updatedTrackDto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{trackId}/remove-artist/{artistId}")
    public ResponseEntity<TrackDto> removeArtistFromTrack(@PathVariable Long trackId, @PathVariable Long artistId) {
        TrackDto updatedTrack = trackDataService.removeArtist(trackId, artistId);
        return updatedTrack != null ? ResponseEntity.ok(updatedTrack) : ResponseEntity.badRequest().build();
    }
}
