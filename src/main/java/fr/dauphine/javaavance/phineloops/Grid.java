package fr.dauphine.javaavance.phineloops;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

public class Grid  extends Observable {
	int height;
	int width;
	Piece cases[][];
	
	public Grid(int height, int width){
		this.height = height;
		this.width = width;
		cases = new Piece[this.height][this.width];
	}
	
	/**
	 * Says if a piece can exists in the case considering the grid
	 * @param x
	 * @param y
	 * @return true if the piece can exists
	 */
	public boolean existsPiece(int x, int y) {
		return ((x < 0) || (y > 0) || (x >= this.height) || (y >= this.width));
		
	}
	
	/**
	 * Add the piece in its case
	 * @param Piece p
	 */
	public void add (Piece p) {
		cases[p.x][p.y] = p;
	}
	

	/**
	 * Find a piece with its coordinates
	 * @param x
	 * @param y
	 * @return Piece
	 */
	public Piece find(int x, int y) {
		return this.cases[x][y];
	}
	
	/**
	 * Says if a piece has all its links linked to theirs neighbors
	 * @param Piece p
	 * @return true if it is linked
	 */
	public boolean allLinked(Piece p) {
		int cpt = 0;
		
		if ( (p.links[0] == 1) && this.existsPiece(p.x,p.y+1) ){
				if (p.isLinked(this.cases[p.x][p.y+1])){
					cpt++;
				}
		}
		
		if ( (p.links[2] == 1) && this.existsPiece(p.x,p.y-1) ){
			if (p.isLinked(this.cases[p.x][p.y-1])){
				cpt++;
			}
		}
		
		if ( (p.links[1] == 1) && this.existsPiece(p.x+1,p.y) ){
			if (p.isLinked(this.cases[p.x+1][p.y])){
				cpt++;
			}
		}
		
		if ( (p.links[3] == 1) && this.existsPiece(p.x-1,p.y) ){
			if (p.isLinked(this.cases[p.x-1][p.y])){
				cpt++;
			}
		}
		return (cpt == p.nbneighbors);
	}

	/**
	 * Display in console the unicode for each piece of the grid
	 */
	public void displayInConsole() {
		String rows = "";
		for(int i=0; i<height; i++) {
			String row = "";
			for(int j=0; j<width; j++) {
				row += " " + cases[i][j].unicode();
			}
			rows += row +"\n";
		}
		System.out.println(rows);	
	}
	
	/**
	 * 
	 * @return all pieces of the grid
	 */
	public Piece[][] getCase() {
		return cases;
	}
	
	/** 
	 * Lock pieces that will never move considered their position on the side of the grid and affect the good orientation as well
	 */
	public void defaultLock() {
		if (this.cases[0][0].type == 5) {
			this.cases[0][0].orientation = 1;
			this.cases[0][0].lock = 1;
		}
		
		if (this.cases[0][this.width-1].type == 5) {
			this.cases[0][this.width-1].orientation = 2;
			this.cases[0][this.width-1].lock = 1;
		}
		
		if (this.cases[this.height-1][0].type == 5) {
			this.cases[this.height-1][0].orientation = 0;
			this.cases[this.height-1][0].lock = 1;
		}
		this.cases[this.height-1][0].toString();
		
		if (this.cases[this.height-1][this.width-1].type == 5) {
			this.cases[this.height-1][this.width-1].orientation = 3;
			this.cases[this.height-1][this.width-1].lock = 1;
		}
		
		for (int i = 1; i < this.height-1; i++) {
			switch (this.cases[i][0].type) {
				case 2 : 
					this.cases[i][0].orientation = 1;
					this.cases[i][0].lock = 1;
					break;
				case 3 : 
					this.cases[i][0].orientation = 2;
					this.cases[i][0].lock = 1;
					break;
			}
			
			switch (this.cases[i][this.width-1].type) {
				case 2 : 
					this.cases[i][this.width-1].orientation = 1;
					this.cases[i][this.width-1].lock = 1;
					break;
				case 3 : 
					this.cases[i][this.width-1].orientation = 0;
					this.cases[i][this.width-1].lock = 1;
					break;
			}
		}
		
		for (int j = 1; j < this.width-1; j++) {
			switch (this.cases[0][j].type) {
				case 2 : 
					this.cases[0][j].orientation = 0;
					this.cases[0][j].lock = 1;
					break;
				case 3 : 
					this.cases[0][j].orientation = 1;
					this.cases[0][j].lock = 1;
					break;
			}
			
			switch (this.cases[this.height-1][j].type) {
				case 2 : 
					this.cases[this.height-1][j].orientation = 0;
					this.cases[this.height-1][j].lock = 1;
					break;
				case 3 : 
					this.cases[this.height-1][j].orientation = 3;
					this.cases[this.height-1][j].lock = 1;
					break;
			}
		}
		
	}
	
	/**
	 * Write the grid in a file
	 * @param filename
	 * @throws IOException
	 */
	public void writeFile(String filename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(Integer.toString(height));
			writer.newLine();
			writer.write(Integer.toString(width));
			writer.newLine();
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					writer.write(cases[i][j].toString2());
					writer.newLine();
				}
			}
			writer.close();	
	}
	
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
					grid.cases[i][j] = new Piece(x,y); 
				}
			}
			reader.close();	
		return grid;
	}

}