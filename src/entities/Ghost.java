package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import maps.Map;
import maps.Node;

public abstract class Ghost extends Entity {

	private final ArrayList<BufferedImage[]> animations = new ArrayList<BufferedImage[]>();
	private int currentAnimation;
	private int currentFrame;

	private Point target;
	private Point corner;
	private Point otherCorner;
	private static final Point MONSTER_PANE = new Point(13, 11);
	private Node current;
	private ArrayList<Node> openList;
	private ArrayList<Node> closedList;
	private Stack<Node> path;
	private boolean found;

	private boolean chase = false;
	private boolean scatter = true;
	private boolean frightened = false;

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public static final int FRIGHTENED = 4;
	public static final int FRIGHTENED_FADE = 5;

	public static final int DEAD_UP = 6;
	public static final int DEAD_RIGHT = 7;
	public static final int DEAD_DOWN = 8;
	public static final int DEAD_LEFT = 9;

	public Ghost(int x, int y, int size, Map map, Point corner, Point otherCorner) {
		super(x, y, size, map);
		this.corner = corner;
		this.otherCorner = otherCorner;

		setVelocity(2);
	}

	@Override
	public void tick() {
		setTicks(getTicks() + 1);
		if (getTicks() == Entity.FRAMES_BEFORE_UPDATE) {
			currentFrame++;
			if (currentFrame == animations.get(currentAnimation).length)
				currentFrame = 0;
		}

		int nextMove = 4;

		resetForPathFinding();

		if (isDead()) {
			if (current.equals(getMap().getNodes()[MONSTER_PANE.y][MONSTER_PANE.x])) {
				revive();
			} else {
				astar(target);
			}
		} else if (frightened) {
			astar(target);
		} else if (chase) {
			astar(target);
		} else if (scatter) {
			if (current.equals(getMap().getNodes()[corner.y][corner.x])) {
				Point tmp = (Point) (corner.clone());
				corner = (Point) (otherCorner.clone());
				otherCorner = tmp;
			}
			astar(corner);
		}
		if (found) {
			constructPath();
			path.pop();
			if (!path.isEmpty()) {
				Node next = path.peek();
				if (next.getX() > getX() / getSize()) {
					nextMove = 2;
				} else if (next.getX() < getX() / getSize()) {
					nextMove = 3;
				} else if (next.getY() > (getY() / getSize()) - getMap().getVerticalOffset()) {
					nextMove = 1;
				} else if (next.getY() < (getY() / getSize()) - getMap().getVerticalOffset()) {
					nextMove = 0;
				}
			}
		}

		getNextMove(nextMove);
		calculateCorners();

		if (getVX() > 0) {
			if (isDead()) {
				currentAnimation = DEAD_RIGHT;
			} else if (!frightened)
				currentAnimation = RIGHT;
		} else if (getVX() < 0) {
			if (isDead()) {
				currentAnimation = DEAD_LEFT;
			} else if (!frightened)
				currentAnimation = LEFT;
		} else if (getVY() > 0) {
			if (isDead()) {
				currentAnimation = DEAD_DOWN;
			} else if (!frightened)
				currentAnimation = DOWN;
		} else if (getVY() < 0) {
			if (isDead()) {
				currentAnimation = DEAD_UP;
			} else if (!frightened)
				currentAnimation = UP;
		}
		if (getTicks() == Entity.FRAMES_BEFORE_UPDATE) {
			setTicks(0);
		}
	}

	@Override
	public void render(Graphics g) {
		if (currentFrame < animations.get(currentAnimation).length)
			g.drawImage(animations.get(currentAnimation)[currentFrame], (int) getX() - getSize() / 2,
					(int) getY() - getSize() / 2, getSize() * 2, getSize() * 2, null);

//		for (Node n : openList) {
//			n.render(g, Color.GREEN);
//		}
//		for (Node n : closedList) {
//			n.render(g, new Color(0, 0, 255, 100));
//		}

	}

