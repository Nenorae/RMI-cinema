package com.cinema.ticketing.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long showtimeId;
    private List<String> seatNumbers;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}