import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String bookingId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(String bookingId, String customerName, String roomType, int nights) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "BookingID: " + bookingId +
                ", Customer: " + customerName +
                ", Room: " + roomType +
                ", Nights: " + nights;
    }
}

// Validator class
class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));

    // Validate booking input
    public static void validate(Reservation reservation, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate nights
        if (reservation.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0.");
        }

        // Check availability
        int available = inventory.getOrDefault(reservation.getRoomType(), 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

// Booking Service
class BookingService {

    private Map<String, Integer> inventory;

    public BookingService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void processBooking(Reservation reservation) {
        try {
            // Step 1: Validate (Fail Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Update inventory
            String roomType = reservation.getRoomType();
            inventory.put(roomType, inventory.get(roomType) - 1);

            // Step 3: Success message
            System.out.println("Booking Confirmed: " + reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory: " + inventory);
    }
}

// Main Class
public class BookMyStay
{

    public static void main(String[] args) {

        BookingService service = new BookingService();

        // Valid booking
        Reservation r1 = new Reservation("B201", "Amrutha", "Deluxe", 2);

        // Invalid room type
        Reservation r2 = new Reservation("B202", "Rahul", "Premium", 1);

        // Invalid nights
        Reservation r3 = new Reservation("B203", "Sneha", "Standard", 0);

        // Overbooking case
        Reservation r4 = new Reservation("B204", "John", "Deluxe", 1);

        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4); // should fail if Deluxe exhausted

        service.showInventory();
    }
}