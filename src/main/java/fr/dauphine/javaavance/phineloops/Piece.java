package fr.dauphine.javaavance.phineloops;

public class Piece {
	int x;
	int y;
	int orientation = 0; //depends of piece between 0 and 3
	int type; //kind of piece 0,1,2,3,4,5
	int lock = 0; //indicate if we can move a piece, it is lock if = 1
	int links[] = {0,0,0,0}; //tab to define where the piece makes a link with value 1 {North,East,South,West}
	int nbneighbors = 0;
	
	public Piece() {
		this.x = -1;
		this.y = -1;
	}
	
	public Piece(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.defineLinks();
		this.defineNbNeighbors();
	}

	public Piece(int x, int y, int orientation, int type) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.type = type;
		this.defineLinks();
		this.defineNbNeighbors();
	}
	
	public void defineNbNeighbors() {
		switch (this.type) {
		case 1 : this.nbneighbors = 1;
		case 2 : this.nbneighbors = 2;
		case 3 : this.nbneighbors = 3;
		case 4 : this.nbneighbors = 4;
		case 5 : this.nbneighbors = 2;
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
			return (orientation<=2 && orientation>=0);
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
		if (this.isOrientationChoice(this.orientation+1)) {
			this.orientation++;
		}
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
		case 2 : 
			this.links[this.orientation] = 1;
			this.links[this.orientation+2] = 1;
		case 3 :
			switch (this.orientation) {
			case 0 :
				this.links[0] = 1;
				this.links[1] = 1;
				this.links[3] = 1;
			case 1 :
				this.links[0] = 1;
				this.links[1] = 1;
				this.links[2] = 1;
			case 2 :
				this.links[1] = 1;
				this.links[2] = 1;
				this.links[3] = 1;
			case 3 :
				this.links[0] = 1;
				this.links[2] = 1;
				this.links[3] = 1;
			}

		case 5 : 
			switch (this.orientation) {
			case 0 :
				this.links[0] = 1;
				this.links[1] = 1;
			case 1 :
				this.links[1] = 1;
				this.links[2] = 1;
			case 2 :
				this.links[2] = 1;
				this.links[3] = 1;
			case 3 :
				this.links[0] = 1;
				this.links[3] = 1;
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

}
