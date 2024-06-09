package main;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

public class CircleElement extends JLabel {
  protected Integer diameter;

  public CircleElement(Integer[] coordenadas) {
    this.diameter = MainScreen.CELL_SIZE / 2; // Diámetro igual a la mitad del tamaño de la celda
    setOpaque(false);
    // Calcular las coordenadas para centrar el círculo en la celda
    int x = coordenadas[0] * MainScreen.CELL_SIZE + (MainScreen.CELL_SIZE - diameter) / 2;
    int y = coordenadas[1] * MainScreen.CELL_SIZE + (MainScreen.CELL_SIZE - diameter) / 2;
    setBounds(x, y, diameter, diameter);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.RED);
    g.fillOval(0, 0, diameter, diameter); // Dibujar un círculo sólido centrado en la celda
  }
}
