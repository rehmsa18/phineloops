package fr.dauphine.javaavance.phineloops.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import fr.dauphine.javaavance.phineloops.model.Grid;
public class Write {

	/**
	 * Write the grid in a file
	 * @param filename
	 * @throws IOException
	 */
	public static void writeFile(String filename, Grid grid) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(Integer.toString(grid.getHeight()));
			writer.newLine();
			writer.write(Integer.toString(grid.getWidth()));
			writer.newLine();
			for(int i=0; i<grid.getHeight(); i++) {
				for(int j=0; j<grid.getWidth(); j++) {
					writer.write(grid.getCases()[i][j].toString2());
					writer.newLine();
				}
			}
			writer.close();	
	}
}
