package cliente;

import java.awt.BorderLayout;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;


import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.*;

public class Interface {
	public Interface(String PATH) throws IOException {
		JFrame frame = new JFrame();
		frame.setSize(1500,800);
		frame.setLocation(150,150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.pink);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(canvas);
		frame.add(panel);
		
		JPanel aux = new JPanel( new GridLayout(3, 3) );
		aux.add( new JLabel("Ponga la ruta de carpeta VCL ") );
		JTextField pathvcl = new JTextField(20);
		pathvcl.setText("C:\\Program Files (x86)\\VideoLAN\\VLC");	
		aux.add( pathvcl );
		
		int x = JOptionPane.showConfirmDialog(null, aux,"Config video",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(x==JOptionPane.OK_OPTION) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), pathvcl.getText());
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);

			MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
			//EmbeddedMediaPlayer emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(frame));
			EmbeddedMediaPlayerComponent empc = new EmbeddedMediaPlayerComponent();
			frame.setContentPane(empc);
			empc.getMediaPlayer().playMedia(PATH);
//			emp.setVideoSurface(mpf.newVideoSurface(canvas));
//			emp.setEnableMouseInputHandling(false);
//			emp.setEnableKeyInputHandling(false);
//
//
//			emp.prepareMedia(path);
//			emp.play();
		}
	}}



