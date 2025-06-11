package com.cinema.ticketing.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinema.ticketing.dto.BookingRequest;
import com.cinema.ticketing.dto.BookingResponse;
import com.cinema.ticketing.entity.Booking;
import com.cinema.ticketing.entity.Seat;
import com.cinema.ticketing.entity.Showtime;
import com.cinema.ticketing.repository.BookingRepository;
import com.cinema.ticketing.repository.SeatRepository;
import com.cinema.ticketing.repository.ShowtimeRepository;
import com.cinema.ticketing.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request) {
        try {
            // VALIDASI AWAL
            if (request == null) {
                return BookingResponse.failure("Invalid booking request: request is null.");
            }
            if (request.getShowtimeId() == null) {
                return BookingResponse.failure("Showtime ID is required for booking.");
            }
            if (request.getSeatNumbers() == null || request.getSeatNumbers().isEmpty()) {
                return BookingResponse.failure("At least one seat number is required.");
            }

            Showtime showtime = getShowtimeById(request.getShowtimeId());
            BigDecimal pricePerTicket = validatePrice(showtime.getPrice());
            Set<Long> bookedSeatIds = seatRepository.findBookedSeatIdsByShowtimeId(showtime.getId());

            Set<Seat> seatsToBeBooked = new HashSet<>();
            for (String seatNumber : request.getSeatNumbers()) {
                Seat seat = seatRepository.findByTheaterIdAndSeatNumber(showtime.getTheater().getId(), seatNumber)
                        .orElseThrow(() -> new RuntimeException("Seat " + seatNumber + " not found in theater " + showtime.getTheater().getName()));
                
                if (bookedSeatIds.contains(seat.getId())) {
                    return BookingResponse.failure("Seat " + seatNumber + " is already booked for this showtime.");
                }
                seatsToBeBooked.add(seat);
            }

            if (seatsToBeBooked.isEmpty()) {
                return BookingResponse.failure("No seats selected or all seats are already booked.");
            }

            BigDecimal totalPrice = pricePerTicket.multiply(BigDecimal.valueOf(seatsToBeBooked.size()));

            Booking booking = new Booking();
            booking.setShowtime(showtime);
            booking.setCustomerName(request.getCustomerName());
            booking.setCustomerEmail(request.getCustomerEmail());
            booking.setCustomerPhone(request.getCustomerPhone());
            booking.setBookingTime(LocalDateTime.now());
            booking.setTotalPrice(totalPrice);
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            booking.setSeats(seatsToBeBooked);

            Booking savedBooking = bookingRepository.save(booking);

            String seatNumbersStr = seatsToBeBooked.stream()
                    .map(Seat::getSeatNumber)
                    .sorted()
                    .collect(Collectors.joining(", "));

            return BookingResponse.success(
                    savedBooking.getId(),
                    showtime.getMovie().getTitle(),
                    showtime.getShowDate().toString(),
                    showtime.getShowTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                    seatNumbersStr,
                    request.getCustomerName(),
                    savedBooking.getTotalPrice(),
                    savedBooking.getBookingTime()
            );

        } catch (Exception e) {
            System.err.println("---------- EXCEPTION SAAT CREATE BOOKING ----------");
            e.printStackTrace();
            System.err.println("---------------------------------------------------");
            return BookingResponse.failure("Booking failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByEmail(String customerEmail) {
        return bookingRepository.findByCustomerEmail(customerEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByPhone(String customerPhone) {
        return bookingRepository.findByCustomerPhone(customerPhone);
    }

    @Override
    public boolean cancelBooking(Long bookingId) {
        try {
            Booking booking = getBookingById(bookingId);
            if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
                return true;
            }
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean confirmBooking(Long bookingId) {
        try {
            Booking booking = getBookingById(bookingId);
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getSeatsByShowtime(Long showtimeId) {
        Showtime showtime = getShowtimeById(showtimeId);
        return seatRepository.findByTheaterIdOrderBySeatNumberAsc(showtime.getTheater().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getAvailableSeatsByShowtime(Long showtimeId) {
        Showtime showtime = getShowtimeById(showtimeId);
        return seatRepository.findAvailableSeatsForShowtime(showtime.getTheater().getId(), showtimeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSeatAvailable(Long showtimeId, String seatNumber) {
        Showtime showtime = getShowtimeById(showtimeId);
        Seat seat = seatRepository.findByTheaterIdAndSeatNumber(showtime.getTheater().getId(), seatNumber).orElse(null);
        if (seat == null) {
            return false;
        }
        Set<Long> bookedSeatIds = seatRepository.findBookedSeatIdsByShowtimeId(showtimeId);
        return !bookedSeatIds.contains(seat.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getAvailableSeatsCount(Long showtimeId) {
        Showtime showtime = getShowtimeById(showtimeId);
        return seatRepository.countAvailableSeatsForShowtime(showtime.getTheater().getId(), showtimeId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPrice(Long showtimeId, List<String> seatNumbers) {
        Showtime showtime = getShowtimeById(showtimeId);
        BigDecimal pricePerTicket = validatePrice(showtime.getPrice());
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return pricePerTicket.multiply(BigDecimal.valueOf(seatNumbers.size()));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = bookingRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalBookingsCount() {
        return bookingRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public String getSeatStatus(Long showtimeId, String seatNumber, List<Seat> unusedParam) {
        Showtime showtime = getShowtimeById(showtimeId);
        Optional<Seat> seatOpt = seatRepository.findByTheaterIdAndSeatNumber(showtime.getTheater().getId(), seatNumber);
        if (!seatOpt.isPresent()) {
            return "NOT_FOUND";
        }
        Seat seat = seatOpt.get();
        Set<Long> bookedSeatIds = seatRepository.findBookedSeatIdsByShowtimeId(showtimeId);
        return bookedSeatIds.contains(seat.getId()) ? "BOOKED" : "AVAILABLE";
    }

    // Helper Methods
    private Showtime getShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found with id: " + showtimeId));
    }

    private BigDecimal validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Price for showtime is not set or invalid.");
        }
        return price;
    }
}
