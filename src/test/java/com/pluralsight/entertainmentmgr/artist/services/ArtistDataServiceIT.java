//package com.pluralsight.entertainmentmgr.artist.services;
//
//import com.pluralsight.entertainmentmgr.artist.entities.Artist;
//import com.pluralsight.entertainmentmgr.artist.enums.ArtistType;
//import com.pluralsight.entertainmentmgr.artist.mapper.ArtistMapper;
//import com.pluralsight.entertainmentmgr.artist.models.ArtistDto;
//import com.pluralsight.entertainmentmgr.artist.repositories.ArtistRepository;
//import com.pluralsight.entertainmentmgr.core.exceptions.InvalidArtistException;
//import com.pluralsight.entertainmentmgr.core.security.app_user.entities.AppUser;
//import com.pluralsight.entertainmentmgr.core.security.app_user.repositories.AppUserRepository;
//import com.pluralsight.entertainmentmgr.genre.mapper.GenreMapper;
//import com.pluralsight.entertainmentmgr.genre.repositories.GenreRepository;
//import com.pluralsight.entertainmentmgr.track.repositories.TrackRepository;
//import com.pluralsight.entertainmentmgr.track.services.TrackDataService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//@ActiveProfiles("test")
//@Tag("integration")
//public class ArtistDataServiceIT {
//
//    @Autowired
//    private ArtistDataService artistDataService;
//
//    @Autowired
//    private ArtistRepository artistRepository;
//
//    @Autowired
//    private final AppUserRepository appUserRepository;
//
//    @Autowired
//    private TrackDataService trackDataService;
//
//    @Autowired
//    private TrackRepository trackRepository;
//
//    @Autowired
//    private GenreRepository genreRepository;
//
//    @Autowired
//    private ArtistMapper artistMapper;
//
//    @Autowired
//    private GenreMapper genreMapper;
//
//    @Transactional
//    @BeforeEach
//    public void setUp() {
//        genreRepository.deleteAll();
//        trackRepository.deleteAll();
//        artistRepository.deleteAll();
//        appUserRepository.deleteAll();
//   }
//
////    @AfterEach
////    public void tearDown() {
////        artistRepository.deleteAll();
////        appUserRepository.deleteAll();
////        trackRepository.deleteAll();
////        genreRepository.deleteAll();
////    }
//
//    @Test
//    void createArtist_shouldThrowException_whenArtistDtoPassedWithId() {
//        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
//        InvalidArtistException iae = assertThrows(InvalidArtistException.class, () -> artistDataService.createArtist(artistDto));
//        assertNotNull(iae);
//        assertEquals("Attempted to create a new artist that already has an Id", iae.getMessage());
//    }
//
//    @Test
//    void createArtist_shouldThrowException_whenNullArgumentsArePassed() {
//        assertThrows(NullPointerException.class, () -> artistDataService.createArtist(null));
//    }
//
//    @Test
//    void createArtist_shouldThrowException_whenArtistDtoPassedWithoutAppUserUsername() {
//        // Arrange
//        ArtistDto artistDto = ArtistDto.builder().appUser(null).build();
//
//        // Act
//        NullPointerException npe = assertThrows(NullPointerException.class, () -> artistDataService.createArtist(artistDto));
//
//        // Assert
//        assertNotNull(npe);
//
//    }
//
//    @Test
//    void createArtist_shouldThrowException_whenArtistDtoPassedWithoutExistingAppUserId() {
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        ArtistDto artistDto = ArtistDto.builder().appUser(user).build();
//
//        InvalidArtistException iae = assertThrows(InvalidArtistException.class, () -> artistDataService.createArtist(artistDto));
//
//        assertNotNull(iae);
//        assertEquals("Attempted to create an artist with an invalid username", iae.getMessage());
//    }
//
//    @Test
//    void createArtist_shouldDoSomething_whenArtistDtoPassedWithAlreadyLinkedAppUserUsername() {
//        // Arrange
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
//        ArtistDto firstArtistDto = ArtistDto.builder().appUser(user).build();
//        ArtistDto firstArtistLinked = artistDataService.createArtist(firstArtistDto);
//        assertNotNull(firstArtistLinked);
//        assertEquals(persistedUser.getUsername(), firstArtistLinked.getAppUser().getUsername());
//        ArtistDto secondArtistDto = ArtistDto.builder().appUser(user).build();
//
//        // act
//        DataIntegrityViolationException e = assertThrows(DataIntegrityViolationException.class, () -> artistDataService.createArtist(secondArtistDto));
//
//        // Assert
//        assertNotNull(e);
//        assertTrue(e.getMessage().contains("ERROR: duplicate key value violates unique constraint"));
//
//    }
//
//    @Test
//    void createArtist_shouldPersistArtist() {
//        // Arrange
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
////        ArtistDto artistDto = ArtistDto.builder().name("fake").appUser(persistedUser).build();
//
//        // Act
//        ArtistDto persistedArtist = artistDataService.createArtist(artistDto);
//
//        // Assert
//        assertNotNull(persistedArtist);
//        assertNotNull(persistedArtist.getId());
//    }
//
//    @Test
//    void findArtistById_shouldThrowException_whenNullArgumentsArePassed() {
//        assertThrows(NullPointerException.class, () -> artistDataService.findArtistById(null));
//    }
//
//    @Test
//    void findArtistById_shouldReturnEmptyOptional_whenArtistDoesNotExist() {
//        // Arrange
//        ArtistDto artistDto = ArtistDto.builder().id(1L).build();
//
//        // Act
//        Optional<ArtistDto> artist = artistDataService.findArtistById(artistDto.getId());
//
//        // Assert
//        assertTrue(artist.isEmpty());
//    }
//
//    @Test
//    void findArtistById_shouldReturnArtist_whenArtistExists() {
//        // Arrange
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
//        assertNotNull(persistedUser);
//
//        ArtistDto artistDto = ArtistDto.builder().appUser(persistedUser).build();
//        ArtistDto persistedArtist = artistDataService.createArtist(artistDto);
//        assertNotNull(persistedArtist);
//        assertNotNull(persistedArtist.getId());
//
//        // Act
//        Optional<ArtistDto> optionalArtist = artistDataService.findArtistById(persistedArtist.getId());
//
//        // Asset
//        assertTrue(optionalArtist.isPresent());
//        ArtistDto artist = optionalArtist.get();
//        assertNotNull(artist.getId());
//        assertEquals(persistedArtist.getId(), artist.getId());
//    }
//
//    @Test
//    void findAllArtists_shouldReturnEmptyList_whenNoArtistsCurrentlyExist() {
//        List<ArtistDto> allArtists = artistDataService.findAllArtists();
//        assertEquals(0, allArtists.size());
//    }
//
//    @Test
//    void findAllArtists_shouldReturnListOfArtists_whenArtistsExist() {
//        AppUser user1 = AppUser.builder().username("fake").password("fake").build();
//        AppUser user2 = AppUser.builder().username("fake2").password("fake").build();
//        appUserRepository.saveAll(List.of(user1, user2));
//
//        Artist artist1 = Artist.builder().name("fake").artistType(ArtistType.SOLO).yearFounded(2000).appUser(user1).build();
//        Artist artist2 = Artist.builder().name("fake2").artistType(ArtistType.SOLO).yearFounded(2000).appUser(user2).build();
//        artistRepository.saveAll(List.of(artist1, artist2));
//
//        List<ArtistDto> allArtists = artistDataService.findAllArtists();
//
//        assertEquals(2, allArtists.size());
//    }
//
//    @Test
//    void findArtistByName_shouldThrowException_whenNullArgumentsArePassed() {
//        assertThrows(NullPointerException.class, () -> artistDataService.findArtistsByName(null));
//    }
//
//    @Test
//    void findArtistsByName_shouldReturnEmptyList_whenNoArtistsCurrentlyExist() {
//        List<ArtistDto> allArtists = artistDataService.findAllArtists();
//        assertEquals(0, allArtists.size());
//    }
//
//    @Test
//    void findArtistByName_shouldReturnArtist_whenArtistExists() {
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
//        ArtistDto artist = ArtistDto.builder().name("fake").appUser(persistedUser).build();
//        ArtistDto persistedArtist = artistDataService.createArtist(artist);
//
//        List<ArtistDto> artistsByName = artistDataService.findArtistsByName(persistedArtist.getName());
//
//        assertEquals(1, artistsByName.size());
//    }
//
//    @Test
//    void updateArtist_shouldThrowNullPointerExceptionsExceptions_whenArgumentsAreNull() {
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
//        ArtistDto artist = ArtistDto.builder().name("fake").appUser(persistedUser).build();
//        ArtistDto persistedArtist = artistDataService.createArtist(artist);
//
//        assertAll(
//                () -> assertThrows(NullPointerException.class, () -> artistDataService.updateArtist(null, persistedArtist)),
//                () -> assertThrows(NullPointerException.class, () -> artistDataService.updateArtist(persistedArtist.getId(), null))
//        );
//    }
//
//    @Test
//    void updateArtist_shouldThrowInvalidArtisExceptions() {
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
//        ArtistDto artist = ArtistDto.builder().name("fake").appUser(persistedUser).build();
//        ArtistDto persistedArtist = artistDataService.createArtist(artist);
//
//        InvalidArtistException noIdOnArtist = assertThrows(InvalidArtistException.class, () -> artistDataService.updateArtist(persistedArtist.getId(), artist));
//        InvalidArtistException idMismatch = assertThrows(InvalidArtistException.class, () -> artistDataService.updateArtist(Long.MAX_VALUE, persistedArtist));
//        artistDataService.deleteArtist(persistedArtist.getId());
//        InvalidArtistException notInDatabase = assertThrows(InvalidArtistException.class, () -> artistDataService.updateArtist(persistedArtist.getId(), persistedArtist));
//
//        assertEquals("Cannot update an artist that does not have an Id", noIdOnArtist.getMessage());
//        assertEquals("Discrepancy between id and artist id", idMismatch.getMessage());
//        assertEquals("No artist found", notInDatabase.getMessage());
//    }
//
//    @Test
//    void updateArtist_shouldUpdateArtist_whenArtistExists() {
//        // Arrange
//        AppUser user = AppUser.builder().username("fake").password("fake").build();
//        AppUser persistedUser = appUserRepository.save(user);
//        ArtistDto artist = ArtistDto.builder().name("fake").appUser(persistedUser).build();
//        ArtistDto persistedArtist = artistDataService.createArtist(artist);
//        Optional<ArtistDto> optionalArtistToUpdate = artistDataService.findArtistById(persistedArtist.getId());
//        assertTrue(optionalArtistToUpdate.isPresent());
//        ArtistDto artistToUpdate = optionalArtistToUpdate.get();
//        artistToUpdate.setBiography("test");
//
//        // Act
//        ArtistDto updatedArtist = artistDataService.updateArtist(persistedArtist.getId(), artistToUpdate);
//
//        // Assert
//        assertEquals(artistToUpdate.getBiography(), updatedArtist.getBiography());
//    }
//
//    @Test
//    void deleteArtist_shouldThrowExceptions() {
//        assertThrows(NullPointerException.class, () -> artistDataService.deleteArtist(null));
//        assertThrows(InvalidArtistException.class, () -> artistDataService.deleteArtist(Long.MAX_VALUE));
//    }
//
////    @Test
////    void deleteArtist_shouldThrowException_whenDeleteAttemptButTracksNotRemoved() {
////        AppUser user = AppUser.builder().username("fake").password("fake").build();
////        AppUser persistedUser = appUserRepository.save(user);
////        ArtistDto artist = ArtistDto.builder().name("fake").appUser(persistedUser).build();
////        ArtistDto persistedArtist = artistDataService.createArtist(artist);
////        Artist artistEntity = artistMapper.toEntity(persistedArtist);
////        GenreDto genre = GenreDto.builder().name("fake").build();
////        Genre persistedGenre = genreRepository.save(genreMapper.toEntity(genre));
////        GenreDto genreDto = genreMapper.toDTO(persistedGenre);
////        TrackDto track = TrackDto.builder().title("fake").genre(genreDto).artists(Set.of(persistedArtist)).build();
////        TrackDto track1 = trackDataService.createTrack(track);
////
////        artistDataService.deleteArtist(persistedArtist.getId());
////    }
//
//}
