package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import gfx.Assets;
import maps.Map;
import maps.Node;

public class Inky extends Ghost{

	public Inky(int x, int y, int size, Map map, Point corner, Point otherCorner) {
		super(x, y, size, map, corner, otherCorner);
		addAnimation(Assets.inkyUp);
		addAnimation(Assets.inkyRight);
		addAnimation(Assets.inkyDown);
		addAnimation(Assets.inkyLeft);
		addAnimation(Assets.frightened);
		addAnimation(Assets.frightenedFade);
		addAnimation(Assets.deadUp);
		addAnimation(Assets.deadRight);
		addAnimation(Assets.deadDown);
		addAnimation(Assets.deadLeft);
		
		setCurrentAnimation(DOWN);
		setCurrentFrame(0);
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		if(isVisPath()) {
			for(Node n: getPath()) {
				n.render(g, new Color(100, 0, 150, 100));
			}
		}
	}

}
