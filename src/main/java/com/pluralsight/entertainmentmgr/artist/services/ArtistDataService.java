package com.pluralsight.entertainmentmgr.artist.services;

import com.pluralsight.entertainmentmgr.artist.entities.Artist;
import com.pluralsight.entertainmentmgr.artist.mapper.ArtistMapper;
import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
import com.pluralsight.entertainmentmgr.artist.repositories.ArtistRepository;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidArtistException;
import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import com.pluralsight.entertainmentmgr.core.security.app_user.repositories.AppUserRepository;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArtistDataService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final AppUserRepository appUserRepository;

    public Optional<ArtistDto> findArtistById(@NonNull Long artistId) {
        Optional<Artist> optionalArtist = artistRepository.findById(artistId);
        return optionalArtist.map(artistMapper::toDTO);
    }

    public List<ArtistDto> findAllArtists() {
        Year year = Year.now();
        return artistRepository.findAll().stream()
                .map(artistMapper::toDTO)
                .toList();
    }

    public List<ArtistDto> findArtistsByName(@NonNull String name) {
        return artistRepository.findAllByNameIgnoreCase(name).stream()
                .map(artistMapper::toDTO)
                .toList();
    }

    @Transactional
    public ArtistDto createArtist(@NonNull ArtistDto artistDto) {
        if (artistDto.getId() != null) {
            throw new InvalidArtistException("Attempted to create a new artist that already has an Id");
        }
        Artist artistEntity = artistMapper.toEntity(artistDto);
//        Optional<AppUser> optionalUser = appUserRepository.findByUsername(artistDto.getAppUser().getUsername());
//        if (optionalUser.isEmpty()) {
//            throw new InvalidArtistException("Attempted to create an artist with an invalid username");
//        }
//        artistEntity.setAppUser(optionalUser.get());
        Artist persistedArtist = artistRepository.save(artistEntity);
        log.info("New artist created with id {}", persistedArtist.getId());
        return artistMapper.toDTO(persistedArtist);
    }

    @Transactional
    public ArtistDto updateArtist(@NonNull Long id, @NonNull ArtistDto artistDto) {
        if (artistDto.getId() == null) {
            throw new InvalidArtistException("Cannot update an artist that does not have an Id");
        } else if (!id.equals(artistDto.getId())) {
            throw new InvalidArtistException("Discrepancy between id and artist id");
        }
        Optional<Artist> optionalArtist = artistRepository.findById(artistDto.getId());
        Artist artistEntity = findAndReturnArtistEntity(optionalArtist);
        if (artistEntity == null) {
            throw new InvalidArtistException("No artist found");
        }
        artistMapper.updateEntityFromDto(artistDto, artistEntity);
        artistRepository.save(artistEntity);
        return artistMapper.toDTO(artistEntity);
    }

    @Transactional
    public void deleteArtist(@NonNull Long artistId) {
        Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) {
            throw new InvalidArtistException("No artist found");
        }
        for(Track track : artist.getTracks()) {
            track.getArtists().remove(artist);
        }
        artistRepository.delete(artist);
    }

    private Artist findAndReturnArtistEntity(Optional<Artist> optionalArtist) {
        return optionalArtist.orElse(null);
    }


}
