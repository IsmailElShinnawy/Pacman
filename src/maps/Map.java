package maps;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.LevelReader;

public class Map {
	
	private Node[][] nodes;
	private int rows;
	private int cols;
	private final int verticalOffset = 3;
	
	private final int SIZE = 8*2;
	private BufferedImage img;
	
	public Map(int rows, int cols, String fileName, BufferedImage img) {
		this.rows = rows;
		this.cols = cols;
		this.nodes = new Node[rows][cols];
		this.img = img;
		int[][] levelTokens = LevelReader.read(fileName);
		for(int i = 0; i<this.rows; i++) {
			for(int j = 0; j<this.cols; j++) {
				if(levelTokens[i][j]==0) {
					this.nodes[i][j] = new Node(j, i, SIZE, false, verticalOffset, false, false);
				}else if(levelTokens[i][j]==1){
					this.nodes[i][j] = new Node(j, i, SIZE, true, verticalOffset, false, false);
				}else if(levelTokens[i][j]==2) {
					this.nodes[i][j] = new Node(j, i, SIZE, false, verticalOffset, true, false);
				}else if(levelTokens[i][j]==3) {
					this.nodes[i][j] = new Node(j, i, SIZE, false, verticalOffset, false, true);
				}
			}
		}
		
		for(int i = 0; i<nodes.length; i++) {
			for(int j = 0; j<nodes[i].length; j++) {
				nodes[i][j].setNeighbours(this.nodes);
			}
		}
	}
	
	public void resetNodesForAStar() {
		for(int i = 0; i<nodes.length; i++) {
			for(int j = 0; j<nodes[i].length; j++) {
				nodes[i][j].resetForAStar();
			}
		}
	}
	
	public void resetNodesForDFS() {
		for(int i = 0; i<nodes.length; i++) {
			for(int j = 0; j<nodes[i].length; j++) {
				nodes[i][j].resetForDFS();
			}
		}
	}
	
	public void render(Graphics g) {
		
		g.drawImage(img, 0, verticalOffset*SIZE, nodes[0].length*SIZE, nodes.length*SIZE, null);
//		g.drawImage(img, 0, verticalOffset*SIZE, Game.CANVAS_WIDTH, Game.CANVAS_HEIGHT, null);
		for(int i = 0; i<rows; i++) {
			for(int j = 0; j<cols; j++) {
				this.nodes[i][j].render(g);
			}
		}
	}
	
	public int getVerticalOffset() {
		return verticalOffset;
	}
	
	public Node[][] getNodes(){
		return nodes;
	}
}
