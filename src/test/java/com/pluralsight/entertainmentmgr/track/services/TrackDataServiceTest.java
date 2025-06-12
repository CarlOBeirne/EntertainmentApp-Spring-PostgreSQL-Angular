package com.pluralsight.entertainmentmgr.track.services;

import com.pluralsight.entertainmentmgr.artist.entities.Artist;
import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
import com.pluralsight.entertainmentmgr.artist.repositories.ArtistRepository;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidTrackException;
import com.pluralsight.entertainmentmgr.genre.entities.Genre;
import com.pluralsight.entertainmentmgr.genre.mapper.GenreMapper;
import com.pluralsight.entertainmentmgr.genre.models.GenreDto;
import com.pluralsight.entertainmentmgr.genre.service.GenreDataService;
import com.pluralsight.entertainmentmgr.track.entities.Track;
import com.pluralsight.entertainmentmgr.track.mapper.TrackMapper;
import com.pluralsight.entertainmentmgr.track.models.TrackDto;
import com.pluralsight.entertainmentmgr.track.repositories.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackDataServiceTest {

    private Track track;

    private TrackDto trackDto;

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private TrackMapper trackMapper;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private GenreDataService genreDataService;

    @Mock
    private GenreMapper genreMapper;

    @Captor
    private ArgumentCaptor<Long> trackCaptor;

    @InjectMocks
    private TrackDataService trackDataService;

    @BeforeEach
    void setUp() {

        trackDto = TrackDto.builder().id(1L).build();
        track = Track.builder().id(1L).build();

    }

    @Test
    void findTrackById_shouldReturnTrackIfTrackExists() {
        // Arrange
        when(trackRepository.findById(1L)).thenReturn(Optional.of(track));
        when(trackMapper.toDTO(track)).thenReturn(trackDto);

        // Act
        Optional<TrackDto> optionalTrack = trackDataService.findTrackById(1L);

        // Assert
        assertTrue(optionalTrack.isPresent());
        assertEquals(trackDto, optionalTrack.get());
    }

    @Test
    void findTrackById_shouldReturnEmptyOptionalIfTrackDoesNotExist() {
        // Arrange
        when(trackRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<TrackDto> optionalTrack = trackDataService.findTrackById(1L);

        // Assert
        assertTrue(optionalTrack.isEmpty());
    }

    @Test
    void findTrackById_shouldThrowNullPointerExceptionIfTrackIdIsNull() {
        assertThrows(NullPointerException.class, () -> trackDataService.findTrackById(null));
    }

    @Test
    void findTrackById_shouldCallFindTrackByIdAndMapperOnce() {
        // Arrange
        when(trackRepository.findById(1L)).thenReturn(Optional.of(track));

        // Act
        trackDataService.findTrackById(1L);

        // Assert
        verify(trackMapper, times(1)).toDTO(track);
        verify(trackRepository, times(1)).findById(1L);
    }

    @Test
    void findAllTrack_shouldReturnAllTracks() {
        // Arrange
        List<Track> tracks = List.of(track);
        when(trackRepository.findAll()).thenReturn(tracks);
        when(trackMapper.toDTO(track)).thenReturn(trackDto);

        // Act
        List<TrackDto> allTracks = trackDataService.findAllTracks();

        // Assert
        verify(trackRepository, times(1)).findAll();
        verify(trackMapper, times(1)).toDTO(track);

        assertEquals(1, allTracks.size());
        assertEquals(trackDto, allTracks.getFirst());
    }

    @Test
    void createTrack_shouldThrowNullPointerExceptionIfTrackDtoIsNull() {
        assertThrows(NullPointerException.class, () -> trackDataService.createTrack(null));
    }

    @Test
    void createTrack_shouldThrowInvalidTrackExceptionIfTrackIdIsNotNull() {
        assertThrows(InvalidTrackException.class, () -> trackDataService.createTrack(trackDto));
    }

    @Test
    void createTrack_shouldCreateTrackWithArtistAndGenreIfTrackDoesNotExist() {
        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
        Artist artist = Artist.builder().id(1L).build();
        Set<ArtistDto> artists = new HashSet<>();
        artists.add(artistDto);
        trackDto.setArtists(artists);
        Genre genre = Genre.builder().id(1L).build();
        GenreDto genreDto = GenreDto.builder().id(1L).build();
        trackDto.setGenre(genreDto);
        trackDto.setId(null);
        track.setId(null);

        when(trackMapper.toDTO(track)).thenReturn(trackDto);
        when(trackMapper.toEntity(trackDto)).thenReturn(track);
        when(trackRepository.save(track)).thenReturn(track);
        when(artistRepository.findById(1L)).thenReturn(Optional.ofNullable(artist));
        when(genreDataService.findGenreById(1L)).thenReturn(Optional.of(genreDto));
        when(genreMapper.toEntity(genreDto)).thenReturn(genre);

        TrackDto newTrack = trackDataService.createTrack(trackDto);

        assertEquals(trackDto, newTrack);
        verify(trackRepository, times(1)).save(track);
        verify(trackMapper, times(1)).toDTO(track);

    }

    @Test
    void updateTrack_shouldCallFindByIdAndSaveOnceAndMapper3x() {
        // Arrange
        when(trackMapper.toEntity(trackDto)).thenReturn(track);
        when(trackRepository.findById(1L)).thenReturn(Optional.ofNullable(track));
        when(trackRepository.save(track)).thenReturn(track);
        when(trackMapper.toDTO(track)).thenReturn(trackDto);

        // Act
        trackDataService.updateTrack(1L, trackDto);

        // Assert
        verify(trackMapper, times(1)).toDTO(track);
        verify(trackMapper, times(1)).toEntity(trackDto);
        verify(trackMapper, times(1)).updateEntityFromDto(trackDto, track);

        verify(trackRepository, times(1)).findById(1L);
        verify(trackRepository, times(1)).save(track);
    }

    @Test
    void updateTrack_shouldUpdateTrack() {
        // Arrange
        when(trackMapper.toEntity(trackDto)).thenReturn(track);
        when(trackRepository.findById(1L)).thenReturn(Optional.ofNullable(track));
        when(trackRepository.save(track)).thenReturn(track);
        when(trackMapper.toDTO(track)).thenReturn(trackDto);

        // Act
        TrackDto persistedTrack = trackDataService.updateTrack(1L, trackDto);

        // Assert
        assertNotNull(persistedTrack);
        assertEquals(trackDto.getId(), persistedTrack.getId());
    }

    @Test
    void updateTrack_shouldThrowNullPointerExceptions_WhenIdOrTrackDtoIsNull() {
        trackDto.setId(null);

        assertThrows(NullPointerException.class, () -> trackDataService.updateTrack(null, trackDto));
        assertThrows(NullPointerException.class, () -> trackDataService.updateTrack(1L, null));
    }

    @Test
    void updateTrack_shouldThrowInvalidTrackExceptions_WhenIdIsNullOrMismatch() {
        trackDto.setId(null);
        assertThrows(InvalidTrackException.class, () -> trackDataService.updateTrack(1L, trackDto));

        trackDto.setId(2L);
        assertThrows(InvalidTrackException.class, () -> trackDataService.updateTrack(1L, trackDto));
    }

    @Test
    void updateTrack_shouldThrowInvalidTrackException_WhenTrackDoesNotExist() {
        when(trackRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvalidTrackException.class, () -> trackDataService.updateTrack(1L, trackDto));
    }

    @Test
    void deleteTrack_shouldCallDeleteByIdOnceAndDeleteTrack() {
        // Arrange
        doNothing().when(trackRepository).deleteById(trackCaptor.capture());

        // Act
        System.out.println(track.getId());
        trackDataService.deleteTrack(track.getId());

        // Assert
        verify(trackRepository, times(1)).deleteById(track.getId());
        assertEquals(track.getId(), trackCaptor.getValue());
    }

    @Test
    void deleteTrack_shouldThrowNullPointerExceptions_WhenTrackIsNull() {
        assertThrows(NullPointerException.class, () -> trackDataService.deleteTrack(null));
    }

}