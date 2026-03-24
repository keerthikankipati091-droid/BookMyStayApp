
import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

// Room Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void displayHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r.getReservationId() + " | "
                    + r.getGuestName() + " | " + r.getRoomType());
        }
    }
}

// Wrapper class to store full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("\nSystem state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("\nNo saved data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nError loading data. Starting with clean state.");
        }
        return null;
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {
        SystemState state = PersistenceService.load();

        RoomInventory inventory;
        BookingHistory history;

        if (state != null) {
            inventory = state.inventory;
            history = state.history;
        } else {
            // Fresh start
            inventory = new RoomInventory();
            history = new BookingHistory();

            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Double", 3);

            history.addReservation(new Reservation("RES101", "Alice", "Single"));
            history.addReservation(new Reservation("RES102", "Bob", "Double"));
        }

        // Display current state
        inventory.displayInventory();
        history.displayHistory();

        // Simulate new booking
        history.addReservation(new Reservation("RES103", "Charlie", "Single"));

        // Save state before shutdown
        SystemState newState = new SystemState(inventory, history);
        PersistenceService.save(newState);
    }
}