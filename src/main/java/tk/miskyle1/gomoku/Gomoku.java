package tk.miskyle1.gomoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gomoku extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Gomoku.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 525, 557);
    GomokuController controller = fxmlLoader.getController();
    controller.init(stage);
    stage.setTitle("Gomoku!");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setOnCloseRequest(e -> {
      controller.newGame();
      System.exit(0);
    });
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}