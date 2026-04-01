import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

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
        return "BookingID: " + bookingId +
                ", Customer: " + customerName +
                ", Room: " + roomType;
    }
}

// Booking Service with cancellation + rollback
class BookingService {

    private Map<String, Integer> inventory;
    private Map<String, Reservation> confirmedBookings;
    private Stack<String> rollbackStack;

    public BookingService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);

        confirmedBookings = new HashMap<>();
        rollbackStack = new Stack<>();
    }

    // Confirm booking
    public void confirmBooking(Reservation reservation) {
        String roomType = reservation.getRoomType();

        if (inventory.getOrDefault(roomType, 0) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            confirmedBookings.put(reservation.getBookingId(), reservation);

            System.out.println("Booking Confirmed: " + reservation);
        } else {
            System.out.println("Booking Failed: No rooms available.");
        }
    }

    // Cancel booking with rollback
    public void cancelBooking(String bookingId) {
        try {
            // Step 1: Validate booking existence
            if (!confirmedBookings.containsKey(bookingId)) {
                throw new CancellationException("Booking does not exist or already cancelled.");
            }

            Reservation reservation = confirmedBookings.get(bookingId);

            // Step 2: Push to rollback stack (LIFO tracking)
            rollbackStack.push(bookingId);

            // Step 3: Restore inventory
            String roomType = reservation.getRoomType();
            inventory.put(roomType, inventory.get(roomType) + 1);

            // Step 4: Remove booking from confirmed list
            confirmedBookings.remove(bookingId);

            // Step 5: Success message
            System.out.println("Booking Cancelled Successfully: " + bookingId);

        } catch (CancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    // Show current inventory
    public void showInventory() {
        System.out.println("\nCurrent Inventory: " + inventory);
    }

    // Show rollback stack
    public void showRollbackStack() {
        System.out.println("Rollback Stack (LIFO): " + rollbackStack);
    }
}

// Main class
public class BookMyStay {

    public static void main(String[] args) {

        BookingService service = new BookingService();

        // Confirm bookings
        Reservation r1 = new Reservation("B301", "Amrutha", "Deluxe");
        Reservation r2 = new Reservation("B302", "Rahul", "Standard");

        service.confirmBooking(r1);
        service.confirmBooking(r2);

        // Cancel valid booking
        service.cancelBooking("B301");

        // Attempt invalid cancellation
        service.cancelBooking("B999");

        // Attempt duplicate cancellation
        service.cancelBooking("B301");

        service.showInventory();
        service.showRollbackStack();
    }
}