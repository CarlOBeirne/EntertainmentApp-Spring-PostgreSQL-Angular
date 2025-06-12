package com.pluralsight.entertainmentmgr.track.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
import com.pluralsight.entertainmentmgr.artist.repositories.ArtistRepository;
import com.pluralsight.entertainmentmgr.artist.services.ArtistDataService;
import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import com.pluralsight.entertainmentmgr.core.security.app_user.repositories.AppUserRepository;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import com.pluralsight.entertainmentmgr.genre.repositories.GenreRepository;
import com.pluralsight.entertainmentmgr.genre.service.GenreDataService;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.repositories.TrackRepository;
import com.pluralsight.entertainmentmgr.track.services.TrackDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Tag("integration")
@ActiveProfiles("test")
public class TrackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TrackDataService trackDataService;

    @Autowired
    private ArtistDataService artistDataService;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private GenreDataService genreDataService;

    @BeforeEach
    public void setUp() throws Exception {
        trackRepository.deleteAll();
        artistRepository.deleteAll();
        appUserRepository.deleteAll();
        genreRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void findAllTracks() throws Exception {
        AppUser user = AppUser.builder().username("fake").password("fake").build();
        AppUser persistedUser = appUserRepository.save(user);

        TrackDto trackDto = TrackDto.builder().title("Track 1").build();

        ArtistDto artistDto = ArtistDto.builder().name("Artist 1").appUser(persistedUser).build();
        ArtistDto artist = artistDataService.createArtist(artistDto);
        Set<ArtistDto> artists = Set.of(artist);
        trackDto.setArtists(artists);

        GenreDto genreDto = GenreDto.builder().name("Genre 1").build();
        GenreDto genre = genreDataService.createGenre(genreDto);
        trackDto.setGenre(genre);

        trackDataService.createTrack(trackDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/track/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void findTrackById() throws Exception {
        AppUser user = AppUser.builder().username("fake").password("fake").build();
        AppUser persistedUser = appUserRepository.save(user);

        TrackDto trackDto = TrackDto.builder().title("Track 1").build();

        ArtistDto artistDto = ArtistDto.builder().name("Artist 1").appUser(persistedUser).build();
        ArtistDto artist = artistDataService.createArtist(artistDto);
        Set<ArtistDto> artists = Set.of(artist);
        trackDto.setArtists(artists);

        GenreDto genreDto = GenreDto.builder().name("Genre 1").build();
        GenreDto genre = genreDataService.createGenre(genreDto);
        trackDto.setGenre(genre);

        TrackDto persistedTrack = trackDataService.createTrack(trackDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/track/" + persistedTrack.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(persistedTrack.getId()));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void createTrack() throws Exception {
        AppUser user = AppUser.builder().username("fake").password("fake").build();
        AppUser persistedUser = appUserRepository.save(user);

        TrackDto trackDto = TrackDto.builder().title("Track 1").build();

        ArtistDto artistDto = ArtistDto.builder().name("Artist 1").appUser(persistedUser).build();
        ArtistDto artist = artistDataService.createArtist(artistDto);
        Set<ArtistDto> artists = Set.of(artist);
        trackDto.setArtists(artists);

        GenreDto genreDto = GenreDto.builder().name("Genre 1").build();
        GenreDto genre = genreDataService.createGenre(genreDto);
        trackDto.setGenre(genre);

        mockMvc.perform(post("/track/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trackDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(trackDto.getTitle()));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void updateTrack_shouldReturnStatusOkWhenAllParametersMatch() throws Exception {
        AppUser user = AppUser.builder().username("fake").password("fake").build();
        AppUser persistedUser = appUserRepository.save(user);

        TrackDto trackDto = TrackDto.builder().title("Track 1").build();

        ArtistDto artistDto = ArtistDto.builder().name("Artist 1").appUser(persistedUser).build();
        ArtistDto artist = artistDataService.createArtist(artistDto);
        Set<ArtistDto> artists = Set.of(artist);
        trackDto.setArtists(artists);

        GenreDto genreDto = GenreDto.builder().name("Genre 1").build();
        GenreDto genre = genreDataService.createGenre(genreDto);
        trackDto.setGenre(genre);

        TrackDto persistedTrack = trackDataService.createTrack(trackDto);

        persistedTrack.setTitle("Track 2");

        mockMvc.perform(put("/track/update/{id}", persistedTrack.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persistedTrack)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Track 2"));
    }



}