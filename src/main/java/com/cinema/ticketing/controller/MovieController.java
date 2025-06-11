// File: MovieController.java
package com.cinema.ticketing.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map; // Import Map
import java.util.HashMap; // Import HashMap
import java.util.stream.Collectors; // Import Collectors
import java.util.LinkedHashMap; // Import LinkedHashMap


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinema.ticketing.entity.Movie;
import com.cinema.ticketing.entity.Seat; // Import Seat
import com.cinema.ticketing.entity.Showtime;
import com.cinema.ticketing.service.BookingService;
import com.cinema.ticketing.service.MovieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final BookingService bookingService;

    // Web pages
    @GetMapping("/movies")
    public String moviesPage(Model model, @RequestParam(required = false) String genre) {
        List<Movie> movies;
        String currentGenre = genre;

        if (genre == null || genre.isEmpty() || "All".equalsIgnoreCase(genre)) {
            movies = movieService.getAllMovies(); // Ambil semua film untuk "Semua Genre"
            currentGenre = "All"; // Untuk menandai tab "Semua" sebagai aktif
        } else {
            movies = movieService.getMoviesByGenre(genre); // Ambil film berdasarkan genre spesifik
        }

        // Menyediakan daftar genre untuk ditampilkan sebagai tab
        // Anda bisa membuatnya lebih dinamis jika diperlukan, misalnya dari database
        List<String> availableGenres = List.of("All", "Action", "Animation", "Comedy", "Drama", "Horror"); // Tambahkan genre yang ada

        model.addAttribute("movies", movies);
        model.addAttribute("availableGenres", availableGenres); // Kirim daftar genre ke template
        model.addAttribute("selectedGenre", currentGenre); // Kirim genre yang dipilih saat ini
        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        List<Showtime> showtimes = movieService.getShowtimesByMovieId(id);

        model.addAttribute("movie", movie);
        model.addAttribute("showtimes", showtimes);
        return "movie-detail";
    }

    @GetMapping("/showtimes/{id}/seats")
    public String seatSelection(@PathVariable Long id, Model model) {
        Showtime showtime = movieService.getShowtimeById(id);
        List<Seat> allSeatsInTheater = bookingService.getSeatsByShowtime(id);

        Map<String, String> seatStatuses = new HashMap<>();
        if (allSeatsInTheater != null) { // Pastikan allSeatsInTheater tidak null
            for (Seat seat : allSeatsInTheater) {
                String status = bookingService.getSeatStatus(showtime.getId(), seat.getSeatNumber(), null);
                seatStatuses.put(seat.getSeatNumber(), status);
            }
        }


        Map<String, List<Seat>> seatsByRow = allSeatsInTheater.stream()
            .sorted((s1, s2) -> s1.getSeatNumber().compareTo(s2.getSeatNumber()))
            .collect(Collectors.groupingBy(
                seat -> seat.getSeatNumber().substring(0, 1), // Asumsi format kursi A1, B2, dst.
                LinkedHashMap::new,
                Collectors.toList()
            ));


        model.addAttribute("showtime", showtime);
        model.addAttribute("seatsByRow", seatsByRow); // Kirim data kursi yang sudah dikelompokkan
        model.addAttribute("seatStatuses", seatStatuses); // Kirim status kursi
        model.addAttribute("availableSeatsCount", bookingService.getAvailableSeatsCount(id));
        return "seat-selection";
    }

    // REST API endpoints (tidak ada perubahan di sini untuk permintaan ini)
    @GetMapping("/api/movies")
    @ResponseBody
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/api/movies/{id}")
    @ResponseBody
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        try {
            Movie movie = movieService.getMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/movies/search")
    @ResponseBody
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(title));
    }

    @GetMapping("/api/movies/genre/{genre}")
    @ResponseBody
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/api/showtimes")
    @ResponseBody
    public ResponseEntity<List<Showtime>> getShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String date) {

        if (movieId != null && date != null) {
            LocalDate showDate = LocalDate.parse(date);
            return ResponseEntity.ok(movieService.getShowtimesByDate(showDate)
                    .stream()
                    .filter(st -> st.getMovie().getId().equals(movieId))
                    .toList());
        } else if (movieId != null) {
            return ResponseEntity.ok(movieService.getShowtimesByMovieId(movieId));
        } else if (date != null) {
            LocalDate showDate = LocalDate.parse(date);
            return ResponseEntity.ok(movieService.getShowtimesByDate(showDate));
        } else {
            return ResponseEntity.ok(movieService.getUpcomingShowtimes());
        }
    }

    @GetMapping("/api/showtimes/{id}")
    @ResponseBody
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        try {
            Showtime showtime = movieService.getShowtimeById(id);
            return ResponseEntity.ok(showtime);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/showtimes/{id}/seats")
    @ResponseBody
    public ResponseEntity<?> getSeats(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.getSeatsByShowtime(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/showtimes/{id}/available-seats")
    @ResponseBody
    public ResponseEntity<?> getAvailableSeats(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.getAvailableSeatsByShowtime(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}