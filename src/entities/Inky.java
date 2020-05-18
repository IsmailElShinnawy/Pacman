package entities;

import java.awt.Point;

import gfx.Assets;
import maps.Map;

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

}
