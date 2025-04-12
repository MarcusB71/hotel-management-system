module ca.senecapolytechnic.hotelreservationsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.media;

    opens ca.senecapolytechnic.hotelreservationsystem.models to javafx.base;
    opens ca.senecapolytechnic.hotelreservationsystem.controller to javafx.fxml;
    exports ca.senecapolytechnic.hotelreservationsystem;
}