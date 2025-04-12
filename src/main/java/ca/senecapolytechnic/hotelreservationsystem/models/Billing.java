package ca.senecapolytechnic.hotelreservationsystem.models;

public class Billing {
    private String billID;
    private String reservationID;
    private double amount;
    private double tax;
    private double totalAmount;
    private double discount;

    public void generateBill(double amount, double tax, double discount) {
        this.amount = amount;
        this.tax = tax;
        this.discount = discount;
        calculateTotal();
    }

    public void applyDiscount(double discount) {
        this.discount = discount;
        calculateTotal();
    }

    public void calculateTotal() {
        totalAmount = (amount + tax) - discount;
    }

    public void printBill() {
        System.out.printf("Bill ID: %s\nAmount: %.2f\nTax: %.2f\nDiscount: %.2f\nTotal: %.2f\n",
                billID, amount, tax, discount, totalAmount);
    }
}
