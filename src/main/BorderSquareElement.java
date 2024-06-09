package main;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JLabel;

public class BorderSquareElement extends JLabel {
  protected static Integer width = 1;
  protected static Integer height = 1;

  public BorderSquareElement() {
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.BLACK);
    g.drawRect(0, 0, getWidth(), getHeight());
  }
}
