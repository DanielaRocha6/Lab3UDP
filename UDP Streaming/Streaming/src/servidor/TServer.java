package servidor;
import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;


public class TServer extends Thread{

	public final static String FILE_TO_SEND = "C:\\Users\\mariana\\Desktop\\hola(2).mp4";    

	private DatagramSocket sktServidor = null;
	private int id = -1;
	/**
	 * String para almacenar la ip del grupo de multicast
	 */
	public static String groupIp = "";

	public TServer(DatagramSocket pSocket, int n)
	{
		this.id = n;
		this.sktServidor = pSocket;
		//se genera la ip en los rangos disponibles de multicast
		long red = (int)Math.random()*239+225;
		while(red<225 || red>239)
		{red = (int)Math.random()*239+225;}
		this.groupIp = red+"."+Math.round(Math.random()*255)+"."+Math.round(Math.random()*255)+"."+Math.round(Math.random()*255); 
		System.out.println(this.groupIp);
	}
	public void run()
	{
		try {
			System.out.println("Inicio de un nuevo thread servidor: "+this.id);
			//Inicia multicast
			MulticastSocket socket = new MulticastSocket(6789);
			int puertoGrupo = 6789;
			// Se une al grupo
			InetAddress ipGrupo= InetAddress.getByName(this.groupIp);
			socket.joinGroup(ipGrupo);
//			byte[] buffer = new byte[8000];
//			//Se recibe el packete con el preparado de parte del cliente
//			DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
//			sktServidor.receive(packet);
//			String str = new String(packet.getData());
//			//se obtiene ip y puerto de origen del paquete recibido, esto para poder enviar
//			InetAddress addr = packet.getAddress();
//			int port = packet.getPort();

			//Envio ip del grupo al cliente
			//if(str.equals("Preparado")){
//			buffer=this.groupIp.getBytes();
//				sktServidor.send(new DatagramPacket(buffer,buffer.length,addr,port));
//			//}
			//Envio video a la ip del grupo
			//	TODO
			FileInputStream fis = new FileInputStream(new File("C:\\Users\\mariana\\Desktop\\hola(2).mp4"));
			byte[] buffer = new byte[8000];
			int count = fis.read(buffer);
			while(count>0)
			{
				DatagramPacket mensajeSalida = new DatagramPacket(buffer, buffer.length, ipGrupo, puertoGrupo);
				socket.send(mensajeSalida);
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}