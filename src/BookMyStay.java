import java.util.*;

// Reservation class
class Reservation {
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

// Shared Booking System
class BookingSystem {

    private Map<String, Integer> inventory;
    private Queue<Reservation> bookingQueue;

    public BookingSystem() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);

        bookingQueue = new LinkedList<>();
    }

    // Add booking request (shared queue)
    public synchronized void addBookingRequest(Reservation r) {
        bookingQueue.add(r);
        System.out.println("Request Added: " + r);
    }

    // Process booking (critical section)
    public synchronized void processBooking() {
        if (bookingQueue.isEmpty()) return;

        Reservation r = bookingQueue.poll();
        String roomType = r.getRoomType();

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            // Critical section: update shared state
            inventory.put(roomType, available - 1);
            System.out.println(Thread.currentThread().getName() +
                    " CONFIRMED: " + r);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED: No " + roomType + " rooms available for " + r);
        }
    }

    public void showInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingSystem system;

    public BookingProcessor(BookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    @Override
    public void run() {
        // Each thread tries to process multiple bookings
        for (int i = 0; i < 3; i++) {
            system.processBooking();
            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Add booking requests
        system.addBookingRequest(new Reservation("B401", "Amrutha", "Deluxe"));
        system.addBookingRequest(new Reservation("B402", "Rahul", "Deluxe"));
        system.addBookingRequest(new Reservation("B403", "Sneha", "Standard"));
        system.addBookingRequest(new Reservation("B404", "John", "Standard"));
        system.addBookingRequest(new Reservation("B405", "Priya", "Suite"));

        // Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor(system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(system, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        system.showInventory();
    }
}