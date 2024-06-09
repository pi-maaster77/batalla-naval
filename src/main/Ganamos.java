package main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ganamos extends JFrame {
	public Ganamos() {
		setTitle("Game Over");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.add(new JLabel("Ganamos"));
		add(panel);
		setSize(200, 100); // Set the size of the window
		setLocationRelativeTo(null); // Center the window on the screen
		setVisible(true); // Make the window visible
	}
}
