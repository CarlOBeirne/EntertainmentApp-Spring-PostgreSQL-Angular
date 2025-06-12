package com.pluralsight.entertainmentmgr.artist.services;

import com.pluralsight.entertainmentmgr.artist.entities.Artist;
import com.pluralsight.entertainmentmgr.artist.mapper.ArtistMapper;
import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
import com.pluralsight.entertainmentmgr.artist.repositories.ArtistRepository;
import com.pluralsight.entertainmentmgr.core.exceptions.InvalidArtistException;
import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
import com.pluralsight.entertainmentmgr.core.security.app_user.repositories.AppUserRepository;
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
public class ArtistDataServiceTest {

    @Mock
    private ArtistRepository artistRepositoryMock;

    @Mock
    private ArtistMapper artistMapperMock;

    @Mock
    private AppUser appUserMock;

    @Mock
    private AppUserRepository appUserRepositoryMock;

    @InjectMocks
    private ArtistDataService artistDataService;

    @Captor
    private ArgumentCaptor<Artist> artistCaptor;

    @Test
    void findArtistById_shouldReturnArtistMatchingId() {
        // Arrange
        Artist artist = Artist.builder().id(1L).build();
        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
        when(artistRepositoryMock.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(artistMapperMock.toDTO(artist)).thenReturn(artistDto);

        // Act
        Optional<ArtistDto> optionalArtist = artistDataService.findArtistById(artist.getId());

        // Assert
        assertTrue(optionalArtist.isPresent());
        ArtistDto actualArtist = optionalArtist.get();
        assertEquals(artistDto.getId(), actualArtist.getId());
    }

    @Test
    void findArtistById_shouldReturnEmptyOptionalIfArtistDoesNotExist() {
        // Arrange
        when(artistRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<ArtistDto> optionalArtist = artistDataService.findArtistById(1L);

        // Assert
        assertTrue(optionalArtist.isEmpty());

    }

    @Test
    void findArtistById_shouldThrowExceptionIfNullIdIsPassed() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> artistDataService.findArtistById(null));
        verify(artistRepositoryMock, times(0)).findById(anyLong());

    }

    @Test
    void findAllArtists_shouldReturnAllArtists() {
        // Arrange
        Artist artist1 = Artist.builder().id(1L).build();
        ArtistDto artist1Dto = ArtistDto.builder().id(1L).build();
        List<Artist> artists = List.of(artist1);
        when(artistRepositoryMock.findAll()).thenReturn(artists);
        when(artistMapperMock.toDTO(artist1)).thenReturn(artist1Dto);

        // Act
        List<ArtistDto> allArtists = artistDataService.findAllArtists();

        // Assert
        verify(artistRepositoryMock, times(1)).findAll();
        assertEquals(1, allArtists.size());
        ArtistDto firstArtist = allArtists.getFirst();
        assertEquals(artist1Dto.getId(), firstArtist.getId());

    }

    @Test
    void createArtist_shouldThrowExceptionIfArtistIsNull() {
        assertThrows(NullPointerException.class, () -> artistDataService.createArtist(null));
        verify(artistRepositoryMock, times(0)).save(any());
    }

    @Test
    void createArtist_shouldCreateNewArtistWhenIdIsNull() {
        // Arrange
        AppUser user = AppUser.builder().id(1L).build();
        ArtistDto artistDto = ArtistDto.builder().id(null).build();
        Artist artist = Artist.builder().id(1L).build();
        ArtistDto expectedArtistDto = ArtistDto.builder().id(1L).appUser(user).build();
        when(artistMapperMock.toEntity(artistDto)).thenReturn(artist);
        when(artistMapperMock.toDTO(any())).thenReturn(expectedArtistDto);
        when(artistRepositoryMock.save(any(Artist.class))).thenReturn(artist);
        when(appUserRepositoryMock.findByUsername(artistDto.getAppUser().getUsername())).thenReturn(Optional.of(appUserMock));

        // Act
        ArtistDto persistedArtist = artistDataService.createArtist(artistDto);

        // Assert
        verify(artistRepositoryMock, times(1)).save(any(Artist.class));
        assertNotNull(persistedArtist);
        assertEquals(expectedArtistDto.getId(), persistedArtist.getId());
    }

    @Test
    void createArtist_shouldThrowInvalidArtistException_whenArtistIdIsNotNull() {
        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
        InvalidArtistException iae = assertThrows(InvalidArtistException.class, () -> artistDataService.createArtist(artistDto));
        String message = iae.getMessage();
        assertEquals("Attempted to create a new artist that already has an Id", message);
        verify(artistRepositoryMock, times(0)).save(any());
    }

    @Test
    void updateArtist_shouldThrowExceptions_whenIdsAreNullOrMismatch() {
        ArtistDto artistDto = ArtistDto.builder().id(null).build();
        assertThrows(NullPointerException.class, () -> artistDataService.updateArtist(null, artistDto));
        assertThrows(NullPointerException.class, () -> artistDataService.updateArtist(1L, null));

        InvalidArtistException iae = assertThrows(InvalidArtistException.class, () -> artistDataService.updateArtist(1L, artistDto));
        String message = iae.getMessage();
        assertEquals("Cannot update an artist that does not have an Id", message);

        artistDto.setId(2L);
        InvalidArtistException iae2 = assertThrows(InvalidArtistException.class, () -> artistDataService.updateArtist(1L, artistDto));
        String message2 = iae2.getMessage();
        assertEquals("Discrepancy between id and artist id", message2);


    }

    @Test
    void updateArtist_shouldUpdateArtist_whenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
        Artist artistEntity = Artist.builder().id(1L).build();
        ArtistDto updatedArtistDto = ArtistDto.builder().id(1L).name("Updated Name").build();

        when(artistRepositoryMock.findById(artistEntity.getId())).thenReturn(Optional.of(artistEntity));
        when(artistRepositoryMock.save(artistEntity)).thenReturn(artistEntity);
        doNothing().when(artistMapperMock).updateEntityFromDto(artistDto, artistEntity);
        when(artistMapperMock.toDTO(artistEntity)).thenReturn(updatedArtistDto);

        // Act
        ArtistDto persistedArtist = artistDataService.updateArtist(1L, artistDto);

        // Assert
        assertNotNull(persistedArtist);
        assertEquals(artistDto.getId(), persistedArtist.getId());
        assertEquals("Updated Name", persistedArtist.getName());
        verify(artistRepositoryMock, times(1)).save(artistEntity);
    }

    @Test
    void updateArtist_shouldThrowException_whenUnableToFindArtistById() {
        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
        when(artistRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        InvalidArtistException iae = assertThrows(InvalidArtistException.class, () -> artistDataService.updateArtist(1L, artistDto));
        String message = iae.getMessage();
        assertEquals("No artist found", message);
    }

    @Test
    void deleteArtistById_shouldThrowException_whenArtistIdIsNull() {
        assertThrows(NullPointerException.class, () -> artistDataService.deleteArtist(null));
    }

    @Test
    void deleteArtistById_shouldDeleteArtistAndReturnTrueWhenIdIsNotNullAndIdExistsInDatastore() {
        // Arrange
        Artist artist = Artist.builder().id(1L).build();
        when(artistRepositoryMock.findById(artist.getId())).thenReturn(Optional.of(artist));
        doNothing().when(artistRepositoryMock).delete(artistCaptor.capture());

        // Act
        artistDataService.deleteArtist(artist.getId());

        // Assert
        verify(artistRepositoryMock, times(1)).delete(artist);
        assertEquals(artist.getId(), artistCaptor.getValue().getId());

    }
}
