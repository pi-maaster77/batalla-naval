package mensajeria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IngresarIp {
  private static String resultado = null;
  private static JList<String> hostsList;
  private static DefaultListModel<String> listModel;

  public static String obtenerIp() {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new GridBagLayout());

    frame.setTitle("miApp");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    panel.setBackground(new Color(0xFFFFFF));
    frame.add(panel);

    listModel = new DefaultListModel<>();
    hostsList = new JList<>(listModel);
    hostsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JButton scanButton = new JButton("Escanear Hosts");
    scanButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        listModel.clear();
        for (String subred : obtenerSubredesLocales()) {
          scanNetwork(subred);
        }
      }
    });

    JButton enviar = new JButton("Enviar");
    enviar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        synchronized (IngresarIp.class) {
          resultado = hostsList.getSelectedValue();
          IngresarIp.class.notify();
        }
        frame.dispose();
      }
    });

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(scanButton, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(new JScrollPane(hostsList), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    panel.add(enviar, gbc);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    synchronized (IngresarIp.class) {
      while (resultado == null) {
        try {
          IngresarIp.class.wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return null;
        }
      }
    }

    return resultado;
  }

  private static void scanNetwork(String subred) {
    ExecutorService executor = Executors.newFixedThreadPool(20);
    for (int i = 1; i < 255; i++) {
      String host = subred + i;
      executor.submit(() -> {
        try {
          InetAddress inetAddress = InetAddress.getByName(host);
          if (inetAddress.isReachable(1000)) {
            SwingUtilities.invokeLater(() -> listModel.addElement(host));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }

    executor.shutdown();
    try {
      executor.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static java.util.List<String> obtenerSubredesLocales() {
    java.util.List<String> subredes = new java.util.ArrayList<>();
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      for (NetworkInterface ni : Collections.list(interfaces)) {
        if (ni.isUp() && !ni.isLoopback()) {
          Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
          for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if (inetAddress instanceof java.net.Inet4Address) {
              String ip = inetAddress.getHostAddress();
              String subred = ip.substring(0, ip.lastIndexOf('.') + 1);
              subredes.add(subred);
            }
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return subredes;
  }
}
