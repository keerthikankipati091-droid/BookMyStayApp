import java.util.*;

// Actor: Reservation (represents a booking request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Actor: Booking Request Queue
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: "
                + reservation.getGuestName() + " -> "
                + reservation.getRoomType());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO Order) ---");

        for (Reservation r : queue) {
            System.out.println(r.getGuestName() + " requested " + r.getRoomType());
        }
    }

    // Get next request (peek, no removal)
    public Reservation peekNextRequest() {
        return queue.peek();
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Suite"));
        bookingQueue.addRequest(new Reservation("Charlie", "Double"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();

        // Show next request to be processed
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            System.out.println("\nNext request to process: "
                    + next.getGuestName() + " -> " + next.getRoomType());
        }
    }
}