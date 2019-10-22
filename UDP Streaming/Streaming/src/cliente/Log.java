package cliente;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Log  extends JFrame {
	
	
	static JTextField username;
	static JTextField pass;

	public Log() throws FileNotFoundException
	{
		createComponents();
		setSize(350, 200);
		setTitle("Login");

	}

	private void createComponents() throws FileNotFoundException
	{
		username = new JTextField(30);
		username.setBounds(100, 10, 160, 25);

		pass = new JTextField(30);
		pass.setBounds(100, 40, 160, 25);


		JLabel userNameLabel = new JLabel("User");
		userNameLabel.setBounds(10, 10, 80, 25);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);


		JButton loginButton = new JButton("Login");
		loginButton.setBounds(180, 80, 80, 25);

		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(10, 80, 80, 25);


		JPanel panel = new JPanel();
		panel.setLayout(null);

		panel.add(userNameLabel);
		panel.add(username);
		panel.add(passwordLabel);
		panel.add(pass);
		panel.add(loginButton);
		panel.add(exitButton);
		add(panel);

		ActionListener exitListener = new ClickListener1();
		ActionListener loginListener = new ClickListener2();
		exitButton.addActionListener(exitListener);
		loginButton.addActionListener(loginListener);
	}
	public class ClickListener1 implements ActionListener 
	{

		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}
	public class ClickListener2 implements ActionListener 
	{

		public void actionPerformed(ActionEvent event)
		{

			String userNameInput = username.getText();
			String passwordInput = pass.getText();
//
//			try {
//				Scanner in = new Scanner(new File("./data/login.txt"));
//				while (in.hasNextLine())
//				{
//					String s = in.nextLine();  
//					String[] sArray = s.split(",");
					
					if (userNameInput.equals("Mariana") && passwordInput.equals("1234"))
//					if (userNameInput.equals(sArray[0]) && passwordInput.equals(sArray[1]))
					{

						JOptionPane.showMessageDialog(null,
								"Login Successful", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						
//					Cliente c = new Cliente(passwordInput, defaultCloseOperation);
	
					}
					else if (userNameInput.isEmpty() || passwordInput.isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"Missing data", "Warning",
								JOptionPane.WARNING_MESSAGE);

					}
//					else if (!userNameInput.equals(sArray[0]) || !passwordInput.equals(sArray[1])) {
					else if (!userNameInput.equals("Mariana") || !passwordInput.equals("1234")) {	
					JOptionPane.showMessageDialog(null,
								"Invalid Username / Password ", "Error",
								JOptionPane.ERROR_MESSAGE);

					}
	
				}
	}
}

//				in.close();

//			} catch (FileNotFoundException e) {
//				JOptionPane.showMessageDialog(null,
//						"User Database Not Found", "Error",
//						JOptionPane.ERROR_MESSAGE);
//			}

//		}
	
//}


