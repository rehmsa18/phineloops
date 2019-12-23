package fr.dauphine.javaavance.phineloops.model;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import fr.dauphine.javaavance.phineloops.view.DrawablePiece;

public class Piece {
	private int i;
	private int j;
	private int orientation = 0; //depends of piece between 0 and 3
	private int type; //kind of piece 0,1,2,3,4,5
	private int lock = 0; //indicate if we can move a piece, it is lock if = 1
	private int links[] = {0,0,0,0}; //tab to define where the piece makes a link with value 1 {North,East,South,West}
	private int nbneighbors = 0;
	private int gridX;
	private int gridY;
	private int nbRotation; 
	private int index = 0;
	private ArrayList <Integer> possibleOrientations;
	
	public Piece() {
		this.i = -1;
		this.j = -1;
	}
	
	public Piece(int type, int orientation) {
		this.type = type;
		this.orientation = orientation;
		this.defineLinks();
		this.defineNbNeighbors();
		this.defineNbRotation();
		if ( (this.type == 0) || (this.type == 4) ) { //this kind of piece will never move
			this.lock = 1; 
		}
	}

	public Piece(int i, int j, int type, int orientation) {
		this.i = i;
		this.j = j;
		this.orientation = orientation;
		this.type = type;
		this.defineLinks();
		this.defineNbNeighbors();
		this.defineNbRotation();
		if ( (this.type == 0) || (this.type == 4) ) { //this kind of piece will never move
			this.lock = 1; 
		}
	}
	
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLock() {
		return lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public int[] getLinks() {
		return links;
	}

	public int getNbneighbors() {
		return nbneighbors;
	}

	public int getGridX() {
		return gridX;
	}

	public void setGridX(int gridX) {
		this.gridX = gridX;
	}

	public int getGridY() {
		return gridY;
	}

	public void setGridY(int gridY) {
		this.gridY = gridY;
	}

	public int getNbRotation() {
		return nbRotation;
	}

	public ArrayList<Integer> getPossibleOrientations() {
		return possibleOrientations;
	}

	public void setPossibleOrientations(ArrayList<Integer> possibleOrientations) {
		this.possibleOrientations = possibleOrientations;
	}

	public void defineNbNeighbors() {
		switch (this.type) {
			case 1 : 
				this.nbneighbors = 1;
				break;
			case 2 : 
				this.nbneighbors = 2;
				break;
			case 3 : 
				this.nbneighbors = 3;
				break;
			case 4 : 
				this.nbneighbors = 4;
				break;
			case 5 : 
				this.nbneighbors = 2;
				break;
		}
	}
	
	/**
	 * Define orientation of a piece considered it type and its links
	 * @param type
	 * @param links
	 */
	public void defineOrientation (int type, int []links) {
		switch (this.type) {
		case 0 : case 4 : 
			this.orientation = 0;
			break;
		case 1 : 
			if (links[0] == 1) {
				this.orientation = 0;
				break;
			}
			if (links[1] == 1) {
				this.orientation = 1;
				break;
			}
			if (links[2] == 1) {
				this.orientation = 2;
				break;
			}
			if (links[3] == 1) {
				this.orientation = 3;
				break;
			}
			break;
		case 2 : 
			if (links[0] == 1) {
				this.orientation = 0;
				break;
			}
			if (links[1] == 1) {
				this.orientation = 1;
				break;
			}
			break;
		case 3 : 
			if (links[0] == 0) {
				this.orientation = 2;
				break;
			}
			if (links[1] == 0) {
				this.orientation = 3;
				break;
			}
			if (links[2] == 0) {
				this.orientation = 0;
				break;
			}
			if (links[3] == 0) {
				this.orientation = 1;
				break;
			}
			break;

		case 5 : 
			if (links[0] == 1 && links[1] == 1) {
				this.orientation = 0;
				break;
			}
			if (links[1] == 1 && links[2] == 1) {
				this.orientation = 1;
				break;
			}
			if (links[2] == 1 && links[3] == 1) {
				this.orientation = 2;
				break;
			}
			if (links[3] == 1 && links[0] == 1) {
				this.orientation = 3;
				break;
			}
			break;
		}
	}
	/**
	   * Says if orientation in parameter is possible
	   * for this kind of piece
	   * @param int corresponding to an orientation
	   * @return boolean true for possible.
	   */
	public boolean isOrientationChoice(int orientation) {
		switch (this.type) {
		case 0  : case 4 :
			return orientation == 0;
		case 1 : case 3 : case 5 : 
			return (orientation<=3 && orientation>=0);
		
		case 2 : 
			return (orientation==0 || orientation==1);
		}
		
		return false;	
	}
	
	/**
	   * change orientation of piece
	   * go back to initial orientation 0 if we can't increment this current orientation
	   * @param Any
	   * @return Any
	   */
	
	public void rotatePiece() {
		this.orientation ++;
		int limitOrientation = 4;
		while ( !this.isOrientationChoice(this.orientation) && orientation < limitOrientation) {
			this.orientation++;
		}
		if(this.orientation >= 4)
			this.orientation = 0;
		
		this.defineLinks();
	}
	
	/**
	 * define the links of a piece considering its type and its orientation
	 * @param Any
	 * @return Any
	 */
	public void defineLinks() {
		if (this.type == 4) { //never change
			for (int i=0; i<4; i++) {
				this.links[i] = 1;
			}
			return;
		}
		for (int i=0; i<4; i++) {
			this.links[i] = 0;
		}
		switch (this.type) {
		//for type 1 and 2 orientation and place in tab links are linked so useless to write a switch for orientation
		case 1  : 
			this.links[this.orientation] = 1;
			break;
		case 2 : 
			this.links[this.orientation] = 1;
			this.links[this.orientation+2] = 1;
			break;
		case 3 :
			switch (this.orientation) {
			case 0 :
				this.links[0] = 1;
				this.links[1] = 1;
				this.links[3] = 1;
				break;
			case 1 :
				this.links[0] = 1;
				this.links[1] = 1;
				this.links[2] = 1;
				break;
			case 2 :
				this.links[1] = 1;
				this.links[2] = 1;
				this.links[3] = 1;
				break;
			case 3 :
				this.links[0] = 1;
				this.links[2] = 1;
				this.links[3] = 1;
				break;
			}

		case 5 : 
			switch (this.orientation) {
			case 0 :
				this.links[0] = 1;
				this.links[1] = 1;
				break;
			case 1 :
				this.links[1] = 1;
				this.links[2] = 1;
				break;
			case 2 :
				this.links[2] = 1;
				this.links[3] = 1;
				break;
			case 3 :
				this.links[0] = 1;
				this.links[3] = 1;
				break;
			}

		}
	}
	
	
	/**
	 * Says if pieces are neighbors considering theirs coordinates is says where
	 * @param Piece p
	 * @return -1 not neighbors, 0 p is at North, 1 at East, 2 at South, 3 at West
	 */
	public int isNeighbor(Piece p) {
		if ( (this.i == p.i + 1) && (this.j == p.j)) {
			return 0;
		}
		
		if ( (this.i == p.i - 1) && (this.j == p.j)) {
			return 2;
		}
		
		if ( (this.j == p.j - 1) && (this.i == p.i)) {
			return 1;
		}
		
		if ( (this.j == p.j + 1) && (this.i == p.i)) {
			return 3;
		}
		return -1;
	}

	/**
	 * Says true if two pieces are linked
	 * @param Piece p
	 * @return true if they are linked
	 */
	public boolean isLinked(Piece p) {
		int pos = this.isNeighbor(p);
		switch (pos) {
		case 0 :
			return p.links[2]==1 && this.links[0]==1;
		case 1 :
			return p.links[3]==1 && this.links[1]==1;
		case 2 :
			return p.links[0]==1 && this.links[2]==1;
		case 3 :
			return p.links[1]==1 && this.links[3]==1;
		}
		return false;
	}
	
	/**
	 * Says if a piece can be linked by its north neighbor 
	 * @return true if no link possible
	 */
	public boolean noLinkNorth() {
		return this.links[0] == 0;
	}
	
	/**
	 * Says if a piece can be linked by its east neighbor 
	 * @return true if no link possible
	 */
	public boolean noLinkEast() {
		return this.links[1] == 0;
	}
	
	/**
	 * Says if a piece can be linked by its south neighbor 
	 * @return true if no link possible
	 */
	public boolean noLinkSouth() {
		return this.links[2] == 0;
	}
	
	/**
	 * Says if a piece can be linked by its west neighbor 
	 * @return true if no link possible
	 */
	public boolean noLinkWest() {
		return this.links[3] == 0;
	}
	
	/**
	 * Display the type and orientation of the piece, place in grid, and unicode shape
	 */
	public String toString() {
		return "(" + i + "," + j + ")" +type + " " + orientation + " " + possibleOrientations + " " + this.unicode()+" "+index;
	}
	
	/**
	 * Display the type and orientation of the piece
	 */
	public String toString2() {
		return type + " " + orientation;
	}
	
	/**
	 * Associate each piece with the good unicode
	 * @return
	 */
	public String unicode() {
		switch (this.type) {
			case 0 : 
				return " ";
			case 1  : 
				switch (this.orientation) {
					case 0 :
						return "\u2579";
					case 1 :
						return "\u257A";
					case 2 :
						return "\u257B";
					case 3 :
						return "\u2578";
					}
			case 2 : 
				switch (this.orientation) {
					case 0 :
						return "\u2503";
					case 1 :
						return "\u2501";
				}
			case 3 :
				switch (this.orientation) {
					case 0 :
						return "\u253B";
					case 1 :
						return "\u2523";
					case 2 :
						return "\u2533";
					case 3 :
						return "\u252B";
					}
			case 4 : 
				return "\u2549";
			case 5 : 
				switch (this.orientation) {
					case 0 :
						return "\u2517";
					case 1 :
						return "\u250F";
					case 2 :
						return "\u2513";
					case 3 :
						return "\u251B";
					}
			}
		return null;	
	}
	
	/**
	 * Modifies the orientation of the piece randomly
	 */
	public void shufflePiece() {
		int orientations[] = {0,1,2,3};
		int temp;
		do {
			temp = orientations[new Random().nextInt(orientations.length)];
		}while( !isOrientationChoice( temp ) );
		
		this.orientation = temp;
		this.defineLinks();
	}
	
	/**
	 * Draw piece on graphic interface
	 * @param g
	 */
	public void draw(Graphics g, int DIM) {
		// TODO Auto-generated method stub
		DrawablePiece dp=new DrawablePiece(this, DIM);
		dp.paintComponent(g);
	}
	
	/**
	 * Says true if two pieces are neighbors, ig they have same value of their link or if they are not neighbors
	 * @param Piece p
	 * @return true if they are linked
	 */
	public boolean linkedNeighborOrNoNeighbor(Piece p) {
		int pos = this.isNeighbor(p);
		switch (pos) {
		case 0 :
			return p.links[2] == this.links[0] ;
		case 1 :
			return p.links[3]== this.links[1] ;
		case 2 :
			return p.links[0]== this.links[2] ;
		case 3 :
			return p.links[1]== this.links[3] ;
		case -1 :
			return true;
		}
		return false;
	}	
	
	/**
	 * Number of rotation possible for a piece
	 */
	public void defineNbRotation() {
		switch (this.type) {
			case 0  : 
				nbRotation = 0;
				break;
			case 1  : 
				nbRotation = 4;
				break;
			case 2  : 
				nbRotation = 2;
				break;
			case 3  : 
				nbRotation = 4;
				break;
			case 4  : 
				nbRotation = 0;
				break;
			case 5  : 
				nbRotation = 4;
				break;
			}
	}

	/**
	 * initialize to 0 the position in the list of possible orientations of a piece
	 */
	public void initializeIndex() {
		index = 0;
	}
	
	/**
	 * the position in the list of possible orientations of a piece
	 * @return index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * get the position in which is the piece
	 * @return int
	 */
	public int getIndexOrientation() {
		return possibleOrientations.indexOf(orientation)+1;
	}
	
	/**
	 * rotate piece depending the possible orientations
	 */
	public void rotatePossibleOrientation() {
		index++;
		this.orientation = this.possibleOrientations.get(index);
		this.defineLinks();
	}
}
