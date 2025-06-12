package com.pluralsight.entertainmentmgr.genre.service;

import com.pluralsight.entertainmentmgr.core.exceptions.InvalidGenreException;
import com.pluralsight.entertainmentmgr.genre.entities.Genre;
import com.pluralsight.entertainmentmgr.genre.mapper.GenreMapper;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import com.pluralsight.entertainmentmgr.genre.repositories.GenreRepository;
import com.pluralsight.entertainmentmgr.track.entities.Track;
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
public class GenreDataService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public List<GenreDto> findAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<GenreDto> findGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .map(genreMapper::toDTO);
    }

    @Transactional
    public GenreDto createGenre(GenreDto genreDto) {
        Genre entity = genreMapper.toEntity(genreDto);
        return genreMapper.toDTO(genreRepository.save(entity));
    }

    @Transactional
    public GenreDto updateGenre(Long genreId, GenreDto genreDto) {
        if (genreDto.getId() == null) {
            throw new InvalidGenreException("Genre ID does not exist.");
        }
        if(!genreDto.getId().equals(genreId)) {
            throw new InvalidGenreException("Genre ID and id does not match.");
        }

        Genre entity = genreMapper.toEntity(genreDto);
        Optional<Genre> existing = genreRepository.findById(genreId);
        if (existing.isEmpty()) {
            throw new InvalidGenreException("Genre does not exist.");
        }
        genreMapper.updateEntityFromDto(genreDto, existing.get());
        return genreMapper.toDTO(genreRepository.save(entity));
    }

    @Transactional
    public void deleteGenre(Long genreId) {
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre == null) { throw new InvalidGenreException("Genre does not exist."); }

        for (Track track : genre.getTracks())
        {
            track.setGenre(null);
        }
        genreRepository.deleteById(genreId);
    }
}
