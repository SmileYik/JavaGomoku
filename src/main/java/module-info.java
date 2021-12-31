module tk.miskyle1.gomoku {
  requires javafx.controls;
  requires javafx.fxml;

  opens tk.miskyle1.gomoku to javafx.fxml;
  exports tk.miskyle1.gomoku;
}