module com.chess2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chess2 to javafx.fxml;
    exports com.chess2;
}