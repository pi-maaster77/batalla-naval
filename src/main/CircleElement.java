package main;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JLabel;

public class CircleElement extends JLabel {
    protected Integer width;
    protected Integer height;

    public CircleElement(Integer[] coordenadas) {
        this.width = 1;
        this.height = 1;
        setOpaque(false);
        setBounds(coordenadas[0]*MainScreen.CELL_SIZE, coordenadas[1]*MainScreen.CELL_SIZE, width * MainScreen.CELL_SIZE, height * MainScreen.CELL_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawOval(0, 0, getWidth(), getHeight());
    }
}
