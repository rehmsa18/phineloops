package fr.dauphine.javaavance.phineloops;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GeneratorGrid {
	
	protected Grid grid;
	protected int width;
	protected int height;
	ArrayList<Piece> pieces;

	public GeneratorGrid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new Grid(width, height);
		pieces = this.definePieces();
	}
	
	public ArrayList<Piece> definePieces() {	
		ArrayList<Piece> pieces = new ArrayList<>();	
		pieces.add(new Piece(0,0));
		pieces.add(new Piece(1,0));
		pieces.add(new Piece(1,1));
		pieces.add(new Piece(1,2));
		pieces.add(new Piece(1,3));
		pieces.add(new Piece(2,0));
		pieces.add(new Piece(2,1));
		pieces.add(new Piece(3,0));
		pieces.add(new Piece(3,1));
		pieces.add(new Piece(3,2));
		pieces.add(new Piece(3,3));
		pieces.add(new Piece(4,0));
		pieces.add(new Piece(5,0));
		pieces.add(new Piece(5,1));
		pieces.add(new Piece(5,2));
		pieces.add(new Piece(5,3));
		return pieces;
	}
	
	/**
	 * Says if a piece respects the links between its neighbors 
	 * @param x
	 * @param y
	 * @param p
	 * @return true if all possible links done with neighbors
	 */
	public boolean respectLinkNeighbors(int x, int y, Piece p) {	
		if( x > 0 ) {
			if( p.links[3] != grid.getCase()[x-1][y].links[1] )
				return false;
		}
		if( y > 0) {
			if( p.links[0] != grid.getCase()[x][y-1].links[2] )
				return false;
		}
		return true;
	}

	/**
	 * Says if a piece in the north west side of the grid
	 * @param x
	 * @param y
	 * @return true if in the north west
	 */
	public boolean northWestSide(int x, int y) {
		return ( x==0 && y==0 );		
	}

	/**
	 * Says if a piece in the north east side of the grid
	 * @param x
	 * @param y
	 * @return true if in the north east
	 */
	public boolean northEastSide(int x, int y) {
		return ( x==width-1 && y==0 );		
	}
	
	/**
	 * Says if a piece in the south west side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south west
	 */
	public boolean southWestSide(int x, int y) {
		return ( x==0 && y==height-1 );		
	}
	
	/**
	 * Says if a piece in the south east side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south east
	 */
	public boolean southEastSide(int x, int y) {
		return ( x==width-1 && y==height-1 );		
	}
	
	/**
	 * Says if a piece in the west border of the grid
	 * @param x
	 * @param y
	 * @return true if in the west border
	 */
	public boolean westBorder(int x, int y) {
		return ( x==0 );		
	}	
	
	/**
	 * Says if a piece in the east border of the grid
	 * @param x
	 * @param y
	 * @return true if in the east border
	 */
	public boolean eastBorder(int x, int y) {
		return ( x==width-1 );		
	}	
	
	/**
	 * Says if a piece in the north border of the grid
	 * @param x
	 * @param y
	 * @return true if in the north border
	 */
	public boolean northBorder(int x, int y) {
		return ( y==0 );		
	}
	
	/**
	 * Says if a piece in the south border of the grid
	 * @param x
	 * @param y
	 * @return true if in the south border
	 */
	public boolean southBorder(int x, int y) {
		return ( y==height-1 );		
	}	
	
	
	
	/**
	 * Choose the piece to place in a position in the grid
	 * @param x
	 * @param y
	 * @return Piece
	 */
	public Piece associatePieceToGrid(int x, int y) {
		ArrayList<Piece> piecesPossible = new ArrayList<Piece>();

		if( northWestSide(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkNorth() && p.noLinkWest() && this.respectLinkNeighbors(x, y, p)) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( northEastSide(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkNorth() && p.noLinkEast() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}		
			}		
		}
		
		else if( southWestSide(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkSouth() && p.noLinkWest() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( southEastSide(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkSouth() && p.noLinkEast() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( westBorder(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkWest() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( eastBorder(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkEast() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( northBorder(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkNorth() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( southBorder(x,y) ){
			for(Piece p : pieces ) {
				if( p.noLinkSouth() && this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
	
		else {	
			for(Piece p : pieces ) {
				if( this.respectLinkNeighbors(x, y, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		Random rand = new Random(); 
		Piece chosenPiece = piecesPossible.get(rand.nextInt(piecesPossible.size())); 
        chosenPiece = new Piece(x, y, chosenPiece.type, chosenPiece.orientation);
		
		return chosenPiece;
	}
	
	
	/**
	 * Build a solution which will be shuffled to generate a grid 
	 * @return a grid to solve
	 */
	public void buildSolution() {
		String rows = "";
		for(int y=0; y<height; y++) {
			String row = "";
			for(int x=0; x<width; x++) {
				Piece chosenPiece = associatePieceToGrid(x,y);
				grid.add(chosenPiece);
				row += chosenPiece.toString() +  "  "; 
			}	
			rows += row +"\n\n";
		}
		System.out.println(rows);
		System.out.println("Solution generated besfore shuffle");
		grid.displayInConsole();
	}

	/**
	 * Shuffle the generated grid
	 */
	public void shuffleSolution() {
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				grid.cases[x][y].shufflePiece();
			}		
		}
		System.out.println("Solution after shuffle");
		grid.displayInConsole();
	}

	/**
	 * Store in a file the generated grid after shuffle
	 * @param nomFichier
	 * @throws IOException
	 */
	public void storeInFile(String nomFichier) throws IOException {
		BufferedWriter fichier = new BufferedWriter(new FileWriter(nomFichier));
			fichier.write(Integer.toString(width));
			fichier.newLine();
			fichier.write(Integer.toString(height));
			fichier.newLine();
			for(int y=0; y<height; y++) {
				for(int x=0; x<width; x++) {
					fichier.write(grid.cases[x][y].toString());
					fichier.newLine();
				}
			}
			fichier.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		GeneratorGrid gg = new GeneratorGrid(10,10);
		gg.buildSolution();
		gg.shuffleSolution();
		gg.storeInFile("game_to_solve");

	}

}
