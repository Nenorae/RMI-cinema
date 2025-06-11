package com.cinema.ticketing.service;

import java.time.LocalDate;
import java.util.List;

import com.cinema.ticketing.entity.Movie;
import com.cinema.ticketing.entity.Showtime;

public interface MovieService {
    List<Movie> getAllMovies();
    Movie getMovieById(Long id);
    List<Movie> getMoviesByGenre(String genre);
    List<Movie> searchMoviesByTitle(String title);
    List<Movie> getMoviesWithUpcomingShowtimes();
    List<Showtime> getShowtimesByMovieId(Long movieId);
    List<Showtime> getShowtimesByDate(LocalDate date);
    Showtime getShowtimeById(Long id);
    List<Showtime> getUpcomingShowtimes();
}