package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Barco extends JLabel {
  private Point initialClick;
  private Point initialPosition;
  public List<Integer[]> casillasOcupadas = new ArrayList<>();
  public Integer ancho;
  public Integer alto;

  public Barco(Integer ancho, Integer alto, Color color, Integer x, Integer y) {
    this.ancho = ancho;
    this.alto = alto;
    setOpaque(true);
    setBackground(color);
    setBounds(x * MainScreen.CELL_SIZE, y * MainScreen.CELL_SIZE, this.ancho * MainScreen.CELL_SIZE,
        this.alto * MainScreen.CELL_SIZE);
    MainScreen.grillaPropia.revalidate();
    MainScreen.grillaPropia.repaint();
    MainScreen.barcos.add(this);
    MainScreen.grillaPropia.add(this, 1);
    obtenerCasillasOcupadas();

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (!MainScreen.enableDrag)
          return;
        MainScreen.draggedElement = Barco.this;
        initialClick = e.getPoint();
        initialPosition = MainScreen.draggedElement.getLocation();
        MainScreen.grillaPropia.revalidate();
        MainScreen.grillaPropia.repaint();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (!MainScreen.enableDrag)
          return;
        if (MainScreen.draggedElement != null) {
          Point releasePoint = SwingUtilities.convertPoint(MainScreen.draggedElement, e.getPoint(),
              MainScreen.grillaPropia);
          Integer col = Math.max(0,
              Math.min((releasePoint.x - initialClick.x + MainScreen.CELL_SIZE / 2) / MainScreen.CELL_SIZE,
                  MainScreen.GRID_COLS - MainScreen.draggedElement.ancho));
          Integer row = Math.max(0,
              Math.min((releasePoint.y - initialClick.y + MainScreen.CELL_SIZE / 2) / MainScreen.CELL_SIZE,
                  MainScreen.GRID_ROWS - MainScreen.draggedElement.alto));
          Rectangle newBounds = new Rectangle(col * MainScreen.CELL_SIZE, row * MainScreen.CELL_SIZE,
              MainScreen.draggedElement.ancho * MainScreen.CELL_SIZE,
              MainScreen.draggedElement.alto * MainScreen.CELL_SIZE);

          // Verificar si la posición se superpone con otros elementos
          if (!checkCollision(newBounds)) {
            // Si no hay colisión, actualizar la posición y las casillas ocupadas
            MainScreen.draggedElement.setBounds(newBounds);
            obtenerCasillasOcupadas();
          } else {
            // Si hay colisión, devolver el elemento a su posición original
            MainScreen.draggedElement.setBounds(initialPosition.x, initialPosition.y,
                MainScreen.draggedElement.ancho * MainScreen.CELL_SIZE,
                MainScreen.draggedElement.alto * MainScreen.CELL_SIZE);
          }
          MainScreen.draggedElement = null;
        }
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent e) {
        if (!MainScreen.enableDrag)
          return;
        if (MainScreen.draggedElement != null) {
          Integer x = MainScreen.draggedElement.getX() + e.getX() - initialClick.x;
          Integer y = MainScreen.draggedElement.getY() + e.getY() - initialClick.y;
          x = Math.max(0,
              Math.min(x, MainScreen.grillaPropia.getWidth() - MainScreen.draggedElement.ancho * MainScreen.CELL_SIZE));
          y = Math.max(0,
              Math.min(y, MainScreen.grillaPropia.getHeight() - MainScreen.draggedElement.alto * MainScreen.CELL_SIZE));
          MainScreen.draggedElement.setLocation(x, y);
        }
      }
    });

    addMouseWheelListener(e -> {
      if (!MainScreen.enableDrag)
        return;
      if (e.getWheelRotation() != 0) {
        rotateElement();
      }
    });
  }

  public List<Integer[]> getCasillasOcupadas() {
    return casillasOcupadas;
  }

  public void imprimirCasillasOcupadas() {
    for (Integer[] casilla : casillasOcupadas) {
      System.out.println("[" + casilla[0] + ", " + casilla[1] + "]");
    }
  }

  public boolean golpear(String casillaGolpeada) {
    for (int i = 0; i < casillasOcupadas.size(); i++) {
      String casilla = String.valueOf(casillasOcupadas.get(i)[0]) + String.valueOf(casillasOcupadas.get(i)[1]);
      System.out.println(casilla);
      if (casilla.equals(casillaGolpeada)) {
        casillasOcupadas.remove(i);
        return true;
      }
    }
    return false;
  }

  public String getDimentions() {
    return String.valueOf(alto) + String.valueOf(ancho);
  }

  protected void rotateElement() {
    Integer newAlto = ancho;
    Integer newAncho = alto;
    Rectangle newBounds = new Rectangle(getX(), getY(), newAncho * MainScreen.CELL_SIZE,
        newAlto * MainScreen.CELL_SIZE);

    if (newBounds.x + newBounds.width <= MainScreen.grillaPropia.getWidth() &&
        newBounds.y + newBounds.height <= MainScreen.grillaPropia.getHeight() &&
        newBounds.x >= 0 && newBounds.y >= 0 &&
        !checkCollision(newBounds)) {
      Integer temp = alto;
      alto = ancho;
      ancho = temp;
      setSize(ancho * MainScreen.CELL_SIZE, alto * MainScreen.CELL_SIZE);
      revalidate();
      repaint();
      obtenerCasillasOcupadas();
    }
  }

  private boolean checkCollision(Rectangle newBounds) {
    for (Barco other : MainScreen.barcos) {
      if (other != this && newBounds.intersects(other.getBounds())) {
        return true;
      }
    }
    return false;
  }

  private void obtenerCasillasOcupadas() {
    Point location = getLocation();
    casillasOcupadas.clear();
    for (int i = 0; i < ancho; i++) {
      for (int j = 0; j < alto; j++) {
        casillasOcupadas
            .add(new Integer[] { location.x / MainScreen.CELL_SIZE + i, location.y / MainScreen.CELL_SIZE + j });
      }
    }
  }
}
