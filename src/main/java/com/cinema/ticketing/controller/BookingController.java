package com.cinema.ticketing.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinema.ticketing.dto.BookingRequest;
import com.cinema.ticketing.dto.BookingResponse;
import com.cinema.ticketing.entity.Booking;
import com.cinema.ticketing.entity.Showtime;
import com.cinema.ticketing.service.BookingService;
import com.cinema.ticketing.service.MovieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final MovieService movieService;

    // WEB PAGE BOOKING
    @GetMapping("/booking")
    public String bookingPage(@RequestParam Long showtimeId,
                               @RequestParam String selectedSeats,
                               Model model) {
        try {
            Showtime showtime = movieService.getShowtimeById(showtimeId);
            List<String> seatNumbers = List.of(selectedSeats.split(","));
            BigDecimal totalPrice = bookingService.calculateTotalPrice(showtimeId, seatNumbers);

            // Buat BookingRequest yg sudah pre-filled
            BookingRequest bookingRequest = new BookingRequest();
            bookingRequest.setShowtimeId(showtimeId);
            bookingRequest.setSeatNumbers(seatNumbers);

            model.addAttribute("showtime", showtime);
            model.addAttribute("selectedSeatsString", selectedSeats); // String asli
            model.addAttribute("seatCount", seatNumbers.size());
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("bookingRequest", bookingRequest);

            return "booking";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid booking parameters: " + e.getMessage());
            return "booking-error";
        }
    }

    @PostMapping("/booking")
    public String processBooking(@ModelAttribute BookingRequest request, Model model) {
        if (request.getShowtimeId() == null || request.getSeatNumbers() == null || request.getSeatNumbers().isEmpty()) {
            model.addAttribute("error", "Booking request invalid. Showtime or seats missing.");
            return "booking-error";
        }

        BookingResponse response = bookingService.createBooking(request);

        if (response.isSuccess()) {
            model.addAttribute("booking", response);
            return "booking-success";
        } else {
            model.addAttribute("error", response.getMessage());
            return "booking-error";
        }
    }

    // SEARCH PAGE
    @GetMapping("/bookings/search")
    public String searchBookings(Model model) {
        return "booking-search";
    }

    @GetMapping("/bookings/results")
    public String searchResults(@RequestParam(required = false) String email,
                                 @RequestParam(required = false) String phone,
                                 Model model) {
        List<Booking> bookings;
        if (email != null && !email.isEmpty()) {
            bookings = bookingService.getBookingsByEmail(email);
            model.addAttribute("searchType", "email");
            model.addAttribute("searchValue", email);
        } else if (phone != null && !phone.isEmpty()) {
            bookings = bookingService.getBookingsByPhone(phone);
            model.addAttribute("searchType", "phone");
            model.addAttribute("searchValue", phone);
        } else {
            bookings = List.of();
            model.addAttribute("error", "Please provide email or phone number");
        }

        model.addAttribute("bookings", bookings);
        return "booking-results";
    }

    // REST API SECTION
    @PostMapping("/api/bookings")
    @ResponseBody
    public ResponseEntity<BookingResponse> createBookingApi(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bookings/{id}")
    @ResponseBody
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/bookings/email/{email}")
    @ResponseBody
    public ResponseEntity<List<Booking>> getBookingsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
    }

    @GetMapping("/api/bookings/phone/{phone}")
    @ResponseBody
    public ResponseEntity<List<Booking>> getBookingsByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(bookingService.getBookingsByPhone(phone));
    }

    @PostMapping("/api/bookings/{id}/cancel")
    @ResponseBody
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        boolean success = bookingService.cancelBooking(id);
        if (success) {
            return ResponseEntity.ok().body("{\"message\":\"Booking cancelled successfully\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\":\"Failed to cancel booking\"}");
        }
    }

    @PostMapping("/api/calculate-price")
    @ResponseBody
    public ResponseEntity<BigDecimal> calculatePrice(@RequestParam Long showtimeId,
                                                      @RequestParam List<String> seatNumbers) {
        try {
            BigDecimal price = bookingService.calculateTotalPrice(showtimeId, seatNumbers);
            return ResponseEntity.ok(price);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
