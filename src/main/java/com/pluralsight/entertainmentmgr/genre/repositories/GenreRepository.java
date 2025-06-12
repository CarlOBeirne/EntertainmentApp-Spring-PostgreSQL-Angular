package com.pluralsight.entertainmentmgr.genre.repositories;

import com.pluralsight.entertainmentmgr.genre.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
