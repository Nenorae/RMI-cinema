package com.cinema.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint; // Untuk unique constraint
import java.util.Set; // Jika Anda ingin relasi dua arah ke BookingSeat
import jakarta.persistence.OneToMany; // Jika Anda ingin relasi dua arah ke BookingSeat


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // Untuk @EqualsAndHashCode.Exclude

@Entity
@Table(name = "seat", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"theater_id", "seat_number"}) // Sesuai schema.sql
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Sesuai schema.sql (AUTO_INCREMENT)
    private Long id;

    @ManyToOne // Seat dimiliki oleh Theater
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater; // Relasi ke Theater

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    // Kolom is_booked di tabel seat mungkin kurang ideal untuk status per showtime.
    // Status booking kursi seharusnya dikelola melalui tabel booking_seat.
    // Namun, jika Anda tetap ingin mempertahankannya untuk tujuan lain (misal, kursi rusak), biarkan.
    // Untuk logika ketersediaan per showtime, kita akan query ke booking_seat.
    @Column(name = "is_booked", nullable = false)
    private Boolean isBooked = false;

    // Relasi ke booking_seat jika diperlukan untuk navigasi dari Seat
    // Ini adalah sisi invers dari relasi ManyToMany antara Booking dan Seat
    // @OneToMany(mappedBy = "seat")
    // @EqualsAndHashCode.Exclude // Hindari loop tak terbatas saat generate equals/hashcode
    // private Set<BookingSeat> bookingSeats; // Merujuk ke entitas join table jika Anda membuatnya
    //                                       // atau @ManyToMany(mappedBy="seats") private Set<Booking> bookings; jika langsung
}