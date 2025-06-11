package com.cinema.ticketing.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cinema.ticketing.entity.Movie;
import com.cinema.ticketing.entity.Seat;
import com.cinema.ticketing.entity.Showtime;
import com.cinema.ticketing.rmi.remote.CinemaRemoteService;
import com.cinema.ticketing.service.BookingService;
import com.cinema.ticketing.service.MovieService;

import lombok.RequiredArgsConstructor;

@Component
public class CinemaRemoteServiceImpl extends UnicastRemoteObject implements CinemaRemoteService {
    
    private final MovieService movieService;
    private final BookingService bookingService;
    
    public CinemaRemoteServiceImpl(MovieService movieService, BookingService bookingService) 
            throws RemoteException {
        super();
        this.movieService = movieService;
        this.bookingService = bookingService;
    }
    
    @Override
    public List<Movie> getAllMovies() throws RemoteException {
        try {
            return movieService.getAllMovies();
        } catch (Exception e) {
            throw new RemoteException("Failed to get all movies", e);
        }
    }
    
    @Override
    public Movie getMovieById(Long id) throws RemoteException {
        try {
            return movieService.getMovieById(id);
        } catch (Exception e) {
            throw new RemoteException("Failed to get movie by id: " + id, e);
        }
    }
    
    @Override
    public List<Movie> getMoviesByGenre(String genre) throws RemoteException {
        try {
            return movieService.getMoviesByGenre(genre);
        } catch (Exception e) {
            throw new RemoteException("Failed to get movies by genre: " + genre, e);
        }
    }
    
    @Override
    public List<Movie> searchMoviesByTitle(String title) throws RemoteException {
        try {
            return movieService.searchMoviesByTitle(title);
        } catch (Exception e) {
            throw new RemoteException("Failed to search movies by title: " + title, e);
        }
    }
    
    @Override
    public List<Showtime> getShowtimesByMovieId(Long movieId) throws RemoteException {
        try {
            return movieService.getShowtimesByMovieId(movieId);
        } catch (Exception e) {
            throw new RemoteException("Failed to get showtimes for movie: " + movieId, e);
        }
    }
    
    @Override
    public List<Showtime> getShowtimesByDate(LocalDate date) throws RemoteException {
        try {
            return movieService.getShowtimesByDate(date);
        } catch (Exception e) {
            throw new RemoteException("Failed to get showtimes for date: " + date, e);
        }
    }
    
    @Override
    public Showtime getShowtimeById(Long id) throws RemoteException {
        try {
            return movieService.getShowtimeById(id);
        } catch (Exception e) {
            throw new RemoteException("Failed to get showtime by id: " + id, e);
        }
    }
    
    @Override
    public List<Showtime> getUpcomingShowtimes() throws RemoteException {
        try {
            return movieService.getUpcomingShowtimes();
        } catch (Exception e) {
            throw new RemoteException("Failed to get upcoming showtimes", e);
        }
    }
    
    @Override
    public List<Seat> getSeatsByShowtime(Long showtimeId) throws RemoteException {
        try {
            return bookingService.getSeatsByShowtime(showtimeId);
        } catch (Exception e) {
            throw new RemoteException("Failed to get seats for showtime: " + showtimeId, e);
        }
    }
    
    @Override
    public List<Seat> getAvailableSeatsByShowtime(Long showtimeId) throws RemoteException {
        try {
            return bookingService.getAvailableSeatsByShowtime(showtimeId);
        } catch (Exception e) {
            throw new RemoteException("Failed to get available seats for showtime: " + showtimeId, e);
        }
    }
    
    @Override
    public boolean isSeatAvailable(Long showtimeId, String seatNumber) throws RemoteException {
        try {
            return bookingService.isSeatAvailable(showtimeId, seatNumber);
        } catch (Exception e) {
            throw new RemoteException("Failed to check seat availability", e);
        }
    }
    
    @Override
    public Long getAvailableSeatsCount(Long showtimeId) throws RemoteException {
        try {
            return bookingService.getAvailableSeatsCount(showtimeId);
        } catch (Exception e) {
            throw new RemoteException("Failed to get available seats count", e);
        }
    }
}