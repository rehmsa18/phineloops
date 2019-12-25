package fr.dauphine.javaavance.phineloops.levelFunctions;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.dauphine.javaavance.phineloops.model.ConnectedComponent;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class LevelGenerator {
	
	private Grid grid;
	private int width;
	private int height;
	private ArrayList<Piece> pieces;
	private ArrayList<ConnectedComponent> connectedComponents  = new ArrayList<>();
	private int maxConnectedComponent; 
	private int nbConnectedComponent;
	private boolean connectedComponentIndicated = false;

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public LevelGenerator(int height, int width) {
		if(height<0 || width<0){
            throw new IllegalArgumentException();
        }
		this.width = width;
		this.height = height;
		this.maxConnectedComponent = -1;
		grid = new Grid(height, width);
		pieces = this.definePieces();
	}
	
	public LevelGenerator(int height, int width, int maxConnectedComponent) {
		if(height<0 || width<0 || maxConnectedComponent<0){
            throw new IllegalArgumentException();
        }
		this.width = width;
		this.height = height;
		this.maxConnectedComponent = maxConnectedComponent;
		this.nbConnectedComponent = 0;
		this.connectedComponentIndicated = true;
		grid = new Grid(height, width);
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
		if(p == null){
			throw new NullPointerException();
        }
		if(i<0 || j<0){
            throw new IllegalArgumentException();
        }
		
		//for piece not in the west border 
		//the piece on its west side need to have the same value for the link west-east
		if( j > 0 ) {
			if( p.getLinks()[3] != grid.getCases()[i][j-1].getLinks()[1] )
				return false;
		}
		//for piece not in the north border 
		//the piece on its north side need to have the same value for the link north-south
		if( i > 0) {
			if( p.getLinks()[0] != grid.getCases()[i-1][j].getLinks()[2] )
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
		if(p == null){
			throw new NullPointerException();
        }
		if(i<0 || j<0){
            throw new IllegalArgumentException();
        }
		
		//the piece and its west neighbor can not be of the type 1 to avoid too much connected component in the grid
		if( j > 0 ) {
			if( (p.getType() == 1) && (grid.getCases()[i][j-1].getType() == 1) )
				return true;
		}
	    //the piece and its north neighbor can not be of the type 1 to avoid too much connected component in the grid
		if( i > 0) {
			if( (p.getType() == 1) && (grid.getCases()[i-1][j].getType() == 1) )
				return true;
		}
		return false;
	}

	/**
	 * Get the alternatives of a piece depending on its position in the grid
	 * @param i
	 * @param j
	 * @return list of pieces 
	 */
	public ArrayList<Piece> pieceAlternative(int i, int j){
		if(i<0 || j<0){
            throw new IllegalArgumentException();
        }
				
		ArrayList<Piece> piecesPossible = new ArrayList<Piece>();
		ArrayList<Piece> piecesChoice = new ArrayList<Piece>();
		if (this.grid.getHeight() == 1 && this.grid.getWidth() == 1) {
			piecesPossible.add(pieces.get(0));
			return piecesPossible;
		}
		
		if (this.grid.getHeight() == 1 && this.grid.getWidth() == 2) {
			//piecesChoice.add(pieces.get(0));
			piecesChoice.add(pieces.get(1));
			piecesChoice.add(pieces.get(3));
		}
		
		else if (this.grid.getHeight() == 2 && this.grid.getWidth() == 1) {
			//piecesChoice.add(pieces.get(0));
			piecesChoice.add(pieces.get(2));
			piecesChoice.add(pieces.get(4));
		}
		else {
			piecesChoice = pieces;
			/*piecesChoice.add(pieces.get(0));
			piecesChoice.add(pieces.get(1));
			piecesChoice.add(pieces.get(2));
			piecesChoice.add(pieces.get(3));
			piecesChoice.add(pieces.get(4));
			piecesChoice.add(pieces.get(15));*/
			
		}
		
		if( this.grid.northWestSide(i,j) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkNorth() && p.noLinkWest() && this.respectLinkNeighbors(i, j, p)) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( this.grid.northEastSide(i,j) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkNorth() && p.noLinkEast() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}		
		}
		
		else if( this.grid.southWestSide(i,j) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkSouth() && p.noLinkWest() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( this.grid.southEastSide(i,j) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkSouth() && p.noLinkEast() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( this.grid.westBorder(j) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkWest() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( this.grid.eastBorder(j) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkEast() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}		
			}
		}
		
		else if( this.grid.northBorder(i) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkNorth() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
		
		else if( this.grid.southBorder(i) ){
			for(Piece p : piecesChoice ) {
				if( p.noLinkSouth() && this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}
	
		else {	
			for(Piece p : piecesChoice ) {
				if( this.respectLinkNeighbors(i, j, p) ) {
					piecesPossible.add(p);
				}	
			}
		}	
		return piecesPossible;
	}
	
	
	/**
	 * Choose piece depending of the indicated number of connected components 
	 * @param i
	 * @param j
	 * @param chosenPiece
	 * @param piecesPossible
	 * @return piece
	 */
	public Piece respectNumberConnectedComponents(int i, int j, Piece chosenPiece, ArrayList<Piece> piecesPossible) {
		if(i<0 || j<0){
            throw new IllegalArgumentException();
        }
		if(chosenPiece == null || piecesPossible == null){
			throw new NullPointerException();
        }
		
    	// if the number of connected components is indicated
        // and if we have almost the maximum number of connected componenet with no more links possible
        // the rest of the connected componenets with open links need to be connected
        // we select the pieces with links on their east or south side or are of type 0
		if( (maxConnectedComponent-nbConnectedComponent) == 1) {		
			Collections.shuffle(piecesPossible);
			for(Piece piece : piecesPossible){
				if( piece.getLinks()[1]==1 || piece.getLinks()[2]==1)
					chosenPiece = new Piece(i, j, piece.getType(), piece.getOrientation());	
			}
		}
		
		// if the number of maximum of connected component is highter 
		// than number of connected componenet with no more links possible
		// Choose piece of type 1
		if( (maxConnectedComponent-nbConnectedComponent) > 1 ){
			for(Piece piece : piecesPossible){
				if( piece.getType() == 1 && piece.getOrientation() == 1 )
					chosenPiece = new Piece(i, j, piece.getType(), piece.getOrientation());
				else if(piece.getType() == 1 && piece.getOrientation() == 3)
					chosenPiece = new Piece(i, j, piece.getType(), piece.getOrientation());
				else if(piece.getType() == 1 && piece.getOrientation() == 2)
					chosenPiece = new Piece(i, j, piece.getType(), piece.getOrientation());
				else if(piece.getType() == 1 && piece.getOrientation() == 0)
					chosenPiece = new Piece(i, j, piece.getType(), piece.getOrientation());			
			}
		}
        

        // add the piece to a new connected component 
		// the new connected component is added in a list of connected components
    	ConnectedComponent connectedComponent = new ConnectedComponent();
        connectedComponent.addPiece(chosenPiece);
        connectedComponents.add(connectedComponent);


        // if the piece is linked on its north side with its neighbor
        // then add in its connected component, all pieces from its neighbor's connected component  
        // delete the neighbor's connected component
        if(chosenPiece.getLinks()[0]==1) {
        	for(int k=0; k<connectedComponents.size(); k++) {
        		if(connectedComponents.get(k).contains(grid.getCases()[i-1][j]) && connectedComponents.get(k)!=connectedComponents.get(connectedComponents.size()-1)  ) {
        			connectedComponents.get(connectedComponents.size()-1).addAll(connectedComponents.get(k));
        			connectedComponents.remove(connectedComponents.get(k));
        		}
        	}
		}
        
        // if the piece is linked on its west side with its neighbor
        // then add in its connected component, all pieces from its neighbor's connected component  
        // delete the neighbor's connected component
        if(chosenPiece.getLinks()[3]==1) {
        	for(int k=0; k<connectedComponents.size(); k++) {
        		if(connectedComponents.get(k).contains(grid.getCases()[i][j-1]) && connectedComponents.get(k)!=connectedComponents.get(connectedComponents.size()-1)) {
        			connectedComponents.get(connectedComponents.size()-1).addAll(connectedComponents.get(k));
        			connectedComponents.remove(connectedComponents.get(k));
        		}
        	}
		}
       
        // if a connected component can no longer be linked to another piece
        // then the number of real connected component increments
        for(int k=0; k<connectedComponents.size(); k++) {
        	if(connectedComponents.get(k).getNbLinkPossible() == 0) {
        		connectedComponents.remove(connectedComponents.get(k));
        		nbConnectedComponent++;
        	}
        }
        
        return chosenPiece;	
	}
	
	
	
	/**
	 * Choose the piece to place in a position in the grid
	 * @param x
	 * @param y
	 * @return Piece
	 */
	public Piece associatePieceToGrid(int i, int j) {
		if(i<0 || j<0){
            throw new IllegalArgumentException();
        }		
		ArrayList<Piece> piecesPossible = this.pieceAlternative(i, j);
	
		// choose randomly a piece among the possibilities
		Random rand = new Random(); 
		Piece chosenPiece = piecesPossible.get(rand.nextInt(piecesPossible.size())); 
        chosenPiece = new Piece(i, j, chosenPiece.getType(), chosenPiece.getOrientation());     

        if(connectedComponentIndicated == true) {       	
        	chosenPiece = respectNumberConnectedComponents(i, j, chosenPiece, piecesPossible);
        }   
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
				grid.add(chosenPiece);
			}	
		}
	}
	
	/**
	 * Shuffle the generated grid
	 */
	public void shuffleSolution() {
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				grid.getCases()[i][j].shufflePiece();
				if (this.grid.getCases()[i][j].getType()!=4 && this.grid.getCases()[i][j].getType()!=0) {
					this.grid.getCases()[i][j].setLock(0);
				}
			}		
		}
	}

}
