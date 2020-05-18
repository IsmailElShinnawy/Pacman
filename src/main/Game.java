package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import gfx.Assets;
import input.KeyManager;
import states.GameStateManager;

public class Game implements Runnable {

	private GameView gameView;
	private Graphics g;
	private BufferStrategy bs;

	private Thread thread;
	private boolean running;

	private GameStateManager gsm;
	private KeyManager keyManager;

	public Game() {
		start();
	}

	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;

		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		Assets.init(); //initiating the game assets for smoother running
		gameView = new GameView();
		gsm = new GameStateManager();
		keyManager = new KeyManager();
		gameView.getFrame().addKeyListener(keyManager);
		gsm.init();
		gsm.setCurrentState(GameStateManager.LEVEL1);
		gsm.getCurrentState().getPlayer().setKeyManager(keyManager);
	}

	public void tick() {
		if (gsm != null) {
			gsm.getCurrentState().tick();
		}
	}

	public void render(Graphics g) {
		bs = gameView.getCanvas().getBufferStrategy();
		if (bs == null) {
			gameView.getCanvas().createBufferStrategy(3);
			bs = gameView.getCanvas().getBufferStrategy();
		}

		g = bs.getDrawGraphics();
		
		g.clearRect(0, 0, gameView.getFrame().getWidth(), gameView.getFrame().getHeight());
		g.setColor(Color.black);
		g.fillRect(0, 0, gameView.getFrame().getWidth(), gameView.getFrame().getHeight());

		if (gsm != null) {
			gsm.getCurrentState().render(g);
		}

		bs.show();
		g.dispose();
	}

	@Override
	public void run() {

		init();

		int fps = 60;
		double timePerTick = 1000000000 / fps;
		long now;
		long lastTime = System.nanoTime();
//		long timer = 0;
		double delta = 0;
//		int ticks = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
//			timer += (now - lastTime);
			lastTime = now;

			if (delta > 0) {
				tick();
				render(g);
				delta--;
//				ticks++;
			}

//			if (timer >= 1000000000) {
//				System.out.println("fps: " + ticks);
//				ticks = 0;
//				timer = 0;
//			}
		}

		this.stop();
	}

}
