package fr.dauphine.javaavance.phineloops;

import java.util.Observable;

public class Grid  extends Observable {
	int width;
	int height;
	Piece cases[][];
	
	public Grid(int width, int height){
		this.width = width;
		this.height = height;
		cases = new Piece[width][height];
	}
	
	/**
	 * Says if a piece can exists in the case considering the grid
	 * @param x
	 * @param y
	 * @return true if the piece can exists
	 */
	public boolean existsPiece(int x, int y) {
		return ((x < 0) || (y > 0) || (x >= this.width) || (y >= this.height));
		
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
		for(int y=0; y<height; y++) {
			String row = "";
			for(int x=0; x<width; x++) {
				row += " " + cases[x][y].unicode();
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

}