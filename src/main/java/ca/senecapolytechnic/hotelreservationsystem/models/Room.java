package ca.senecapolytechnic.hotelreservationsystem.models;

public class Room {
    private int roomID;
    private RoomType roomType;
    private int numberOfBeds;
    private double price;
    private String status;


    public String getRoomDetails() {
        return String.format("Room ID: %s, Type: %s, Beds: %d, Price: %.2f, Status: %s",
                roomID, roomType, numberOfBeds, price, status);
    }

    public void setRoomDetails(int id, RoomType type, int beds, double price, String status) {
        this.roomID = id;
        this.roomType = type;
        this.numberOfBeds = beds;
        this.price = price;
        this.status = status;
    }

    public boolean checkRoomAvailability() {
        return status.equalsIgnoreCase("Available");
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
