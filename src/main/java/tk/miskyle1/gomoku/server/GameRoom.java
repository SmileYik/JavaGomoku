package tk.miskyle1.gomoku.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class GameRoom extends TimerTask {
  public static final Long PERIOD = 64L;

  private final int roomId;

  private boolean                 closed     = false;
  private final Socket[]          players    = new Socket[2];
  private final BufferedReader[]  reads      = new BufferedReader[2];
  private final BufferedWriter[]  writes     = new BufferedWriter[2];

  public GameRoom(int roomId) {
    this.roomId = roomId;
  }

  public void run() {
    if (closed) {
      return;
    }
    for (int i = 0; i < 2; ++i) {
      try {
        readMessage(i);
      } catch (IOException e) {
        e.printStackTrace();
        closeRoom();
      }
    }
  }

  private void readMessage(int i) throws IOException {
    if (reads[i] != null && reads[i].ready()) {
      String line = reads[i].readLine();
      handleMessage(i, line);
    }
  }

  private void sendMessage(int to, String msg) {
    if (writes[to] == null) {
      return;
    }
    try {
      writes[to].write(msg);
      writes[to].newLine();
      writes[to].flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleMessage(int from, String message) {
    if (message.equalsIgnoreCase("exit")) {
      closeRoom();
    } else if (message.equalsIgnoreCase("win")) {
      sendMessage(from == 0 ? 1 : 0, message);
      closeRoom();
    } else {
      sendMessage(from == 0 ? 1 : 0, message);
    }
  }

  public void closeRoom() {
    RoomManager.closeRoom(roomId);
    closed = true;
    cancel();
    for (int i = 0; i < 2; ++i) {
      if (writes[i] != null) {
        try {
          writes[i].close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (reads[i] != null) {
        try {
          reads[i].close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (players[i] != null) {
        try {
          players[i].close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void start() {
    Timer timer = new Timer();
    timer.schedule(this, PERIOD, PERIOD);
  }

  public boolean setPlayer(int i, Socket player) {
    players[i] = player;
    try {
      reads[i]  = new BufferedReader(new InputStreamReader(player.getInputStream(), StandardCharsets.UTF_8));
      writes[i] = new BufferedWriter(new OutputStreamWriter(player.getOutputStream(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    if (i == 1) {
      String str = Math.random() < 0.5 ? "White" : "Black";
      String str2 = str.equalsIgnoreCase("White") ? "Black" : "White";
      sendMessage(0, "pawn" + str);
      sendMessage(1, "pawn" + str2);
    }
    return true;
  }
}
