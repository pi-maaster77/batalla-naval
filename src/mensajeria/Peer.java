package mensajeria;

import java.io.*;
import java.net.*;

public class Peer {
  private Integer puertoMio;
  private String hostRemoto;
  private Integer puertoRemoto;
  private MensajeListener mensajeListener;

  public Peer(String hostRemoto, Integer puertoRemoto) {
    this.hostRemoto = hostRemoto;
    this.puertoRemoto = puertoRemoto;
  }

  public Peer(String hostRemoto, Integer puertoRemoto, Integer puertoMio) {
    this.hostRemoto = hostRemoto;
    this.puertoRemoto = puertoRemoto;
    this.puertoMio = puertoMio;
  }

  public void setMensajeListener(MensajeListener mensajeListener) {
    this.mensajeListener = mensajeListener;
  }

  public void iniciar() {
    new Thread(this::iniciarServidor).start();
  }

  private void iniciarServidor() {
    try (ServerSocket servidor = new ServerSocket(puertoMio)) {
      System.out.println("Servidor escuchando en el puerto " + puertoMio);
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

    Peer peer = new Peer(IngresarIp.obtenerIp(), 12345);
    peer.iniciar();
  }
}
