package gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;

public class HUD {
	
	private Player player;
	private BufferedImage live = Assets.pacmanLeft[1];
	private ArrayList<BufferedImage> highscoreLabel = new ArrayList<BufferedImage>();
	private static final int SIZE = 16;
	
	public HUD(Player player) {
		this.player = player;
		highscoreLabel.add(Assets.alphabets['h'-'a']);
		highscoreLabel.add(Assets.alphabets['i'-'a']);
		highscoreLabel.add(Assets.alphabets['g'-'a']);
		highscoreLabel.add(Assets.alphabets['h'-'a']);
		highscoreLabel.add(Assets.alphabets['s'-'a']);
		highscoreLabel.add(Assets.alphabets['c'-'a']);
		highscoreLabel.add(Assets.alphabets['o'-'a']);
		highscoreLabel.add(Assets.alphabets['r'-'a']);
		highscoreLabel.add(Assets.alphabets['e'-'a']);
	}
	
	public void render(Graphics g) {
		int startX = 10*SIZE;
		int startY = 0;
		for(int i = 0; i<highscoreLabel.size(); i++) {
			g.drawImage(highscoreLabel.get(i), startX+(i*SIZE), startY+2, SIZE, SIZE, null);
		}
		
		String score = ""+player.getScore();
		startX = 0;
		startY = 1*SIZE;
		for(int i = 0; i<score.length(); i++) {
			int digit = Integer.parseInt(score.charAt(i)+"");
			g.drawImage(Assets.numbers[digit], startX+(i*SIZE), startY, SIZE, SIZE, null);
		}
		
		startX = 0;
		startY = 34*SIZE;
		for(int i = 0; i<player.getRemainingLives(); i++) {
			g.drawImage(live, startX+(i*2*SIZE), startY, SIZE*2, SIZE*2, null);
		}
	}
	
}
