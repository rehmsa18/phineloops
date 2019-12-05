package fr.dauphine.javaavance.phineloops;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class LevelGenerator {
	
	protected Grid grid;
	protected int width;
	protected int height;
	ArrayList<Piece> pieces;
	ArrayList<ConnectedComponent> connectedComponents  = new ArrayList<>();
	int maxConnectedComponent; 
	int nbConnectedComponent;
	boolean connectedComponentIndicated = false;

	public LevelGenerator(int width, int height) {
		this.width = width;
		this.height = height;
		this.maxConnectedComponent = -1;
		grid = new Grid(width, height);
		pieces = this.definePieces();
	}
	
	public LevelGenerator(int width, int height, int maxConnectedComponent) {
		this.width = width;
		this.height = height;
		this.maxConnectedComponent = maxConnectedComponent;
		this.nbConnectedComponent = 0;
		this.connectedComponentIndicated = true;
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
	public boolean respectLinkNeighbors(int i, int j, Piece p) {	
		//for piece not in the west border 
		//the piece on its west side need to have the same value for the link west-east
		if( j > 0 ) {
			if( p.links[3] != grid.getCase()[i][j-1].links[1] )
				return false;
		}
		//for piece not in the north border 
		//the piece on its north side need to have the same value for the link north-south
		if( i > 0) {
			if( p.links[0] != grid.getCase()[i-1][j].links[2] )
				return false;
		}
		return true;
	}
	
	/**
	 * Says two neighbors can not be of the type 1 
	 * @param x
	 * @param y
	 * @param p
	 * @return true if the two piece are of the type 1
	 */
	public boolean pieceType1Neighbors(int i, int j, Piece p) {	
		//the piece and its west neighbor can not be of the type 1 to avoid too much connected component in the grid
		if( j > 0 ) {
			if( (p.type == 1) && (grid.getCase()[i][j-1].type == 1) )
				return true;
		}
	    //the piece and its north neighbor can not be of the type 1 to avoid too much connected component in the grid
		if( i > 0) {
			if( (p.type == 1) && (grid.getCase()[i-1][j].type == 1) )
				return true;
		}
		return false;
	}

	/**
	 * Says if a piece in the north west side of the grid
	 * @param x
	 * @param y
	 * @return true if in the north west
	 */
	public boolean northWestSide(int i, int j) {
		return ( i==0 && j==0 );		
	}

	/**
	 * Says if a piece in the north east side of the grid
	 * @param x
	 * @param y
	 * @return true if in the north east
	 */
	public boolean northEastSide(int i, int j) {
		return ( i==0 && j==width-1 );		
	}
	
	/**
	 * Says if a piece in the south west side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south west
	 */
	public boolean southWestSide(int i, int j) {
		return ( i==height-1 && j==0 );		
	}
	
	/**
	 * Says if a piece in the south east side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south east
	 */
	public boolean southEastSide(int i, int j) {
		return ( i==height-1 && j==width-1 );		
	}
	
	/**
	 * Says if a piece in the west border of the grid
	 * @param x
	 * @param y
	 * @return true if in the west border
	 */
	public boolean westBorder(int j) {
		return ( j==0 );		
	}	
	
	/**
	 * Says if a piece in the east border of the grid
	 * @param x
	 * @param y
	 * @return true if in the east border
	 */
	public boolean eastBorder(int j) {
		return ( j==width-1 );		
	}	
	
	/**
	 * Says if a piece in the north border of the grid
	 * @param x
	 * @param y
	 * @return true if in the north border
	 */
	public boolean northBorder(int i) {
		return ( i==0 );		
	}
	
	/**
	 * Says if a piece in the south border of the grid
	 * @param x
	 * @param y
	 * @return true if in the south border
	 */
	public boolean southBorder(int i) {
		return ( i==height-1 );		
	}	
	
	
	
	/**
	 * Choose the piece to place in a position in the grid
	 * @param x
	 * @param y
	 * @return Piece
	 */
	public Piece associatePieceToGrid(int i, int j) {
		ArrayList<Piece> piecesPossible = new ArrayList<Piece>();

		if( northWestSide(i,j) ){
			for(Piece p : pieces ) {
				if( p.noLinkNorth() && p.noLinkWest() && this.respectLinkNeighbors(i, j, p)) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( northEastSide(i,j) ){
			for(Piece p : pieces ) {
				if( p.noLinkNorth() && p.noLinkEast() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}		
		}
		
		else if( southWestSide(i,j) ){
			for(Piece p : pieces ) {
				if( p.noLinkSouth() && p.noLinkWest() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( southEastSide(i,j) ){
			for(Piece p : pieces ) {
				if( p.noLinkSouth() && p.noLinkEast() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( westBorder(j) ){
			for(Piece p : pieces ) {
				if( p.noLinkWest() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( eastBorder(j) ){
			for(Piece p : pieces ) {
				if( p.noLinkEast() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( northBorder(i) ){
			for(Piece p : pieces ) {
				if( p.noLinkNorth() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( southBorder(i) ){
			for(Piece p : pieces ) {
				if( p.noLinkSouth() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
	
		else {	
			for(Piece p : pieces ) {
				if( this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
			
		// choose randomly a piece among the possibilities
		Random rand = new Random(); 
		Piece chosenPiece = piecesPossible.get(rand.nextInt(piecesPossible.size())); 
        chosenPiece = new Piece(i, j, chosenPiece.type, chosenPiece.orientation);
        
        


		return chosenPiece;
	}
	
	
	/**
	 * Build a solution which will be shuffled to generate a grid 
	 * @return a grid to solve
	 */
	public void buildSolution() {
		nbConnectedComponent = 0;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				Piece chosenPiece = associatePieceToGrid(i,j);
				System.out.println(chosenPiece);
				grid.add(chosenPiece);
			}	
		}
		System.out.println("Solution generated besfore shuffle");
		grid.displayInConsole();
    	
    	System.out.println("nombre de composantes connexes : " + nbConnectedComponent +"\n");
	}

	/**
	 * Shuffle the generated grid
	 */
	public void shuffleSolution() {
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				grid.cases[i][j].shufflePiece();
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
			fichier.write(Integer.toString(height));
			fichier.newLine();
			fichier.write(Integer.toString(width));
			fichier.newLine();
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					fichier.write(grid.cases[i][j].toString2());
					fichier.newLine();
				}
			}
			fichier.close();
		
	}

}