	public void getNextMove(int nextMove) {
		if (nextMove == 0) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(-getVelocity());
			setVX(0);
		} else if (nextMove == 1) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(getVelocity());
			setVX(0);
		} else if (nextMove == 2) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(0);
			setVX(getVelocity());
		} else if (nextMove == 3) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(0);
			setVX(-getVelocity());
		}

	}

	public void resetForPathFinding() {
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		path = new Stack<Node>();
		found = false;
		getMap().resetNodesForAStar();

		int x = (int) (getX() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize();
		int y = (int) (getY() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize()
				- getMap().getVerticalOffset();
		current = getMap().getNodes()[y][x];
		openList.add(current);
	}

	public void astar(Point dest) {
		while (!openList.isEmpty()) {
			int winnerIndex = 0;
			for (int i = 0; i < openList.size(); i++) {
				if (openList.get(i).getFScore() < openList.get(winnerIndex).getFScore()) {
					winnerIndex = i;
				}
			}

			current = openList.remove(winnerIndex);
			if (current.equals(getMap().getNodes()[dest.y][dest.x])) {
				found = true;
				break;
			}

			closedList.add(current);
			for (Node n : current.getNeighbours()) {
				if (n == null)
					continue;
				if (!closedList.contains(n)) {
					double tmpGScore = 1 + current.getGScore();
					if (openList.contains(n)) {
						if (tmpGScore < n.getGScore()) {
							n.setGScore(tmpGScore);
							n.setHScore(getEuclideanDistance(n.getX(), n.getY(), dest.x, dest.y));
							n.setFScore(n.getGScore() + n.getHScore());
							n.setPrevious(current);
						}
					} else {
						n.setGScore(tmpGScore);
						n.setHScore(getEuclideanDistance(n.getX(), n.getY(), dest.x, dest.y));
						n.setFScore(n.getGScore() + n.getHScore());
						n.setPrevious(current);
						openList.add(n);
					}
				}
			}
		}

	}

	public Point dfs(int steps) {
		Stack<Node> stack = new Stack<Node>();
		Node ghostNode = getMap().getNodes()[((int) getY() / getSize()) - getMap().getVerticalOffset()][(int) getX()
				/ getSize()];
		ghostNode.setVisited(true);
		if (getVX() > 0) {
			ghostNode.setLastDir(1);
		} else if (getVX() < 0) {
			ghostNode.setLastDir(3);
		} else if (getVY() > 0) {
			ghostNode.setLastDir(2);
		} else if (getVY() < 0) {
			ghostNode.setLastDir(0);
		}
		stack.push(ghostNode);
		Node current = null;
		while (!stack.isEmpty()) {
			current = stack.pop();
			if (steps-- == 0) {
				break;
			}
			Integer[] perm = null;
			if (current.getLastDir() == 0) {
				perm = new Integer[] { 0, 1, 3 };
			} else if (current.getLastDir() == 1) {
				perm = new Integer[] { 0, 1, 2 };
			} else if (current.getLastDir() == 2) {
				perm = new Integer[] { 1, 2, 3 };
			} else if (current.getLastDir() == 3) {
				perm = new Integer[] { 0, 2, 3 };
			}
			Collections.shuffle(Arrays.asList(perm));
			for (Integer i : perm) {
				addToStack(stack, current.getNeighbours()[i], i, current);
			}
		}
		Point res = new Point(current.getX(), current.getY());
		return res;
	}

	public void addToStack(Stack<Node> stack, Node n, int lastDir, Node previous) {
		if (n == null || n.isVisited())
			return;
		n.setVisited(true);
		n.setLastDir(lastDir);
		n.setPrevious(previous);
		stack.push(n);
	}

	public void constructPath() {
		if (found) {
			path = new Stack<Node>();
			while (current != null) {
				path.push(current);
				current = current.getPrevious();
			}
		}
	}

	public static double getEuclideanDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public void reverseDirection() {
		if (getVX() > 0) {
			setVX(-getVelocity());
		} else if (getVX() < 0) {
			setVX(getVelocity());
		} else if (getVY() > 0) {
			setVY(-getVelocity());
		} else {
			setVY(getVelocity());
		}
	}

	public void kill() {
		setDead(true);
		setFrightened(false);
		setCurrentFrame(0);
		if (getVX() > 0) {
			setCurrentAnimation(DEAD_RIGHT);
		} else if (getVX() < 0) {
			setCurrentAnimation(DEAD_LEFT);
		} else if (getVY() > 0) {
			setCurrentAnimation(DEAD_DOWN);
		} else {
			setCurrentAnimation(DEAD_UP);
		}
		setTarget(MONSTER_PANE);
	}

	public void revive() {
		setDead(false);
		setVX(0);
		setVY(0);
		setCurrentFrame(0);
		setCurrentAnimation(LEFT);
		setFrightened(false);
	}

	public void addAnimation(BufferedImage[] animation) {
		animations.add(animation);
	}

	public void setCurrentAnimation(int currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void setTarget(Point target) {
		this.target = target;
	}

	public Stack<Node> getPath() {
		return path;
	}

	public boolean isFrightened() {
		return frightened;
	}

	public boolean isChase() {
		return chase;
	}

	public boolean isScatter() {
		return scatter;
	}

	public void setScatter(boolean scatter) {
		this.scatter = scatter;
	}

	public void setFrightened(boolean frightened) {
		this.frightened = frightened;
	}

	public void setChase(boolean chase) {
		this.chase = chase;
	}

	public Point getCorner() {
		return corner;
	}
}
