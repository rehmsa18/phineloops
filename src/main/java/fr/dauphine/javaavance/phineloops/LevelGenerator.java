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
	public boolean respectLinkNeighbors(int x, int y, Piece p) {	
		//for piece not in the west border 
		//the piece on its west side need to have the same value for the link west-east
		if( x > 0 ) {
			if( p.links[3] != grid.getCase()[x-1][y].links[1] )
				return false;
		}
		//for piece not in the north border 
		//the piece on its north side need to have the same value for the link north-south
		if( y > 0) {
			if( p.links[0] != grid.getCase()[x][y-1].links[2] )
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
	public boolean pieceType1Neighbors(int x, int y, Piece p) {	
		//the piece and its west neighbor can not be of the type 1 to avoid too much connected component in the grid
		if( x > 0 ) {
			if( (p.type == 1) && (grid.getCase()[x-1][y].type == 1) )
				return true;
		}
	    //the piece and its north neighbor can not be of the type 1 to avoid too much connected component in the grid
		if( y > 0) {
			if( (p.type == 1) && (grid.getCase()[x][y-1].type == 1) )
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
			
		// choose randomly a piece among the possibilities
		Random rand = new Random(); 
		Piece chosenPiece = piecesPossible.get(rand.nextInt(piecesPossible.size())); 
        chosenPiece = new Piece(x, y, chosenPiece.type, chosenPiece.orientation);
        
        // if the number of connected components is indicated
        // and if we have almost the maximum number of connected componenet with no more links possible
        // the rest of the connected componenets with open links need to be connected
        // we select the pieces with links on their east or south side or are of type 0
		if( (maxConnectedComponent-nbConnectedComponent) == 1 && connectedComponentIndicated == true) {		
			Collections.shuffle(piecesPossible);
			for(Piece piece : piecesPossible){
				if( piece.links[1]==1 || piece.links[2]==1 || piece.type == 0)
					chosenPiece = new Piece(x, y, piece.type, piece.orientation);	
			}
		}
		
		// if the number of maximum of connected component is highter 
		// than number of connected componenet with no more links possible
		// Choose piece of type 1
		if( (maxConnectedComponent-nbConnectedComponent) > 1 && connectedComponentIndicated == true){
			for(Piece piece : piecesPossible){
				if( piece.type == 1 && piece.orientation == 1 )
					chosenPiece = new Piece(x, y, piece.type, piece.orientation);
				else if(piece.type == 1 && piece.orientation == 3)
					chosenPiece = new Piece(x, y, piece.type, piece.orientation);
				else if(piece.type == 1 && piece.orientation == 2)
					chosenPiece = new Piece(x, y, piece.type, piece.orientation);
				else if(piece.type == 1 && piece.orientation == 0)
					chosenPiece = new Piece(x, y, piece.type, piece.orientation);
				
			}
		}
        
		// if the piece type is different from 0
        // add the piece to a new connected component 
		// the new connected component is added in a list of connected components
        if(chosenPiece.type!=0) {
        	ConnectedComponent connectedComponent = new ConnectedComponent();
            connectedComponent.addPiece(chosenPiece);
            connectedComponents.add(connectedComponent);
        }

        // if the piece is linked on its north side with its neighbor
        // then add in its connected component, all pieces from its neighbor's connected component  
        // delete the neighbor's connected componenet
        if(chosenPiece.links[0]==1) {
        	for(int i=0; i<connectedComponents.size(); i++) {
        		if(connectedComponents.get(i).contains(grid.cases[x][y-1]) && connectedComponents.get(i)!=connectedComponents.get(connectedComponents.size()-1)  ) {
        			connectedComponents.get(connectedComponents.size()-1).addAll(connectedComponents.get(i));
        			connectedComponents.remove(connectedComponents.get(i));
        		}
        	}
		}
        
        // if the piece is linked on its west side with its neighbor
        // then add in its connected component, all pieces from its neighbor's connected component  
        // delete the neighbor's connected componenet
        if(chosenPiece.links[3]==1) {
        	for(int i=0; i<connectedComponents.size(); i++) {
        		if(connectedComponents.get(i).contains(grid.cases[x-1][y]) && connectedComponents.get(i)!=connectedComponents.get(connectedComponents.size()-1)) {
        			connectedComponents.get(connectedComponents.size()-1).addAll(connectedComponents.get(i));
        			connectedComponents.remove(connectedComponents.get(i));
        		}
        	}
		}
       
        // if a connected component can no longer be linked to another piece
        // then the number of real connected component increments
        for(int i=0; i<connectedComponents.size(); i++) {
        	if(connectedComponents.get(i).nbLinkPossible == 0) {
        		connectedComponents.remove(connectedComponents.get(i));
        		nbConnectedComponent++;
        	}
        }

		return chosenPiece;
	}
	
	
	/**
	 * Build a solution which will be shuffled to generate a grid 
	 * @return a grid to solve
	 */
	public void buildSolution() {
		nbConnectedComponent = 0;
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				Piece chosenPiece = associatePieceToGrid(x,y);
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
					fichier.write(grid.cases[x][y].toString2());
					fichier.newLine();
				}
			}
			fichier.close();
		
	}

}
