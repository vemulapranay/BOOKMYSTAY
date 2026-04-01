import java.util.*;

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

    void decreaseAvailability(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }

    void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}

class Reservation {

    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
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

    Reservation getNextRequest() {
        return requestQueue.poll();
    }

    boolean hasRequests() {
        return !requestQueue.isEmpty();
    }
}

class RoomAllocationService {

    private Map<String, Set<String>> allocatedRooms;
    private Set<String> allRoomIds;

    RoomAllocationService() {
        allocatedRooms = new HashMap<>();
        allRoomIds = new HashSet<>();
    }

    void processRequests(BookingRequestQueue queue, RoomInventory inventory) {

        while (queue.hasRequests()) {

            Reservation request = queue.getNextRequest();
            String roomType = request.roomType;

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                allRoomIds.add(roomId);

                allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decreaseAvailability(roomType);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + request.guestName);
                System.out.println("Room Type: " + roomType);
                System.out.println("Room ID: " + roomId);
                System.out.println();

            } else {

                System.out.println("Reservation Failed for " + request.guestName + " - No rooms available");
                System.out.println();
            }
        }
    }

    String generateRoomId(String roomType) {
        String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
        String roomId;
        int counter = 1;

        do {
            roomId = prefix + counter;
            counter++;
        } while (allRoomIds.contains(roomId));

        return roomId;
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Suite Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        RoomAllocationService allocationService = new RoomAllocationService();

        System.out.println("Book My Stay - Hotel Booking System v6.0");
        System.out.println();

        allocationService.processRequests(queue, inventory);

        System.out.println("Updated Inventory");
        inventory.displayInventory();
    }
}