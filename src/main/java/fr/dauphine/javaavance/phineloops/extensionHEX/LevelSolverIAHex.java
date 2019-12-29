package fr.dauphine.javaavance.phineloops.extensionHEX;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class LevelSolverIAHex {
	private GridHex grid;
	public LevelSolverIAHex(GridHex grid) {
		this.grid = grid;
	}
	
	/** Solve grid with chocosolver
	 * Define a variable for each piece which has a value between 0 and 63
	 * It corresponds to a bit code where : 
	 * First bit notify a link to the north (value 1) or not (value 0)
	 * Second bit a link to the NE
	 * Third bit a link to the SE
	 * Fourth bit a link to the S
	 * Third bit a link to the SW
	 * Fourth bit a link to the SN
	 * With this modeling each piece is defined with a different value (for more details go check our project documentation)
	 */
	public boolean solve() {
		Model model = new Model("choco solver");		
		IntVar[][]pieces = new IntVar[this.grid.getHeight()][this.grid.getWidth()];
		//init variables to IntVar 
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
				//define possible values considering type and position on the grid of a piece
				switch(this.grid.getCases()[i][j].getType()) {
				case 0 :
					pieces[i][j] = model.intVar(new int[] {0});
					break;
				case 1 :
					pieces[i][j] = model.intVar(new int[] {1,2,4,8,16,32});
					break;
				case 2 :
					pieces[i][j] = model.intVar(new int[] {5,10,20,40,17,34});
					break;
				case 3 :
					pieces[i][j] = model.intVar(new int[] {3,6,12,24,48,33});
					break;
				case 4 :
					pieces[i][j] = model.intVar(new int[] {7,14,28,56,49,35});
					break;

				case 5 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {15,30,60,57,51,39});
					break;
				case 6 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)
						||this.grid.northBorder(i)||this.grid.southBorder(i)||this.grid.westBorder(j)||this.grid.eastBorder(j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {29,58,53,43,23,46});
					break;
				case 7 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)
						||this.grid.northBorder(i)||this.grid.southBorder(i)||this.grid.westBorder(j)||this.grid.eastBorder(j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {31,62,61,59,55,47});
					break;
				case 8 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)
						||this.grid.northBorder(i)||this.grid.southBorder(i)||this.grid.westBorder(j)||this.grid.eastBorder(j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {63});
					break;
				case 9 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
							return false;
						}
					pieces[i][j] = model.intVar(new int[] {9,18,36});
					break;
				case 10 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)
						||this.grid.northBorder(i)||this.grid.southBorder(i)||this.grid.westBorder(j)||this.grid.eastBorder(j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {21,42});
					break;
				case 11 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)
						||this.grid.northBorder(i)||this.grid.southBorder(i)||this.grid.westBorder(j)||this.grid.eastBorder(j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {27,54,45});
					break;
				case 12 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
							return false;
						}
					pieces[i][j] = model.intVar(new int[] {11,22,44,25,50,37});
					break;
				case 13 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
							return false;
						}
					pieces[i][j] = model.intVar(new int[] {41,19,38,13,26,52});
					break;
				}
			}			
		}
		
		//Constraints : check two neighbors have to be linked considered their values
		// For that we just have to check bit value of the piece and the bit value corresponding
		//to the link with the right neighbor
		// North : value % 2
		//NE : value % 4 / 2
		//SE : value % 8 / 4
		//S : value %16 / 8 
		//SW : value %32 / 16 
		//NW : value /32 
		
		//Constraints for corner pieces
		
		// Piece corner NW
		//N side
		model.arithm(pieces[0][0].mod(2).intVar(),"=", 0).post();
		//NE side
		model.arithm(pieces[0][0].mod(4).div(2).intVar(),"=",pieces[0][1].div(32).intVar()).post();
		//SE side
		model.arithm(pieces[0][0].mod(8).div(4).intVar(),"=",pieces[0][1].mod(32).div(16).intVar()).post();
		//S side
		model.arithm(pieces[0][0].mod(16).div(8).intVar(),"=",pieces[1][0].mod(2).intVar()).post();
		//SW side
		model.arithm(pieces[0][0].mod(32).div(16).intVar(),"=",0).post();
		//NW side
		model.arithm(pieces[0][0].mod(32).div(16).intVar(),"=",0).post();
		
		// Piece corner NE
		//N side
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(2).intVar(),"=", 0).post();
		//NE side 
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(4).div(2).intVar(),"=",0).post();
		//SE side
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(8).div(4).intVar(),"=",0).post();
		//S side 
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(16).div(8).intVar(),"=",pieces[1][this.grid.getWidth()-1].mod(2).intVar()).post();
		//SW side
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(32).div(16).intVar(),"=",pieces[0][this.grid.getWidth()-2].mod(8).div(4).intVar()).post();
		//NW side
		model.arithm(pieces[0][this.grid.getWidth()-1].div(32).intVar(),"=",pieces[0][this.grid.getWidth()-2].mod(4).div(2).intVar()).post();
		
		// Piece corner SW
		//N side
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(2).intVar(),"=",pieces[this.grid.getHeight()-2][0].mod(16).div(8).intVar()).post();
		//NE side 
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(4).div(2).intVar(),"=",pieces[this.grid.getHeight()-1][1].div(32).intVar()).post();
		//SE side 
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(8).div(4).intVar(),"=",pieces[this.grid.getHeight()-1][1].mod(32).div(16).intVar()).post();
		//S side 
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(16).div(8).intVar(),"=",0).post();
		//SW side 
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(32).div(16).intVar(),"=",0).post();
		//NW side 
		model.arithm(pieces[this.grid.getHeight()-1][0].div(32).intVar(),"=",0).post();
		
		// Piece corner SE
		//N side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(2).intVar(),"=",pieces[this.grid.getHeight()-2][this.grid.getWidth()-1].mod(16).div(8).intVar()).post();
		//NE side 
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(4).div(2).intVar(),"=",0).post();
		//SE side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(8).div(4).intVar(),"=",0).post();
		//S side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(16).div(8).intVar(),"=",0).post();
		//SW side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(32).div(16).intVar(),"=",pieces[this.grid.getHeight()-1][this.grid.getWidth()-2].mod(8).div(4).intVar()).post();
		//NW side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].div(32).intVar(),"=",pieces[this.grid.getHeight()-1][this.grid.getWidth()-2].mod(4).div(2).intVar()).post();
		
		//Constraints for border pieces north and south
		for (int j = 1; j < this.grid.getWidth()-1; j++) {
			//North border
			//N side
			model.arithm(pieces[0][j].mod(2).intVar(),"=", 0).post();
			//NE side
			model.arithm(pieces[0][j].mod(4).div(2).intVar(),"=",pieces[0][j+1].div(32).intVar()).post();
			//SE side
			model.arithm(pieces[0][j].mod(8).div(4).intVar(),"=",pieces[0][j+1].mod(32).div(16).intVar()).post();
			//S side
			model.arithm(pieces[0][j].mod(16).div(8).intVar(),"=",pieces[1][j].mod(2).intVar()).post();
			//SW side
			model.arithm(pieces[0][j].mod(32).div(16).intVar(),"=",pieces[0][j-1].mod(8).div(4).intVar()).post();
			//NW side
			model.arithm(pieces[0][j].div(32).intVar(),"=",pieces[0][j-1].mod(4).div(2).intVar()).post();
			
			//South border
			//N side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(2).intVar(),"=",pieces[this.grid.getHeight()-2][j].mod(16).div(8).intVar()).post();
			//NE side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(4).div(2).intVar(),"=",pieces[this.grid.getHeight()-1][j+1].div(32).intVar()).post();
			//SE side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(8).div(4).intVar(),"=",pieces[this.grid.getHeight()-1][j+1].mod(32).div(16).intVar()).post();
			//S side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(16).div(8).intVar(),"=",0).post();
			//SW side 
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(32).div(16).intVar(),"=",pieces[this.grid.getHeight()-1][j-1].mod(8).div(4).intVar()).post();	
			//NW side 
			model.arithm(pieces[this.grid.getHeight()-1][j].div(32).intVar(),"=",pieces[this.grid.getHeight()-1][j-1].mod(4).div(2).intVar()).post();	
		}
		
		//Constraints for west and east borders
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
			//West border
			//N side
			model.arithm(pieces[i][0].mod(2).intVar(),"=",pieces[i-1][0].mod(16).div(8).intVar()).post();
			//NE side 
			model.arithm(pieces[i][0].mod(4).div(2).intVar(),"=",pieces[i][1].div(32).intVar()).post();
			//SE side 
			model.arithm(pieces[i][0].mod(8).div(4).intVar(),"=",pieces[i][1].mod(32).div(16).intVar()).post();
			//S side
			model.arithm(pieces[i][0].mod(16).div(8).intVar(),"=",pieces[i+1][0].mod(2).intVar()).post();
			//SW side
			model.arithm(pieces[i][0].mod(32).div(16).intVar(),"=",0).post();
			//NW side
			model.arithm(pieces[i][0].div(32).intVar(),"=",0).post();
			
			//East border
			//N side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(2).intVar(),"=",pieces[i-1][this.grid.getWidth()-1].mod(16).div(8).intVar()).post();
			//NE side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(4).div(2).intVar(),"=",0).post();
			//SE side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(8).div(4).intVar(),"=",0).post();
			//S side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(16).div(8).intVar(),"=",pieces[i+1][this.grid.getWidth()-1].mod(2).intVar()).post();
			//SW side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(32).div(16).intVar(),"=",pieces[i][this.grid.getWidth()-2].mod(8).div(4).intVar()).post();
			//NW side
			model.arithm(pieces[i][this.grid.getWidth()-1].div(32).intVar(),"=",pieces[i][this.grid.getWidth()-2].mod(4).div(2).intVar()).post();
		}
		
		//Constraints for others pieces in the grid
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
			for (int j = 1; j < this.grid.getWidth()-1; j++) {				
				//N side
				model.arithm(pieces[i][j].mod(2).intVar(),"=",pieces[i-1][j].mod(16).div(8).intVar()).post();
				//NE side
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(32).intVar()).post();
				//SE side
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i][j+1].mod(32).div(16).intVar()).post();
				//S side
				model.arithm(pieces[i][j].mod(16).div(8).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
				//SW side
				model.arithm(pieces[i][j].mod(32).div(16).intVar(),"=",pieces[i][j-1].mod(8).div(4).intVar()).post();
				//NW side
				model.arithm(pieces[i][j].div(32).intVar(),"=",pieces[i][j-1].mod(4).div(2).intVar()).post();
			}
		}
		Solver solver = model.getSolver();
		boolean resolution = solver.solve();	
		return resolution;
	}
	
	
	/**
	 * Try to find if all pieces can be not linked to each other
	 * @return if it's can be solve or not
	 */
	public boolean nosolve() {
		Model model = new Model("choco solver");		
		IntVar[][]pieces = new IntVar[this.grid.getHeight()][this.grid.getWidth()];
		//init variables to IntVar 
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
				//define possible values considering type and position on the grid of a piece
				switch(this.grid.getCases()[i][j].getType()) {
				case 0 :
					pieces[i][j] = model.intVar(new int[] {0});
					break;
				case 1 :
					pieces[i][j] = model.intVar(new int[] {1,2,4,8,16,32});
					break;
				case 2 :
					pieces[i][j] = model.intVar(new int[] {5,10,20,40,17,34});
					break;
				case 3 :
					pieces[i][j] = model.intVar(new int[] {3,6,12,24,48,33});
					break;
				case 4 :
					pieces[i][j] = model.intVar(new int[] {7,14,28,56,49,35});
					break;
				case 5 :
					pieces[i][j] = model.intVar(new int[] {15,30,60,57,51,39});
					break;
				case 6 :
					pieces[i][j] = model.intVar(new int[] {29,58,53,43,23,46});
					break;
				case 7 :
					pieces[i][j] = model.intVar(new int[] {31,62,61,59,55,47});
					break;
				case 8 :
					pieces[i][j] = model.intVar(new int[] {63});
					break;
				case 9 :
					pieces[i][j] = model.intVar(new int[] {9,18,36});
					break;
				case 10 :
					pieces[i][j] = model.intVar(new int[] {21,42});
					break;
				case 11 :
					pieces[i][j] = model.intVar(new int[] {27,54,45});
					break;
				case 12 :
					pieces[i][j] = model.intVar(new int[] {11,22,44,25,50,37});
					break;
				case 13 :
					pieces[i][j] = model.intVar(new int[] {41,19,38,13,26,52});
					break;
				}
			}			
		}
		
		//Constraints : check two neighbors have to be linked considered their values
		// For that we just have to check bit value of the piece and the bit value corresponding
		//to the link with the right neighbor
		// North : value % 2
		//NE : value % 4 / 2
		//SE : value % 8 / 4
		//S : value %16 / 8 
		//SW : value %32 / 16 
		//NW : value /32 
		
		//Constraints for corner pieces
		
		// Piece corner NW
		//NE side
		model.arithm(pieces[0][0].mod(4).div(2).intVar(),"*",pieces[0][1].div(32).intVar(),"=",0).post();
		//SE side
		model.arithm(pieces[0][0].mod(8).div(4).intVar(),"*",pieces[0][1].mod(32).div(16).intVar(),"=",0).post();
		//S side
		model.arithm(pieces[0][0].mod(16).div(8).intVar(),"*",pieces[1][0].mod(2).intVar(),"=",0).post();

		// Piece corner NE
		//S side 
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(16).div(8).intVar(),"*",pieces[1][this.grid.getWidth()-1].mod(2).intVar(),"=",0).post();
		//SW side
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(32).div(16).intVar(),"*",pieces[0][this.grid.getWidth()-2].mod(8).div(4).intVar(),"=",0).post();
		//NW side
		model.arithm(pieces[0][this.grid.getWidth()-1].div(32).intVar(),"*",pieces[0][this.grid.getWidth()-2].mod(4).div(2).intVar(),"=",0).post();
		
		// Piece corner SW
		//N side
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(2).intVar(),"*",pieces[this.grid.getHeight()-2][0].mod(16).div(8).intVar(),"=",0).post();
		//NE side 
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(4).div(2).intVar(),"*",pieces[this.grid.getHeight()-1][1].div(32).intVar(),"=",0).post();
		//SE side 
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(8).div(4).intVar(),"*",pieces[this.grid.getHeight()-1][1].mod(32).div(16).intVar(),"=",0).post();
		
		// Piece corner SE
		//N side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getHeight()-1].mod(2).intVar(),"*",pieces[this.grid.getHeight()-2][this.grid.getWidth()-1].mod(16).div(8).intVar(),"=",0).post();
		//SW side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(32).div(16).intVar(),"*",pieces[this.grid.getHeight()-1][this.grid.getWidth()-2].mod(8).div(4).intVar(),"=",0).post();
		//NW side
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].div(32).intVar(),"*",pieces[this.grid.getHeight()-1][this.grid.getWidth()-2].mod(4).div(2).intVar(),"=",0).post();
		
		//Constraints for border pieces north and south
		for (int j = 1; j < this.grid.getWidth()-1; j++) {
			//North border
			//NE side
			model.arithm(pieces[0][j].mod(4).div(2).intVar(),"*",pieces[0][j+1].div(32).intVar(),"=",0).post();
			//SE side
			model.arithm(pieces[0][j].mod(8).div(4).intVar(),"*",pieces[0][j+1].mod(32).div(16).intVar(),"=",0).post();
			//S side
			model.arithm(pieces[0][j].mod(16).div(8).intVar(),"*",pieces[1][j].mod(2).intVar(),"=",0).post();
			//SW side
			model.arithm(pieces[0][j].mod(32).div(16).intVar(),"*",pieces[0][j-1].mod(8).div(4).intVar(),"=",0).post();
			//NW side
			model.arithm(pieces[0][j].div(32).intVar(),"*",pieces[0][j-1].mod(4).div(2).intVar(),"=",0).post();
			
			//South border
			//N side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(2).intVar(),"*",pieces[this.grid.getHeight()-2][j].mod(16).div(8).intVar(),"=",0).post();
			//NE side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(4).div(2).intVar(),"*",pieces[this.grid.getHeight()-1][j+1].div(32).intVar(),"=",0).post();
			//SE side
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(8).div(4).intVar(),"*",pieces[this.grid.getHeight()-1][j+1].mod(32).div(16).intVar(),"=",0).post();
			//SW side 
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(32).div(16).intVar(),"*",pieces[this.grid.getHeight()-1][j-1].mod(8).div(4).intVar(),"=",0).post();	
			//NW side 
			model.arithm(pieces[this.grid.getHeight()-1][j].div(32).intVar(),"*",pieces[this.grid.getHeight()-1][j-1].mod(4).div(2).intVar(),"=",0).post();	
		}
		
		//Constraints for west and east borders
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
			//West border
			//N side
			model.arithm(pieces[i][0].mod(2).intVar(),"*",pieces[i-1][0].mod(16).div(8).intVar(),"=",0).post();
			//NE side 
			model.arithm(pieces[i][0].mod(4).div(2).intVar(),"*",pieces[i][1].div(32).intVar(),"=",0).post();
			//SE side 
			model.arithm(pieces[i][0].mod(8).div(4).intVar(),"*",pieces[i][1].mod(32).div(16).intVar(),"=",0).post();
			//S side
			model.arithm(pieces[i][0].mod(16).div(8).intVar(),"*",pieces[i+1][0].mod(2).intVar(),"=",0).post();
			
			//East border
			//N side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(2).intVar(),"*",pieces[i-1][this.grid.getWidth()-1].mod(16).div(8).intVar(),"=",0).post();
			//S side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(16).div(8).intVar(),"*",pieces[i+1][this.grid.getWidth()-1].mod(2).intVar(),"=",0).post();
			//SW side
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(32).div(16).intVar(),"*",pieces[i][this.grid.getWidth()-2].mod(8).div(4).intVar(),"=",0).post();
			//NW side
			model.arithm(pieces[i][this.grid.getWidth()-1].div(32).intVar(),"*",pieces[i][this.grid.getWidth()-2].mod(4).div(2).intVar(),"=",0).post();
		}
		
		//Constraints for others pieces in the grid
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
			for (int j = 1; j < this.grid.getWidth()-1; j++) {				
				//N side
				model.arithm(pieces[i][j].mod(2).intVar(),"*",pieces[i-1][j].mod(16).div(8).intVar(),"=",0).post();
				//NE side
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(32).intVar(),"=",0).post();
				//SE side
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i][j+1].mod(32).div(16).intVar(),"=",0).post();
				//S side
				model.arithm(pieces[i][j].mod(16).div(8).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
				//SW side
				model.arithm(pieces[i][j].mod(32).div(16).intVar(),"*",pieces[i][j-1].mod(8).div(4).intVar(),"=",0).post();
				//NW side
				model.arithm(pieces[i][j].div(32).intVar(),"*",pieces[i][j-1].mod(4).div(2).intVar(),"=",0).post();
			}
		}
		Solver solver = model.getSolver();
		boolean resolution = solver.solve();
		return resolution;
	}
	

	
	/*public static void main(String[] args) {
		GridHex grid = new GridHex(3,3);
		//NW
		grid.add(new PieceHex(0,0,3,0));
		
		grid.add(new PieceHex(0,1,9,0));
		
		//NE
		grid.add(new PieceHex(0,2,2,0));
		
		grid.add(new PieceHex(1,0,9,0));
		
		//SW
		grid.add(new PieceHex(2,0,2,0));
		
		grid.add(new PieceHex(1,2,9,0));
		
		//SE
		grid.add(new PieceHex(2,2,3,0));
		
		grid.add(new PieceHex(2,1,9,0));
		
		grid.add(new PieceHex(1,1,0,0));
		
		LevelSolverIAHex sol = new LevelSolverIAHex(grid);
		System.out.println("Solution after solver : " + sol.solve());

	}*/
}	
	


