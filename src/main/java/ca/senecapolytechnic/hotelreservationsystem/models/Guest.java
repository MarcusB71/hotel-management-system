package ca.senecapolytechnic.hotelreservationsystem.models;

public class Guest {
    private int guestID;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String feedback;

    public Guest(){};
    public Guest(int id, String name, String phoneNumber, String email, String address) {
        this.guestID= id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public String getGuestDetails() {
        return String.format("ID: %s, Name: %s, Phone: %s, Email: %s, Address: %s",
                guestID, name, phoneNumber, email, address);
    }

    public void setGuestDetails(int guestID, String name, String phone, String email, String address) {
        this.guestID = guestID;
        this.name = name;
        this.phoneNumber = phone;
        this.email = email;
        this.address = address;
    }

    public boolean validateGuestDetails() {
        return name != null && !name.isEmpty() &&
                phoneNumber != null && phoneNumber.matches("\\d{10}") &&
                email != null && email.contains("@");
    }

    public int getGuestID() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
