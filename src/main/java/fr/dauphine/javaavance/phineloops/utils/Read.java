package fr.dauphine.javaavance.phineloops.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class Read {
	
	/**
	 * Read a file and get the grid
	 * @param filename
	 * @throws IOException
	 * @return Grid
	 */
	public static Grid readFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
			int height = Integer.parseInt(reader.readLine());
			int width = Integer.parseInt(reader.readLine());

			Grid grid = new Grid(height, width);
			
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					String s = reader.readLine();
					int x = Integer.parseInt(s.split(" ")[0]);
					int y = Integer.parseInt(s.split(" ")[1]);
					grid.add(new Piece(i,j,x,y)); 
				}
			}
			reader.close();	
		return grid;
	}

}
