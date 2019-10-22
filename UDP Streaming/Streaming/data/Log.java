package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
	
	private double tamanioArchivo;
	
	private long tiempoTransferencia;
	
	private boolean exitoso;
	
	private int numPaquetesEnviados;
	
	private int numPaquetesRecibidos;
	
	private int bytesTransmitidos;
	
	private int bytesRecibidos;
	
	private int numero;
	
    public Log()
    {
    	tamanioArchivo = 0.0;
      	tiempoTransferencia = 0;
    	exitoso = false;
    	numPaquetesEnviados = 0;
    	numPaquetesRecibidos = 0;
    	bytesTransmitidos = 0;
    	bytesRecibidos = 0;
    	numero = -1;
    }
    
    public void escribirArchivo()
    {
    	try {
            String ruta = "./data/prueba" + numero + ".txt";
            String fechaActual = obtenerFechActual();
            String contenido = 
            		  "Fecha actual: "+ fechaActual + "\n"
            		+ "Nombre del archivo: Prueba"+ numero + "\n"
            		+ "Tamaño del archivo: " + tamanioArchivo + " MiB\n"
            		+ "Cliente con id: #" + numero + "\n";
            if (exitoso) {
				contenido += 
					  "La entrega del archivo fue exitosa"+ "\n";
			}
            else
            {
            	contenido += 
  					  "La entrega del archivo no fue exitosa"+ "\n";
            	
            }
            contenido +=
            		  "Tiempo de transferencia: " + tiempoTransferencia + " ms\n"
			        + "Numero de paquetes enviados: " + numPaquetesEnviados + " paquetes\n"
			        + "Numero de paquetes recibidos: " + numPaquetesRecibidos + " paquetes\n"
			        + "Numero de paquetes transmitidos: " + numPaquetesEnviados + " paquetes\n"
			        + "Numero de bytes transmitidos: " + bytesTransmitidos + " bytes\n"
			        + "Numero de bytes enviados: " + bytesRecibidos + " bytes"
            		;
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void setTamanioArchivo(double tamanioArchivo) {
		this.tamanioArchivo = tamanioArchivo;
	}

	public void setPrimerTiempo(int primerTiempo) {
		this.tiempoTransferencia = primerTiempo;
	}

	public void setExitoso(boolean exitoso) {
		this.exitoso = exitoso;
	}

	public void setTiempoTransferencia(long tiempoTransferencia) {
		this.tiempoTransferencia = tiempoTransferencia;
	}

	public void setNumPaquetesEnviados(int numPaquetesEnviados) {
		this.numPaquetesEnviados = numPaquetesEnviados;
	}

	public void setNumPaquetesRecibidos(int numPaquetesRecibidos) {
		this.numPaquetesRecibidos = numPaquetesRecibidos;
	}

	public void setBytesTransmitidos(int bytesTransmitidos) {
		this.bytesTransmitidos = bytesTransmitidos;
	}

	public void setBytesRecibidos(int bytesRecibidos) {
		this.bytesRecibidos = bytesRecibidos;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String obtenerFechActual()
    {
    	String formato = "yyyy-MM-dd HH:mm:ss";
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
		LocalDateTime ahora = LocalDateTime.now();
		return formateador.format(ahora);
    }
}
