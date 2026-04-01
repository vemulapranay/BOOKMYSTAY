import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private String customerName;
    private String roomType;

    public Reservation(String bookingId, String customerName, String roomType) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return bookingId + " - " + customerName + " (" + roomType + ")";
    }
}

// Wrapper class to persist full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("✅ System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading state. Starting with clean data.");
        }

        return null;
    }
}

// Booking System
class BookingSystem {

    private Map<String, Integer> inventory;
    private List<Reservation> bookingHistory;

    public BookingSystem() {
        inventory = new HashMap<>();
        bookingHistory = new ArrayList<>();

        // default inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Restore state
    public void restore(SystemState state) {
        if (state != null) {
            this.inventory = state.inventory;
            this.bookingHistory = state.bookingHistory;
        }
    }

    // Book room
    public void book(Reservation r) {
        String roomType = r.getRoomType();

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            bookingHistory.add(r);
            System.out.println("Booking Confirmed: " + r);
        } else {
            System.out.println("Booking Failed: No rooms available.");
        }
    }

    // Show data
    public void showData() {
        System.out.println("\nInventory: " + inventory);
        System.out.println("Booking History:");
        for (Reservation r : bookingHistory) {
            System.out.println(r);
        }
    }

    // Get current state
    public SystemState getState() {
        return new SystemState(inventory, bookingHistory);
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Step 1: Load previous state
        SystemState savedState = PersistenceService.load();
        system.restore(savedState);

        // Step 2: Perform operations
        system.book(new Reservation("B501", "Amrutha", "Deluxe"));
        system.book(new Reservation("B502", "Rahul", "Standard"));

        // Step 3: Show current state
        system.showData();

        // Step 4: Save before exit
        PersistenceService.save(system.getState());
    }
}