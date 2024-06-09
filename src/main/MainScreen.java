package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import mensajeria.*;

import java.util.ArrayList;

public class MainScreen extends JFrame {
    public static final Integer GRID_ROWS = 10;
    public static final Integer GRID_COLS = 10;
    public static final Integer CELL_SIZE = 50;
    public static Grilla grillaPropia;
    public static ArrayList<Barco> barcos;
    public static Barco draggedElement;
    public static Boolean enableDrag = true;
    private Grilla grillaContrincante;
    private ArrayList<Coordenada> hits;
    public Peer peer;
    public Boolean hit = false;
    public Boolean hitting = false;

    public MainScreen(Peer peer) {
        crearGrillas();
        setTitle("Arrastrar y Soltar en una Grilla");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        barcos = new ArrayList<>();
        hits = new ArrayList<>();
        add(grillaPropia, BorderLayout.CENTER);
        this.peer = peer;
        peer.setMensajeListener(new MensajeListener() {
            @Override
            public void onMensajeRecibido(String mensaje) {
                miTurno();
            }
        });

        new Barco(1, 2, new Color(0x00FFFF), 0, 0);
        new Barco(1, 2, new Color(0x00FFFF), 1, 0);
        new Barco(1, 3, new Color(0xFFFF00), 2, 0);
        new Barco(1, 4, new Color(0xF000FF), 3, 0);
        new Barco(1, 5, new Color(0xAA02C4), 4, 0);
        new Barco(2, 5, new Color(0x00FF00), 5, 0);

        JButton button = new JButton("Obtener Coordenadas");
        button.addActionListener(e -> {
            enableDrag = false;
            for (Barco elem : barcos) {
                Point location = elem.getLocation();
                System.out.println("Coordenadas de la parte superior izquierda: " + location.x + ", " + location.y + ", " + elem.getDimentions());
                hitting = true;
            }
            peer.enviarMensaje("true");
            peer.setMensajeListener(new MensajeListener() {
                @Override
                public void onMensajeRecibido(String mensaje) {
                    if (mensaje.equals("true")) {
                        suTurno();
                    } else {
                        miTurno();
                    }
                }
            });
        });
        add(button, BorderLayout.SOUTH);
        peer.iniciar();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void crearGrillas() {
        JPanel gridPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(GRID_COLS * CELL_SIZE, GRID_ROWS * CELL_SIZE);
            }
        };

        grillaPropia = new Grilla();
        grillaPropia.setPreferredSize(gridPanel.getPreferredSize());
        grillaPropia.add(gridPanel, JLayeredPane.DEFAULT_LAYER);

        grillaContrincante = new Grilla();
        grillaContrincante.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (peer == null) {
                    System.err.println("Error: 'peer' es nulo.");
                    return;
                }
                if (!hitting) {
                    System.err.println("Error: 'hitting' es falso.");
                    return;
                }
                if (hitting) {
                    Integer[] coordenadas = {e.getX() / 50, e.getY() / 50};
                    peer.enviarMensaje(String.valueOf(coordenadas[0]) + String.valueOf(coordenadas[1]));

                    peer.setMensajeListener(new MensajeListener() {
                        @Override
                        public void onMensajeRecibido(String mensaje) {
                            if (mensaje.length() > 2) {
                                if (hits == null) {
                                    System.err.println("Error: 'hits' es nulo.");
                                    return;
                                }
                                if (!hits.contains(new Coordenada(coordenadas[0], coordenadas[1])) && hitting) {
                                    hits.add(new Coordenada(coordenadas[0], coordenadas[1]));
                                    System.out.println("se golpeo: " + coordenadas[0] + "," + coordenadas[1]);
                                    if (mensaje.equals("true")) {
                                        grillaContrincante.add(new CircleElement(coordenadas), JLayeredPane.DRAG_LAYER + 1000);
                                        Timer timer = new Timer(2000, new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent evt) {
                                                ((Timer) evt.getSource()).stop();
                                            }
                                        });
                                        timer.setRepeats(false);
                                        timer.start();
                                    } else {
                                        grillaContrincante.add(new CrossElement(coordenadas), JLayeredPane.DRAG_LAYER + 1000);
                                        Timer timer = new Timer(2000, new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent evt) {
                                                miTurno();
                                                ((Timer) evt.getSource()).stop();
                                            }
                                        });
                                        hitting = false;
                                        timer.setRepeats(false);
                                        timer.start();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        grillaContrincante.setPreferredSize(gridPanel.getPreferredSize());
        grillaContrincante.add(gridPanel, JLayeredPane.DEFAULT_LAYER);
        grillaContrincante.setVisible(false);
    }

    public void miTurno() {
        peer.setMensajeListener(new MensajeListener() {
            @Override
            public void onMensajeRecibido(String mensaje) {
                Boolean perdimos = true;
                Boolean golpeo = false;
                Integer[] punto = {mensaje.charAt(0) - '0', mensaje.charAt(1) - '0'};
                for (Barco barco : barcos) {
                    golpeo = barco.golpear(mensaje) || golpeo;
                    perdimos = perdimos && barco.casillasOcupadas.size() == 0;
                }
                if (golpeo) {
                    grillaPropia.add(new CircleElement(punto));
                    peer.enviarMensaje("true");
                } else {
                    grillaPropia.add(new CrossElement(punto));
                    peer.enviarMensaje("false");
                    suTurno();
                }
                if (perdimos) {
                    System.out.println("perdimos");
                }
            }
        });

        add(grillaPropia, BorderLayout.CENTER);
        grillaPropia.setVisible(true);
        remove(grillaContrincante);
        grillaContrincante.setVisible(false);
    }

    public void suTurno() {
        peer.enviarMensaje("miturno");
        hitting = true;
        add(grillaContrincante, BorderLayout.CENTER);
        grillaContrincante.setVisible(true);
        remove(grillaPropia);
        grillaPropia.setVisible(false);
    }
}
