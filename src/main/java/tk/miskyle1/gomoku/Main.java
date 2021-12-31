package tk.miskyle1.gomoku;

import tk.miskyle1.gomoku.server.GameServer;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    if (args.length != 0) {
      GameServer.main(args);
    } else {
      Gomoku.main(args);
    }
  }
}
