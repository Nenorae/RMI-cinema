# 🎟️ Cinema Ticket Gateway Project

Welcome to the Cinema Ticket Gateway Project! This is a simple web application built with Spring Boot for booking movie tickets. Think of this application as the main "gateway" that provides a user-friendly web interface and a REST API. The goal is to be able to connect it with other, more complex backend systems, for example, via RMI (Remote Method Invocation).

## ✨ Technologies Used

This project is built using several modern and cool technologies:

*   **Java 17**: A modern and stable version of Java.
*   **Spring Boot 3.5.0**: The main framework that makes development fast and easy.
*   **Spring Data JPA**: For communicating with the database without writing complex SQL queries.
*   **Thymeleaf**: A template engine to create the dynamic web pages we see in the browser.
*   **H2 Database**: A super lightweight, in-memory database, perfect for development and testing.
*   **Lombok**: A great library to automatically reduce boilerplate code (like getters, setters, constructors).
*   **RMI (Remote Method Invocation)**: A Java technology to "call" methods from other applications running on different computers.

## 🗄️ Database Structure

Behind the scenes, this application stores all data in several interconnected tables:

*   `movie`: Stores all movie info, from title, duration, genre, rating, synopsis, to the poster link.
*   `theater`: Contains information about the cinemas, such as name, location, and total capacity.
*   `showtime`: Records the screening schedule for films in each cinema, including date, time, screen number, and ticket price.
*   `seat`: Stores data for each seat in a theater, complete with its status (whether it's booked or available).
*   `booking`: A record of each booking transaction, such as customer data, booking time, total payment, and status.
*   `booking_seat`: A join table linking the `booking` transaction with the selected `seat`.

## 🚀 Cool Features

This application has several key features you can try out:

### 1. Web Interface (Frontend)
*   **Homepage**: You are immediately greeted with a list of currently playing and upcoming movies.
*   **Movie List**: See all available movies and filter them by your favorite genre.
*   **Movie Details**: Get the full scoop on a movie, from its synopsis and rating to all its showtimes.
*   **Seat Selection**: An interactive visual interface to choose the available seats you want.
*   **Book Ticket**: Fill in your details in a simple form to complete the booking.
*   **Search Booking**: Easily track your booking history, just enter your email or phone number.
*   **Dashboard**: View brief statistics like total movies, number of bookings, and total revenue.

### 2. REST API
*   Provides endpoints that can be accessed by other applications to get data on movies, schedules, and seat info.
*   Allows external systems to create, view, and manage ticket bookings.

### 3. RMI Integration
*   Exposes core services (`CinemaRemoteService` and `BookingRemoteService`) via RMI.
*   This means other Java applications can "talk to" and use functions from this system remotely.

## 👍 Pros vs. 👎 Cons

### Pros
*   **Clean Architecture**: The structure is very clear, with a separation between controllers, services, repositories, and entities that makes the code easy to manage.
*   **Flexible**: By providing three "doors" (Web, REST API, and RMI), this application is easy to integrate with other systems.
*   **Modern**: Uses the latest versions of Java and Spring Boot, so it's feature-complete and fully supported.
*   **Easy for Development**: With the H2 Database, you don't have to worry about setting up an external database. Just run it, and everything is ready!

### Cons
*   **No Security**: There is no login or authentication system. Anyone can access all features without restrictions.
*   **Simplified Seat Management**: Seat status is managed centrally, which might not be ideal for many concurrent transactions (large scale).
*   **No Payment Integration**: The booking process is not yet connected to an online payment gateway.

## ⚙️ How It Works

In short, the application's workflow is as follows:

1.  **Request from Browser**: You click something on the web, and the browser sends an HTTP Request to the server.
2.  **Controller Receives**: The `Controller` catches the request and knows what to do.
3.  **Service Acts**: The `Controller` then "tells" the `Service` to execute its business logic (e.g., fetching movie data).
4.  **Repository to Database**: The `Service` requests data from the `Repository`, which magically (via Spring Data JPA) translates it into a database query.
5.  **Data Becomes an Object**: Data from the database is converted into a Java (`Entity`) object.
6.  **Displayed on the Web**: The `Controller` passes the data to `Thymeleaf` to be assembled into a beautiful HTML page and sent back to your browser.
7.  **The RMI Door**: Separately, `RmiConfig` sets up the RMI services so they can be called by other Java applications from anywhere.

## 💻 How to Run the Project

Want to try running this application on your computer? It's easy!

**Prerequisites**: Make sure you have **Java 17** and **Maven** installed.

**1. Clone the Repository:**
```sh
git clone https://github.com/username/repository.git
cd repository
```

**2. Build the Project:**
Navigate to the project directory and run this command to build it.
```sh
mvn clean install
```

**3. Run the Application:**
```sh
mvn spring-boot:run
```

**4. Access the Application:**
Open your browser and go to `http://localhost:8080`.

**5. Peek at the Database:**
Curious about the database content? Go to `http://localhost:8080/h2-console` and log in with the credentials from `application.properties`.

## 📦 Running on Another Computer

Want to show it off to a friend or deploy it on a server? You can package this application into a single `.jar` file.

**1. Build the JAR:**
```sh
mvn clean package
```

**2. Copy the JAR:**
Find the `.jar` file inside the `target/` folder and copy it to the target computer.

**3. Run the JAR:**
Just run it with the following command (make sure the target computer has Java 17).
```sh
java -jar your-application-file.jar
```

## ⚠️ Error Handling

Spring Boot is smart enough to handle errors. But if you want to be more specific:

*   **Custom Error Page**: Create an `error.html` file in `src/main/resources/templates/` to display your own version of an error page.
*   **Specific Handling**: Use the `@ExceptionHandler` annotation inside a `Controller` to handle specific types of errors.
*   **Global Handling**: Create a class with the `@ControllerAdvice` annotation to handle all errors that occur throughout the application centrally.