package entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import gfx.Assets;
import gfx.HUD;
import input.KeyManager;
import maps.Map;
import maps.Node;
import sfx.AudioPlayer;
import states.GameStateManager;

public class Player extends Entity {

	private KeyManager keyManager;
	private GameStateManager gsm;
	private PlayerListener listener;
	private boolean[] keys;
	private final ArrayList<BufferedImage[]> animations = new ArrayList<BufferedImage[]>();
	private int currentAnimation;
	private int currentFrame;

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public static final int DEAD = 4;

	private int score = 0;
	private int highScore = 0;
	private int ghostEatenScore = 200;
	private int ghostEatenScoreIndex = -1;
	private int xEaten = -1, yEaten = -1;
	private int remainingLives = 3;

	private long now;
	private long lastTime;
	private long deathTime = 3000;
	private long deathTimer = 0;
	private long ghostEatenScoreTime = 1000;
	private long ghostEatenScoreTimer = 0;
	private boolean renderGhostEatenScore = false;

	private HUD hud;
	private AudioPlayer munchFood;
	private AudioPlayer death;
	private AudioPlayer eatGhost;

	public Stack<Node> dfsPath = new Stack<Node>();

	public Player(int x, int y, int size, Map map, GameStateManager gsm) {
		super(x, y, size, map);
		this.gsm = gsm;

		setVelocity(3);

		animations.add(Assets.pacmanUp);
		animations.add(Assets.pacmanRight);
		animations.add(Assets.pacmanDown);
		animations.add(Assets.pacmanLeft);
		animations.add(Assets.pacmanDead);

		currentAnimation = LEFT;
		currentFrame = 0;

		hud = new HUD(this);

		munchFood = new AudioPlayer("/sounds/pacman_chomp.wav");
		death = new AudioPlayer("/sounds/pacman_death.wav");
		eatGhost = new AudioPlayer("/sounds/pacman_eatghost.wav");
	}

	public void tick() {
		setTicks(getTicks() + 1);
		updateFrame();
		
		if(score>highScore) {
			highScore = score;
		}
		
		if (!isDead()) {
			getNextMove();
			calculateCorners();

			collideWithCoin();
			collideWithEnergizer();
			collideWithGhosts();
			
			now = System.currentTimeMillis();
			ghostEatenScoreTimer += (now-lastTime);
			if(ghostEatenScoreTimer>=ghostEatenScoreTime) {
				renderGhostEatenScore = false;
			}
			lastTime = System.currentTimeMillis();

			if (getVX() > 0) {
				currentAnimation = RIGHT;
			} else if (getVX() < 0) {
				currentAnimation = LEFT;
			} else if (getVY() > 0) {
				currentAnimation = DOWN;
			} else if (getVY() < 0) {
				currentAnimation = UP;
			}else {
				currentFrame = 1;
			}

		} else {
			now = System.currentTimeMillis();
			deathTimer += (now - lastTime);
			if (deathTimer >= deathTime) {
				deathTimer = 0;
				listener.onPlayerRevive();
			}
			lastTime = System.currentTimeMillis();
		}
		if (getTicks() == Entity.FRAMES_BEFORE_UPDATE) {
			setTicks(0);
			if(keyManager.getKeys()[KeyManager.SPACE]) {
				listener.onSpaceBarPress();
			}
		}
	}

	public void updateFrame() {
		if ((getVX() != 0 || getVY() != 0 || isDead()) && getTicks() == Entity.FRAMES_BEFORE_UPDATE) {
			currentFrame++;
			if (currentFrame == animations.get(currentAnimation).length) {
				if (!isDead()) {
					currentFrame = 0;
				}
			}
		}
	}

