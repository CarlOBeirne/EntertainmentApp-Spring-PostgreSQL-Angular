package com.pluralsight.entertainmentmgr.playlist.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import com.pluralsight.entertainmentmgr.core.security.app_user.repositories.AppUserRepository;
import com.pluralsight.entertainmentmgr.playlist.models.PlaylistDto;
import com.pluralsight.entertainmentmgr.playlist.repositories.PlaylistRepository;
import com.pluralsight.entertainmentmgr.playlist.services.PlaylistDataService;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Tag("integration")
@ActiveProfiles("test")
public class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PlaylistDataService playlistDataService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @BeforeEach
    public void setUp() {
        playlistRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    private AppUser createAndSaveUser() {
        AppUser user = AppUser.builder()
                .username("testuser")
                .password("testpass")
                .build();
        return appUserRepository.save(user);
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void findAllPlaylists() throws Exception {
        AppUser user = createAndSaveUser();

        PlaylistDto playlistDto = PlaylistDto.builder()
                .name("Playlist")
                .description("findall")
                //.appUser(user) ?
                .build();

        playlistDataService.createPlaylist(playlistDto);

        mockMvc.perform(get("/playlist/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void findPlaylistById() throws Exception {
        AppUser user = createAndSaveUser();

        PlaylistDto playlistDto = PlaylistDto.builder()
                .name("Playlist")
                .description("find me by id")
                //.appUser(user)
                .build();

        PlaylistDto persistedPlaylist = playlistDataService.createPlaylist(playlistDto);

        mockMvc.perform(get("/playlist/" + persistedPlaylist.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(persistedPlaylist.getId()));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void createPlaylist() throws Exception {
        AppUser user = createAndSaveUser();

        PlaylistDto playlistDto = PlaylistDto.builder()
                .name("New Playlist")
                .description("create me")
                //.appUser(user)
                .build();

        mockMvc.perform(post("/playlist/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playlistDto)))
                .andExpect(status().isCreated());

        //Looks for 200 while I create -> 201
        //mockMvc.perform(post("/playlist/new")
        //                .contentType(MediaType.APPLICATION_JSON)
        //                .content(objectMapper.writeValueAsString(playlistDto)))
        //        .andExpect(status().isOk())
        //        .andExpect(jsonPath("$.name").value("New Playlist"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void updatePlaylist() throws Exception {
        AppUser user = createAndSaveUser();

        PlaylistDto playlistDto = PlaylistDto.builder()
                .name("Playlist")
                .description("update me")
                //should be empty not null? kind of todo
                .playlistTracks(new ArrayList<>())
                //.appUser(user)
                .build();

        PlaylistDto persistedPlaylist = playlistDataService.createPlaylist(playlistDto);

        persistedPlaylist.setName("Updated Playlist");

        mockMvc.perform(put("/playlist/update/{id}", persistedPlaylist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persistedPlaylist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Playlist"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void deletePlaylist() throws Exception {
        AppUser user = createAndSaveUser();

        PlaylistDto playlistDto = PlaylistDto.builder()
                .name("Playlist")
                .description("Delete me")
                .build();

        PlaylistDto persistedPlaylist = playlistDataService.createPlaylist(playlistDto);

        mockMvc.perform(delete("/playlist/delete/{id}", persistedPlaylist.getId()))
                .andExpect(status().isOk());
    }
}
