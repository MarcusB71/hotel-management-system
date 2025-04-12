package ca.senecapolytechnic.hotelreservationsystem.models;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private Guest guest;
    private List<Room> rooms = new ArrayList<>();
    private int reservationID = 0;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private String status;

    public Reservation() {
        this.guest = new Guest();
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    public void addRoom(Room room) {
        rooms.add(room);
    }
    public int getReservationID() {
        return reservationID;
    }
    public String getGuestName(){
        return guest.getName();
    }
    public String getGuestPhone(){
        return guest.getPhoneNumber();
    }
    public void setGuestName(String name){
        this.guest.setName(name);
    }
    public void setGuestPhone(String phone){
        this.guest.setPhoneNumber(phone);
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public double calculatePrice() {
        double totalPrice = 0.0;
        for (Room room : rooms) {
            totalPrice += room.getPrice();
        }
        return totalPrice;
    }
}
