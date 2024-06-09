package main;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JLabel;

public class BorderSquareElement extends JLabel {
    protected Integer width;
    protected Integer height;

    public BorderSquareElement() {
        this.width = 1;
        this.height = 1;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth(), getHeight());
    }
}
