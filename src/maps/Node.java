package maps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Node {

	// private BufferedImage img;
	private int size;
	private boolean wall;
	private int x;
	private int y;
	private boolean coin;
	private boolean energizer;

	private static final int coinSize = 2;
	private static final int energizerSize = 16;

	private double fScore = 0;
	private double gScore = 0;
	private double hScore = 0;
	private Node previous;
	private Node[] neighbours = new Node[4];

	private boolean visited = false;
	private int lastDir = -1;

	private final int verticalOffset = 3;

	public Node(int x, int y, int size, boolean wall, int verticalOffset, boolean coin, boolean energizer) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.wall = wall;
		this.coin = coin;
		this.energizer = energizer;
	}

	public void setNeighbours(Node[][] nodes) {
		if (y > 0) {
			if (!nodes[y - 1][x].isWall())
				neighbours[0] = nodes[y - 1][x];
		}
		if (y < nodes.length - 1) {
			if (!nodes[y + 1][x].isWall())
				neighbours[2] = nodes[y + 1][x];
		}
		if (x > 0) {
			if (!nodes[y][x - 1].isWall())
				neighbours[3] = nodes[y][x - 1];
		}
		if (x < nodes[y].length - 1) {
			if (!nodes[y][x + 1].isWall())
				neighbours[1] = nodes[y][x + 1];
		}

		if (x == 0) {
			if (!isWall()) {
				neighbours[2] = nodes[y][nodes[y - verticalOffset].length - 1];
			}
		}

		if (x == nodes[y].length - 1) {
			if (!isWall()) {
				neighbours[3] = nodes[y][0];
			}
		}
	}

	public Node[] getNeighbours() {
		return neighbours;
	}

	public void render(Graphics g) {
		// if (wall) {
		// g.setColor(Color.BLACK);
		// g.fillRect(getRect().x, getRect().y, getRect().width, getRect().height);
		// } else {
		// g.setColor(Color.WHITE);
		// g.fillRect(getRect().x, getRect().y, getRect().width, getRect().height);
		// g.setColor(Color.BLACK);
		// g.drawRect(getRect().x, getRect().y, getRect().width, getRect().height);
		// }
		if (coin) {
			g.setColor(new Color(0xFF, 0xFD, 0xD0));
			g.fillRect((x * size) + (size / 2) - coinSize, ((y + verticalOffset) * size) + (size / 2) - coinSize,
					coinSize, coinSize);
		} else if (energizer) {
			g.setColor(new Color(0xFF, 0xE5, 0xB4));
			g.fillOval(x * size, (y + verticalOffset) * size, energizerSize, energizerSize);
		}
	}
	
	public void render(Graphics g, boolean renderEnergizer) {
		if (coin) {
			g.setColor(Color.YELLOW);
			g.fillRect((x * size) + (size / 2) - coinSize, ((y + verticalOffset) * size) + (size / 2) - coinSize,
					coinSize, coinSize);
		} else if (energizer && renderEnergizer) {
			g.setColor(Color.WHITE);
			g.fillOval(x * size, (y + verticalOffset) * size, energizerSize, energizerSize);
		}
	}
	
	public void render(Graphics g, Color color) {
		render(g);
		g.setColor(color);
		g.fillRect(x * size, (y + verticalOffset) * size, size, size);
	}
	
	public void resetForAStar() {
		fScore = 0;
		gScore = 0;
		hScore = 0;
		previous = null;
	}

	public void resetForDFS() {
		visited = false;
		previous = null;
		 lastDir = -1;
	}

	public boolean equals(Node other) {
		return x == other.x && y == other.y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return this.size;
	}

	public boolean isWall() {
		return this.wall;
	}

	public boolean isCoin() {
		return coin;
	}

	public void setCoin(boolean coin) {
		this.coin = coin;
	}

	public void setFScore(double fScore) {
		this.fScore = fScore;
	}

	public double getFScore() {
		return fScore;
	}

	public void setGScore(double gScore) {
		this.gScore = gScore;
	}

	public double getGScore() {
		return gScore;
	}

	public void setHScore(double hScore) {
		this.hScore = hScore;
	}

	public double getHScore() {
		return hScore;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public Node getPrevious() {
		return previous;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setLastDir(int lastDir) {
		this.lastDir = lastDir;
	}

	public int getLastDir() {
		return lastDir;
	}

	public boolean isEnergizer() {
		return energizer;
	}

	public void setEnergizer(boolean energizer) {
		this.energizer = energizer;
	}

	public Rectangle getRect() {
		// return new Rectangle((x * size)+size/2-(size-1)/2, ((y + verticalOffset) *
		// size)+size/2-(size-1)/2, size, size);
		return new Rectangle((x * size), ((y + verticalOffset) * size), size, size);
	}

	public String toString() {
		return x + " " + y;
	}

}
