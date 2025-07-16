# 🎟️ Proyek Gateway Tiket Bioskop

Selamat datang di Proyek Gateway Tiket Bioskop! Ini adalah aplikasi web sederhana yang dibuat dengan Spring Boot untuk memesan tiket film. Anggap saja aplikasi ini sebagai "gerbang" utama yang menyediakan antarmuka web yang ramah pengguna dan juga REST API. Tujuannya adalah untuk bisa dihubungkan dengan sistem backend lain yang lebih kompleks, misalnya melalui RMI (Remote Method Invocation).

## ✨ Teknologi yang Digunakan

Proyek ini dibangun menggunakan beberapa teknologi modern dan keren:

*   **Java 17**: Versi Java yang modern dan stabil.
*   **Spring Boot 3.5.0**: Framework utama yang membuat pengembangan jadi cepat dan mudah.
*   **Spring Data JPA**: Untuk berkomunikasi dengan database tanpa perlu menulis query SQL yang rumit.
*   **Thymeleaf**: Mesin template untuk membuat halaman web dinamis yang kita lihat di browser.
*   **H2 Database**: Database super ringan yang berjalan di memori, cocok banget buat development dan testing.
*   **Lombok**: Pustaka hebat untuk mengurangi kode-kode boilerplate (seperti getter, setter, constructor) secara otomatis.
*   **RMI (Remote Method Invocation)**: Teknologi Java untuk "memanggil" fungsi dari aplikasi lain yang berjalan di komputer berbeda.

## 🗄️ Struktur Database

Di balik layar, aplikasi ini menyimpan semua data dalam beberapa tabel yang saling berhubungan:

*   `movie`: Menyimpan semua info film, mulai dari judul, durasi, genre, rating, sinopsis, sampai link posternya.
*   `theater`: Berisi informasi tentang bioskop, seperti nama, lokasi, dan total kursinya.
*   `showtime`: Mencatat jadwal tayang film di setiap bioskop, termasuk tanggal, jam, studio, dan harga tiket.
*   `seat`: Menyimpan data setiap kursi di dalam studio, lengkap dengan statusnya (apakah sudah dipesan atau masih kosong).
*   `booking`: Rekaman setiap transaksi pemesanan, seperti data pelanggan, waktu pesan, total bayar, dan statusnya.
*   `booking_seat`: Tabel penghubung antara transaksi `booking` dan kursi `seat` yang dipilih.

## 🚀 Fitur-Fitur Keren

Aplikasi ini punya beberapa fitur utama yang bisa kamu coba:

### 1. Tampilan Web (Frontend)
*   **Beranda**: Langsung disambut dengan daftar film yang sedang tayang dan yang akan datang.
*   **Daftar Film**: Lihat semua film yang ada dan filter berdasarkan genre favoritmu.
*   **Detail Film**: Kepoin info lengkap sebuah film, dari sinopsis, rating, sampai semua jadwal tayangnya.
*   **Pilih Kursi**: Antarmuka visual yang interaktif buat milih kursi kosong yang kamu mau.
*   **Pesan Tiket**: Isi data dirimu di formulir sederhana untuk menyelesaikan pemesanan.
*   **Cari Pesanan**: Lacak riwayat pesananmu dengan mudah, cukup masukkan email atau nomor telepon.
*   **Dashboard**: Lihat statistik singkat seperti total film, jumlah pesanan, dan total pendapatan.

### 2. REST API
*   Menyediakan endpoint yang bisa diakses oleh aplikasi lain untuk mendapatkan data film, jadwal, dan info kursi.
*   Memungkinkan sistem eksternal untuk membuat, melihat, dan mengelola pesanan tiket.

### 3. Integrasi RMI
*   Mengekspos layanan inti (`CinemaRemoteService` dan `BookingRemoteService`) lewat RMI.
*   Ini artinya, aplikasi Java lain bisa "ngobrol" dan menggunakan fungsi dari sistem ini dari jarak jauh.

## 👍 Kelebihan vs. 👎 Kekurangan

