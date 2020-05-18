package states;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import entities.Blinky;
import entities.Clyde;
import entities.Entity;
import entities.Ghost;
import entities.Inky;
import entities.Pinky;
import entities.Player;
import entities.PlayerListener;
import gfx.Assets;
import maps.Map;

public class Level1 extends State implements PlayerListener {

	private Map map;
	private static final String fileName = "res/levels/level1.txt";

	// private AudioPlayer beginning;
	// private AudioPlayer intermission;

	private boolean chaseMode = false;
	private int chaseTimer = 0;
	private int chaseTime = 20000;

	private boolean scatterMode = true;
	private int scatterTimer = 0;
	private int scatterTime = 7000;

	private boolean frightenedMode = false;
	private int frightenedTimer = 0;
	private int frightenedTime = 7000;
	private int frightenedFade = 2000;

	private long now;
	private long lastTime = System.currentTimeMillis();

	public Level1(GameStateManager gsm) {
		map = new Map(31, 28, fileName, Assets.map);
		addPlayerAndGhosts(new Player(14 * 16, 26 * 16, 16, map, gsm));

		// beginning = new AudioPlayer("/sounds/pacman_beginning.wav");
		// intermission = new AudioPlayer("/sounds/pacman_intermission.wav");
	}

	public void init() {
	}

	public void tick() {
		getPlayer().tick();

		updateMode();

		for (Ghost g : getGhosts()) {
			if (!g.isDead()) {
				if (g.isFrightened()) {
					map.resetNodesForDFS();
					g.setTarget(g.dfs(1));
				} else {
					if (g.isChase()) {
						if (g instanceof Blinky)
							g.setTarget(getPlayer().getPlayerTarget());
						else if (g instanceof Pinky) {
							Point pinkyTarget = getPinkyTarget();
							g.setTarget(pinkyTarget);
						} else if (g instanceof Clyde) {
							g.setTarget(getPlayer().getPlayerTarget());
							if(g.getPath().size()<=8) {
								g.setTarget(g.getCorner());
							} else {;
								g.setTarget(getPlayer().getPlayerTarget());
							}
						} else if (g instanceof Inky) {

						}
					}
				}
			}
			g.tick();
		}

	}

	public void render(Graphics g) {
		map.render(g);
		getPlayer().render(g);
		for (Entity e : getGhosts()) {
			e.render(g);
		}
	}

	public void updateMode() {
		now = System.currentTimeMillis();
		if (frightenedMode) {
			frightenedTimer += (now - lastTime);
			if (frightenedTimer >= frightenedTime) {
				frightenedTimer = 0;
				frightenedMode = false;
				for (Ghost g : getGhosts()) {
					if (!g.isDead()) {
						g.setFrightened(false);
						g.setCurrentAnimation(Ghost.LEFT);
						g.setCurrentFrame(0);
					}
				}
			} else if (frightenedTime - frightenedTimer <= frightenedFade) {
				for (Ghost g : getGhosts()) {
					if (g.isFrightened() && !g.isDead()) {
						g.setCurrentAnimation(Ghost.FRIGHTENED_FADE);
					}
				}
			}
		} else if (chaseMode) {
			chaseTimer += (now - lastTime);
			if (chaseTimer >= chaseTime) {
				chaseTimer = 0;
				chaseMode = false;
				scatterMode = true;
				for (Ghost g : getGhosts()) {
					g.setChase(false);
					g.setScatter(true);
				}
			}
		} else if (scatterMode) {
			scatterTimer += (now - lastTime);
			if (scatterTimer >= scatterTime) {
				scatterTimer = 0;
				chaseMode = true;
				scatterMode = false;
				for (Ghost g : getGhosts()) {
					g.setChase(true);
					g.setScatter(false);
				}
			}
		}
		lastTime = System.currentTimeMillis();
	}

	public void addPlayerAndGhosts(Player player) {
		setPlayer(player);
		getPlayer().setListener(this);
		addGhost(new Blinky(13 * 16, 14 * 16, 16, map, new Point(1, 1), new Point(6, 5)));
		addGhost(new Pinky(11 * 16, 14 * 16, 16, map, new Point(26, 1), new Point(21, 5)));
		// addEntity(new Inky(14 * 16, 17 * 16, 16, map, ));
		// addGhost(new Clyde(16 * 16, 17 * 16, 16, map, new Point(26, 29), new
		// Point(19, 23)));
		addGhost(new Clyde(13 * 16, 14 * 16, 16, map, new Point(26, 29), new Point(18, 26)));
	}

	public Point getPinkyTarget() {
		map.resetNodesForDFS();
		return getPlayer().dfs(4);
	}

	public void onPlayerDeath() {
		getPlayer().getDeath().play();
		getPlayer().setRemainingLives(getPlayer().getRemainingLives() - 1);
		getPlayer().setCurrentAnimation(Player.DEAD);
		getPlayer().setCurrentFrame(0);
		getPlayer().setDead(true);
		getPlayer().setVX(0);
		getPlayer().setVY(0);
		setEntities(new ArrayList<Ghost>());
	}

	public void onPlayerRevive() {
		scatterMode = true;
		chaseMode = false;
		frightenedMode = false;
		scatterTimer = 0;
		chaseTimer = 0;
		frightenedTimer = 0;
		getPlayer().resetForRevive();
		addPlayerAndGhosts(getPlayer());
	}

	public void onEatingEnergizer() {
		frightenedMode = true;
		frightenedTimer = 0;
		for (Ghost g : getGhosts()) {
			g.reverseDirection();
			g.setFrightened(true);
			g.setCurrentAnimation(Ghost.FRIGHTENED);
		}
	}

	public static double getEuclideanDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

}
