package main;

import javax.swing.*;

import mensajeria.IngresarIp;
import mensajeria.Peer;

public class App {
  public static void main(String[] args) {
    String ip = (args.length > 0) ? args[0] : IngresarIp.obtenerIp();
    SwingUtilities.invokeLater(() -> new MainScreen(new Peer(ip, 12345)));
  }
}
