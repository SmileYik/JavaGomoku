package tk.miskyle1.gomoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tk.miskyle1.gomoku.client.Game;
import tk.miskyle1.gomoku.client.GameClient;
import tk.miskyle1.gomoku.components.GameColumn;

import java.util.Optional;

public class GomokuController {
  private static GomokuController controller;

  @FXML
  private GridPane gamePane;

  private Stage stage;
  private Game game = new Game();
  private GameClient client = new GameClient(game);

  public void init(Stage stage) {
    controller = this;
    this.stage = stage;
    newGame();
  }

  public void newGame() {
    client.disconnect();
    gamePane.getChildren().clear();
    game = new Game();
    client = new GameClient(game);
    for (int i = 0; i < 15; ++i) {
      for (int j = 0; j < 15; ++j) {
        GameColumn column = game.getColumn(i, j);
        gamePane.add(column, i, j);
        int finalI = i;
        int finalJ = j;
        column.setOnMouseClicked(e -> {
          if (game.putPawn(finalI, finalJ)) {
            client.setPawn(finalI, finalJ);
            if (game.analyzePawn(finalI, finalJ)) {
              client.win();
              game.setPawn(GameColumn.Status.None);
              client.disconnect();
              new Alert(Alert.AlertType.INFORMATION, "你赢了.").showAndWait();
            }
            refreshTitle();
          }
        });
      }
    }
  }

  private boolean connect() {
    TextInputDialog dialog = new TextInputDialog("172.21.6.13:13333");
    dialog.setHeaderText("注意, 如果点击确定后会重设当前游戏!");
    dialog.setContentText("请输入服务器ip地址: \n如127.0.0.1:13333");
    Optional<String> optionalIp = dialog.showAndWait();
    if (optionalIp.isEmpty()) {
      return false;
    }
    String[] data = optionalIp.get().split(":");
    newGame();
    client.connect(data[0], Integer.parseInt(data[1]));
    return true;
  }

  public void createRoom(ActionEvent actionEvent) {
    if (!connect()) {
      return;
    }
    int id = client.createRoom();
    if (id != -1) {
      game.setId(id);
      new Alert(Alert.AlertType.INFORMATION, "房间号为: " + id).showAndWait();
    }
  }

  public void showRoomId() {
    if (game.getId() == -1) {
      new Alert(Alert.AlertType.INFORMATION, "你没有创建一个房间哦").showAndWait();
    } else {
      new Alert(Alert.AlertType.INFORMATION, "房间号为: " + game.getId()).showAndWait();
    }
  }

  public void joinRoom(ActionEvent actionEvent) {
    if (!connect()) {
      return;
    }
    TextInputDialog dialog = new TextInputDialog();
    dialog.setHeaderText("请输入房间号");
    dialog.setContentText("请输入房间号");
    Optional<String> id = dialog.showAndWait();
    if (id.isEmpty()) {
      return;
    }
    client.joinRoom(Integer.parseInt(id.get()));
  }

  public static void refreshTitle() {
    if (controller.game.isYourTurn()) {
      controller.stage.setTitle("Gomoku! (到你了)");
    } else {
      controller.stage.setTitle("Gomoku! (对方正在思考)");
    }
  }

  public void showAbout(ActionEvent actionEvent) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("关于");
    alert.setHeaderText("关于");
    alert.setContentText("作者: SmileYik\n五子棋:\n黑子先下, 5个相同子连到一起就算赢.\n");
    alert.showAndWait();
  }
}