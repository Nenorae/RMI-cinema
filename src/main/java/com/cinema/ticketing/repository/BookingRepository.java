package com.cinema.ticketing.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cinema.ticketing.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByCustomerEmail(String customerEmail);
    
    List<Booking> findByCustomerPhone(String customerPhone);
    
    List<Booking> findByShowtimeId(Long showtimeId);
    
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.bookingTime >= :startTime AND b.bookingTime <= :endTime")
    List<Booking> findByBookingTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT b FROM Booking b WHERE b.customerName LIKE %:name%")
    List<Booking> findByCustomerNameContaining(@Param("name") String name);
    
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal getTotalRevenue();
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.showtime.id = :showtimeId AND b.status = 'CONFIRMED'")
    Long countConfirmedBookingsByShowtime(@Param("showtimeId") Long showtimeId);
}