package ca.senecapolytechnic.hotelreservationsystem.models;

public class Admin {
    private String adminID;
    private String username;
    private String password;

    public boolean login(String user, String pass) {
        return this.username.equals(user) && this.password.equals(pass);
    }

    public void searchGuest(String guestID) {
        System.out.println("Searching guest with ID: " + guestID);
    }

    public void checkOutGuest(String reservationID) {
        System.out.println("Guest checked out for reservation: " + reservationID);
    }

    public void generateReport() {
        System.out.println("Generating report...");
    }
}
