package fr.dauphine.javaavance.phineloops;

import java.awt.Graphics;
import java.util.Random;

public class Piece {
	int x;
	int y;
	int orientation = 0; //depends of piece between 0 and 3
	int type; //kind of piece 0,1,2,3,4,5
	int lock = 0; //indicate if we can move a piece, it is lock if = 1
	int links[] = {0,0,0,0}; //tab to define where the piece makes a link with value 1 {North,East,South,West}
	int nbneighbors = 0;
	int gridX;
	int gridY;
	
	public Piece() {
		this.x = -1;
		this.y = -1;
	}
	
	public Piece(int type, int orientation) {
		this.type = type;
		this.orientation = orientation;
		this.defineLinks();
		this.defineNbNeighbors();
	}
	
	public Piece(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.defineLinks();
		this.defineNbNeighbors();
	}

	public Piece(int x, int y, int type, int orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.type = type;
		this.defineLinks();
		this.defineNbNeighbors();
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
		if ( (this.x == p.x) && (this.y + 1 == p.y)) {
			return 0;
		}
		
		if ( (this.x == p.x) && (this.y - 1 == p.y)) {
			return 2;
		}
		
		if ( (this.y == p.y) && (this.x + 1 == p.x)) {
			return 1;
		}
		
		if ( (this.y == p.y) && (this.x - 1 == p.x)) {
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
			return p.links[2]==1;
		case 1 :
			return p.links[3]==1;
		case 2 :
			return p.links[0]==1;
		case 3 :
			return p.links[1]==1;
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
		return "(" + x + "," + y + ") " +type + " " + orientation + " " + this.unicode();
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
	}
	
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		DrawablePiece dp=new DrawablePiece(this);
		dp.paintComponent(g);
	}
	
}
