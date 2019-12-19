package fr.dauphine.javaavance.phineloops.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

public class Grid  extends Observable {
	private int height;
	private int width;
	private Piece cases[][];
	
	public Grid(int height, int width){
		this.height = height;
		this.width = width;
		cases = new Piece[this.height][this.width];
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Piece[][] getCases() {
		return cases;
	}

	public void setCases(Piece[][] cases) {
		this.cases = cases;
	}

	/**
	 * Says if a piece can exists in the case considering the grid
	 * @param x
	 * @param y
	 * @return true if the piece can exists
	 */
	public boolean existsPiece(int i, int j) {
		return ( (i >= 0) && (j >= 0) && (i < this.height) && (j < this.width) );	
	}
	
	/**
	 * Add the piece in its case
	 * @param Piece p
	 */
	public void add (Piece p) {
		cases[p.getI()][p.getJ()] = p;
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
		
		if ( (p.getLinks()[0] == 1) && this.existsPiece(p.getI()-1,p.getJ()) ){
				if (p.isLinked(this.getCases()[p.getI()-1][p.getJ()])){
					cpt++;
				}
		}
		
		if ( (p.getLinks()[2] == 1) && this.existsPiece(p.getI()+1,p.getJ()) ){
			if (p.isLinked(this.getCases()[p.getI()+1][p.getJ()])){
				cpt++;
			}
		}

		
		if ( (p.getLinks()[1] == 1) && this.existsPiece(p.getI(),p.getJ()+1) ){
			if (p.isLinked(this.cases[p.getI()][p.getJ()+1])){
				cpt++;
			}
		}
		
		if ( (p.getLinks()[3] == 1) && this.existsPiece(p.getI(),p.getJ()-1) ){
			if (p.isLinked(this.cases[p.getI()][p.getJ()-1])){
				cpt++;
			}
		}
		return (cpt == p.getNbneighbors());
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
					grid.add(new Piece(i,j,x,y)); 
				}
			}
			reader.close();	
		return grid;
	}
	
	/**
	 * detect if for a dim 1*l or h*1 the pieces are only type 0
	 * detect if the sum of all the links are even in other case its impossible to have a solution
	 * @return
	 */
	public boolean detectImpossible() {
		if (this.getHeight() == 1 && this.getWidth() == 1 && this.getCase()[0][0].getType()!= 0) {
			return false;
		}
		if (this.getHeight() == 1  && this.getWidth() == 2 && (this.getCase()[0][0].getType() > 1 || this.getCase()[0][1].getType() > 1)) {
			return false;
		}
		if (this.getHeight() == 2  && this.getWidth() == 1 && (this.getCase()[0][0].getType() > 1 || this.getCase()[1][0].getType() > 1)) {
			return false;
		}
		int count = 0;
		for (Piece[] l : this.cases) {
			for (Piece p : l) {
				count+=p.getNbneighbors();
			}
		}
		return count%2 == 0;
	}
	
	public boolean detectCircledByType0() {
		if (this.getWidth() >=2 && this.getHeight() >=2) {
		for (int i = 0; i<this.height; i++) {
			for (int j = 0; j<this.width; j++) {
				if (northWestSide(i,j)) {
					if(cases[i][j].getType() != 0 && cases[i][j+1].getType() == 0 && cases[i+1][j].getType() == 0) {
						return false;
					}
				}
				else if (northEastSide(i,j)) {
					if (cases[i][j].getType() != 0 && cases[i][j-1].getType() == 0 && cases[i+1][j].getType() == 0) {
						return false;
					}
				}
				else if (southWestSide(i,j)) {
					if (cases[i][j].getType() != 0 && cases[i][j+1].getType() == 0 && cases[i-1][j].getType() == 0) {
						return false;
					}
				}
				else if (southEastSide(i,j)){
					if (cases[i][j].getType() != 0 && cases[i][j-1].getType() == 0 && cases[i-1][j].getType() == 0) {
						return false;
					}
				}
				else if (northBorder(i) && !northEastSide(i,j) && !northWestSide(i,j)) {
					if (cases[i][j].getType() != 0 && cases[i][j-1].getType() == 0 && cases[i][j+1].getType() == 0 && cases[i+1][j].getType() == 0) {
						return false;
					}
				}
				else if (southBorder(i) && !southEastSide(i,j) && !southWestSide(i,j)) {
					if (cases[i][j].getType() != 0 && cases[i][j-1].getType() == 0 && cases[i][j+1].getType() == 0 && cases[i-1][j].getType() == 0) {
						return false;
					}
				}
				else if (westBorder(j) && !northWestSide(i,j) && !southWestSide(i,j)) {
					if (cases[i][j].getType() != 0 && cases[i+1][j].getType() == 0 && cases[i][j+1].getType() == 0 && cases[i-1][j].getType() == 0) {
						return false;
					}
				}
				else if (eastBorder(j) && !northEastSide(i,j) && !southEastSide(i,j)) { 
					if (cases[i][j].getType() != 0 && cases[i+1][j].getType() == 0 && cases[i][j-1].getType() == 0 && cases[i-1][j].getType() == 0) {
						return false;
					}
				}
				else if (!northBorder(i) && !southBorder(i) && !westBorder(j) && !eastBorder(j) 
				&& !northWestSide(i,j) && !southWestSide(i,j) && !northEastSide(i,j) && !southEastSide(i,j)) {
					if (cases[i][j].getType() != 0 && cases[i+1][j].getType() == 0 && cases[i-1][j].getType() == 0 && cases[i][j-1].getType() == 0 && cases[i][j+1].getType() == 0) {
						return false;
					}
				}
			}
		}
		}
		return true;
	}
	
	/**
	 * Says if a piece in the north west side of the grid
	 * @param i
	 * @param j
	 * @return true if in the north west
	 */
	public boolean northWestSide(int i, int j) {
		return ( i==0 && j==0 );		
	}

	/**
	 * Says if a piece in the north east side of the grid
	 * @param i
	 * @param j
	 * @return true if in the north east
	 */
	public boolean northEastSide(int i, int j) {
		return ( i==0 && j==this.width-1 );		
	}
	
	/**
	 * Says if a piece in the south west side of the grid
	 * @param i
	 * @param j
	 * @return true if in the south west
	 */
	public boolean southWestSide(int i, int j) {
		return ( i==this.height-1 && j==0 );		
	}
	
	/**
	 * Says if a piece in the south east side of the grid
	 * @param i
	 * @param j
	 * @return true if in the south east
	 */
	public boolean southEastSide(int i, int j) {
		return ( i==this.height-1 && j==this.width-1 );		
	}
	
	/**
	 * Says if a piece in the west border of the grid
	 * @param i
	 * @param j
	 * @return true if in the west border
	 */
	public boolean westBorder(int j) {
		return ( j==0 );		
	}	
	
	/**
	 * Says if a piece in the east border of the grid
	 * @param i
	 * @param j
	 * @return true if in the east border
	 */
	public boolean eastBorder(int j) {
		return ( j==this.width-1 );		
	}	
	
	/**
	 * Says if a piece in the north border of the grid
	 * @param i
	 * @param j
	 * @return true if in the north border
	 */
	public boolean northBorder(int i) {
		return ( i==0 );		
	}
	
	/**
	 * Says if a piece in the south border of the grid
	 * @param i
	 * @param j
	 * @return true if in the south border
	 */
	public boolean southBorder(int i) {
		return ( i==this.height-1 );		
	}
	
	/**
	 * Says if a piece is linked to a locked piece 
	 * @param p
	 * @return true if no link respected
	 */
	public boolean noRespectedLockPiece(Piece p) {
		if( p.getI() > 0 ) {
			if( this.cases[p.getI()-1][p.getJ()].getLock() == 1 && this.cases[p.getI()-1][p.getJ()].getLinks()[2] != p.getLinks()[0]) {
				//System.out.println("a" + " "+ this.cases[p.x-1][p.y] +" "+this.cases[p.x-1][p.y].links[2] +" "+ p.links[0]);
				return true;
			}
		}
		if( p.getI() < this.height-1 ) {
			if( this.cases[p.getI()+1][p.getJ()].getLock() == 1 && this.cases[p.getI()+1][p.getJ()].getLinks()[0] != p.getLinks()[2]) {
				//System.out.println("b" + " " + this.cases[p.x+1][p.y] +" "+this.cases[p.x+1][p.y].links[0] + " "+ p.links[2]);
				return true;
			}
				
		}
		if( p.getJ() > 0 ) {
			if( this.cases[p.getI()][p.getJ()-1].getLock() == 1 && this.cases[p.getI()][p.getJ()-1].getLinks()[1] != p.getLinks()[3] ) {
				//System.out.println("c"+" " + this.cases[p.x][p.y-1] +" "+this.cases[p.x][p.y-1].links[1] +" "+ p.links[3]);
				return true;
			}
		}
		if( p.getJ() < this.width-1 ) {
			if( this.cases[p.getI()][p.getJ()+1].getLock() == 1 && this.cases[p.getI()][p.getJ()+1].getLinks()[3] != p.getLinks()[1] ) {
				//System.out.println("d"+ " " +this.cases[p.x][p.y+1] +" "+this.cases[p.x][p.y+1].links[3] +" "+p.links[1]);
				return true;
			}
				
		}
		return false;
	}

}