package com.cinema.ticketing.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cinema.ticketing.entity.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    
    List<Showtime> findByMovieId(Long movieId);
    
    List<Showtime> findByShowDate(LocalDate showDate);
    
    List<Showtime> findByMovieIdAndShowDate(Long movieId, LocalDate showDate);
    
    @Query("SELECT s FROM Showtime s WHERE s.showDate >= :startDate AND s.showDate <= :endDate")
    List<Showtime> findByShowDateBetween(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    @Query("SELECT s FROM Showtime s WHERE s.showDate >= CURRENT_DATE ORDER BY s.showDate, s.showTime")
    List<Showtime> findUpcomingShowtimes();
    
    List<Showtime> findByScreenNumber(Integer screenNumber);
}