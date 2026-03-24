
import java.util.*;

// Reservation
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

// Thread-safe Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // Add request
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        notify();
    }

    // Get request (with exception handling)
    public synchronized Reservation getRequest() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null; // exit safely
            }
        }
        return queue.poll();
    }
}

// Thread-safe Inventory
class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Critical section
    public synchronized boolean bookRoom(String type) {
        int available = inventory.getOrDefault(type, 0);

        if (available > 0) {
            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " : " + inventory.get(type));
        }
    }
}

// Booking Processor Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r = queue.getRequest();

            // Exit if interrupted
            if (r == null) break;

            boolean success = inventory.bookRoom(r.getRoomType());

            if (success) {
                System.out.println(getName() + " booked "
                        + r.getRoomType() + " for " + r.getGuestName());
            } else {
                System.out.println(getName() + " FAILED booking for "
                        + r.getGuestName() + " (" + r.getRoomType() + ")");
            }
        }
    }
}
public class BookMyStayApp {

    public static void main(String[] args) {
        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Setup inventory
        inventory.addRoomType("Single", 2);

        // Add requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));

        // Create threads
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        // Stop threads safely
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.interrupt();
        t2.interrupt();

        inventory.displayInventory();
    }
}