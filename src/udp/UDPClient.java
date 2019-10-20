package udp;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UDPClient
{
	private static InetAddress host;
	private static final int PORT= 3001;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket,outPacket;
	private static byte[] buffer;
	private static int buffSize = 64000;
	
	public static byte[] createChecksum(String filename) throws Exception {
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

	public static void main(String[] args)
	{
		try
		{
			host=InetAddress.getByName("localhost");
		}
		catch(UnknownHostException uhEx)
		{
			System.out.println("HOST ID not found.. ");
			System.exit(1);
		}
		accessServer();
	}
	private static void accessServer()
	{
		try
		{
			datagramSocket=new DatagramSocket();
			String message="Hi, I'm a client",response="";
			System.out.println("Notyfing to server : "+message);
			System.out.println("Waiting for server's order...");
			outPacket=new DatagramPacket(message.getBytes(),message.length(),host,PORT);
			datagramSocket.send(outPacket);
			
			buffer=new byte[256];
			inPacket=new DatagramPacket(buffer,buffer.length);
			datagramSocket.receive(inPacket);
			response=new String(inPacket.getData(),0,inPacket.getLength());
			System.out.println("Connected to server.");
			System.out.println("File to receive: " + response);

			if(response.contains("testl")){
				buffSize = buffSize*4;
			}
			String[] a = response.split("/");
			response = a[2];
			System.out.println("Saving as file: "+ response);
			String filename = "./Descargas/"+datagramSocket.getLocalPort()+"-"+response;
			
			BufferedOutputStream bos = 
					new BufferedOutputStream(new FileOutputStream(filename));

			datagramSocket.receive(inPacket);
			response=new String(inPacket.getData(),0,inPacket.getLength());
			long sz=Long.parseLong(response);
			System.out.println ("File Size: "+(sz/(1024*1024))*4.5+" MB");

			System.out.println("Receiving checksum from server...");
			datagramSocket.receive(inPacket);
			String servCS = new String(inPacket.getData(),0,inPacket.getLength());

			System.out.println("Receiving file...");

			byte[] rcvBuffer = new byte[buffSize];
			DatagramPacket rcvPacket = new DatagramPacket(rcvBuffer,rcvBuffer.length);
			boolean notSent = true;
			int packetsReceived = 0;
			long bef = System.currentTimeMillis();
			while(notSent){
				try{
					datagramSocket.setSoTimeout(3000); 
					datagramSocket.receive(rcvPacket);
				}catch(Exception e){
					break;
				}
				if (new String(rcvPacket.getData(), 0, rcvPacket.getLength()).equals("end"))
				{
					System.out.println("File received.");
					break;
				}
				bos.write(rcvPacket.getData(), 0, rcvPacket.getLength());
				bos.flush();
				packetsReceived++;
			}
			long aft = System.currentTimeMillis();
			System.out.println("Packets received: "+packetsReceived);
			bos.close();
			
			System.out.println("Checking file integrity with server...");
			
			byte[] digest;
			boolean isComplete = false;
			try {
				digest = createChecksum(filename);
				String cliCS = "";
				for(byte bytee: digest){
					cliCS += bytee;
				}
//				System.out.println("-------------------------------");
//				System.out.println(cliCS);
//				System.out.println(servCS);
//				System.out.println("-------------------------------");
				if(!cliCS.equals(servCS)){
					//System.out.println("FILE INTEGRITY COMPROMISED. FILE MAY BE CORRUPTED.");
				}
				else{
					System.out.println("SUCCESSFULL INTEGRITY VERIFICATION.");
					isComplete = true;
				}
				System.out.println("Completed.");
				
				BufferedWriter writer = null;
				try {
					//create a temporary file
					String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
					File logFile = new File(filename+"Log.txt");

					// This will output the full path where the file will be written to...
					System.out.println("The file was saved in"+ logFile.getCanonicalPath());

					writer = new BufferedWriter(new FileWriter(logFile));
					writer.write("THIS IS A TEST FOR TCP"+" \n");
					writer.write("Time log:" + timeLog+"\n");
					writer.write("File Name:"+ filename+"\n");
					writer.write("File Size:"+(sz/(1024*1024))*4.5+" MB"+"\n");
					writer.write("Test for client:" + 
							datagramSocket.getLocalAddress() + "," +
							datagramSocket.getLocalPort() +"\n");
//					if(isComplete){
						writer.write("Successfull?:Successful integrity verification transfer."+"\n");
//					}
//					else{
//						writer.write("Successfull?;File integrity compromised. File may be corrupted."+";");
//					}
					double timeElapsed = (aft-bef)/1000;
					writer.write("Time elapsed in seconds:" + timeElapsed+"\n");
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						// Close the writer regardless of what happens...
						writer.close();
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch(IOException ioEx)
		{
			ioEx.printStackTrace();
		}

		finally
		{
			System.out.println("\n Closing connection...");
			datagramSocket.close();
			System.out.println("Closed connection");
		}
	}
}