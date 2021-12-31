package tk.miskyle1.gomoku.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import tk.miskyle1.gomoku.GomokuController;
import tk.miskyle1.gomoku.components.GameColumn;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class GameClient implements Runnable {
  private Thread thread;
  private Socket socket;
  private BufferedWriter bw;
  private BufferedReader br;
  private final Game game;

  public GameClient(Game game) {
    this.game = game;
  }

  @Override
  public void run() {
    while (true) {
      try {
        String line = br.readLine();
        System.out.println(line);
        if (line == null) {
          break;
        }
        if (line.startsWith("pawn")) {
          Platform.runLater(() -> {
            game.setPawn(GameColumn.Status.valueOf(line.substring(4)));
          });
        } else if (line.startsWith("set")) {
          String[] data = line.split(",");
          Platform.runLater(() -> {
            game.setPawn(Integer.parseInt(data[1]), Integer.parseInt(data[2]), game.getOtherPawn());
            GomokuController.refreshTitle();
          });
        } else if (line.startsWith("win")) {
          disconnect();
          Platform.runLater(() -> {
            game.setPawn(GameColumn.Status.None);
            new Alert(Alert.AlertType.INFORMATION, "对方赢了.").showAndWait();
          });
          break;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public int createRoom() {
    try {
      sendMessage("start");
      String line = br.readLine();
      thread = new Thread(this);
      thread.start();
      return Integer.parseInt(line.substring(4));
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

  public boolean joinRoom(int id) {
    try {
      sendMessage("join" + id);
      thread = new Thread(this);
      thread.start();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void setPawn(int x, int y) {
    try {
      sendMessage("set," + x + "," + y);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void win() {
    try {
      sendMessage("win");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendMessage(String str) throws IOException {
    bw.write(str);
    bw.newLine();
    bw.flush();
  }

  public boolean connect(String ip, int port) {
    try {
      socket = new Socket(ip, port);
      bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
      br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
      disconnect();
      socket = null;
      bw = null;
      br = null;
    }
    return socket != null;
  }

  public void disconnect() {
    if (br != null) {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (bw != null) {
      try {
        bw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
