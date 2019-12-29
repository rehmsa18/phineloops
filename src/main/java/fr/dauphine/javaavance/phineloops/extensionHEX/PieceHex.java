package fr.dauphine.javaavance.phineloops.extensionHEX;

public class PieceHex {
	private int x;
	private int y;
	private int orientation = 0; //depends of piece between 0 and 5
	private int type; //kind of piece 0 to 13

	public PieceHex(int type, int orientation) {
		this.type = type;
		this.orientation = orientation;
	}
	
	public PieceHex(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public PieceHex(int x, int y, int type, int orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.type = type;
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

	/**
	 * Display the type and orientation of the piece
	 */
	public String toString() {
		return "(" + x + "," + y + ") " +type + " " + orientation;
	}
	
}

