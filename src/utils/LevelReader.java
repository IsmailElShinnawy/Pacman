package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class LevelReader {
	
	public static int[][] read(String fileName){
		int[][] tokens = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int rows = Integer.parseInt(br.readLine());
			int cols = Integer.parseInt(br.readLine());
			tokens = new int[rows][cols];
			String line;
			int i = 0;
			while((line = br.readLine())!=null) {
				StringTokenizer st = new StringTokenizer(line);
				for(int j = 0; j<cols; j++) {
					tokens[i][j] = Integer.parseInt(st.nextToken());
				}
				i++;
			}
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return tokens;
	}
	
	
}
