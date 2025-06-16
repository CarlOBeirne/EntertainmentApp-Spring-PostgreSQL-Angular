package com.pluralsight.entertainmentmgr.artist.controllers;

import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
import com.pluralsight.entertainmentmgr.artist.services.ArtistDataService;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidArtistException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
@Log4j2
public class ArtistController {

    private final ArtistDataService artistDataService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/new")
    public ResponseEntity<ArtistDto> createArtist(@NonNull @RequestBody ArtistDto artist)  {
        try {
            if (artist.getId() != null) {
                log.warn("Artist was passed with an Id of {} (expecting Id to be null)", artist.getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(201).body(artistDataService.createArtist(artist));
        } catch (InvalidArtistException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error creating new artist with exception", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and hasAuthority('CREATOR'))")
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@NonNull @PathVariable Long id,
                                                  @NonNull @RequestBody ArtistDto artistDto,
                                                  @AuthenticationPrincipal User user)  {
        try {
            Optional<ArtistDto> optionalArtist = artistDataService.findArtistById(artistDto.getId());
            if (optionalArtist.isEmpty()) {
                log.info("Could not find artist with id {}", artistDto.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
//            boolean isAdmin = user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//            if (!isAdmin && !artistDto.getAppUser().getUsername().equals(user.getUsername())) {
//                log.warn("Attempt to update artist \"{}\" was made by a user with different username \"{}\"", artistDto.getName(), user.getUsername());
//                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//            }
            return ResponseEntity.ok(artistDataService.updateArtist(id, artistDto));
        } catch (InvalidArtistException e) {
            log.warn("Warning trying to update artist. {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Unknown error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@NonNull @PathVariable Long id)  {
        try {
            Optional<ArtistDto> optionalArtist = artistDataService.findArtistById(id);
            return optionalArtist
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        catch (Exception e) {
            log.error("Unknown error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        try {
            List<ArtistDto> allArtists = artistDataService.findAllArtists();
            if (allArtists.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(allArtists);
        } catch (Exception e) {
            log.error("Unknown error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ArtistDto>> getArtistByName(@RequestParam("name") String name) {
        if (name.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        try {
            List<ArtistDto> allArtistsByName = artistDataService.findArtistsByName(name);;
            if (allArtistsByName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(allArtistsByName);
        } catch (NullPointerException npe) {
            log.warn("Artist name is null", npe);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unknown error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> deleteArtistById(@NonNull @PathVariable Long id)  {
        try {
            Optional<ArtistDto> optionalArtist = artistDataService.findArtistById(id);
            if (optionalArtist.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            artistDataService.deleteArtist(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (InvalidArtistException e) {
            log.info("Could not find artist associated with id {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {

            log.error("Unknown error occurred", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
