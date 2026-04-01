import java.util.*;

abstract class Room {

    String roomType;
    int beds;
    double price;

    Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: " + price);
    }
}

class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 1800);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 3000);
    }
}

class RoomInventory {

    private Map<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void displayRequest() {
        System.out.println("Guest: " + guestName + " requested " + roomType);
    }
}

class BookingRequestQueue {

    Queue<Reservation> requestQueue;

    BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    void displayRequests() {
        for (Reservation r : requestQueue) {
            r.displayRequest();
        }
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        System.out.println("Book My Stay - Hotel Booking System v5.0");
        System.out.println();
        System.out.println("Booking Requests in Queue");
        System.out.println();

        bookingQueue.displayRequests();
    }
}