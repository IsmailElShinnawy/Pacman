package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import gfx.Assets;
import maps.Map;
import maps.Node;

public class Clyde extends Ghost{

	public Clyde(int x, int y, int size, Map map, Point corner, Point otherCorner) {
		super(x, y, size, map, corner, otherCorner);
		addAnimation(Assets.clydeUp);
		addAnimation(Assets.clydeRight);
		addAnimation(Assets.clydeDown);
		addAnimation(Assets.clydeLeft);
		addAnimation(Assets.frightened);
		addAnimation(Assets.frightenedFade);
		addAnimation(Assets.deadUp);
		addAnimation(Assets.deadRight);
		addAnimation(Assets.deadDown);
		addAnimation(Assets.deadLeft);
		
		setCurrentAnimation(UP);
		setCurrentFrame(0);
	}
	
	public void render(Graphics g) {
		super.render(g);
//		for(Node n: getPath()) {
//			n.render(g, new Color(0xff, 0xa5, 0x00, 100));
//		}
	}
}
