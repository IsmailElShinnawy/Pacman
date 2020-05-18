package states;

import java.awt.Graphics;
import java.util.ArrayList;

import entities.Ghost;
import entities.Player;

public abstract class State {
	
	private Player player;
	private ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void init();
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void addGhost(Ghost entity) {
		ghosts.add(entity);
	}
	
	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}
	
	public void setEntities(ArrayList<Ghost> entities) {
		this.ghosts = entities;
	}
	
}
