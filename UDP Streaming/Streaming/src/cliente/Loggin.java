package cliente;
import java.io.FileNotFoundException;

import javax.swing.JFrame;


public class Loggin {
	
	public static void main(String[] args) throws FileNotFoundException
	{
		JFrame frame = new Log();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}

}
