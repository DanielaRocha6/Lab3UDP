package cliente;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.*;

public class Conexion {
	
	public Conexion() throws IOException {

		JPanel p = new JPanel( new GridLayout(6, 4) );
		p.add( new JLabel("IP del servidor al que quiere conectarse") );
		JTextField ip = new JTextField(20);
		p.add( ip );
		p.add( new JLabel("Puerto del servidor") );
		JTextField port = new JTextField(30);
		port.setText("8000");
		p.add( port );
		p.add( new JLabel("# de usuarios") );
		JTextField numU = new JTextField(20);
		numU.setText("-1");
		p.add( numU );
		p.add( new JLabel("file a enviar") );
		JRadioButton b1=new JRadioButton("json",true);
		JRadioButton b2=new JRadioButton("video",false);
		ButtonGroup archivo = new ButtonGroup();
		archivo.add(b1);
		archivo.add(b2);
		JPanel panel2 = new JPanel( new GridLayout(1, 2) );
		panel2.add(b1);
		panel2.add(b2);
		p.add( panel2 );

		int result = JOptionPane.showConfirmDialog(null, p,"Conexion",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {

			Cliente cliente = new Cliente(ip.getText(), Integer.parseInt(port.getText()));
			int rta = b1.isSelected() ? 1:2;
			cliente.proceso(Integer.parseInt(numU.getText()), rta);
			if(cliente.getEstado())
				JOptionPane.showConfirmDialog(null, "Se establecio la conexion", "Estado de la conexion", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showConfirmDialog(null, "No se conecto "+ip.getText()+": "+port.getText(), "Estado de la conexion", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

		}
	}
}



