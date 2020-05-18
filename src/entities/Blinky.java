package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import gfx.Assets;
import maps.Map;
import maps.Node;

public class Blinky extends Ghost{

	public Blinky(int x, int y, int size, Map map, Point corner, Point otherCorner) {
		super(x, y, size, map, corner, otherCorner);
		addAnimation(Assets.blinkyUp);
		addAnimation(Assets.blinkyRight);
		addAnimation(Assets.blinkyDown);
		addAnimation(Assets.blinkyLeft);
		addAnimation(Assets.frightened);
		addAnimation(Assets.frightenedFade);
		addAnimation(Assets.deadUp);
		addAnimation(Assets.deadRight);
		addAnimation(Assets.deadDown);
		addAnimation(Assets.deadLeft);
		setCurrentAnimation(LEFT);
		setCurrentFrame(0);
	}
	
	public void render(Graphics g) {
		super.render(g);
		
//		for (Node n : getPath()) {
//			n.render(g, new Color(255, 0, 0, 100));
//		}
	}

}
