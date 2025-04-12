package ca.senecapolytechnic.hotelreservationsystem.models;

public class Feedback {
    private String feedbackID;
    private String guestID;
    private String reservationID;
    private String comments;
    private int rating;

    public void submitFeedback(String guestID, String reservationID, String comments, int rating) {
        this.guestID = guestID;
        this.reservationID = reservationID;
        this.comments = comments;
        this.rating = rating;
    }

    public String getFeedbackDetails() {
        return String.format("Guest: %s, Reservation: %s, Rating: %d\nComments: %s",
                guestID, reservationID, rating, comments);
    }
}
