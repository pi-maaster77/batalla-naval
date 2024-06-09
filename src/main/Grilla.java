package main;

import javax.swing.JLayeredPane;

public class Grilla extends JLayeredPane {
  public Grilla() {
    for (int i = 0; i < MainScreen.GRID_ROWS; i++) {
      for (int j = 0; j < MainScreen.GRID_COLS; j++) {
        BorderSquareElement borderSquare = new BorderSquareElement();
        borderSquare.setBounds(j * MainScreen.CELL_SIZE, i * MainScreen.CELL_SIZE, // Cambiados j e i
            BorderSquareElement.width * MainScreen.CELL_SIZE,
            BorderSquareElement.height * MainScreen.CELL_SIZE);
        this.add(borderSquare, JLayeredPane.DEFAULT_LAYER); // Asegurarse de agregarlo en la capa predeterminada
      }
    }
  }
}
