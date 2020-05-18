package main;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class GameView {
	
	private JFrame frame;
	private Canvas canvas;
	
	public GameView() {
		frame = new JFrame();
		frame.setTitle("Pacman");
		frame.setSize(new Dimension(448, 576)); //original dim is 224x288 this is scaled up by 2
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(448, 576));
		canvas.setMaximumSize(new Dimension(448, 576));
		canvas.setMinimumSize(new Dimension(448, 576));
		
		frame.add(canvas);
		frame.pack();
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
}
