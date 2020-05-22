package entities;

public interface PlayerListener {
	public void onPlayerDeath();
	public void onPlayerRevive();
	public void onEatingEnergizer();
	public void onSpaceBarPress();
	public void onEatingDot();
}
