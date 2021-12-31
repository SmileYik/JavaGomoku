package tk.miskyle1.gomoku.server;

import javafx.scene.Scene;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

  private GameServer(int port) throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Server open on port " + port);
    while (true) {
      Socket socket = serverSocket.accept();
      new Thread(new ServerTask(socket)).start();
    }
  }

  public static void main(String[] args) throws IOException {
    int port = 13333;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }
    new GameServer(port);
  }
}
