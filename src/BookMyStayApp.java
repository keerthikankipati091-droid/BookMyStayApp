import java.util.*;

// Add-On Service (represents optional service)
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private HashMap<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service: " + service.getServiceName()
                + " to Reservation: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\n--- Services for Reservation: " + reservationId + " ---");

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println(s.getServiceName() + " : ₹" + s.getCost());
        }
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);

        double total = 0;
        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }
        return total;
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        // Example reservation IDs (from previous use case)
        String reservation1 = "RES101";
        String reservation2 = "RES102";

        // Create service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(reservation1, new AddOnService("Breakfast", 500));
        manager.addService(reservation1, new AddOnService("Airport Pickup", 1200));

        manager.addService(reservation2, new AddOnService("Spa", 2000));

        // Display services
        manager.displayServices(reservation1);
        manager.displayServices(reservation2);

        // Show total cost
        System.out.println("\nTotal Add-On Cost for " + reservation1 + " : ₹"
                + manager.calculateTotalCost(reservation1));

        System.out.println("Total Add-On Cost for " + reservation2 + " : ₹"
                + manager.calculateTotalCost(reservation2));
    }
}