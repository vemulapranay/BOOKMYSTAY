import java.util.HashMap;
import java.util.Map;

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

class RoomSearchService {

    void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.roomType);

            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println();
            }
        }
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, doubleRoom, suite};

        RoomInventory inventory = new RoomInventory();
        RoomSearchService searchService = new RoomSearchService();

        System.out.println("Book My Stay - Hotel Booking System v4.0");
        System.out.println();
        System.out.println("Available Rooms");
        System.out.println();

        searchService.searchAvailableRooms(rooms, inventory);
    }
}