### Kelebihan
*   **Arsitektur Rapi**: Strukturnya jelas banget, dengan pemisahan antara controller, service, repository, dan entity yang bikin kode gampang diurus.
*   **Fleksibel**: Karena menyediakan tiga "pintu" (Web, REST API, dan RMI), aplikasi ini gampang diintegrasikan dengan sistem lain.
*   **Modern**: Pakai versi terbaru Java dan Spring Boot, jadi fiturnya lengkap dan didukung penuh.
*   **Mudah untuk Development**: Dengan H2 Database, kamu nggak perlu pusing setup database eksternal. Cukup jalankan dan semua siap!

### Kekurangan
*   **Tanpa Keamanan**: Belum ada sistem login atau otentikasi. Siapa saja bisa mengakses semua fitur tanpa batasan.
*   **Manajemen Kursi Sederhana**: Status kursi dikelola secara terpusat, yang mungkin kurang ideal jika digunakan untuk banyak transaksi bersamaan (skala besar).
*   **Belum Ada Pembayaran**: Proses pesannya belum nyambung ke sistem pembayaran online (payment gateway).

## ⚙️ Bagaimana Cara Kerjanya?

Secara singkat, alur kerja aplikasi ini seperti ini:

1.  **Permintaan dari Browser**: Kamu klik sesuatu di web, dan browser mengirim permintaan (HTTP Request) ke server.
2.  **Controller Menerima**: `Controller` menangkap permintaan itu dan tahu harus melakukan apa.
3.  **Service Bekerja**: `Controller` kemudian "menyuruh" `Service` untuk menjalankan logika bisnisnya (misalnya, mengambil data film).
4.  **Repository ke Database**: `Service` meminta data ke `Repository`, yang secara ajaib (lewat Spring Data JPA) menerjemahkannya menjadi query ke database.
5.  **Data Jadi Objek**: Data dari database diubah menjadi objek Java (`Entity`).
6.  **Tampil di Web**: `Controller` memberikan data tersebut ke `Thymeleaf` untuk dirangkai menjadi halaman HTML yang cantik dan dikirim kembali ke browsermu.
7.  **Pintu RMI**: Secara terpisah, `RmiConfig` menyiapkan layanan RMI agar bisa dipanggil oleh aplikasi Java lain dari mana saja.

## 💻 Cara Menjalankan Proyek

Ingin mencoba menjalankan aplikasi ini di komputermu? Gampang!

**Syarat**: Pastikan kamu sudah punya **Java 17** dan **Maven**.

**1. Clone Repositori:**
```sh
git clone https://github.com/username/repository.git
cd repository
```

**2. Build Proyek:**
Masuk ke direktori proyek dan jalankan perintah ini untuk build.
```sh
mvn clean install
```

**3. Jalankan Aplikasi:**
```sh
mvn spring-boot:run
```

**4. Buka Aplikasi:**
Buka browser dan pergi ke `http://localhost:8080`.

**5. Intip Database:**
Penasaran sama isi databasenya? Buka `http://localhost:8080/h2-console` dan login dengan kredensial di `application.properties`.

## 📦 Menjalankan di Komputer Lain

Mau pamer ke teman atau deploy di server? Kamu bisa bungkus aplikasi ini jadi satu file `.jar`.

**1. Build JAR:**
```sh
mvn clean package
```

**2. Salin JAR:**
Temukan file `.jar` di dalam folder `target/` dan salin ke komputer tujuan.

**3. Jalankan JAR:**
Cukup jalankan dengan perintah berikut (pastikan komputer tujuan punya Java 17).
```sh
java -jar nama-file-aplikasimu.jar
```

## ⚠️ Penanganan Error

Spring Boot sudah cukup pintar menangani error. Tapi kalau kamu mau lebih spesifik:

*   **Halaman Error Cantik**: Buat file `error.html` di `src/main/resources/templates/` untuk menampilkan halaman error versimu sendiri.
*   **Penanganan Spesifik**: Gunakan anotasi `@ExceptionHandler` di dalam `Controller` untuk menangani jenis error tertentu.
*   **Penanganan Global**: Buat sebuah kelas dengan anotasi `@ControllerAdvice` untuk menangani semua error yang terjadi di seluruh aplikasi secara terpusat.
