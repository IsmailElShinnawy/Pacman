package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import maps.Map;

public abstract class Entity {

	private int x;
	private int y;

	private int vx;
	private int vy;
	private int velocity;
	private int pvx;
	private int pvy;

	private int padding = 1;

	private boolean dead;
	private BufferedImage img;
	private int size;
	private Map map;

	private int ticks = 0;

	private boolean topLeft, topRight, bottomRight, bottomLeft;

	public static final int FRAMES_BEFORE_UPDATE = 3;

	public Entity(int x, int y, int size, Map map) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.map = map;
	}

	public void calculateCorners() {
		int xTmp = x + vx;
		int yTmp = y + vy;

		int xPosLeft = (int) (xTmp + size / 2 - ((size / 2) - padding)) / size;
		int yPosTop = (int) (yTmp + size / 2 - ((size / 2) - padding)) / size - map.getVerticalOffset();

		int xPosRight = (int) (xTmp + size / 2 + ((size / 2) - padding)) / size;
		int yPosBottom = (int) (yTmp + size / 2 + ((size / 2) - padding)) / size - map.getVerticalOffset();

		if (xPosLeft < 0) xPosLeft = 0;
		if (xPosRight < 0) xPosRight = 0;
		if (xPosRight >= map.getNodes()[0].length)
			xPosRight = map.getNodes()[0].length - 1;
		if (xPosLeft >= map.getNodes()[0].length)
			xPosLeft = map.getNodes()[0].length - 1;

		if (vx < 0) {
			topLeft = map.getNodes()[yPosTop][xPosLeft].isWall();
			bottomLeft = map.getNodes()[yPosBottom][xPosLeft].isWall();
			if ((pvx != 0 && pvy == 0) || (pvx == 0 && pvy == 0)) {
				if (topLeft) {
					setX((xPosLeft + 1) * size);
					vx = 0;
					pvx = 0;
				} else {
					setX(xTmp);
					y = yTmp;
				}
			} else if (pvy != 0 && pvx == 0) {
				if (topLeft || bottomLeft) {
					vx = 0;
					vy = pvy;
					calculateCorners();
				} else {
					setX(xTmp);
					y = yTmp;
				}
			}
		} else if (vx > 0) {
			topRight = map.getNodes()[yPosTop][xPosRight].isWall();
			bottomRight = map.getNodes()[yPosBottom][xPosRight].isWall();

			if ((pvx != 0 && pvy == 0) || (pvx == 0 && pvy == 0)) {
				if (topRight) {
					setX((xPosLeft) * size);
					vx = 0;
					pvx = 0;
				} else {
					setX(xTmp);
					y = yTmp;
				}
			} else if (pvy != 0 && pvx == 0) {
				if (topRight || bottomRight) {
					vx = 0;
					pvx = 0;
					vy = pvy;
					calculateCorners();
				} else {
					setX(xTmp);
					y = yTmp;
				}
			}
		} else if (vy < 0) {
			topLeft = map.getNodes()[yPosTop][xPosLeft].isWall();
			topRight = map.getNodes()[yPosTop][xPosRight].isWall();

			if ((pvy != 0 && pvx == 0) || (pvx == 0 && pvy == 0)) {
				if (topLeft) {
					y = (yPosTop + 1 + map.getVerticalOffset()) * size;
					vy = 0;
					pvy = 0;
				} else {
					setX(xTmp);
					y = yTmp;
				}
			} else if (pvx != 0 && pvy == 0) {
				if (topRight || topLeft) {
					vy = 0;
					pvy = 0;
					vx = pvx;
					calculateCorners();
				} else {
					setX(xTmp);
					y = yTmp;
				}
			}
		} else if (vy > 0) {
			bottomLeft = map.getNodes()[yPosBottom][xPosLeft].isWall();
			bottomRight = map.getNodes()[yPosBottom][xPosRight].isWall();

			if ((pvy != 0 && pvx == 0) || (pvx == 0 && pvy == 0)) {
				if (bottomLeft) {
					y = (yPosTop + map.getVerticalOffset()) * size;
					vy = 0;
					pvy = 0;
				} else {
					x = xTmp;
					y = yTmp;
				}
			} else if (pvx != 0 && pvy == 0) {
				if (bottomLeft || bottomRight) {
					vy = 0;
					pvy = 0;
					vx = pvx;
					calculateCorners();
				} else {
					setX(xTmp);
					y = yTmp;
				}
			}
		}
	}

	public boolean collide(Entity other) {
		Rectangle rec = getRectangle();
		Rectangle oRec = other.getRectangle();

		return rec.intersects(oRec);

	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getVX() {
		return vx;
	}

	public int getVY() {
		return vy;
	}

	public int getPVY() {
		return pvy;
	}

	public int getPVX() {
		return pvx;
	}

	public int getVelocity() {
		return velocity;
	}

	public boolean isDead() {
		return dead;
	}

	public BufferedImage getImg() {
		return img;
	}

	public int getSize() {
		return size;
	}

	public Map getMap() {
		return map;
	}

	public int getTicks() {
		return ticks;
	}

	public int getPadding() {
		return padding;
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) (x + size / 2 - (size / 2 - padding)), (int) (y + size / 2 - (size / 2 - padding)),
				size - padding, size - padding);
	}

	public void setX(int x) {
		if (x < 0) {
			x = (map.getNodes()[0].length - 1) * size;
		} else if (x >= map.getNodes()[0].length * size) {
			x = 0;
		}
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setVX(int vx) {
		this.vx = vx;
	}

	public void setVY(int vy) {
		this.vy = vy;
	}

	public void setPVX(int pvx) {
		this.pvx = pvx;
	}

	public void setPVY(int pvy) {
		this.pvy = pvy;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
}
