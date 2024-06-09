package main;

import javax.swing.JLayeredPane;

public class Grilla extends JLayeredPane{
    Grilla(){
        for (int i = 0; i < MainScreen.GRID_ROWS; i++) {
            for (int j = 0; j < MainScreen.GRID_COLS; j++) {
                BorderSquareElement borderSquare = new BorderSquareElement();
                borderSquare.setBounds(i * 50, j * 50, borderSquare.width * MainScreen.CELL_SIZE, borderSquare.height * MainScreen.CELL_SIZE);
                this.add(borderSquare, JLayeredPane.DEFAULT_LAYER);
            }
        }
    }
}