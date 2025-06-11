package com.cinema.ticketing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cinema.ticketing.service.BookingService;
import com.cinema.ticketing.service.MovieService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final MovieService movieService;
    private final BookingService bookingService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movies", movieService.getMoviesWithUpcomingShowtimes());
        model.addAttribute("upcomingShowtimes", movieService.getUpcomingShowtimes());
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMovies", movieService.getAllMovies().size());
        model.addAttribute("totalBookings", bookingService.getTotalBookingsCount());
        model.addAttribute("totalRevenue", bookingService.getTotalRevenue());
        model.addAttribute("upcomingShowtimes", movieService.getUpcomingShowtimes());
        return "dashboard";
    }
}