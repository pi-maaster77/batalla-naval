package main;

import javax.swing.JLabel;
import java.awt.Color;

public class BarcoHundido extends JLabel {
	private Integer ancho;
	private Integer alto;

	public BarcoHundido(Integer ancho, Integer alto, Color color, Integer x, Integer y) {
		this.ancho = ancho;
		this.alto = alto;
		setOpaque(true);
		setBackground(color);
		setBounds(x * MainScreen.CELL_SIZE, y * MainScreen.CELL_SIZE, this.ancho * MainScreen.CELL_SIZE,
				this.alto * MainScreen.CELL_SIZE);
		MainScreen.grillaContrincante.revalidate();
		MainScreen.grillaContrincante.repaint();
		MainScreen.grillaContrincante.add(this, 1);
	}

}
