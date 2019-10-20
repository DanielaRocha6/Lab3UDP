package udp;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;



public class UDPServer
{
	private static final int PORT =  3001;
	private static int numClientsDesired = 0;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket;
	private static byte[] bufferMsg;
	private static String fileToSend;
	private static BufferedReader br;
	private static BufferedReader br2;
	private static BufferedReader br3;

	public static void main(String[] args)
	{
		System.out.println("Opening port \n");
		fileToSend = "";
		System.out.println("Welcome to file transfer through UDP");
		String file1;
		file1= "./Archivos/4.ogv";
		String file2;
		file2= "./Archivos/5.ogv";
		System.out.println("Files: ");
		System.out.println(file1 + " of 100 MB");
		System.out.println(file2 + " of 250 MB");
		System.out.println("------------------------------------------------------------------");

		br=new BufferedReader(new InputStreamReader(System.in));
		String fileSelect = "";
		System.out.println("Which file would you like to send?");
		System.out.println("Type 1 to send 100 MB file");
		System.out.println("Type 2 to send 250 MB file");

		while (!fileSelect.equals("1") || !fileSelect.equals("2")) {
			try {
				fileSelect = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(fileSelect.equals("1")){
				fileToSend = file1;
				break;
			} 
			else if(fileSelect.equals("2")){
				fileToSend = file2;
				break;
			}
			else{
				System.out.println("Not a valid entry :(");
			}
		}

		br2=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How many clients would you like to connect to this UDP server?");
		int whichFile2 = 0;

			try {
				whichFile2 = Integer.parseInt(br2.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(whichFile2<=25){
				numClientsDesired = whichFile2;
			} 
			else{
				System.out.println("Not a valid entry :(");
			}

		System.out.println("The file to send is: " + fileToSend);
		System.out.println("There'd be " +numClientsDesired + " clients included in this file transfer.");
		try
		{
			datagramSocket=new DatagramSocket(PORT);
		}
		catch(SocketException sockEx)
		{
			System.out.println("Unable to open port");
			System.exit(1);
		}
		handleClient();
	}
	private static void handleClient()
	{
		try
		{
			String messageIn;
			InetAddress clientAddress=null;
			int clientPort;
			ArrayList<DatagramPacket> clients = new ArrayList<>();
			ArrayList<Thread> threads = new ArrayList<>();
			final CyclicBarrier gate = new CyclicBarrier(numClientsDesired+1);
			do
			{
				bufferMsg= new byte[256];
				inPacket=new DatagramPacket(bufferMsg,bufferMsg.length);
				
				datagramSocket.receive(inPacket);
				clientAddress=inPacket.getAddress();
				clientPort=inPacket.getPort();
				clients.add(inPacket);
				messageIn=new String(inPacket.getData(),0,inPacket.getLength());
				System.out.print(clientAddress);
				System.out.print(" : ");
				System.out.print(clientPort);
				System.out.print(" : ");
				System.out.println(messageIn);
				if(clients.size() == numClientsDesired){
					System.out.println("All clients required are ready.");
					System.out.println("Assigning new thread for each new client...");

					for(DatagramPacket pa: clients){
						Thread t = new ClientHandler(datagramSocket,pa, fileToSend, gate, 64000, numClientsDesired);
						threads.add(t);
						
					}
					System.out.println("Number of threads ready: " + threads.size());
					for(Thread th: threads){
						th.start();
						
						
					}

					br3=new BufferedReader(new InputStreamReader(System.in));
					System.out.println();
					System.out.println("Type Y to start all threads: ");
					String yas = "";

					while(!(yas.equalsIgnoreCase("Y"))){
						yas = br3.readLine();
						if(yas.equalsIgnoreCase("Y")){
							try {
								gate.await();
							} catch (Exception e) {
								e.printStackTrace();
							} 
							break;
						}
						else{
							System.out.println("Not the expected entry");
						}
					}
					System.out.println("All threads started");
					br.close();
					br2.close();
					br3.close();

					for (Thread thread : threads) {
						try {
							thread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("Waiting for thread");
					}
					System.out.println("Done.");
					clients.remove(0);
					break;
				}
			}while(true);
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}
		finally
		{
			System.out.println("\n Closing connection ");
			datagramSocket.close();
			System.out.println("Closed connection.");
		}
	}
}


class ClientHandler extends Thread{ 
	final DatagramSocket datagramSocket;
	final DatagramPacket pa;
	final String fileToSend;
	final CyclicBarrier gate;
	final int buffSize;
	final int clientsDesired;

	// Constructor 
	public ClientHandler(DatagramSocket ds, DatagramPacket pa, String fileToSend, CyclicBarrier gate,
			int buffSize, int clientsDesired)  
	{ 
		this.datagramSocket = ds;
		this.pa = pa; 
		this.fileToSend = fileToSend;
		this.gate = gate;
		this.buffSize = buffSize;
		this.clientsDesired = clientsDesired;
	}

	public byte[] createChecksum(String filename) throws Exception {
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[buffSize];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	@Override
	public void run()  
	{
		try{
			gate.await(); 
			System.out.println("File to send: "+ fileToSend);
			DatagramPacket outPacket=new DatagramPacket(fileToSend.getBytes(),fileToSend.length(),
					pa.getAddress(),pa.getPort());
			datagramSocket.send(outPacket); 

			File f=new File(fileToSend);
			System.out.println("Reading file in: "+f.getCanonicalPath());
			long sz=(int) f.length();
			byte buffer[]=new byte [buffSize];

			outPacket=new DatagramPacket(Long.toString(sz).getBytes(),Long.toString(sz).length(),
					pa.getAddress(),pa.getPort());
			datagramSocket.send(outPacket); 

			System.out.println("");
			System.out.println ("Size: "+ sz);
			System.out.println ("Buffer size: "+ buffSize);

			byte[] digest = createChecksum(fileToSend);

			String digStr = "";

			for(byte by: digest){
				digStr += by;
			}

			System.out.println("Sending checksum of file to client");
			outPacket=new DatagramPacket(digStr.getBytes(),digStr.length(),
					pa.getAddress(),pa.getPort());
			datagramSocket.send(outPacket);

			System.out.println("SENDING FILE");

			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToSend));
			
			int len;
			int packetsSent = 0;
			System.out.println( bis.read(buffer));
			while ((len = bis.read(buffer)) != -1){
				outPacket=new DatagramPacket(buffer,len,
						pa.getAddress(),pa.getPort());
				datagramSocket.setSoTimeout(5000);
				datagramSocket.send(outPacket);
				packetsSent++;
			}
			System.out.println("Packets Sent: " + packetsSent);
			buffer = "end".getBytes();
			outPacket = new DatagramPacket(buffer, buffer.length, pa.getAddress(),pa.getPort());
			System.out.println("Sending end of the file transfer");
			datagramSocket.send(outPacket);

			bis.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("An error occured");
		} 
	} 
} 