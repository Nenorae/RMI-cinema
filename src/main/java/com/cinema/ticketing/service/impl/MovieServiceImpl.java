package com.cinema.ticketing.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinema.ticketing.entity.Movie;
import com.cinema.ticketing.entity.Showtime;
import com.cinema.ticketing.repository.MovieRepository;
import com.cinema.ticketing.repository.ShowtimeRepository;
import com.cinema.ticketing.service.MovieService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {
    
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    
    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }
    
    @Override
    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }
    
    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }
    
    @Override
    public List<Movie> getMoviesWithUpcomingShowtimes() {
        return movieRepository.findMoviesWithUpcomingShowtimes();
    }
    
    @Override
    public List<Showtime> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }
    
    @Override
    public List<Showtime> getShowtimesByDate(LocalDate date) {
        return showtimeRepository.findByShowDate(date);
    }
    
    @Override
    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + id));
    }
    
    @Override
    public List<Showtime> getUpcomingShowtimes() {
        return showtimeRepository.findUpcomingShowtimes();
    }
}