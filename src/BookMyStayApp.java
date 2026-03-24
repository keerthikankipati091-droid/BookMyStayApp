import java.util.*;

// Reservation (same as Use Case 5)
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

// Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // dequeue (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
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

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void decreaseAvailability(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

// Booking Service (Core Logic)
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs per type
    private HashMap<String, Set<String>> allocatedRooms;

    // Global set to ensure uniqueness
    private Set<String> allRoomIds;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
        allRoomIds = new HashSet<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
        } while (allRoomIds.contains(roomId));

        return roomId;
    }

    // Process booking request
    public void processReservation(Reservation r) {
        String type = r.getRoomType();

        // Check availability
        if (inventory.getAvailability(type) > 0) {

            // Generate unique room ID
            String roomId = generateRoomId(type);

            // Add to global set
            allRoomIds.add(roomId);

            // Map room type → allocated IDs
            allocatedRooms.putIfAbsent(type, new HashSet<>());
            allocatedRooms.get(type).add(roomId);

            // Decrease inventory immediately
            inventory.decreaseAvailability(type);

            // Confirm booking
            System.out.println("Booking Confirmed for " + r.getGuestName()
                    + " | Room Type: " + type
                    + " | Room ID: " + roomId);

        } else {
            System.out.println("Booking Failed for " + r.getGuestName()
                    + " | No rooms available for " + type);
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize queue
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);

        // Setup queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single")); // should fail
        queue.addRequest(new Reservation("David", "Double"));

        // Booking service
        BookingService bookingService = new BookingService(inventory);

        // Process all requests (FIFO)
        System.out.println("\n--- Processing Booking Requests ---");
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }
    }
}