package tk.miskyle1.gomoku.client;

import javafx.scene.control.Alert;
import tk.miskyle1.gomoku.components.GameColumn;

public class Game {
  private int id = -1;
  private final GameColumn[][] columns;
  private GameColumn.Status pawn = GameColumn.Status.None;
  private int times = 0;

  public Game() {
    columns = new GameColumn[15][15];
    for (int i = 0; i < 15; ++i) {
      for (int j = 0; j < 15; ++j) {
        columns[i][j] = new GameColumn(35, 35, 12.5);
      }
    }

    columns[7][7].setLittleCycle(true);
    columns[3][3].setLittleCycle(true);
    columns[3][11].setLittleCycle(true);
    columns[11][11].setLittleCycle(true);
    columns[11][3].setLittleCycle(true);
  }

  public GameColumn getColumn(int x, int y) {
    return columns[x][y];
  }

  public synchronized void setPawn(GameColumn.Status pawn) {
    this.pawn = pawn;
    if (pawn != GameColumn.Status.None) {
      new Alert(
              Alert.AlertType.INFORMATION,
              "你的棋子是:" + pawn.name() + (pawn == GameColumn.Status.Black ? "你先下哦!" : "等黑子下完后你再下哦~"
              )
      ).showAndWait();
    }
  }

  public synchronized boolean putPawn(int x, int y) {
    if (pawn == GameColumn.Status.None) {
      return false;
    }
    if (columns[x][y].getStatus() != GameColumn.Status.None) {
      return false;
    }
    if (pawn == GameColumn.Status.Black && (times & 1) == 0) {
      ++times;
      columns[x][y].setStatus(pawn);
      return true;
    } else if (pawn == GameColumn.Status.White && (times & 1) == 1) {
      ++times;
      columns[x][y].setStatus(pawn);
      return true;
    }
    return false;
  }

  public boolean analyzePawn(int x, int y) {
    // row
    int num = 0;
    int tempX = x;

    while (tempX < 15 && columns[tempX][y].getStatus() == pawn) {
      ++num;
      ++tempX;
    }
    tempX = x - 1;
    while (tempX >= 0 && columns[tempX][y].getStatus() == pawn) {
      ++num;
      --tempX;
    }
    if (num >= 5) {
      return true;
    }
    // column
    int tempY = y;
    num = 0;
    while (tempY < 15 && columns[x][tempY].getStatus() == pawn) {
      ++num;
      ++tempY;
    }
    tempY = y - 1;
    while (tempY >= 0 && columns[x][tempY].getStatus() == pawn) {
      ++num;
      --tempY;
    }
    if (num >= 5) {
      return true;
    }
    // \
    tempX = x;
    tempY = y;
    num = 0;
    while (tempX >= 0 && tempY >= 0 && tempY < 15 && tempX < 15 && columns[tempX][tempY].getStatus() == pawn) {
      ++num;
      ++tempX;
      ++tempY;
    }
    tempX = x - 1;
    tempY = y - 1;
    while (tempX >= 0 && tempY >= 0 && tempY < 15 && tempX < 15 && columns[tempX][tempY].getStatus() == pawn) {
      ++num;
      --tempX;
      --tempY;
    }
    if (num >= 5) {
      return true;
    }
    // /
    tempX = x;
    tempY = y;
    num = 0;
    while (tempX >= 0 && tempY >= 0 && tempY < 15 && tempX < 15 && columns[tempX][tempY].getStatus() == pawn) {
      ++num;
      --tempX;
      ++tempY;
    }
    tempX = x - 1;
    tempY = y - 1;
    while (tempX >= 0 && tempY >= 0 && tempY < 15 && tempX < 15 && columns[tempX][tempY].getStatus() == pawn) {
      ++num;
      ++tempX;
      --tempY;
    }

    return num >= 5;
  }

  public synchronized void setPawn(int x, int y, GameColumn.Status status) {
    ++times;
    columns[x][y].setStatus(status);
  }

  public synchronized GameColumn.Status getPawn() {
    return pawn;
  }

  public synchronized GameColumn.Status getOtherPawn() {
    return pawn == GameColumn.Status.White ?
            GameColumn.Status.Black : GameColumn.Status.White;
  }

  public boolean isYourTurn() {
    return pawn == GameColumn.Status.Black && (times & 1) == 0
            || pawn == GameColumn.Status.White && (times & 1) == 1;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
