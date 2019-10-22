package servidor;
import java.io.*;

import java.net.*;
import java.util.Iterator;

public class Servidor {  

	public static final int PORT = 8000;


	public static void main(String[] args) throws IOException{
		try {
			
			//crea socket udp
			DatagramSocket socketServidor = new DatagramSocket(PORT);

			DatagramSocket socketCliente = null;

			System.out.println("Escuchando: " + socketServidor);
// crean 2 threads para escuchar peticiones
			int t = 1;
			while(t<2)
			{
				TServer thread = new TServer(socketCliente, t);
				thread.start();
				t++;
			}
		} catch (IOException e) {
			System.out.println("No se pudo escuchar en el puerto");
		}
	}
}