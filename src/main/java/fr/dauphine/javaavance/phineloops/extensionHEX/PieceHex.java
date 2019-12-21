package fr.dauphine.javaavance.phineloops.extensionHEX;

public class PieceHex {
	private int x;
	private int y;
	private int orientation = 0; //depends of piece between 0 and 5
	private int type; //kind of piece 0 to 13
	private int lock = 0; //indicate if we can move a piece, it is lock if = 1
	private int links[] = {0,0,0,0,0}; //tab to define where the piece makes a link with value 1 {N, NE, SE, S, SW, NW}
	private int nbneighbors = 0;
	private int connectedComponent;
	private int gridX;
	private int gridY;
	
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
	
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public void setLinks(int[] links) {
		this.links = links;
	}

	public int getNbneighbors() {
		return nbneighbors;
	}

	public void setNbneighbors(int nbneighbors) {
		this.nbneighbors = nbneighbors;
	}

	public int getConnectedComponent() {
		return connectedComponent;
	}

	public void setConnectedComponent(int connectedComponent) {
		this.connectedComponent = connectedComponent;
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
	}
	
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
}

