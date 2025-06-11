package com.cinema.ticketing.rmi.remote;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.cinema.ticketing.dto.BookingRequest;
import com.cinema.ticketing.dto.BookingResponse;
import com.cinema.ticketing.entity.Booking;

public interface BookingRemoteService extends Remote {

    // Booking operations
    BookingResponse createBooking(BookingRequest request) throws RemoteException;
    Booking getBookingById(Long id) throws RemoteException;
    List<Booking> getBookingsByEmail(String email) throws RemoteException;
    List<Booking> getBookingsByPhone(String phone) throws RemoteException;

    // Booking management
    boolean cancelBooking(Long bookingId) throws RemoteException;
    boolean confirmBooking(Long bookingId) throws RemoteException;
    BigDecimal calculateTotalPrice(Long showtimeId, List<String> seatNumbers) throws RemoteException;

    // Statistics
    BigDecimal getTotalRevenue() throws RemoteException;
    Long getTotalBookingsCount() throws RemoteException;
}
