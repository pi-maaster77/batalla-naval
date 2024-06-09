package mensajeria;

import java.io.*;
import java.net.*;

public class Peer {
  private static final Integer PUERTO = 12345; // Puerto en el que cada peer escuchará
  private String hostRemoto;
  private Integer puertoRemoto;
  private MensajeListener mensajeListener;

  public Peer(String hostRemoto, Integer puertoRemoto) {
    this.hostRemoto = hostRemoto;
    this.puertoRemoto = puertoRemoto;
  }

  public Peer(int puertoRemoto) {
    this.hostRemoto = IngresarIp.obtenerIp();
    this.puertoRemoto = puertoRemoto;
  }

  public void setMensajeListener(MensajeListener mensajeListener) {
    this.mensajeListener = mensajeListener;
  }

  public void iniciar() {
    new Thread(this::iniciarServidor).start();
  }

  private void iniciarServidor() {
    try (ServerSocket servidor = new ServerSocket(PUERTO)) {
      System.out.println("Servidor escuchando en el puerto " + PUERTO);
      while (true) {
        try (Socket socket = servidor.accept()) {
          BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          String mensaje = entrada.readLine();
          System.out.println("Mensaje recibido: " + mensaje);
          if (mensajeListener != null) {
            mensajeListener.onMensajeRecibido(mensaje);
          }
        } catch (IOException e) {
          System.out.println("Error al recibir el mensaje: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.out.println("Error al iniciar el servidor: " + e.getMessage());
    }
  }

  public boolean enviarMensaje(String mensaje) {
    try (Socket socket = new Socket(hostRemoto, puertoRemoto)) {
      PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
      salida.println(mensaje);
      System.out.println("Mensaje enviado: " + mensaje);
      return true;
    } catch (UnknownHostException e) {
      System.out.println("Host desconocido: " + e.getMessage());
      return false;
    } catch (IOException e) {
      System.out.println("Error de E/S: " + e.getMessage());
      return false;
    }
  }

  public static void main(String[] args) {

    Peer peer = new Peer(12345);
    peer.iniciar();
  }
}
