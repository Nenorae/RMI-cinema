// Contoh BookingResponse.java
package com.cinema.ticketing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// import java.util.List; // Jika seatNumbers adalah List

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private boolean success;
    private String message;
    private Long bookingId;
    private String movieTitle;
    private String showDate;
    private String showTime;
    private String seatNumbers; // Atau List<String>
    private String customerName;
    private BigDecimal totalPrice; // Pastikan ini BigDecimal
    private LocalDateTime bookingTime;

    public static BookingResponse success(Long bookingId, String movieTitle, String showDate, String showTime,
                                        String seatNumbers, String customerName, BigDecimal totalPrice, // Terima BigDecimal
                                        LocalDateTime bookingTime) {
        return new BookingResponse(true, "Booking successful!", bookingId, movieTitle, showDate, showTime,
                                   seatNumbers, customerName, totalPrice, bookingTime);
    }

    public static BookingResponse failure(String message) {
        BookingResponse response = new BookingResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}