package com.pluralsight.entertainmentmgr.track.repositories;

import com.pluralsight.entertainmentmgr.track.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findAllByTitleIgnoreCase(String title);
}
