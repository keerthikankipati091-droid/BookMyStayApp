
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

// Booking History (stores reservations in order)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Stored Reservation: " + reservation.getReservationId());
    }

    // Retrieve all reservations
    public List<Reservation> getReservations() {
        return history;
    }
}

// Booking Report Service (read-only)
class BookingReportService {

    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Display booking history
    public void displayBookingHistory() {
        System.out.println("\n--- Booking History ---");

        for (Reservation r : bookingHistory.getReservations()) {
            System.out.println("ID: " + r.getReservationId()
                    + " | Guest: " + r.getGuestName()
                    + " | Room: " + r.getRoomType());
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\n--- Summary Report ---");

        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : bookingHistory.getReservations()) {
            countMap.put(r.getRoomType(),
                    countMap.getOrDefault(r.getRoomType(), 0) + 1);
        }

        for (String type : countMap.keySet()) {
            System.out.println(type + " Rooms Booked: " + countMap.get(type));
        }

        System.out.println("Total Reservations: " + bookingHistory.getReservations().size());
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("RES101", "Alice", "Single"));
        history.addReservation(new Reservation("RES102", "Bob", "Double"));
        history.addReservation(new Reservation("RES103", "Charlie", "Suite"));
        history.addReservation(new Reservation("RES104", "David", "Single"));

        // Reporting service
        BookingReportService reportService = new BookingReportService(history);

        // Admin views history
        reportService.displayBookingHistory();

        // Admin generates report
        reportService.generateSummaryReport();
    }
}