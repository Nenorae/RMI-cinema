-- Lanjutan dari data.sql Anda yang sudah ada...

-- INSERT DATA FILM YANG SUDAH ADA (Action, Animation)
INSERT INTO movie (id, title, duration, genre, rating, description, poster_url) VALUES
(1, 'Avengers: Endgame', 181, 'Action', 8.4, 'Epic superhero movie', 'https://i.pinimg.com/736x/c5/1f/15/c51f151dbc5016b4d63d96a9e3f98db7.jpg'),
(2, 'The Lion King', 118, 'Animation', 7.8, 'Disney animated classic', 'https://i.pinimg.com/736x/89/a8/dd/89a8dd629fbf5b875c5de563c3863ba9.jpg'),
(3, 'John Wick 4', 169, 'Action', 8.2, 'Action thriller sequel', 'https://i.pinimg.com/736x/6d/39/e5/6d39e57ff97ecb266d54800558745a27.jpg');

-- TAMBAHAN DATA FILM BARU DENGAN GENRE LAINNYA:
-- Comedy
INSERT INTO movie (id, title, duration, genre, rating, description, poster_url) VALUES
(4, 'Hangover', 100, 'Comedy', 7.7, 'Three buddies wake up from a bachelor party in Las Vegas, with no memory of the previous night and the bachelor missing.', 'https://i.pinimg.com/736x/f5/02/90/f50290989fdcde0cbcc31c6591f94e9b.jpg'),
(5, 'Superbad', 113, 'Comedy', 7.6, 'Two co-dependent high school seniors are forced to deal with separation anxiety after their plan to stage a booze-soaked party goes awry.', 'https://upload.wikimedia.org/wikipedia/en/8/8b/Superbad_Poster.png');

-- Horror
INSERT INTO movie (id, title, duration, genre, rating, description, poster_url) VALUES
(6, 'The Conjuring', 112, 'Horror', 7.5, 'Paranormal investigators Ed and Lorraine Warren work to help a family terrorized by a dark presence in their farmhouse.', 'https://i.pinimg.com/736x/d4/40/4f/d4404f2de426b1bffd8e4149379ffbfb.jpg'),
(7, 'A Quiet Place', 90, 'Horror', 7.5, 'A family struggles for survival in a world where most humans have been killed by blind but noise-sensitive creatures.', 'https://upload.wikimedia.org/wikipedia/en/a/a0/A_Quiet_Place_film_poster.png');

-- Drama
INSERT INTO movie (id, title, duration, genre, rating, description, poster_url) VALUES
(8, 'Forrest Gump', 142, 'Drama', 8.8, 'The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75.', 'https://upload.wikimedia.org/wikipedia/en/6/67/Forrest_Gump_poster.jpg'),
(9, 'The Shawshank Redemption', 142, 'Drama', 9.3, 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 'https://upload.wikimedia.org/wikipedia/en/8/81/ShawshankRedemptionMoviePoster.jpg');

-- Sci-Fi (Contoh genre lain)
INSERT INTO movie (id, title, duration, genre, rating, description, poster_url) VALUES
(10, 'Inception', 148, 'Sci-Fi', 8.8, 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', 'https://upload.wikimedia.org/wikipedia/en/2/2e/Inception_%282010%29_theatrical_poster.jpg');

-- Data theater (sudah OK dari sebelumnya)
INSERT INTO theater (id, name, location, capacity) VALUES
(1, 'CGV Grand Indonesia', 'Jakarta', 200),
(2, 'XXI Taman Anggrek', 'Jakarta', 150),
(3, 'Cinema 21 Plaza Senayan', 'Jakarta', 180);

-- Data showtime (PERBAIKI TANGGALNYA agar MENDATANG!)
-- Anda perlu menambahkan data showtime untuk film-film baru ini jika ingin mereka muncul sebagai "Tayang" atau "Segera Hadir"
INSERT INTO showtime (id, movie_id, theater_id, show_date, show_time, screen_number, price) VALUES
-- Contoh untuk film baru (misal Hangover - ID 4)
(5, 4, 1, '2025-06-15', '18:00:00', 2, 50000), -- Sesuaikan tanggal & waktu
(6, 4, 2, '2025-06-12', '20:00:00', 3, 55000),

-- Contoh untuk film baru (misal The Conjuring - ID 6)
(7, 6, 3, '2025-06-15', '21:30:00', 1, 60000),
(8, 6, 1, '2025-06-12', '22:00:00', 2, 65000),

-- Contoh untuk film baru (misal Forrest Gump - ID 8)
(9, 8, 2, '2025-06-14', '15:00:00', 1, 50000),

-- Contoh untuk film baru (misal Inception - ID 10)
(10, 10, 3, '2025-06-16', '19:30:00', 2, 70000),

-- Lanjutkan data showtime yang sudah ada sebelumnya (PASTIKAN TANGGALNYA MENDATANG)
(11, 1, 1, '2025-06-13', '14:00:00', 1, 50000), -- Sebelumnya ID 1
(12, 1, 1, '2025-06-13', '19:00:00', 1, 60000), -- Sebelumnya ID 2
(13, 2, 2, '2025-06-04', '16:00:00', 2, 45000), -- Sebelumnya ID 3
(14, 3, 3, '2025-06-04', '21:00:00', 3, 55000); -- Sebelumnya ID 4


-- Data seat (sudah OK dari sebelumnya, H2 akan handle auto_increment untuk 'id')
INSERT INTO seat (theater_id, seat_number, is_booked)
-- Theater 1
SELECT 1, 'A' || X, false FROM SYSTEM_RANGE(1, 10)
UNION ALL SELECT 1, 'B' || X, false FROM SYSTEM_RANGE(1, 10)
UNION ALL SELECT 1, 'C' || X, false FROM SYSTEM_RANGE(1, 10)

-- Theater 2
UNION ALL SELECT 2, 'A' || X, false FROM SYSTEM_RANGE(1, 10)
UNION ALL SELECT 2, 'B' || X, false FROM SYSTEM_RANGE(1, 10)
UNION ALL SELECT 2, 'C' || X, false FROM SYSTEM_RANGE(1, 10)

-- Theater 3
UNION ALL SELECT 3, 'A' || X, false FROM SYSTEM_RANGE(1, 10)
UNION ALL SELECT 3, 'B' || X, false FROM SYSTEM_RANGE(1, 10)
UNION ALL SELECT 3, 'C' || X, false FROM SYSTEM_RANGE(1, 10)
;

-- Anda juga perlu menambahkan data untuk tabel booking dan booking_seat jika ingin ada contoh booking awal
-- Contoh:
-- INSERT INTO booking (id, showtime_id, customer_name, customer_email, customer_phone, booking_time, total_price, status) VALUES
-- (1, 11, 'Ganendra', 'ganendra@example.com', '08123456789', CURRENT_TIMESTAMP, 50000.00, 'CONFIRMED');

-- INSERT INTO booking_seat (booking_id, seat_id) VALUES
-- (1, (SELECT id FROM seat WHERE theater_id = 1 AND seat_number = 'A1')); -- Ambil ID seat A1 di theater 1