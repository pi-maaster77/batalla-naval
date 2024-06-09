package main;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JLabel;

public class CrossElement extends JLabel {
  protected Integer width;
  protected Integer height;

  public CrossElement(Integer[] coordenadas) {
    this.width = 1;
    this.height = 1;
    setOpaque(false);
    setBackground(Color.red);
    setBounds(coordenadas[0] * MainScreen.CELL_SIZE, coordenadas[1] * MainScreen.CELL_SIZE,
        width * MainScreen.CELL_SIZE, height * MainScreen.CELL_SIZE);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.RED);
    g.drawLine(0, 0, getWidth(), getHeight());
    g.drawLine(getWidth(), 0, 0, getHeight());
  }
}
