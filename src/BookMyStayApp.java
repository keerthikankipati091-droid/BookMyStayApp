import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

// Actor: RoomInventory (same as Use Case 3)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return inventory.keySet();
    }
}

// Actor: Search Service (Read-only access)
class RoomSearchService {

    private RoomInventory inventory;
    private HashMap<String, Room> roomDetails;

    public RoomSearchService(RoomInventory inventory, HashMap<String, Room> roomDetails) {
        this.inventory = inventory;
        this.roomDetails = roomDetails;
    }

    // Search available rooms (READ-ONLY)
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");

        for (String type : inventory.getAllRoomTypes()) {

            int available = inventory.getAvailability(type);

            // Filter unavailable rooms
            if (available > 0 && roomDetails.containsKey(type)) {
                Room room = roomDetails.get(type);

                System.out.println("Type: " + room.getType());
                System.out.println("Price: ₹" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available: " + available);
                System.out.println("------------------------");
            }
        }
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.addRoomType("Single", 10);
        inventory.addRoomType("Double", 0); // unavailable
        inventory.addRoomType("Suite", 2);

        // Room details (Domain Model)
        HashMap<String, Room> roomDetails = new HashMap<>();
        roomDetails.put("Single", new Room("Single", 2000, "WiFi, AC"));
        roomDetails.put("Double", new Room("Double", 3500, "WiFi, AC, TV"));
        roomDetails.put("Suite", new Room("Suite", 6000, "WiFi, AC, TV, Mini Bar"));

        // Search Service
        RoomSearchService searchService = new RoomSearchService(inventory, roomDetails);

        // Guest performs search
        searchService.searchAvailableRooms();
    }
}