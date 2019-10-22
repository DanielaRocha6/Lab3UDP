package cliente;
import java.net.*;


import java.io.*;

public class Cliente {
	
	public String ip;
	public int port;
	public boolean state;
//	private Log log; 

	/**
	 * Inicializa los atributos de la clase, con los valores recibidos por parametro
	 */
	public Cliente(String server, int port)  throws IOException {
		ip = server;
		port = port;
		state = false;
//		log = new Log();
}
	
	/**
	 * Protocolo UDP, envio y recepcion de paquetes
	 * @param clientes
	 * @param archivo
	 * @throws IOException
	 */
	public void proceso(int clientes, int archivo) throws IOException
	{
		DatagramSocket socketCliente = null;
		byte[] buff = new byte[8000];
		String linea = "";
		buff ="OK".getBytes();

		try {
			
			// el socket UDP se crea
			socketCliente = new DatagramSocket(port);
			
			// la conexion se establece
			state = true;
			
			// se envia el estado de ya se esta preparado al servidor
			InetAddress server = InetAddress.getByName(ip);
			socketCliente.send(new DatagramPacket(buff,buff.length,server,port));
			
			// Cliente recibe el mensaje de confirmacion de conexion con el ip  y puerto del grupo
			
			buff = new byte[8000];
			DatagramPacket paquete = new DatagramPacket(buff,buff.length);
			socketCliente.receive(paquete);
			String data[] = new String(paquete.getData()).split(";");
			if(data[0].equals("OK")){
				
				InetAddress ipg = InetAddress.getByName(data[1]);
			
				//Se inicializa el socket multicast
				
				MulticastSocket socket = new MulticastSocket(Integer.parseInt(data[2]));
				
				//Se crea el grupo
				socket.joinGroup(ipg);

				while (true) {
					
					//recibir nombre del archivo
					buff = new byte[8000];
					paquete = new DatagramPacket(buff,buff.length);
					socketCliente.receive(paquete);
					String PATH = ".\\videosStreaming\\"+new String(paquete.getData());
					FileOutputStream fos = new FileOutputStream(new File(PATH),false);

					buff = new byte[8000];
					paquete = new DatagramPacket(buff,buff.length);
					socketCliente.receive(paquete);
					linea = new String(paquete.getData());
					
					
					Interface reproducer = new Interface(PATH);
					while(linea.equals("DONE")){
						fos.write(paquete.getData());
						buff = new byte[8000];
						paquete = new DatagramPacket(buff,buff.length);
						socketCliente.receive(paquete);
						linea = new String(paquete.getData());
					}
				}
			}
		}
		catch(Exception e)
		{

		}
	}
	public boolean getEstado() {
		return state;
	}

}