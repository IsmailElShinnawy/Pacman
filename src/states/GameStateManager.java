package states;

import java.util.ArrayList;

public class GameStateManager {
	
	private static ArrayList<State> states = new ArrayList<State>();
	private static int currentState = -1;
	public static final int MENU = 0;
	public static final int LEVEL1 = 1;
	
	public void init() {
		MainMenu menu = new MainMenu();
		Level1 lvl1 = new Level1(this);
		
		states.add(menu);
		states.add(lvl1);
		
		currentState = 0;
	}
	
	public State getCurrentState() {
		return states.get(currentState);
	}
	
	public void setCurrentState(int index) {
		currentState = index;
	}
	
}
