package com.cinema.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List; // Jika Anda ingin relasi ke showtimes
import jakarta.persistence.OneToMany; // Jika Anda ingin relasi ke showtimes
import jakarta.persistence.CascadeType; // Jika Anda ingin relasi ke showtimes

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theater")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theater {
    @Id
    private Long id; // Tidak @GeneratedValue karena diisi manual di data.sql

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    // Opsional: Relasi dua arah ke Showtime
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;
}