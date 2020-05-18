package gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Assets {
	
	private static final String spritesheetFileName = "res/sprites/spritesheet.png";
	private static final int size = 12;
	public static BufferedImage[] pacmanUp, pacmanRight, pacmanDown, pacmanLeft, pacmanDead;
	public static BufferedImage[] alphabets, numbers;
	public static BufferedImage[] blinkyUp, blinkyRight, blinkyDown, blinkyLeft;
	public static BufferedImage[] pinkyUp, pinkyRight, pinkyDown, pinkyLeft;
	public static BufferedImage[] inkyUp, inkyRight, inkyDown, inkyLeft;
	public static BufferedImage[] clydeUp, clydeRight, clydeDown, clydeLeft;
	public static BufferedImage[] frightened, frightenedFade, deadUp, deadDown, deadLeft, deadRight;
	public static BufferedImage map;
	
	public static void init() {
		pacmanUp = new BufferedImage[3];
		pacmanRight = new BufferedImage[3];
		pacmanDown = new BufferedImage[3];
		pacmanLeft = new BufferedImage[3];
		
		pacmanDead = new BufferedImage[11];
		
		blinkyUp = new BufferedImage[2];
		blinkyRight = new BufferedImage[2];
		blinkyDown= new BufferedImage[2];
		blinkyLeft = new BufferedImage[2];
		
		pinkyUp = new BufferedImage[2];
		pinkyRight = new BufferedImage[2];
		pinkyDown = new BufferedImage[2];
		pinkyLeft = new BufferedImage[2];
		
		inkyUp = new BufferedImage[2];
		inkyRight = new BufferedImage[2];
		inkyDown = new BufferedImage[2];
		inkyLeft = new BufferedImage[2];
		
		clydeUp = new BufferedImage[2];
		clydeRight = new BufferedImage[2];
		clydeDown = new BufferedImage[2];
		clydeLeft = new BufferedImage[2];
		
		frightened = new BufferedImage[2];
		frightenedFade = new BufferedImage[4];
		
		deadUp = new BufferedImage[2];
		deadRight = new BufferedImage[2];
		deadDown = new BufferedImage[2];
		deadLeft = new BufferedImage[2];
		
		alphabets = new BufferedImage[26];
		numbers = new BufferedImage[10];
		
		try {
			BufferedImage spritesheet = ImageIO.read(new File(spritesheetFileName));
			
			BufferedImage closed = spritesheet.getSubimage(0, size*14, size*2, size*2);
			pacmanUp[0] = closed;
			pacmanRight[0] = closed;
			pacmanDown[0] = closed;
			pacmanLeft[0] = closed;

			pacmanUp[1] = spritesheet.getSubimage(size*2, 6*size, size*2, size*2);
			pacmanUp[2] = spritesheet.getSubimage(size*6, 6*size, size*2, size*2);
			
			pacmanRight[1] = spritesheet.getSubimage(size*8, 6*size, size*2, size*2);
			pacmanRight[2] = spritesheet.getSubimage(size*12, 6*size, size*2, size*2);
			
			pacmanDown[1] = spritesheet.getSubimage(size*10, 6*size, size*2, size*2);
			pacmanDown[2] = spritesheet.getSubimage(size*14, 6*size, size*2, size*2);
			
			pacmanLeft[1] = spritesheet.getSubimage(size*0, 6*size, size*2, size*2);
			pacmanLeft[2] = spritesheet.getSubimage(size*4, 6*size, size*2, size*2);
			
			blinkyUp[0] = spritesheet.getSubimage(12*size, 12*size, size*2, size*2);
			blinkyUp[1] = spritesheet.getSubimage(14*size, 12*size, size*2, size*2);
			blinkyRight[0] = spritesheet.getSubimage(0*size, 12*size, size*2, size*2);
			blinkyRight[1] = spritesheet.getSubimage(2*size, 12*size, size*2, size*2);
			blinkyDown[0] = spritesheet.getSubimage(4*size, 12*size, size*2, size*2);
			blinkyDown[1] = spritesheet.getSubimage(6*size, 12*size, size*2, size*2);
			blinkyLeft[0] = spritesheet.getSubimage(8*size, 12*size, size*2, size*2);
			blinkyLeft[1] = spritesheet.getSubimage(10*size, 12*size, size*2, size*2);
			
			pinkyUp[0] = spritesheet.getSubimage(12*size, 16*size, size*2, size*2);
			pinkyUp[1] = spritesheet.getSubimage(14*size, 16*size, size*2, size*2);
			pinkyRight[0] = spritesheet.getSubimage(0*size, 16*size, size*2, size*2);
			pinkyRight[1] = spritesheet.getSubimage(2*size, 16*size, size*2, size*2);
			pinkyDown[0] = spritesheet.getSubimage(4*size, 16*size, size*2, size*2);
			pinkyDown[1] = spritesheet.getSubimage(6*size, 16*size, size*2, size*2);
			pinkyLeft[0] = spritesheet.getSubimage(8*size, 16*size, size*2, size*2);
			pinkyLeft[1] = spritesheet.getSubimage(10*size, 16*size, size*2, size*2);
			
			inkyUp[0] = spritesheet.getSubimage(28*size, 16*size, size*2, size*2);
			inkyUp[1] = spritesheet.getSubimage(30*size, 16*size, size*2, size*2);
			inkyRight[0] = spritesheet.getSubimage(16*size, 16*size, size*2, size*2);
			inkyRight[1] = spritesheet.getSubimage(18*size, 16*size, size*2, size*2);
			inkyDown[0] = spritesheet.getSubimage(20*size, 16*size, size*2, size*2);
			inkyDown[1] = spritesheet.getSubimage(22*size, 16*size, size*2, size*2);
			inkyLeft[0] = spritesheet.getSubimage(24*size, 16*size, size*2, size*2);
			inkyLeft[1] = spritesheet.getSubimage(26*size, 16*size, size*2, size*2);
			
			clydeUp[0] = spritesheet.getSubimage(12*size, 18*size, size*2, size*2);
			clydeUp[1] = spritesheet.getSubimage(14*size, 18*size, size*2, size*2);
			clydeRight[0] = spritesheet.getSubimage(0*size, 18*size, size*2, size*2);
			clydeRight[1] = spritesheet.getSubimage(2*size, 18*size, size*2, size*2);
			clydeDown[0] = spritesheet.getSubimage(4*size, 18*size, size*2, size*2);
			clydeDown[1] = spritesheet.getSubimage(6*size, 18*size, size*2, size*2);
			clydeLeft[0] = spritesheet.getSubimage(8*size, 18*size, size*2, size*2);
			clydeLeft[1] = spritesheet.getSubimage(10*size, 18*size, size*2, size*2);
			

			int startX = 8*size;
			int startY = 14*size;
			for(int i = 0; i<pacmanDead.length; i++) {
				pacmanDead[i] = spritesheet.getSubimage(startX+(i*2*size), startY, size*2, size*2);
			}
			
			startX = 12*size;
			startY = 8*size;
			for(int i = 0; i<frightenedFade.length; i++) {
				frightenedFade[i] = spritesheet.getSubimage(startX+(i*2*size), startY, size*2, size*2);
			}
			
			startX = 16*size;
			startY = 8*size;
			for(int i = 0; i<frightened.length; i++) {
				frightened[i] = spritesheet.getSubimage(startX+(i*2*size), startY, size*2, size*2);
			}
			
			deadUp[0] = spritesheet.getSubimage(28*size, 18*size, size*2, size*2);
			deadUp[1] = spritesheet.getSubimage(30*size, 18*size, size*2, size*2);
			deadRight[0] = spritesheet.getSubimage(16*size, 18*size, size*2, size*2);
			deadRight[1] = spritesheet.getSubimage(18*size, 18*size, size*2, size*2);
			deadDown[0] = spritesheet.getSubimage(20*size, 18*size, size*2, size*2);
			deadDown[1] = spritesheet.getSubimage(22*size, 18*size, size*2, size*2);
			deadLeft[0] = spritesheet.getSubimage(24*size, 18*size, size*2, size*2);
			deadLeft[1] = spritesheet.getSubimage(26*size, 18*size, size*2, size*2);		
			
			startX = size;
			startY = size*2;
			for(int i = 0; i<alphabets.length; i++) {
				alphabets[i] = spritesheet.getSubimage(startX+(i*size), startY, size, size);
			}
			
			for(int i = 0; i<numbers.length; i++) {
				numbers[i] = spritesheet.getSubimage(i*size, 0, size, size);
			}
			
			map = ImageIO.read(new File("res/maps/map.jpg"));
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
