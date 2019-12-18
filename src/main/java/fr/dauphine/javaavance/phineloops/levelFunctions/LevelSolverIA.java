package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.io.IOException;
import java.util.ArrayList;

import org.chocosolver.solver.ICause;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class LevelSolverIA {
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
	
	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public int getTotalPiece() {
		return totalPiece;
	}

	public void setTotalPiece(int totalPiece) {
		this.totalPiece = totalPiece;
	}

	public int getLockedPiece() {
		return lockedPiece;
	}

	public void setLockedPiece(int lockedPiece) {
		this.lockedPiece = lockedPiece;
	}

	public LevelSolverIA(Grid grid) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
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
		int tmp = -1;
		while(lockedPiece!= tmp){
			tmp = lockedPiece;
			lockedPiece = this.lockPiece();
		}

		if( lockedPiece == totalPiece ) {
			return true;
		}
		Model model = new Model("choco solver");		
		IntVar[][]pieces = new IntVar[this.grid.getHeight()][this.grid.getWidth()];
		//init variables to IntVar 
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
				//define possible values considering type and postion on the grid of a piece
				switch(this.grid.getCases()[i][j].getType()) {
				case 0 :
					pieces[i][j] = model.intVar(new int[] {0});
					break;
				case 1 :
					if (this.grid.getCases()[i][j].getLock() == 1) {
						switch(this.grid.getCases()[i][j].getOrientation()) {
						case 0 : 
							pieces[i][j] = model.intVar(new int[] {1});
							break;
						case 1 : 
							pieces[i][j] = model.intVar(new int[] {2});
							break;
						case 2 : 
							pieces[i][j] = model.intVar(new int[] {4});
							break;
						case 3 : 
							pieces[i][j] = model.intVar(new int[] {8});
							break;
						}
					}
					if (this.grid.northWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {2,4});
						break;
					}
					if (this.grid.northEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {4,8});
						break;
					}
					if (this.grid.southWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {1,2});
						break;
					}
					if (this.grid.southEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {1,8});
						break;
					}
					if (this.grid.northBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {2,4,8});
						break;
					}
					if (this.grid.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {1,2,8});
						break;
					}
					if (this.grid.westBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {1,2,4});
						break;
					}
					if (this.grid.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {1,4,8});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {1,2,4,8});
					break;
				case 2 :
					if (this.grid.getCases()[i][j].getLock() == 1) {
						switch(this.grid.getCases()[i][j].getOrientation()) {
						case 0 : 
							pieces[i][j] = model.intVar(new int[] {5});
							break;
						case 1 : 
							pieces[i][j] = model.intVar(new int[] {10});
							break;
						}
					}
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
						return false;
					}
					if (this.grid.northBorder(i)||this.grid.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {10});
						break;
					}
					if (this.grid.westBorder(j)||this.grid.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {5});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {5,10});
					break;
				case 3 :
					if (this.grid.getCases()[i][j].getLock() == 1) {
						switch(this.grid.getCases()[i][j].getOrientation()) {
						case 0 : 
							pieces[i][j] = model.intVar(new int[] {11});
							break;
						case 1 : 
							pieces[i][j] = model.intVar(new int[] {7});
							break;
						case 2 : 
							pieces[i][j] = model.intVar(new int[] {14});
							break;
						case 3 : 
							pieces[i][j] = model.intVar(new int[] {13});
							break;
						}
					}
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
						return false;
					}
					if (this.grid.northBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {14});
						break;
					}
					if (this.grid.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {11});
						break;
					}
					if (this.grid.westBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {7});
						break;
					}
					if (this.grid.eastBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {13});
						break;
					}
					pieces[i][j] = model.intVar(new int[] {7,14,13,11});
					break;
				case 4 :
					if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
						return false;
					}
					pieces[i][j] = model.intVar(new int[] {15});
					break;
				case 5 :
					if (this.grid.getCases()[i][j].getLock() == 1) {
						switch(this.grid.getCases()[i][j].getOrientation()) {
						case 0 : 
							pieces[i][j] = model.intVar(new int[] {3});
							break;
						case 1 : 
							pieces[i][j] = model.intVar(new int[] {6});
							break;
						case 2 : 
							pieces[i][j] = model.intVar(new int[] {12});
							break;
						case 3 : 
							pieces[i][j] = model.intVar(new int[] {9});
							break;
						}
					}
					if (this.grid.northWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {6});
						break;
					}
					if (this.grid.northEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {12});
						break;
					}
					if (this.grid.southWestSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {3});
						break;
					}
					if (this.grid.southEastSide(i, j)) {
						pieces[i][j] = model.intVar(new int[] {9});
						break;
					}
					if (this.grid.northBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {6,12});
						break;
					}
					if (this.grid.southBorder(i)) {
						pieces[i][j] = model.intVar(new int[] {3,9});
						break;
					}
					if (this.grid.westBorder(j)) {
						pieces[i][j] = model.intVar(new int[] {3,6});
						break;
					}
					if (this.grid.eastBorder(j)) {
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
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(8).div(4).intVar(),"=",pieces[1][this.grid.getWidth()-1].mod(2).intVar()).post();
		//condition with west neighbor
		//model.arithm(pieces[0][this.grid.width-1].div(8).intVar(),"=",pieces[0][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();
		
		// Piece corner SW
		//condition with north neighbor
		//model.arithm(pieces[this.grid.height-1][0].mod(2).intVar(),"=",pieces[this.grid.height-2][0].mod(8).div(4).intVar()).post();
		//condition with east neighbor
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(8).mod(4).div(2).intVar(),"=",pieces[this.grid.getHeight()-1][1].div(8).intVar()).post();
		
		// Piece corner SE
		//condition with north neighbor
		model.arithm(pieces[this.grid.getHeight()-1][this.grid.getWidth()-1].mod(2).intVar(),"=",pieces[this.grid.getHeight()-2][this.grid.getWidth()-1].mod(8).div(4).intVar()).post();
		//condition with west neighbor
		//model.arithm(pieces[this.grid.height-1][this.grid.width-1].div(8).intVar(),"=",pieces[this.grid.height-1][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();

		
		//Constraints for border pieces north and south
		for (int j = 1; j < this.grid.getWidth()-1; j++) {
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
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(8).mod(4).div(2).intVar(),"=",pieces[this.grid.getHeight()-1][j+1].div(8).intVar()).post();
			//condition with west neighbor
			//model.arithm(pieces[this.grid.height-1][j].div(8).intVar(),"=",pieces[this.grid.height-1][j-1].mod(8).mod(4).div(2).intVar()).post();	
		}
		
		//Constraints for west and east borders
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
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
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(8).div(4).intVar(),"=",pieces[i+1][this.grid.getWidth()-1].mod(2).intVar()).post();
			//condition with west neighbor
			//model.arithm(pieces[i][this.grid.width-1].div(8).intVar(),"=",pieces[i][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();
		}
		
		//Constraints for others pieces in the grid
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
			for (int j = 1; j < this.grid.getWidth()-1; j++) {				
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
		IntVar[][]pieces = new IntVar[this.grid.getHeight()][this.grid.getWidth()];
		//init variables to IntVar 
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
				//define possible values considering type and postion on the grid of a piece
				switch(this.grid.getCases()[i][j].getType()) {
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
		model.arithm(pieces[0][this.grid.getWidth()-1].mod(8).div(4).intVar(),"*",pieces[1][this.grid.getWidth()-1].mod(2).intVar(),"=",0).post();
		//condition with west neighbor
		//model.arithm(pieces[0][this.grid.width-1].div(8).intVar(),"*",pieces[0][this.grid.width-2].mod(8).mod(4).div(2).intVar(),"=",0).post();
		
		// Piece corner SW
		//condition with north neighbor
		//model.arithm(pieces[this.grid.height-1][0].mod(2).intVar(),"*",pieces[this.grid.height-2][0].mod(8).div(4).intVar(),"=",0).post();
		//condition with east neighbor
		model.arithm(pieces[this.grid.getHeight()-1][0].mod(8).mod(4).div(2).intVar(),"*",pieces[this.grid.getHeight()-1][1].div(8).intVar(),"=",0).post();
		
		// Piece corner SE
		//condition with north neighbor
		//model.arithm(pieces[this.grid.height-1][this.grid.width-1].mod(2).intVar(),"*",pieces[this.grid.height-2][this.grid.width-1].mod(8).div(4).intVar(),"=",0).post();
		//condition with west neighbor
		//model.arithm(pieces[this.grid.height-1][this.grid.width-1].div(8).intVar(),"*",pieces[this.grid.height-1][this.grid.width-2].mod(8).mod(4).div(2).intVar(),"=",0).post();

		
		//Constraints for border pieces north and south
		for (int j = 1; j < this.grid.getWidth()-1; j++) {
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
			model.arithm(pieces[this.grid.getHeight()-1][j].mod(8).mod(4).div(2).intVar(),"*",pieces[this.grid.getHeight()-1][j+1].div(8).intVar(),"=",0).post();
			//condition with west neighbor
			//model.arithm(pieces[this.grid.height-1][j].div(8).intVar(),"!=",pieces[this.grid.height-1][j-1].mod(8).mod(4).div(2).intVar()).post();	
		}
		
		//Constraints for west and east borders
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
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
			model.arithm(pieces[i][this.grid.getWidth()-1].mod(8).div(4).intVar(),"*",pieces[i+1][this.grid.getWidth()-1].mod(2).intVar(),"=",0).post();
			//condition with west neighbor
			//model.arithm(pieces[i][this.grid.width-1].div(8).intVar(),"!=",pieces[i][this.grid.width-2].mod(8).mod(4).div(2).intVar()).post();
		}
		
		//Constraints for others pieces in the grid
		for (int i = 1; i < this.grid.getHeight()-1; i++) {
			for (int j = 1; j < this.grid.getWidth()-1; j++) {				
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
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
				switch(pieces[i][j].getValue()) {
				case 0 : case 15 :
					break;
				case 1 : case 5 : case 11 : case 3 :
					this.grid.getCases()[i][j].setOrientation(0);
					break;
				case 2 : case 10 : case 7 : case 6 :
					this.grid.getCases()[i][j].setOrientation(1);
					break;
				case 4 : case 14 : case 12 :
					this.grid.getCases()[i][j].setOrientation(2);
					break;
				case 8 : case 13 : case 9 :
					this.grid.getCases()[i][j].setOrientation(3);
					break;
				}
				this.grid.getCases()[i][j].defineLinks();
			}
		}
	}
	
	public int lockPiece() {
		int countLockedPiece = 0;	
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
		
				if(grid.getCases()[i][j].getLock()==0) {

					ArrayList<Integer> orientationPossible = new ArrayList<>();
					
					if (grid.northWestSide(i, j)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) { 
							if( grid.getCases()[i][j].getLinks()[0]==0 && grid.getCases()[i][j].getLinks()[3]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}	
					else if (grid.northEastSide(i, j)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[0]==0 && grid.getCases()[i][j].getLinks()[1]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}					
					else if (grid.southWestSide(i, j)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[2]==0 && grid.getCases()[i][j].getLinks()[3]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}
					else if (grid.southEastSide(i, j)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[2]==0 && grid.getCases()[i][j].getLinks()[1]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}			
					else if (grid.northBorder(i)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[0]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}
					else if (grid.southBorder(i)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[2]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}
					else if (grid.westBorder(j)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[3]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}	
					else if (grid.eastBorder(j)) {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if( grid.getCases()[i][j].getLinks()[1]==0 && !grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}
					else {
						for(int k=0; k<grid.getCases()[i][j].getNbRotation(); k++) {
							if(!grid.noRespectedLockPiece(grid.getCases()[i][j])) 
								orientationPossible.add(grid.getCases()[i][j].getOrientation());
							grid.getCases()[i][j].rotatePiece();
						}
					}
					if( orientationPossible.size() == 1) {
						grid.getCases()[i][j].setOrientation(orientationPossible.get(0));
						grid.getCases()[i][j].defineLinks();
						grid.getCases()[i][j].setLock(1);
					}
					else 
						grid.getCases()[i][j].setPossibleOrientations(orientationPossible);	
				}
				countLockedPiece += grid.getCases()[i][j].getLock();
			}			
		}
		return countLockedPiece;
	}
	
	public static void main(String[] args) throws IOException {
		LevelGenerator test = new LevelGenerator(25, 25);
		test.buildSolution();
		test.shuffleSolution();
		//Grid grid = test.grid;
		//grid.writeFile("file5");
	   	//Grid grid2 = Grid.readFile("file5");
		long debut = System.currentTimeMillis();
		LevelSolverIA sol = new LevelSolverIA(test.getGrid());
		System.out.println("Solution after solver : " + sol.solve());
		long fin = System.currentTimeMillis();
		System.out.println(fin-debut);
		sol.grid.displayInConsole();
		LevelDisplay display = new LevelDisplay(test, sol.grid);

	}
}	
	

