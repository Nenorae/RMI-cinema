package com.cinema.ticketing.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import com.cinema.ticketing.entity.Movie;
import com.cinema.ticketing.entity.Seat;
import com.cinema.ticketing.entity.Showtime;

public interface CinemaRemoteService extends Remote {
    
    // Movie operations
    List<Movie> getAllMovies() throws RemoteException;
    Movie getMovieById(Long id) throws RemoteException;
    List<Movie> getMoviesByGenre(String genre) throws RemoteException;
    List<Movie> searchMoviesByTitle(String title) throws RemoteException;
    
    // Showtime operations
    List<Showtime> getShowtimesByMovieId(Long movieId) throws RemoteException;
    List<Showtime> getShowtimesByDate(LocalDate date) throws RemoteException;
    Showtime getShowtimeById(Long id) throws RemoteException;
    List<Showtime> getUpcomingShowtimes() throws RemoteException;
    
    // Seat operations
    List<Seat> getSeatsByShowtime(Long showtimeId) throws RemoteException;
    List<Seat> getAvailableSeatsByShowtime(Long showtimeId) throws RemoteException;
    boolean isSeatAvailable(Long showtimeId, String seatNumber) throws RemoteException;
    Long getAvailableSeatsCount(Long showtimeId) throws RemoteException;
}
