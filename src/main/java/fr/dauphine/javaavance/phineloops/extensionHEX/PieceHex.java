package fr.dauphine.javaavance.phineloops.extensionHEX;

import java.awt.Graphics;
import java.util.Random;

public class PieceHex {
	int x;
	int y;
	int orientation = 0; //depends of piece between 0 and 5
	int type; //kind of piece 0 to 13
	int lock = 0; //indicate if we can move a piece, it is lock if = 1
	int links[] = {0,0,0,0,0}; //tab to define where the piece makes a link with value 1 {N, NE, SE, S, SW, NW}
	int nbneighbors = 0;
	int connectedComponent;
	int gridX;
	int gridY;
	
	public PieceHex() {
		this.x = -1;
		this.y = -1;
	}
	
	public PieceHex(int type, int orientation) {
		this.type = type;
		this.orientation = orientation;
		//this.defineLinks();
		//this.defineNbNeighbors();
		if ( (this.type == 0) || (this.type == 8) ) { //this kind of piece will never move
			this.lock = 1; 
		}
	}
	
	public PieceHex(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		//this.defineLinks();
		//this.defineNbNeighbors();
		if ( (this.type == 0) || (this.type == 8) ) { //this kind of piece will never move
			this.lock = 1; 
		}
	}

	public PieceHex(int x, int y, int type, int orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.type = type;
		//this.defineLinks();
		//this.defineNbNeighbors();
		if ( (this.type == 0) || (this.type == 8) ) { //this kind of piece will never move
			this.lock = 1; 
		}
	}
	/*
	public void defineNbNeighbors() {
		switch (this.type) {
		case 1 : 
			this.nbneighbors = 1;
			break;
		case 2 : case 3 : case 9 :
			this.nbneighbors = 2;
			break;
		case 4 : case 10 : case 12 : case 13 : 
			this.nbneighbors = 3;
			break;
		case 5 : case 6 : case 11 : 
			this.nbneighbors = 4;
			break;
		case 7 : 
			this.nbneighbors = 5;
			break;
		case 8 : 
			this.nbneighbors = 6;
			break;
		}
	}*/
	
	
	
	/**
	 * Define orientation of a piece considered it type and its links
	 * @param type
	 * @param links
	 */
	/*
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
	}*/
	/**
	   * Says if orientation in parameter is possible
	   * for this kind of piece
	   * @param int corresponding to an orientation
	   * @return boolean true for possible.
	   */
	/*
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
	}*/
	
	/**
	   * change orientation of piece
	   * go back to initial orientation 0 if we can't increment this current orientation
	   * @param Any
	   * @return Any
	   */
	/*
	public void rotatePiece() {
		this.orientation ++;
		int limitOrientation = 4;
		while ( !this.isOrientationChoice(this.orientation) && orientation < limitOrientation) {
			this.orientation++;
		}
		if(this.orientation >= 4)
			this.orientation = 0;
		
		this.defineLinks();
	}*/
	
	/**
	 * define the links of a piece considering its type and its orientation
	 * @param Any
	 * @return Any
	 */
	/*
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
	}*/
	
	
	/**
	 * Says if pieces are neighbors considering theirs coordinates is says where
	 * @param Piece p
	 * @return -1 not neighbors, 0 p is at North, 1 at East, 2 at South, 3 at West
	 */
	/*
	public int isNeighbor(Piece p) {
		if ( (this.x == p.x + 1) && (this.y == p.y)) {
			return 0;
		}
		
		if ( (this.x == p.x - 1) && (this.y == p.y)) {
			return 2;
		}
		
		if ( (this.y == p.y - 1) && (this.x == p.x)) {
			return 1;
		}
		
		if ( (this.y == p.y + 1) && (this.x == p.x)) {
			return 3;
		}
		return -1;
	}*/

	/**
	 * Says true if two pieces are linked
	 * @param Piece p
	 * @return true if they are linked
	 */
	/*
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
	}*/

	/**
	 * Display the type and orientation of the piece, place in grid, and unicode shape
	 */
	public String toString() {
		return "(" + x + "," + y + ") " +type + " " + orientation;
	}
	
	/**
	 * Display the type and orientation of the piece
	 */
	public String toString2() {
		return type + " " + orientation;
	}
	
	/**
	 * Display the connectedComponent of the piece
	 */
	public int getConnectedComponent() {
		return connectedComponent;
	}
		
	/**
	 * Modifies the orientation of the piece randomly
	 */
	/*public void shufflePiece() {
		int orientations[] = {0,1,2,3};
		int temp;
		
		do {
			temp = orientations[new Random().nextInt(orientations.length)];
		}while( !isOrientationChoice( temp ) );
		
		this.orientation = temp;
		this.defineLinks();
	}*/
}

