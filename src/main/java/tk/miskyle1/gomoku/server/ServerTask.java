package tk.miskyle1.gomoku.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerTask implements Runnable {
  private final Socket client;

  protected ServerTask(Socket client) {
    this.client = client;
  }
  @Override
  public void run() {
    try {
      BufferedReader br = new BufferedReader(
              new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
      String line = br.readLine();
      if (line.equalsIgnoreCase("start")) {
        int id = RoomManager.createRoom(client);
        if (id != -1) {
          BufferedWriter bw = new BufferedWriter(
                  new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
          bw.write("room" + id);
          bw.newLine();
          bw.flush();
        } else {
          br.close();
          client.close();
        }
      } else if (line.startsWith("join")) {
        int id = Integer.parseInt(line.substring(4));
        if (!RoomManager.joinRoom(id, client)) {
          br.close();
          client.close();
        } else {
          BufferedWriter bw = new BufferedWriter(
                  new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
          bw.write("room" + id);
          bw.newLine();
          bw.flush();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
