package tk.miskyle1.gomoku.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameColumn extends Canvas {
  public static final double LITTLE_CYCLE_RADIUS = 5;

  public enum Status {
    None,
    Black,
    White
  }
  private final double radius;
  private Status status = Status.None;
  private boolean littleCycle;

  public GameColumn(double width, double height, double radius) {
    super(width, height);
    this.radius = radius;
    reset();
  }

  private void reset() {
    GraphicsContext gc = getGraphicsContext2D();
    gc.restore();
    double width = getWidth();
    double height = getHeight();
    gc.strokeLine(0, height/2, width, height/2);
    gc.strokeLine(width/2, 0, width/2, height);
    if (littleCycle) {
      gc.setFill(Color.BLACK);
      gc.fillOval(
              width/2 - LITTLE_CYCLE_RADIUS,
              height/2 - LITTLE_CYCLE_RADIUS,
              LITTLE_CYCLE_RADIUS*2,
              LITTLE_CYCLE_RADIUS*2
      );
    }
  }

  private void drawPawn() {
    reset();
    if (status == Status.None) {
      return;
    }
    Color color = status == Status.Black ? Color.BLACK : Color.WHITE;
    GraphicsContext gc = getGraphicsContext2D();
    double width = getWidth();
    double height = getHeight();
    gc.setFill(color);
    gc.fillOval(width/2 - radius, height/2 - radius, radius*2, radius*2);
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
    drawPawn();
  }

  public void setLittleCycle(boolean littleCycle) {
    this.littleCycle = littleCycle;
    drawPawn();
  }
}
