package com.cinema.ticketing.rmi.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cinema.ticketing.dto.BookingRequest;
import com.cinema.ticketing.dto.BookingResponse;
import com.cinema.ticketing.entity.Booking;
import com.cinema.ticketing.rmi.remote.BookingRemoteService;
import com.cinema.ticketing.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class BookingRemoteServiceImpl implements BookingRemoteService {

    private final BookingService bookingService;

    @Override
    public BookingResponse createBooking(BookingRequest request) throws RemoteException {
        log.info("RMI: Creating booking for customer: {} on showtime: {}", request.getCustomerEmail(), request.getShowtimeId());

        try {
            if (request == null) {
                log.error("RMI: Booking request is null");
                return errorResponse("Invalid booking request");
            }

            if (request.getShowtimeId() == null) {
                log.error("RMI: Showtime ID is required");
                return errorResponse("Showtime ID is required");
            }

            if (request.getSeatNumbers() == null || request.getSeatNumbers().isEmpty()) {
                log.error("RMI: Seat numbers are required");
                return errorResponse("At least one seat number is required");
            }

            if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
                log.error("RMI: Customer name is required");
                return errorResponse("Customer name is required");
            }

            if (request.getCustomerEmail() == null || request.getCustomerEmail().trim().isEmpty()) {
                log.error("RMI: Customer email is required");
                return errorResponse("Customer email is required");
            }

            if (request.getCustomerPhone() == null || request.getCustomerPhone().trim().isEmpty()) {
                log.error("RMI: Customer phone is required");
                return errorResponse("Customer phone is required");
            }

            if (!isValidEmail(request.getCustomerEmail())) {
                log.error("RMI: Invalid email format: {}", request.getCustomerEmail());
                return errorResponse("Invalid email format");
            }

            BookingResponse response = bookingService.createBooking(request);

            if (response.isSuccess()) {
                log.info("RMI: Booking created successfully with ID: {}", response.getBookingId());
            } else {
                log.warn("RMI: Booking creation failed: {}", response.getMessage());
            }

            return response;

        } catch (Exception e) {
            log.error("RMI: Error creating booking", e);
            throw new RemoteException("Error creating booking: " + e.getMessage(), e);
        }
    }

    private BookingResponse errorResponse(String message) {
        return BookingResponse.builder()
                .success(false)
                .message(message)
                .build();
    }

    @Override
    public Booking getBookingById(Long bookingId) throws RemoteException {
        log.info("RMI: Getting booking by ID: {}", bookingId);

        try {
            if (bookingId == null || bookingId <= 0) {
                log.error("RMI: Invalid booking ID: {}", bookingId);
                return null;
            }

            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                log.info("RMI: Booking found for ID: {}", bookingId);
                return booking;
            } else {
                log.warn("RMI: Booking not found for ID: {}", bookingId);
                return null;
            }

        } catch (Exception e) {
            log.error("RMI: Error getting booking by ID", e);
            throw new RemoteException("Error retrieving booking: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> getBookingsByEmail(String email) throws RemoteException {
        log.info("RMI: Getting bookings by email: {}", email);

        try {
            if (email == null || email.trim().isEmpty()) {
                log.error("RMI: Email is required");
                throw new RemoteException("Email is required");
            }

            if (!isValidEmail(email)) {
                log.error("RMI: Invalid email format: {}", email);
                throw new RemoteException("Invalid email format");
            }

            List<Booking> bookings = bookingService.getBookingsByEmail(email.trim());
            log.info("RMI: Found {} bookings for email: {}", bookings.size(), email);

            return bookings;

        } catch (Exception e) {
            log.error("RMI: Error getting bookings by email", e);
            throw new RemoteException("Error retrieving bookings by email: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> getBookingsByPhone(String phone) throws RemoteException {
        log.info("RMI: Getting bookings by phone: {}", phone);

        try {
            if (phone == null || phone.trim().isEmpty()) {
                log.error("RMI: Phone is required");
                throw new RemoteException("Phone is required");
            }

            List<Booking> bookings = bookingService.getBookingsByPhone(phone.trim());
            log.info("RMI: Found {} bookings for phone: {}", bookings.size(), phone);

            return bookings;

        } catch (Exception e) {
            log.error("RMI: Error getting bookings by phone", e);
            throw new RemoteException("Error retrieving bookings by phone: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cancelBooking(Long bookingId) throws RemoteException {
        log.info("RMI: Cancelling booking with ID: {}", bookingId);

        try {
            if (bookingId == null || bookingId <= 0) {
                log.error("RMI: Invalid booking ID: {}", bookingId);
                return false;
            }

            return bookingService.cancelBooking(bookingId);

        } catch (Exception e) {
            log.error("RMI: Error cancelling booking", e);
            throw new RemoteException("Error cancelling booking: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean confirmBooking(Long bookingId) throws RemoteException {
        log.info("RMI: Confirming booking with ID: {}", bookingId);

        try {
            if (bookingId == null || bookingId <= 0) {
                log.error("RMI: Invalid booking ID: {}", bookingId);
                return false;
            }

            return bookingService.confirmBooking(bookingId);

        } catch (Exception e) {
            log.error("RMI: Error confirming booking", e);
            throw new RemoteException("Error confirming booking: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal calculateTotalPrice(Long showtimeId, List<String> seatNumbers) throws RemoteException {
        log.info("RMI: Calculating total price for showtime: {} and seats: {}", showtimeId, seatNumbers);

        try {
            if (showtimeId == null || showtimeId <= 0) {
                log.error("RMI: Invalid showtime ID: {}", showtimeId);
                throw new RemoteException("Invalid showtime ID");
            }
            if (seatNumbers == null || seatNumbers.isEmpty()) {
                log.error("RMI: Seat numbers are required");
                throw new RemoteException("At least one seat number is required");
            }

            return bookingService.calculateTotalPrice(showtimeId, seatNumbers);

        } catch (Exception e) {
            log.error("RMI: Error calculating total price", e);
            throw new RemoteException("Error calculating total price: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getTotalRevenue() throws RemoteException {
        try {
            BigDecimal revenue = bookingService.getTotalRevenue();
            log.info("RMI: Total revenue: {}", revenue);
            return revenue;
        } catch (Exception e) {
            log.error("RMI: Error getting total revenue", e);
            throw new RemoteException("Error getting total revenue: " + e.getMessage(), e);
        }
    }

    @Override
    public Long getTotalBookingsCount() throws RemoteException {
        return bookingService.getTotalBookingsCount();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
