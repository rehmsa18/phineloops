package fr.dauphine.javaavance.phineloops;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.Grid;

public class LevelSolverIA {
	Grid grid;
	public LevelSolverIA(Grid grid) {
		this.grid = grid;
	}
	
	/** Solve grid with chocosolver
	 * Define a variable for each piece which has a value between 0 and 15
	 * It corresponds to a bit code where : 
	 * First bit notify a link to the north (value 1) or not (value 0)
	 * Second bit a link to the east
	 * Third bit a link to the south
	 * Fourth bit a link to the west
	 * With this modelisation each piece is defined with a different value
	 * Value 0 : Type 0
	 * Value 1 : Type 1 orientation 0
	 * Value 2 : Type 1 orientation 1
	 * Value 4 : Type 1 orientation 2
	 * Value 8 : Type 1 orientation 3
	 * Value 5 : Type 2 orientation 0
	 * Value 10 : Type 2 orientation 1
	 * Value 11 : Type 3 orientation 0
	 * Value 7 : Type 3 orientation 1
	 * Value 14 : Type 3 orientation 2
	 * Value 13 : Type 3 orientation 3
	 * Value 15 : Type 4 
	 * Value 3 : Type 5 orientation 0
	 * Value 6 : Type 5 orientation 1
	 * Value 12 : Type 5 orientation 2
	 * Value 9 : Type 5 orientation 3
	 */
	public boolean solve() {
		if (!this.grid.detectImpossible()) {
			return false;
		}
		if (!this.grid.detectCircledByType0()) {
			return false;
		}
		Model model = new Model("choco solver");		
		IntVar[][]pieces = new IntVar[this.grid.height][this.grid.width];
		//init variables to IntVar 
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				//define possible values considering type and postion on the grid of a piece
				switch(this.grid.cases[i][j].type) {
				case 0 :
					pieces[i][j] = model.intVar(new int[] {0});
					break;
				case 1 :
					if (this.northWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {2,4});
						break;
					}
					if (this.northEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {4,8});
						break;
					}
					if (this.southWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {1,2});
						break;
					}
					if (this.southEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {1,8});
						break;
					}
					if (this.northBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {2,4,8});
						break;
					}
					if (this.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {1,2,8});
						break;
					}
					if (this.westBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {1,2,4});
						break;
					}
					if (this.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {1,4,8});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {1,2,4,8});
					break;
				case 2 :
					if (this.northWestSide(i, j)||this.northEastSide(i, j)||this.southWestSide(i, j)||this.southEastSide(i, j)) {
						return false;
					}
					if (this.northBorder(i)||this.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {10});
						break;
					}
					if (this.westBorder(j)||this.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {5});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {5,10});
					break;
				case 3 :
					if (this.northWestSide(i, j)||this.northEastSide(i, j)||this.southWestSide(i, j)||this.southEastSide(i, j)) {
						return false;
					}
					if (this.northBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {14});
						break;
					}
					if (this.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {11});
						break;
					}
					if (this.westBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {7});
						break;
					}
					if (this.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {13});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {7,14,13,11});
					break;
				case 4 :
					if (this.northWestSide(i, j)||this.northEastSide(i, j)||this.southWestSide(i, j)||this.southEastSide(i, j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {15});
					break;
				case 5 :
					if (this.northWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {6});
						break;
					}
					if (this.northEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {12});
						break;
					}
					if (this.southWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {3});
						break;
					}
					if (this.southEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {9});
						break;
					}
					if (this.northBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {6,12});
						break;
					}
					if (this.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {3,9});
						break;
					}
					if (this.westBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {3,6});
						break;
					}
					if (this.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {12,9});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {3,6,12,9});
					break;
				}
			}			
		}
		
		//Constraints : check two neighbors have to be linked considered their values
		// For that we just have to check bit value of the piece and the bit value corresponding
		//to the link with the right neighbor
		// North : value % 2
		//East : value % 8 % 4 / 2
		//South : value % 8 / 4
		//West : value / 8 
		
		//Constraints for corner pieces
		
		// Piece corner NW
		//condition with south neighbor
		model.arithm(pieces[0][0].mod(8).div(4).intVar(),"=",pieces[1][0].mod(2).intVar()).post();
		//condition with east neighbor
		model.arithm(pieces[0][0].mod(8).mod(4).div(2).intVar(),"=",pieces[0][1].div(8).intVar()).post();
		
		// Piece corner NE
		//condition with south neighbor
		model.arithm(pieces[0][this.grid.width-1].mod(8).div(4).intVar(),"=",pieces[1][this.grid.width-1].mod(2).intVar()).post();
		//condition with west neighbor
		//model.arithm(pieces[0][this.grid.width-1].div(8).intVar(),"=",pieces[0][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();
		
		// Piece corner SW
		//condition with north neighbor
		//model.arithm(pieces[this.grid.height-1][0].mod(2).intVar(),"=",pieces[this.grid.height-2][0].mod(8).div(4).intVar()).post();
		//condition with east neighbor
		model.arithm(pieces[this.grid.height-1][0].mod(8).mod(4).div(2).intVar(),"=",pieces[this.grid.height-1][1].div(8).intVar()).post();
		
		// Piece corner SE
		//condition with north neighbor
		model.arithm(pieces[this.grid.height-1][this.grid.width-1].mod(2).intVar(),"=",pieces[this.grid.height-2][this.grid.width-1].mod(8).div(4).intVar()).post();
		//condition with west neighbor
		//model.arithm(pieces[this.grid.height-1][this.grid.width-1].div(8).intVar(),"=",pieces[this.grid.height-1][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();

		
		//Constraints for border pieces north and south
		for (int j = 1; j < this.grid.width-1; j++) {
			//North border
			//condition with south neighbor
			model.arithm(pieces[0][j].mod(8).div(4).intVar(),"=",pieces[1][j].mod(2).intVar()).post();
			//condition with east neighbor
			model.arithm(pieces[0][j].mod(8).mod(4).div(2).intVar(),"=",pieces[0][j+1].div(8).intVar()).post();
			//condition with west neighbor
			//model.arithm(pieces[0][j].div(8).intVar(),"=",pieces[0][j-1].mod(8).mod(4).div(2).intVar()).post();
			
			//South border
			//condition with north neighbor
			//model.arithm(pieces[this.grid.height-1][j].mod(2).intVar(),"=",pieces[this.grid.height-2][j].mod(8).div(4).intVar()).post();
			//condition with east neighbor
			model.arithm(pieces[this.grid.height-1][j].mod(8).mod(4).div(2).intVar(),"=",pieces[this.grid.height-1][j+1].div(8).intVar()).post();
			//condition with west neighbor
			//model.arithm(pieces[this.grid.height-1][j].div(8).intVar(),"=",pieces[this.grid.height-1][j-1].mod(8).mod(4).div(2).intVar()).post();	
		}
		
		//Constraints for west and east borders
		for (int i = 1; i < this.grid.height-1; i++) {
			//West border
			//condition with north neighbor
			//model.arithm(pieces[i][0].mod(2).intVar(),"=",pieces[i-1][0].mod(8).div(4).intVar()).post();
			//condition with east neighbor
			model.arithm(pieces[i][0].mod(8).mod(4).div(2).intVar(),"=",pieces[i][1].div(8).intVar()).post();
			//condition with south neighbor
			model.arithm(pieces[i][0].mod(8).div(4).intVar(),"=",pieces[i+1][0].mod(2).intVar()).post();
			
			//East border
			//condition with north neighbor
			//model.arithm(pieces[i][this.grid.width-1].mod(2).intVar(),"=",pieces[i-1][this.grid.width-1].mod(8).div(4).intVar()).post();
			//condition with south neighbor
			model.arithm(pieces[i][this.grid.width-1].mod(8).div(4).intVar(),"=",pieces[i+1][this.grid.width-1].mod(2).intVar()).post();
			//condition with west neighbor
			//model.arithm(pieces[i][this.grid.width-1].div(8).intVar(),"=",pieces[i][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();
		}
		
		//Constraints for others pieces in the grid
		for (int i = 1; i < this.grid.height-1; i++) {
			for (int j = 1; j < this.grid.width-1; j++) {				
				//condition with north neighbor
				//model.arithm(pieces[i][j].mod(2).intVar(),"=",pieces[i-1][j].mod(8).div(4).intVar()).post();
				//condition with east neighbor
				model.arithm(pieces[i][j].mod(8).mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
				//condition with south neighbor
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
				//condition with west neighbor
				//model.arithm(pieces[i][j].div(8).intVar(),"=",pieces[i][j-1].mod(8).mod(4).div(2).intVar()).post();	
			}
		}
		Solver solver = model.getSolver();
		boolean resolution = solver.solve();	
		this.translate(pieces);
		return resolution;
	}
	
	/**
	 * Try to find if all pieces can be not linked to each other
	 * @return if it's can be solve or not
	 */
	public boolean nosolve() {
		Model model = new Model("choco no solver");		
		IntVar[][]pieces = new IntVar[this.grid.height][this.grid.width];
		//init variables to IntVar 
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				//define possible values considering type and postion on the grid of a piece
				switch(this.grid.cases[i][j].type) {
				case 0 :
					pieces[i][j] = model.intVar(new int[] {0});
					break;
				case 1 :
					pieces[i][j] = model.intVar(new int[] {1,2,4,8});
					break;
				case 2 :
					pieces[i][j] = model.intVar(new int[] {5,10});
					break;
				case 3 :
					pieces[i][j] = model.intVar(new int[] {7,14,13,11});
					break;
				case 4 :
					pieces[i][j] = model.intVar(new int[] {15});
					break;
				case 5 :
					pieces[i][j] = model.intVar(new int[] {3,6,12,9});
					break;
				}
			}			
		}
		
		//Constraints : check two neighbors have to be linked considered their values
		// For that we just have to check bit value of the piece and the bit value corresponding
		//to the link with the right neighbor
		// North : value % 2
		//East : value % 4 / 2
		//South : value % 8 / 4
		//West : value / 8 
		
		//Constraints for corner pieces
		
		// Piece corner NW
		//condition with north neighbor
		//model.arithm(pieces[0][0].mod(2).intVar(), "=", 0).post();
		//condition with east neighbor
		model.arithm(pieces[0][0].mod(8).mod(4).div(2).intVar(),"*",pieces[0][1].div(8).intVar(),"=",0).post();
		//condition with south neighbor
		model.arithm(pieces[0][0].mod(8).div(4).intVar(),"*",pieces[1][0].mod(2).intVar(),"=",0).post();
		//condition with west neighbor
		//model.arithm(pieces[0][0].div(8).intVar(),"=",0).post();
		
		// Piece corner NE
		//condition with north neighbor
		//model.arithm(pieces[0][this.grid.width-1].mod(2).intVar(), "=", 0).post();
		//condition with south neighbor
		model.arithm(pieces[0][this.grid.width-1].mod(8).div(4).intVar(),"*",pieces[1][this.grid.width-1].mod(2).intVar(),"=",0).post();
		//condition with west neighbor
		//model.arithm(pieces[0][this.grid.width-1].div(8).intVar(),"*",pieces[0][this.grid.width-2].mod(8).mod(4).div(2).intVar(),"=",0).post();
		
		// Piece corner SW
		//condition with north neighbor
		//model.arithm(pieces[this.grid.height-1][0].mod(2).intVar(),"*",pieces[this.grid.height-2][0].mod(8).div(4).intVar(),"=",0).post();
		//condition with east neighbor
		model.arithm(pieces[this.grid.height-1][0].mod(8).mod(4).div(2).intVar(),"*",pieces[this.grid.height-1][1].div(8).intVar(),"=",0).post();
		
		// Piece corner SE
		//condition with north neighbor
		//model.arithm(pieces[this.grid.height-1][this.grid.width-1].mod(2).intVar(),"*",pieces[this.grid.height-2][this.grid.width-1].mod(8).div(4).intVar(),"=",0).post();
		//condition with west neighbor
		//model.arithm(pieces[this.grid.height-1][this.grid.width-1].div(8).intVar(),"*",pieces[this.grid.height-1][this.grid.width-2].mod(8).mod(4).div(2).intVar(),"=",0).post();

		
		//Constraints for border pieces north and south
		for (int j = 1; j < this.grid.width-1; j++) {
			//North border
			//condition with south neighbor
			model.arithm(pieces[0][j].mod(8).div(4).intVar(),"*",pieces[1][j].mod(2).intVar(),"=",0).post();
			//condition with east neighbor
			model.arithm(pieces[0][j].mod(8).mod(4).div(2).intVar(),"*",pieces[0][j+1].div(8).intVar(),"=",0).post();
			//condition with west neighbor
			//model.arithm(pieces[0][j].div(8).intVar(),"!=",pieces[0][j-1].mod(8).mod(4).div(2).intVar()).post();
			
			//South border
			//condition with north neighbor
			//model.arithm(pieces[this.grid.height-1][j].mod(2).intVar(),"!=",pieces[this.grid.height-2][j].mod(8).div(4).intVar()).post();
			//condition with east neighbor
			model.arithm(pieces[this.grid.height-1][j].mod(8).mod(4).div(2).intVar(),"*",pieces[this.grid.height-1][j+1].div(8).intVar(),"=",0).post();
			//condition with west neighbor
			//model.arithm(pieces[this.grid.height-1][j].div(8).intVar(),"!=",pieces[this.grid.height-1][j-1].mod(8).mod(4).div(2).intVar()).post();	
		}
		
		//Constraints for west and east borders
		for (int i = 1; i < this.grid.height-1; i++) {
			//West border
			//condition with north neighbor
			//model.arithm(pieces[i][0].mod(2).intVar(),"!=",pieces[i-1][0].mod(8).div(4).intVar()).post();
			//condition with east neighbor
			model.arithm(pieces[i][0].mod(8).mod(4).div(2).intVar(),"*",pieces[i][1].div(8).intVar(),"=",0).post();
			//condition with south neighbor
			model.arithm(pieces[i][0].mod(8).div(4).intVar(),"*",pieces[i+1][0].mod(2).intVar(),"=",0).post();
			
			//East border
			//condition with north neighbor
			//model.arithm(pieces[i][this.grid.width-1].mod(2).intVar(),"=",pieces[i-1][this.grid.width-1].mod(8).div(4).intVar()).post();
			//condition with south neighbor
			model.arithm(pieces[i][this.grid.width-1].mod(8).div(4).intVar(),"*",pieces[i+1][this.grid.width-1].mod(2).intVar(),"=",0).post();
			//condition with west neighbor
			//model.arithm(pieces[i][this.grid.width-1].div(8).intVar(),"!=",pieces[i][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();
		}
		
		//Constraints for others pieces in the grid
		for (int i = 1; i < this.grid.height-1; i++) {
			for (int j = 1; j < this.grid.width-1; j++) {				
				//condition with north neighbor
				//model.arithm(pieces[i][j].mod(2).intVar(),"!=",pieces[i-1][j].mod(8).div(4).intVar()).post();
				//condition with east neighbor
				model.arithm(pieces[i][j].mod(8).mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
				//condition with south neighbor
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
				//condition with west neighbor
				//model.arithm(pieces[i][j].div(8).intVar(),"!=",pieces[i][j-1].mod(8).mod(4).div(2).intVar()).post();	
			}
		}
		Solver solver = model.getSolver();
		boolean resolution = solver.solve();	
		this.translate(pieces);
		return resolution;
	}
	

	/**
	 * Method to translate the piece values to cases of the grid 
	 * Value 0 : Type 0
	 * Value 1 : Type 1 orientation 0
	 * Value 2 : Type 1 orientation 1
	 * Value 4 : Type 1 orientation 2
	 * Value 8 : Type 1 orientation 3
	 * Value 5 : Type 2 orientation 0
	 * Value 10 : Type 2 orientation 1
	 * Value 11 : Type 3 orientation 0
	 * Value 7 : Type 3 orientation 1
	 * Value 14 : Type 3 orientation 2
	 * Value 13 : Type 3 orientation 3
	 * Value 15 : Type 4 
	 * Value 3 : Type 5 orientation 0
	 * Value 6 : Type 5 orientation 1
	 * Value 12 : Type 5 orientation 2
	 * Value 9 : Type 5 orientation 3
	 */

	public void translate(IntVar[][]pieces) {
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				switch(pieces[i][j].getValue()) {
				case 0 : case 15 :
					break;
				case 1 : case 5 : case 11 : case 3 :
					this.grid.cases[i][j].orientation = 0;
					break;
				case 2 : case 10 : case 7 : case 6 :
					this.grid.cases[i][j].orientation = 1;
					break;
				case 4 : case 14 : case 12 :
					this.grid.cases[i][j].orientation = 2;
					break;
				case 8 : case 13 : case 9 :
					this.grid.cases[i][j].orientation = 3;
					break;
				}
			}
		}
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
		return ( i==0 && j==this.grid.width-1 );		
	}
	
	/**
	 * Says if a piece in the south west side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south west
	 */
	public boolean southWestSide(int i, int j) {
		return ( i==this.grid.height-1 && j==0 );		
	}
	
	/**
	 * Says if a piece in the south east side of the grid
	 * @param x
	 * @param y
	 * @return true if in the south east
	 */
	public boolean southEastSide(int i, int j) {
		return ( i==this.grid.height-1 && j==this.grid.width-1 );		
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
		return ( j==this.grid.width-1 );		
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
		return ( i==this.grid.height-1 );		
	}
	
	public static void main(String[] args) {
		LevelGenerator test = new LevelGenerator(20, 20);
		test.buildSolution();
		test.shuffleSolution();
		LevelSolverIA sol = new LevelSolverIA(test.grid);
		System.out.println("Solution after solver : " + sol.nosolve());
		//sol.grid.displayInConsole();
		LevelDisplay display = new LevelDisplay(test, sol.grid);

	}
}	
	

