package com.pluralsight.entertainmentmgr.playlist.services;

import com.pluralsight.entertainmentmgr.playlist.entities.Playlist;
import com.pluralsight.entertainmentmgr.playlist.mapper.PlaylistMapper;
import com.pluralsight.entertainmentmgr.playlist.models.PlaylistDto;
import com.pluralsight.entertainmentmgr.playlist.repositories.PlaylistRepository;
import com.pluralsight.entertainmentmgr.playlist.repositories.PlaylistTrackRepository;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import com.pluralsight.entertainmentmgr.track.mapper.TrackMapper;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.repositories.TrackRepository;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidPlaylistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistDataServiceTest {

    private Playlist playlist;
    private PlaylistDto playlistDto;

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private PlaylistTrackRepository playlistTrackRepository;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private PlaylistMapper playlistMapper;

    @Mock
    private TrackMapper trackMapper;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @InjectMocks
    private PlaylistDataService playlistDataService;

    @BeforeEach
    void setUp() {
        playlistDto = PlaylistDto.builder().id(1L).name("Test Playlist").build();
        playlist = Playlist.builder().id(1L).name("Test Playlist").build();
    }

    @Test
    void getPlaylist_shouldReturnPlaylistDtoIfExists() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(playlistMapper.mapToDto(playlist)).thenReturn(playlistDto);
        Optional<PlaylistDto> result = playlistDataService.getPlaylist(1L);

        assertTrue(result.isPresent());
        assertEquals(playlistDto, result.get());

        verify(playlistRepository, times(1)).findById(1L);
        verify(playlistMapper, times(1)).mapToDto(playlist);
    }

    @Test
    void getPlaylist_shouldReturnEmptyIfNotFound() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<PlaylistDto> result = playlistDataService.getPlaylist(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getPlaylist_shouldReturnEmptyIfIdNull() {
        Optional<PlaylistDto> result = playlistDataService.getPlaylist(null);
        assertTrue(result.isEmpty());
    }


    @Test
    void createPlaylist_shouldSaveAndReturnDto() {
        playlistDto.setId(null);
        playlist.setId(null);

        when(playlistMapper.toEntity(playlistDto)).thenReturn(playlist);
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(playlistMapper.toDTO(playlist)).thenReturn(playlistDto);

        PlaylistDto result = playlistDataService.createPlaylist(playlistDto);

        assertEquals(playlistDto, result);

        verify(playlistMapper, times(1)).toEntity(playlistDto);
        verify(playlistRepository, times(1)).save(playlist);
        verify(playlistMapper, times(1)).toDTO(playlist);
    }

    @Test
    void addTrackToPlaylist_shouldSavePlaylistTrackIfNotExists() {
        Long playlistId = 1L;
        Long trackId = 2L;
        int order = 1;

        when(playlistTrackRepository.existsByPlaylistIdAndTrackId(playlistId, trackId)).thenReturn(false);
        when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
        Track track = new Track();
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(track));
        when(playlistTrackRepository.save(any())).thenReturn(null);
        playlistDataService.addTrackToPlaylist(playlistId, trackId, order);

        verify(playlistTrackRepository, times(1)).existsByPlaylistIdAndTrackId(playlistId, trackId);
        verify(playlistRepository, times(1)).findById(playlistId);
        verify(trackRepository, times(1)).findById(trackId);
        verify(playlistTrackRepository, times(1)).save(any());
    }

    @Test
    void addTrackToPlaylist_shouldNotSaveIfTrackAlreadyExists() {
        when(playlistTrackRepository.existsByPlaylistIdAndTrackId(1L, 2L)).thenReturn(true);
        playlistDataService.addTrackToPlaylist(1L, 2L, 1);

        verify(playlistTrackRepository, times(1)).existsByPlaylistIdAndTrackId(1L, 2L);
        verify(playlistTrackRepository, times(0)).save(any());
    }

    @Test
    void removeTrackFromPlaylist_shouldDeleteIfExists() {
        Long playlistId = 1L;
        Long trackId = 2L;

        when(playlistTrackRepository.existsByPlaylistIdAndTrackId(playlistId, trackId)).thenReturn(true);
        playlistDataService.removeTrackFromPlaylist(playlistId, trackId);

        verify(playlistTrackRepository, times(1)).existsByPlaylistIdAndTrackId(playlistId, trackId);
        verify(playlistTrackRepository, times(1)).deleteByPlaylistIdAndTrackId(playlistId, trackId);
    }

    @Test
    void removeTrackFromPlaylist_shouldNotDeleteIfNotExists() {
        when(playlistTrackRepository.existsByPlaylistIdAndTrackId(1L, 2L)).thenReturn(false);
        playlistDataService.removeTrackFromPlaylist(1L, 2L);

        verify(playlistTrackRepository, times(1)).existsByPlaylistIdAndTrackId(1L, 2L);
        verify(playlistTrackRepository, times(0)).deleteByPlaylistIdAndTrackId(anyLong(), anyLong());
    }

    @Test
    void updatePlaylist_shouldThrowIfIdNullOrDtoNull() {
        assertThrows(NullPointerException.class, () -> playlistDataService.updatePlaylist(null, playlistDto));
        assertThrows(NullPointerException.class, () -> playlistDataService.updatePlaylist(1L, null));
    }

    @Test
    void updatePlaylist_shouldThrowIfIdMismatch() {
        playlistDto.setId(2L);
        assertThrows(InvalidPlaylistException.class, () -> playlistDataService.updatePlaylist(1L, playlistDto));
    }

    @Test
    void updatePlaylist_shouldThrowIfPlaylistNotFound() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.empty());
        playlistDto.setId(1L);
        assertThrows(InvalidPlaylistException.class, () -> playlistDataService.updatePlaylist(1L, playlistDto));
    }

    @Test
    void updatePlaylist_shouldUpdateAndReturnDto() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        doNothing().when(playlistMapper).updateEntityFromDto(playlistDto, playlist);
        when(playlistRepository.save(playlist)).thenReturn(playlist);
        when(playlistMapper.toDTO(playlist)).thenReturn(playlistDto);

        playlistDto.setId(1L);
        PlaylistDto result = playlistDataService.updatePlaylist(1L, playlistDto);

        assertNotNull(result);
        assertEquals(playlistDto, result);

        verify(playlistRepository).findById(1L);
        verify(playlistMapper).updateEntityFromDto(playlistDto, playlist);
        verify(playlistRepository).save(playlist);
        verify(playlistMapper).toDTO(playlist);
    }


    @Test
    void deletePlaylist_shouldThrowIfIdNull() {
        assertThrows(NullPointerException.class, () -> playlistDataService.deletePlaylist(null));
    }

    @Test
    void deletePlaylist_shouldCallDeleteById() {
        doNothing().when(playlistRepository).deleteById(idCaptor.capture());

        playlistDataService.deletePlaylist(1L);

        verify(playlistRepository).deleteById(1L);
        assertEquals(1L, idCaptor.getValue());
    }

    @Test
    void findAllPlaylists_shouldReturnList() {
        List<Playlist> playlists = List.of(playlist);
        when(playlistRepository.findAll()).thenReturn(playlists);
        when(playlistMapper.toDTO(playlist)).thenReturn(playlistDto);

        List<PlaylistDto> result = playlistDataService.findAllPlaylists();

        assertEquals(1, result.size());
        assertEquals(playlistDto, result.get(0));

        verify(playlistRepository).findAll();
        verify(playlistMapper).toDTO(playlist);
    }
}
