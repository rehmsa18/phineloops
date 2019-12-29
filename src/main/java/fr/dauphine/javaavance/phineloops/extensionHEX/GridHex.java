package fr.dauphine.javaavance.phineloops.extensionHEX;

public class GridHex {
	private int height;
	private int width;
	private PieceHex cases[][];
	
	public GridHex(int height, int width){
		this.height = height;
		this.width = width;
		cases = new PieceHex[this.height][this.width];
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public PieceHex[][] getCases() {
		return cases;
	}

	public void add (PieceHex p) {
		cases[p.getX()][p.getY()] = p;
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
		return ( i==0 && j==this.width-1 );		
	}
	
	/**
	 * Says if a piece in the south west side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south west
	 */
	public boolean southWestSide(int i, int j) {
		return ( i==this.height-1 && j==0 );		
	}
	
	/**
	 * Says if a piece in the south east side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south east
	 */
	public boolean southEastSide(int i, int j) {
		return ( i==this.height-1 && j==this.width-1 );		
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
		return ( j==this.width-1 );		
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
		return ( i==this.height-1 );		
	}
	
}
