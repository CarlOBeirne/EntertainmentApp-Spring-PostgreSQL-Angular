package com.pluralsight.entertainmentmgr.playlist.repositories;

import com.pluralsight.entertainmentmgr.playlist.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}