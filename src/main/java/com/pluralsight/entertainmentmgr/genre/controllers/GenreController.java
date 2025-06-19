package com.pluralsight.entertainmentmgr.genre.controllers;

import com.pluralsight.entertainmentmgr.core.exceptions.InvalidGenreException;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import com.pluralsight.entertainmentmgr.genre.service.GenreDataService;
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
@RequestMapping("/genre")
@Log4j2
@CrossOrigin("*")
public class GenreController {

    private final GenreDataService genreDataService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        try {
            List<GenreDto> allGenres = genreDataService.findAllGenres();
            return allGenres.isEmpty() ?
                    ResponseEntity.notFound().build() : ResponseEntity.ok(allGenres);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GenreDto> getGenreById(@NonNull @PathVariable Long id) {
        try {
            Optional<GenreDto> genre = genreDataService.findGenreById(id);
            return genre.isPresent() ? ResponseEntity.ok(genre.get()) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(path = "/new")
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto genreDto) {
        try {
            if (genreDto.getId() != null) {
                log.info("Genre with id {} already exists", genreDto.getId());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return ResponseEntity.ok(genreDataService.createGenre(genreDto));
        } catch (InvalidGenreException e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Long id, @RequestBody GenreDto genreDto) {
        try {
            Optional<GenreDto> genre = genreDataService.findGenreById(id);
            if (genre.isEmpty()) {
                log.info("Genre with id {} not found", id);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(genreDataService.updateGenre(id, genreDto));
        } catch (InvalidGenreException e) {
            log.info("Cannot  update genre. {} ", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenreDto> deleteGenreById(@PathVariable Long id) {
        try {
            Optional<GenreDto> genre = genreDataService.findGenreById(id);
            if (genre.isEmpty()) {
                log.info("Genre with id {} does not exist", id);
                return ResponseEntity.notFound().build();
            }
            genreDataService.deleteGenre(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
