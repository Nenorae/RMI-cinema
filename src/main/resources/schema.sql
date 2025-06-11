-- Hapus tabel jika sudah ada (opsional, berguna untuk dev agar bisa dijalankan ulang dengan bersih)
-- Hati-hati jika ada data penting. Untuk H2 in-memory dengan create-drop atau init.mode=always, ini mungkin tidak selalu diperlukan.
DROP TABLE IF EXISTS booking_seat;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS showtime;
DROP TABLE IF EXISTS theater;
DROP TABLE IF EXISTS movie;

-- Buat tabel movie
CREATE TABLE movie (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    genre VARCHAR(100) NOT NULL,
    rating DECIMAL(3,1) NOT NULL,
    description TEXT,
    poster_url VARCHAR(255) -- Kolom untuk URL poster film
);

-- Buat tabel theater
CREATE TABLE theater (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL
);

-- Buat tabel showtime yang bergantung pada movie dan theater
CREATE TABLE showtime (
    id BIGINT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater_id BIGINT NOT NULL,
    show_date DATE NOT NULL,            -- Menyimpan tanggal saja
    show_time TIME NOT NULL,            -- Menyimpan waktu saja
    screen_number INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE,
    FOREIGN KEY (theater_id) REFERENCES theater(id) ON DELETE CASCADE
);

-- Buat tabel seat yang bergantung pada theater
CREATE TABLE seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- ID unik untuk setiap kursi, auto-increment
    theater_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,     -- Nomor kursi (misal: A1, B5)
    is_booked BOOLEAN NOT NULL DEFAULT false, -- Status apakah kursi sudah dipesan untuk suatu showtime (logika ini mungkin lebih kompleks jika seat bisa reusable per showtime)
    FOREIGN KEY (theater_id) REFERENCES theater(id) ON DELETE CASCADE,
    UNIQUE(theater_id, seat_number)       -- Memastikan nomor kursi unik per teater
);

-- Buat tabel booking yang bergantung pada showtime
CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    showtime_id BIGINT NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    booking_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Waktu pemesanan, default ke waktu sekarang
    total_price DECIMAL(10,2) NOT NULL, -- Kolom untuk total harga booking
    status VARCHAR(20) NOT NULL,        -- Status booking (CONFIRMED, CANCELLED, PENDING)
    FOREIGN KEY (showtime_id) REFERENCES showtime(id) -- Pertimbangkan ON DELETE RESTRICT atau SET NULL jika showtime bisa dihapus
);

-- Buat tabel booking_seat yang menghubungkan booking dengan kursi yang dipesan (relasi ManyToMany)
CREATE TABLE booking_seat (
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,             -- Merujuk ke ID unik dari tabel seat
    PRIMARY KEY (booking_id, seat_id),   -- Composite primary key
    FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seat(id) ON DELETE CASCADE
);
