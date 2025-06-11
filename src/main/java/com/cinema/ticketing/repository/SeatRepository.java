package com.cinema.ticketing.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set; // Untuk Set<Long> dari seat IDs

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Untuk query kustom
import org.springframework.data.repository.query.Param; // Untuk parameter di query kustom

import com.cinema.ticketing.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Mencari kursi berdasarkan theaterId dan seatNumber
    Optional<Seat> findByTheaterIdAndSeatNumber(Long theaterId, String seatNumber);

    // Mengambil semua kursi di suatu theater, diurutkan berdasarkan nomor kursi
    List<Seat> findByTheaterIdOrderBySeatNumberAsc(Long theaterId);

    // Metode ini mungkin tidak lagi relevan jika 'isBooked' di Seat tidak diandalkan
    // untuk ketersediaan per showtime.
    // List<Seat> findByTheaterIdAndIsBooked(Long theaterId, boolean isBooked);
    // Long countByTheaterIdAndIsBooked(Long theaterId, boolean isBooked);

    // Query untuk mendapatkan ID kursi yang sudah dipesan untuk showtime tertentu
   
    @Query("SELECT bs.id FROM Booking b JOIN b.seats bs WHERE b.showtime.id = :showtimeId AND b.status = 'CONFIRMED'")
    Set<Long> findBookedSeatIdsByShowtimeId(@Param("showtimeId") Long showtimeId);

    
    // Query untuk mendapatkan kursi yang tersedia untuk showtime tertentu di theater tertentu
    @Query("SELECT s FROM Seat s WHERE s.theater.id = :theaterId AND s.id NOT IN " +
           "(SELECT bs.id FROM Booking b JOIN b.seats bs WHERE b.showtime.id = :showtimeId AND b.status = 'CONFIRMED') " +
           "ORDER BY s.seatNumber ASC")
    List<Seat> findAvailableSeatsForShowtime(@Param("theaterId") Long theaterId, @Param("showtimeId") Long showtimeId);

    // Query untuk menghitung kursi yang tersedia
    @Query("SELECT count(s) FROM Seat s WHERE s.theater.id = :theaterId AND s.id NOT IN " +
       "(SELECT bs.id FROM Booking b JOIN b.seats bs WHERE b.showtime.id = :showtimeId AND b.status = 'CONFIRMED')")
    Long countAvailableSeatsForShowtime(@Param("theaterId") Long theaterId, @Param("showtimeId") Long showtimeId);
}