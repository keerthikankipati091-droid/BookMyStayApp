
import java.util.*;

// Reservation (confirmed booking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {

    // List to maintain insertion order
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        history.add(r);
        System.out.println("Reservation stored: " + r.getReservationId());
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Booking Report Service (read-only reporting)
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n--- Booking History ---");

        for (Reservation r : history.getAllReservations()) {
            System.out.println("ID: " + r.getReservationId()
                    + " | Guest: " + r.getGuestName()
                    + " | Room: " + r.getRoomType());
        }
    }

    // Generate summary report
    public void generateSummary() {
        System.out.println("\n--- Booking Summary Report ---");

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String type : roomCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomCount.get(type));
        }

        System.out.println("Total Bookings: " + history.getAllReservations().size());
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES101", "Alice", "Single"));
        history.addReservation(new Reservation("RES102", "Bob", "Double"));
        history.addReservation(new Reservation("RES103", "Charlie", "Single"));

        // Reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Admin views booking history
        reportService.displayAllBookings();

        // Admin generates summary report
        reportService.generateSummary();
    }
}