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

public class BookMyStay {

    public static void main(String[] args) {

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        System.out.println("Book My Stay - Hotel Booking System v2.1");
        System.out.println();

        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleRoomAvailability);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailability);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailability);
    }
}