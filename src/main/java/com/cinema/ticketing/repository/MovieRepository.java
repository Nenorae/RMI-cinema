package com.cinema.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cinema.ticketing.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    List<Movie> findByGenreContainingIgnoreCase(String genre);
    
    List<Movie> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT m FROM Movie m WHERE m.rating >= :minRating")
    List<Movie> findByRatingGreaterThanEqual(Double minRating);
    
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.showtimes s WHERE s.showDate >= CURRENT_DATE")
    List<Movie> findMoviesWithUpcomingShowtimes();
}