	public void getNextMove() {
		keys = keyManager.getKeys();
		if (keys[KeyManager.UP]) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(-getVelocity());
			setVX(0);
		} else if (keys[KeyManager.DOWN]) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(getVelocity());
			setVX(0);
		} else if (keys[KeyManager.RIGHT]) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(0);
			setVX(getVelocity());
		} else if (keys[KeyManager.LEFT]) {
			setPVY(getVY());
			setPVX(getVX());
			setVY(0);
			setVX(-getVelocity());
		}
	}

	public void render(Graphics g) {
		if (currentFrame < animations.get(currentAnimation).length)
			g.drawImage(animations.get(currentAnimation)[currentFrame], (int) getX() - getSize() / 2,
					(int) getY() - getSize() / 2, getSize() * 2, getSize() * 2, null);
		
		if(xEaten!=-1 && yEaten!=-1 && renderGhostEatenScore) {
			g.drawImage(Assets.ghostScores[ghostEatenScoreIndex], xEaten, yEaten, getSize()*2, getSize()*2, null);
		}
	
		hud.render(g);
		// g.setColor(Color.GREEN);
		// g.fillRect((int)getX(), (int)getY(), getSize(), getSize());
	}

	public void collideWithCoin() {
		int xPos = (int) (getX() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize();
		int yPos = (int) ((getY() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize()) - getMap().getVerticalOffset();

		if (xPos < 0)
			xPos = 0;
		if (yPos < 0)
			yPos = 0;
		if (xPos >= getMap().getNodes()[yPos].length) {
			xPos = getMap().getNodes()[yPos].length - 1;
		}

		if (getMap().getNodes()[yPos][xPos].isCoin()) {
			listener.onEatingDot();
			score += 10;
			getMap().getNodes()[yPos][xPos].setCoin(false);
			munchFood.play();
		}
	}

	public void collideWithEnergizer() {
		
		int xPos = (int) (getX() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize();
		int yPos = (int) ((getY() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize()) - getMap().getVerticalOffset();

		if (xPos < 0)
			xPos = 0;
		if (yPos < 0)
			yPos = 0;
		if (xPos >= getMap().getNodes()[yPos].length) {
			xPos = getMap().getNodes()[yPos].length - 1;
		}
		
		if (getMap().getNodes()[yPos][xPos].isEnergizer()) {
			xEaten = -1;
			yEaten = -1;
			ghostEatenScore = 200;
			ghostEatenScoreIndex = -1;
			score += 50;
			getMap().getNodes()[yPos][xPos].setEnergizer(false);
			listener.onEatingEnergizer();
		}
	}

	public void collideWithGhosts() {
		for (Ghost g : gsm.getCurrentState().getGhosts()) {
			if (collide(g)) {
				if (g.isFrightened()) {
					
					score+=ghostEatenScore;
					ghostEatenScore*=2;
					ghostEatenScoreIndex++;
					xEaten = getX();
					yEaten = getY();
					ghostEatenScoreTimer = 0;
					renderGhostEatenScore = true;
					
					if(eatGhost.isRunning())
						eatGhost.stop();
					eatGhost.play();
					g.kill();
				} else if(!g.isDead()){
					listener.onPlayerDeath();
				}
			}
		}
	}

	public void reset() {
		score = 0;
		ghostEatenScore = 200;
		ghostEatenScoreIndex = -1;
		xEaten = -1;
		yEaten = -1;
		remainingLives = 3;
	}
	
	public void resetForRevive() {
		setCurrentAnimation(Player.LEFT);
		setCurrentFrame(0);
		setDead(false);
		setX(14 * 16);
		setY(26 * 16);
		setVX(0);
		setVY(0);
	}

	public Point getPlayerTarget() {
		if (getVX() > 0) {
			int x = (int) (getX() + getSize()) / getSize();
			int y = (int) ((getY() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize())
					- getMap().getVerticalOffset();
			if (x >= getMap().getNodes()[y].length) {
				x = getMap().getNodes()[y].length - 1;
			} else if (x < 0) {
				x = 0;
			}
			return new Point(x, y);

		} else if (getVY() > 0) {
			return new Point((int) (getX() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize(),
					(int) ((getY() + getSize()) / getSize()) - getMap().getVerticalOffset());
		} else {
			return new Point((int) (getX() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize(),
					(int) ((getY() + getSize() / 2 - (getSize() / 2 - getPadding())) / getSize())
							- getMap().getVerticalOffset());
		}
	}

	public Point dfs(int steps) {
		dfsPath = new Stack<Node>();
		Stack<Node> stack = new Stack<Node>();
		Node playerNode = getMap().getNodes()[((int) getY() / getSize()) - getMap().getVerticalOffset()][(int) getX()
				/ getSize()];
		playerNode.setVisited(true);
		if (getVX() > 0) {
			playerNode.setLastDir(1);
		} else if (getVX() < 0) {
			playerNode.setLastDir(3);
		} else if (getVY() > 0) {
			playerNode.setLastDir(2);
		} else if (getVY() < 0) {
			playerNode.setLastDir(0);
		}
		stack.push(playerNode);
		Node current = null;
		while (!stack.isEmpty()) {
			current = stack.pop();
			if (steps-- == 0) {
				break;
			}
			if (current.getLastDir() == 0) {
				addToStack(stack, current.getNeighbours()[1], 1, current);
				addToStack(stack, current.getNeighbours()[3], 3, current);
				addToStack(stack, current.getNeighbours()[0], 0, current);
			} else if (current.getLastDir() == 1) {
				addToStack(stack, current.getNeighbours()[0], 0, current);
				addToStack(stack, current.getNeighbours()[2], 2, current);
				addToStack(stack, current.getNeighbours()[1], 1, current);
			} else if (current.getLastDir() == 2) {
				addToStack(stack, current.getNeighbours()[1], 1, current);
				addToStack(stack, current.getNeighbours()[3], 3, current);
				addToStack(stack, current.getNeighbours()[2], 2, current);
			} else if (current.getLastDir() == 3) {
				addToStack(stack, current.getNeighbours()[0], 0, current);
				addToStack(stack, current.getNeighbours()[2], 2, current);
				addToStack(stack, current.getNeighbours()[3], 3, current);
			}
		}
		Point res = new Point(current.getX(), current.getY());
		while (current != null) {
			dfsPath.push(current);
			current = current.getPrevious();
		}
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

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}

	public int getRemainingLives() {
		return remainingLives;
	}

	public int getScore() {
		return score;
	}

	public int getHighScore() {
		return highScore;
	}
	
	public void setListener(PlayerListener listener) {
		this.listener = listener;
	}

	public void setRemainingLives(int remainingLives) {
		this.remainingLives = remainingLives;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public void setCurrentAnimation(int currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public void setDead(boolean dead) {
		super.setDead(dead);
		lastTime = System.currentTimeMillis();
	}

	public AudioPlayer getDeath() {
		return death;
	}

}
