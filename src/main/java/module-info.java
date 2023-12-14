module com.chess2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;


    opens com.chess2 to javafx.fxml;
    exports com.chess2;
    exports com.chess2.pieces;
}