package tk.miskyle1.gomoku.server;

import java.net.Socket;
import java.util.HashMap;

public class RoomManager {
  private final static HashMap<Integer, GameRoom> rooms = new HashMap<>();

  public static int createRoom(Socket socket) {
    int id = -1;
    while (id == -1 || rooms.containsKey(id)) {
      id = (int) (Math.random() * 1000000);
    }
    GameRoom gameRoom = new GameRoom(id);
    if (gameRoom.setPlayer(0, socket)) {
      rooms.put(id, gameRoom);
      gameRoom.start();
      return id;
    }
    return -1;
  }

  public static boolean joinRoom(int id, Socket socket) {
    if (rooms.containsKey(id)) {
      return rooms.get(id).setPlayer(1, socket);
    }
    return false;
  }

  public static void closeRoom(int id) {
    rooms.remove(id);
  }
}
