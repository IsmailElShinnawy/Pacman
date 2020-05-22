package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import gfx.Assets;
import maps.Map;
import maps.Node;

public class Clyde extends Ghost{
	
	private final int SHY_DISTANCE = 8;
	
	private boolean shy = false;
	private int shyTime = 1500;
	private int shyTimer = 0;
	
	private long now;
	private long lastTime = System.currentTimeMillis();

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

	public void tick() {
		if(shy) {
			int x = (int) (getX() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize();
			int y = (int) (getY() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize()
					- getMap().getVerticalOffset();
			Node current = getMap().getNodes()[y][x];
			if (current.equals(getMap().getNodes()[getCorner().y][getCorner().x])) {
				Point tmp = (Point) (getCorner().clone());
				setCorner((Point) (getOtherCorner().clone()));
				setOtherCorner(tmp);
			}
			setTarget(getCorner());
			now = System.currentTimeMillis();
			shyTimer+=(now-lastTime);
			if(shyTimer>=shyTime) {
				shyTimer = 0;
				shy = false;
			}
			lastTime = System.currentTimeMillis();
		}
		super.tick();
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		if(isVisPath()) {
			for(Node n: getPath()) {
				n.render(g, new Color(0xff, 0xa5, 0x00, 100));
			}
		}
	}
	
	public void kill() {
		super.kill();
		shy = false;
		shyTimer = 0;
	}
	
	public void setShy(boolean shy) {
		this.shy = shy;
	}
	
	public boolean isShy() {
		return shy;
	}
	
	public int getShyDistance() {
		return SHY_DISTANCE;
	}
}
