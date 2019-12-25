package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.io.IOException;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.ParallelPortfolio;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;
import fr.dauphine.javaavance.phineloops.utils.Read;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class LevelSolverIA {
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
	private int threads = 1;

	public LevelSolverIA(Grid grid) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
	}
	
	public LevelSolverIA(Grid grid, int threads) {
		this.grid = grid;
		this.threads = threads;
		this.totalPiece = grid.getHeight()*grid.getWidth();
	}
	
	public Grid getGrid() {
		return grid;
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
			lockedPiece = this.grid.lockPiece();
			if (lockedPiece == -2) {
				return false;
			}
		}
		if( lockedPiece == totalPiece ) {
			return true;
		}
		
		IntVar[][]pieces = new IntVar[this.grid.getHeight()][this.grid.getWidth()];
		ParallelPortfolio portfolio = new ParallelPortfolio();

		int nbmodels = 1;
		if (this.threads > 1) {
			nbmodels = this.threads;
		}
		for (int k=0; k<nbmodels; k++) {
			Model model = new Model("choco solver");		
			//init variables to IntVar 
			for (int i = 0; i < this.grid.getHeight(); i++) {
				for (int j = 0; j < this.grid.getWidth(); j++) {
					pieces[i][j] = this.defineDomain(this.grid.getCases()[i][j], i, j, model);
					if (pieces[i][j] == null) {
						return false;
					}
				}			
			}
			
			for (int i = 0; i < this.grid.getHeight(); i++) {
				for (int j = 0; j < this.grid.getWidth(); j++) {
					this.defineConstraintsSolve(pieces, i, j, model);
				}
			}
			portfolio.addModel(model);
		}
		if (portfolio.solve()) {
			for (int i = 0; i < this.totalPiece; i++) {
				pieces[i/this.grid.getHeight()][i % this.grid.getWidth()] = (IntVar) portfolio.getBestModel().getVar(i);
				this.translate(pieces);
			}
			this.translate(pieces);
			return true;	
		}
		return false;
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
				pieces[i][j] = this.defineDomain(this.grid.getCases()[i][j], -1, -1, model);	
			}			
		}
		for (int i = 0; i < this.grid.getHeight(); i++) {
			for (int j = 0; j < this.grid.getWidth(); j++) {
				this.defineConstraintsNoSolve(pieces, i, j, model);
			}
		}
		Solver solver = model.getSolver();
		boolean resolution = solver.solve();	
		this.translate(pieces);
		return resolution;
	}
	
	/**
	 * Define a domain of a piece considered its type if its locked and considered its orientation
	 * for nosolve method just put i =-1 and j = -1 to define domain
	 * @param p
	 * @param i	
	 * @param j
	 * @param model
	 * @return the domain or null if its a case of impossible solve
	 */
	public IntVar defineDomain (Piece p, int i, int j, Model model) {
		switch(p.getType()) {
		case 0 :
			return model.intVar(new int[] {0});
		case 1 :
			if (p.getLock() == 1) {
				switch(p.getOrientation()) {
				case 0 : 
					return model.intVar(new int[] {1});
				case 1 : 
					return model.intVar(new int[] {2});
				case 2 : 
					return model.intVar(new int[] {4});
				case 3 : 
					return model.intVar(new int[] {8});
				}
			}
			if (this.grid.getHeight() == 2 && this.grid.getWidth() == 1) {
				if (this.grid.northWestSide(i, j)) {
					return model.intVar(new int[] {4});
				}
				
				if (this.grid.southWestSide(i, j)) {
					return model.intVar(new int[] {1});
				}				
			}
			
			if (this.grid.getWidth() == 2 && this.grid.getHeight() == 1) {
				if (this.grid.northWestSide(i, j)) {
					return model.intVar(new int[] {2});
				}
				
				if (this.grid.northEastSide(i, j)) {
					return model.intVar(new int[] {8});
				}
				
			}
			if (this.grid.northWestSide(i, j)) {
				return model.intVar(new int[] {2,4});
			}
			if (this.grid.northEastSide(i, j)) {
				return model.intVar(new int[] {4,8});
			}
			if (this.grid.southWestSide(i, j)) {
				return model.intVar(new int[] {1,2});
			}
			if (this.grid.southEastSide(i, j)) {
				return model.intVar(new int[] {1,8});
			}
			if (this.grid.northBorder(i)) {
				return model.intVar(new int[] {2,4,8});
			}
			if (this.grid.southBorder(i)) {
				return model.intVar(new int[] {1,2,8});
			}
			if (this.grid.westBorder(j)) {
				return model.intVar(new int[] {1,2,4});
			}
			if (this.grid.eastBorder(j)) {
				return model.intVar(new int[] {1,4,8});
			}
			return model.intVar(new int[] {1,2,4,8});
			
		case 2 :
			if (p.getLock() == 1) {
				switch(p.getOrientation()) {
				case 0 : 
					return model.intVar(new int[] {5});
				case 1 : 
					return model.intVar(new int[] {10});
				}
			}
			if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
				return null;
			}
			if (this.grid.northBorder(i)||this.grid.southBorder(i)) {
				return model.intVar(new int[] {10});
			}
			if (this.grid.westBorder(j)||this.grid.eastBorder(j)) {
				return model.intVar(new int[] {5});
			}
			return model.intVar(new int[] {5,10});

		case 3 :
			if (p.getLock() == 1) {
				switch(p.getOrientation()) {
				case 0 : 
					return model.intVar(new int[] {11});
				case 1 : 
					return model.intVar(new int[] {7});
				case 2 : 
					return model.intVar(new int[] {14});
				case 3 : 
					return model.intVar(new int[] {13});
				}
			}
			if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
				return null;
			}
			if (this.grid.northBorder(i)) {
				return model.intVar(new int[] {14});
			}
			if (this.grid.southBorder(i)) {
				return model.intVar(new int[] {11});
			}
			if (this.grid.westBorder(j)) {
				return model.intVar(new int[] {7});
			}
			if (this.grid.eastBorder(j)) {
				return model.intVar(new int[] {13});
			}
			return model.intVar(new int[] {7,14,13,11});
			
		case 4 :
			if (this.grid.northWestSide(i, j)||this.grid.northEastSide(i, j)||this.grid.southWestSide(i, j)||this.grid.southEastSide(i, j)) {
				return null;
			}
			return model.intVar(new int[] {15});
		case 5 :
			if (p.getLock() == 1) {
				switch(p.getOrientation()) {
				case 0 : 
					return model.intVar(new int[] {3});
				case 1 : 
					return model.intVar(new int[] {6});
				case 2 : 
					return model.intVar(new int[] {12});
				case 3 : 
					return model.intVar(new int[] {9});
				}
			}
			if (this.grid.northWestSide(i, j)) {
				return model.intVar(new int[] {6});
			}
			if (this.grid.northEastSide(i, j)) {
				return model.intVar(new int[] {12});
			}
			if (this.grid.southWestSide(i, j)) {
				return model.intVar(new int[] {3});
			}
			if (this.grid.southEastSide(i, j)) {
				return model.intVar(new int[] {9});
			}
			if (this.grid.northBorder(i)) {
				return model.intVar(new int[] {6,12});
			}
			if (this.grid.southBorder(i)) {
				return model.intVar(new int[] {3,9});
			}
			if (this.grid.westBorder(j)) {
				return model.intVar(new int[] {3,6});
			}
			if (this.grid.eastBorder(j)) {
				return model.intVar(new int[] {12,9});
			}
			return model.intVar(new int[] {3,6,12,9});
		}
		return null;
	}
	
	/**
	 * Define constraints to check if two neighbors have to be linked considered their values
	 * For that we just have to check bit value of the piece and the bit value corresponding to the link with the right neighbor
	 * North : value % 2
	 * East : value % 4 / 2
	 * South : value % 8 / 4
	 * West : value / 8 
	 * @param pieces
	 * @param i
	 * @param j
	 */
	public void defineConstraintsSolve (IntVar[][] pieces, int i, int j, Model model) {
		if (this.grid.northWestSide(i, j)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
			}
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
			}
			return;
		}
		if (this.grid.northEastSide(i, j)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
			}
			return;
		}
		if (this.grid.southWestSide(i, j)) {
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
			}
			return;
		}
		if (this.grid.southEastSide(i, j)) {
			return;
		}
		if (this.grid.northBorder(i)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
			}
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
			}
			return;
		}
		if (this.grid.southBorder(i)) {
			//condition with east neighbor
			if (this.grid.getWidth() >= 2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
			}
			return;
		}
		if (this.grid.westBorder(j)) {
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
			}
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
			}
			return;
		}
		if (this.grid.eastBorder(j)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
			}
			return;
		}
		//condition with east neighbor
		model.arithm(pieces[i][j].mod(4).div(2).intVar(),"=",pieces[i][j+1].div(8).intVar()).post();
		//condition with south neighbor
		model.arithm(pieces[i][j].mod(8).div(4).intVar(),"=",pieces[i+1][j].mod(2).intVar()).post();
		return;
	}
	
	/**
	 * Define constraints to check if two neighbors are not linked considered their values
	 * For that we just have to check bit value of the piece and the bit value corresponding to the link with the right neighbor
	 * North : value % 2
	 * East : value % 4 / 2
	 * South : value % 8 / 4
	 * West : value / 8 
	 * @param pieces
	 * @param i
	 * @param j
	 */
	public void defineConstraintsNoSolve (IntVar[][] pieces, int i, int j, Model model) {
		if (this.grid.northWestSide(i, j)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
			}
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
			}
			return;
		}
		if (this.grid.northEastSide(i, j)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
			}
			return;
		}
		if (this.grid.southWestSide(i, j)) {
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
			}
			return;
		}
		if (this.grid.southEastSide(i, j)) {
			return;
		}
		if (this.grid.northBorder(i)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
			}
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
			}
			return;
		}
		if (this.grid.southBorder(i)) {
			//condition with east neighbor
			if (this.grid.getWidth() >= 2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
			}
			return;
		}
		if (this.grid.westBorder(j)) {
			//condition with east neighbor
			if (this.grid.getWidth() >=2) {
				model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
			}
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
			}
			return;
		}
		if (this.grid.eastBorder(j)) {
			//condition with south neighbor
			if (this.grid.getHeight() >=2) {
				model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
			}
			return;
		}
		//condition with east neighbor
		model.arithm(pieces[i][j].mod(4).div(2).intVar(),"*",pieces[i][j+1].div(8).intVar(),"=",0).post();
		//condition with south neighbor
		model.arithm(pieces[i][j].mod(8).div(4).intVar(),"*",pieces[i+1][j].mod(2).intVar(),"=",0).post();
		return;
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
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		LevelGenerator test = new LevelGenerator(100, 100);
		//test.buildSolution();
		//test.shuffleSolution();
		//Grid grid = test.getGrid();
		//Write.writeFile("file5", grid);
	   	Grid grid2 = Read.readFile("file5");
		long debut = System.currentTimeMillis();
		//LevelSolverIA sol = new LevelSolverIA(test.getGrid(), 3);
		LevelSolverIA sol = new LevelSolverIA(grid2, 1);
		System.out.println("Solution after solver : " + sol.solve());
		//System.out.println(sol.grid.getCases()[0][0].toString());
		long fin = System.currentTimeMillis();
		System.out.println(fin-debut);
		//sol.grid.displayInConsole();
		LevelDisplay display = new LevelDisplay(test, sol.grid);

	}
}