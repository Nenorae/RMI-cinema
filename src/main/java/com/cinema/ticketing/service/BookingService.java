package com.cinema.ticketing.service;

import com.cinema.ticketing.dto.BookingRequest;
import com.cinema.ticketing.dto.BookingResponse;
import com.cinema.ticketing.entity.Booking;
import com.cinema.ticketing.entity.Seat; // Import Seat

import java.math.BigDecimal; // Import BigDecimal
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    Booking getBookingById(Long id);
    List<Booking> getBookingsByEmail(String email);
    List<Booking> getBookingsByPhone(String phone);
    boolean cancelBooking(Long bookingId);
    boolean confirmBooking(Long bookingId);

    List<Seat> getSeatsByShowtime(Long showtimeId); // Ini akan mengambil seat berdasarkan theater dari showtime
    List<Seat> getAvailableSeatsByShowtime(Long showtimeId); // Ini perlu logika kompleks
    boolean isSeatAvailable(Long showtimeId, String seatNumber); // Ini perlu logika kompleks
    Long getAvailableSeatsCount(Long showtimeId); // Ini perlu logika kompleks

    BigDecimal calculateTotalPrice(Long showtimeId, List<String> seatNumbers); // Return BigDecimal
    BigDecimal getTotalRevenue(); // Return BigDecimal
    Long getTotalBookingsCount();

    // Tambahkan deklarasi ini jika Anda menggunakannya di implementasi
    String getSeatStatus(Long showtimeId, String seatNumber, List<Seat> allSeatsInTheaterForShowtime);
}