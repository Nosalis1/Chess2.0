module com.chess {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.sql;
    requires mysql.connector.j;


    opens com.chess2 to javafx.fxml;
    exports com.chess2;
    exports com.chess2.networking;
    exports com.chess2.pieces;
    exports com.chess2.players;
    exports com.chess2.utility;
}