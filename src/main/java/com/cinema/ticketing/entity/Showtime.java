package com.cinema.ticketing.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List; // Jika ada relasi ke Booking

import jakarta.persistence.CascadeType; // Untuk OneToMany
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany; // Untuk OneToMany
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // Untuk @EqualsAndHashCode.Exclude


@Entity
@Table(name = "showtime")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {
    @Id
    private Long id; // Tidak @GeneratedValue karena diisi manual di data.sql

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater; // Pastikan ini ada dan benar

    @Column(name = "show_date", nullable = false)
    private LocalDate showDate;

    @Column(name = "show_time", nullable = false)
    private LocalTime showTime;

    @Column(name = "screen_number", nullable = false)
    private Integer screenNumber;

    @Column(name = "price", nullable = false, precision = 10, scale = 2) // precision/scale sesuai DECIMAL(10,2)
    private BigDecimal price; // Diubah ke BigDecimal

    @OneToMany(mappedBy = "showtime", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude // Hindari loop tak terbatas
    private List<Booking> bookings; // Relasi dua arah ke Booking
}