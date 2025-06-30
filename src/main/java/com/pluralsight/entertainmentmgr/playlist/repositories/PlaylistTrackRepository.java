package com.pluralsight.entertainmentmgr.playlist.repositories;

import com.pluralsight.entertainmentmgr.playlist.entities.PlaylistTrack;
import com.pluralsight.entertainmentmgr.playlist.entities.PlaylistTrackId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, PlaylistTrackId> {

    List<PlaylistTrack> findAllByPlaylistIdOrderByTrackOrder(Long playlistId);

    void deleteByPlaylistIdAndTrackId(Long playlistId, Long trackId);

    boolean existsByPlaylistIdAndTrackId(Long playlistId, Long trackId);
}
