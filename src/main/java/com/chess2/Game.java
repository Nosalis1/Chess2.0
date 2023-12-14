import com.chess2.App;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Game {
    public static final Game instance = new Game();

    public final GridPane root = new GridPane() {{
        for (int row = 0; row < App.CELL_COUNT; row++) {
            for (int col = 0; col < App.CELL_COUNT; col++) {
                Rectangle square = new Rectangle(App.CELL_SIZE, App.CELL_SIZE);
                square.setFill((row + col) % 2 == 0 ? Color.valueOf("#B58863") : Color.valueOf("#F0D9B5"));
                this.add(square, col, row);

                //square.setOnMouseClicked(new Game.SquareClickHandler(col, row));
            }
        }
    }};
}
