
import java.util.*;

// Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
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

    public String getRoomId() {
        return roomId;
    }
}

// Inventory Service
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void increaseAvailability(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking History (tracks active bookings)
class BookingHistory {
    private HashMap<String, Reservation> bookings;

    public BookingHistory() {
        bookings = new HashMap<>();
    }

    public void addReservation(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }

    public boolean exists(String id) {
        return bookings.containsKey(id);
    }
}

// Cancellation Service (Rollback logic)
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        // Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found -> " + reservationId);
            return;
        }

        // Get reservation
        Reservation r = history.getReservation(reservationId);

        // Step 1: push room ID to rollback stack
        rollbackStack.push(r.getRoomId());

        // Step 2: restore inventory
        inventory.increaseAvailability(r.getRoomType());

        // Step 3: remove from booking history
        history.removeReservation(reservationId);

        // Confirmation
        System.out.println("Booking Cancelled: " + reservationId
                + " | Released Room ID: " + r.getRoomId());
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 1);
        inventory.addRoomType("Double", 0);

        // Setup booking history (simulating confirmed bookings)
        BookingHistory history = new BookingHistory();
        history.addReservation(new Reservation("RES101", "Alice", "Single", "S101"));
        history.addReservation(new Reservation("RES102", "Bob", "Double", "D201"));

        // Cancellation service
        CancellationService service = new CancellationService(inventory, history);

        // Perform cancellations
        service.cancelBooking("RES101"); // valid
        service.cancelBooking("RES999"); // invalid

        // Show rollback stack
        service.showRollbackStack();

        // Show updated inventory
        inventory.displayInventory();

    }
}