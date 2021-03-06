package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;
import java.util.Observable;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class Grid  extends Observable {
	private int height = 0;
	private int width = 0;
	private Piece cases[][];
	
	public Grid(int height, int width){
		if (height > 0) {
			this.height = height;
		}
		if (width > 0) {
			this.width = width;
		}
		cases = new Piece[this.height][this.width];
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Piece[][] getCases() {
		return cases;
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
	 * detect if for a dim 1*2 or 2*1 the pieces are only type 0 or 1 and type 0 for 1*1
	 * detect if the sum of all the links are even in other case its impossible to have a solution
	 * @return
	 */
	public boolean detectImpossible() {
		if (this.getHeight() == 1 && this.getWidth() == 1 && this.getCases()[0][0].getType()!= 0) {
			return false;
		}
		if (this.getHeight() == 1  && this.getWidth() == 2 && (this.getCases()[0][0].getType() > 1 || this.getCases()[0][1].getType() > 1)) {
			return false;
		}
		if (this.getHeight() == 2  && this.getWidth() == 1 && (this.getCases()[0][0].getType() > 1 || this.getCases()[1][0].getType() > 1)) {
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
		if (this.getWidth() >= 2 && this.getHeight() >= 2) {
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
					else if (northBorder(i)) {
						if (cases[i][j].getType() != 0 && cases[i][j-1].getType() == 0 && cases[i][j+1].getType() == 0 && cases[i+1][j].getType() == 0) {
							return false;
						}
					}
					else if (southBorder(i)) {
						if (cases[i][j].getType() != 0 && cases[i][j-1].getType() == 0 && cases[i][j+1].getType() == 0 && cases[i-1][j].getType() == 0) {
							return false;
						}
					}
					else if (westBorder(j)) {
						if (cases[i][j].getType() != 0 && cases[i+1][j].getType() == 0 && cases[i][j+1].getType() == 0 && cases[i-1][j].getType() == 0) {
							return false;
						}
					}
					else if (eastBorder(j)) { 
						if (cases[i][j].getType() != 0 && cases[i+1][j].getType() == 0 && cases[i][j-1].getType() == 0 && cases[i-1][j].getType() == 0) {
							return false;
						}
					}
					else {
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
				return true;
			}
		}
		if( p.getI() < this.height-1 ) {
			if( this.cases[p.getI()+1][p.getJ()].getLock() == 1 && this.cases[p.getI()+1][p.getJ()].getLinks()[0] != p.getLinks()[2]) {
				return true;
			}		
		}
		if( p.getJ() > 0 ) {
			if( this.cases[p.getI()][p.getJ()-1].getLock() == 1 && this.cases[p.getI()][p.getJ()-1].getLinks()[1] != p.getLinks()[3] ) {
				return true;
			}
		}
		if( p.getJ() < this.width-1 ) {
			if( this.cases[p.getI()][p.getJ()+1].getLock() == 1 && this.cases[p.getI()][p.getJ()+1].getLinks()[3] != p.getLinks()[1] ) {
				return true;
			}		
		}
		return false;
	}
	
	/**
	 * Lock the max pieces possible
	 * @return number of fixed pieces
	 */
	public int lockPiece() {
		int countLockedPiece = 0;	
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
		
				if(this.getCases()[i][j].getLock()==0) {

					ArrayList<Integer> orientationPossible = new ArrayList<>();
					
					if (this.northWestSide(i, j)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) { 
							if( this.getCases()[i][j].getLinks()[0]==0 && this.getCases()[i][j].getLinks()[3]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}	
					else if (this.northEastSide(i, j)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[0]==0 && this.getCases()[i][j].getLinks()[1]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}					
					else if (this.southWestSide(i, j)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[2]==0 && this.getCases()[i][j].getLinks()[3]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}
					else if (this.southEastSide(i, j)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[2]==0 && this.getCases()[i][j].getLinks()[1]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}			
					else if (this.northBorder(i)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[0]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}
					else if (this.southBorder(i)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[2]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}
					else if (this.westBorder(j)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[3]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}	
					else if (this.eastBorder(j)) {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if( this.getCases()[i][j].getLinks()[1]==0 && !this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}
					else {
						for(int k=0; k<this.getCases()[i][j].getNbRotation(); k++) {
							if(!this.noRespectedLockPiece(this.getCases()[i][j])) 
								orientationPossible.add(this.getCases()[i][j].getOrientation());
							this.getCases()[i][j].rotatePiece();
						}
					}
					if( orientationPossible.size() == 1) {
						this.getCases()[i][j].setOrientation(orientationPossible.get(0));
						this.getCases()[i][j].defineLinks();
						this.getCases()[i][j].setLock(1);
					}
					else if (orientationPossible.size() == 0) {
						countLockedPiece = -2;
						return countLockedPiece;
					}
					else 
						this.getCases()[i][j].setPossibleOrientations(orientationPossible);	
				}
				countLockedPiece += this.getCases()[i][j].getLock();
			}			
		}
		return countLockedPiece;
	}

}