package fr.dauphine.javaavance.phineloops;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.Grid;

public class SolverGrid {
	Grid grid;
	public SolverGrid(Grid grid) {
		this.grid = grid;
	}
	
	/** Solve grid with chocosolver
	 * Define 4 kinds of pairs
	 * For (i,j) 
	 * define a pair with its right neighbor
	 * define a pair with its left neighbor
	 * define a pair with its top neighbor
	 * define a pair with its bottom neighbor
	 * 
	 * For a side piece, sides not existant neighbors are as well defined
	 * 
	 * A pair as value 0 or 1, 0 if pieces of the pair are not connected, 1 else
	 */
	public void solve() {
		Model model = new Model("choco solver");		
		IntVar[][]pairsrightline = new IntVar[this.grid.height][this.grid.width];
		IntVar[][]pairsleftline = new IntVar[this.grid.height][this.grid.width];
		IntVar[][]pairstopcol = new IntVar[this.grid.height][this.grid.width];
		IntVar[][]pairsbotcol = new IntVar[this.grid.height][this.grid.width];
		
		//init variables to IntVar 
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				IntVar a = model.intVar(new int[]{0,1});
				IntVar b = model.intVar(new int[]{0,1});
				IntVar c = model.intVar(new int[]{0,1});
				IntVar d = model.intVar(new int[]{0,1});
				pairsrightline[i][j] = a;
				pairsleftline[i][j] = b;
				pairstopcol[i][j] = c;
				pairsbotcol[i][j] = d;
			}			
		}
		
		//Constraints
		
		//Corner Piece Constraints
		
		// Piece (0,0) constraints
		
		// define no links with north and west neighbors
		model.arithm(pairstopcol[0][0],"=",0).post();
		model.arithm(pairsleftline[0][0],"=",0).post();
		
		// constraint to impose the numbers of links necessited considered type of piece 
		IntVar[]bloc = new IntVar[2];
		bloc[0] = pairsrightline[0][0];
		bloc[1] = pairsbotcol[0][0];
		model.sum(bloc, "=", this.grid.cases[0][0].nbneighbors).post();
		
		// constraint to impose same value because this two pairs have to have same value because is the same link
		model.arithm(pairsrightline[0][0],"=",pairsleftline[0][1]).post();
		// constraint to impose same value because this two pairs have to have same value because is the same link
		model.arithm(pairsbotcol[0][0],"=",pairstopcol[1][0]).post();
		//define constraint of the shape considered type of the piece
		this.defineConstraintType(this.grid.cases[0][0], model, pairstopcol[0][0], pairsrightline[0][0], pairsbotcol[0][0], pairsleftline[0][0]);
		
		//Same idea for piece (0,width-1),(height-1,0),(height-1,width-1)
		model.arithm(pairstopcol[0][this.grid.width-1],"=",0).post();
		model.arithm(pairsrightline[0][this.grid.width-1],"=",0).post();
		bloc[0] = pairsleftline[0][this.grid.width-1];
		bloc[1] = pairsbotcol[0][this.grid.width-1];
		model.sum(bloc, "=", this.grid.cases[0][this.grid.width-1].nbneighbors).post();
		
		model.arithm(pairsleftline[0][this.grid.width-1],"=",pairsrightline[0][this.grid.width-2]).post();
		model.arithm(pairsbotcol[0][this.grid.width-1],"=",pairstopcol[1][this.grid.width-1]).post();
		this.defineConstraintType(this.grid.cases[0][this.grid.width-1], model, pairstopcol[0][this.grid.width-1], pairsrightline[0][this.grid.width-1], pairsbotcol[0][this.grid.width-1], pairsleftline[0][this.grid.width-1]);
		
		model.arithm(pairsbotcol[this.grid.height-1][0],"=",0).post();
		model.arithm(pairsleftline[this.grid.height-1][0],"=",0).post();
		bloc[0] = pairsrightline[this.grid.height-1][0];
		bloc[1] = pairstopcol[this.grid.height-1][0];
		model.sum(bloc, "=", this.grid.cases[this.grid.height-1][0].nbneighbors).post();
		
		model.arithm(pairsrightline[this.grid.height-1][0],"=",pairsleftline[this.grid.height-1][1]).post();
		model.arithm(pairstopcol[this.grid.height-1][0],"=",pairsbotcol[this.grid.height-2][0]).post();
		this.defineConstraintType(this.grid.cases[this.grid.height-1][0], model, pairstopcol[this.grid.height-1][0], pairsrightline[this.grid.height-1][0], pairsbotcol[this.grid.height-1][0], pairsleftline[this.grid.height-1][0]);

		model.arithm(pairsbotcol[this.grid.height-1][this.grid.width-1],"=",0).post();
		model.arithm(pairsrightline[this.grid.height-1][this.grid.width-1],"=",0).post();
		bloc[0] = pairsleftline[this.grid.height-1][this.grid.width-1];
		bloc[1] = pairstopcol[this.grid.height-1][this.grid.width-1];
		model.sum(bloc, "=", this.grid.cases[this.grid.height-1][this.grid.width-1].nbneighbors).post();
		
		model.arithm(pairsleftline[this.grid.height-1][this.grid.width-1],"=",pairsrightline[this.grid.height-1][this.grid.width-2]).post();
		model.arithm(pairstopcol[this.grid.height-1][this.grid.width-1],"=",pairsbotcol[this.grid.height-2][this.grid.width-1]).post();
		this.defineConstraintType(this.grid.cases[this.grid.height-1][this.grid.width-1], model, pairstopcol[this.grid.height-1][this.grid.width-1], pairsrightline[this.grid.height-1][this.grid.width-1], pairsbotcol[this.grid.height-1][this.grid.width-1], pairsleftline[this.grid.height-1][this.grid.width-1]);
		
		//constraints for border pieces north and south
		for (int j = 1; j < this.grid.width-1; j++) {
			
			// define no links with north neighbor
			model.arithm(pairstopcol[0][j],"=",0).post();

			// constraint to impose the numbers of links necessited considered type of piece 
			IntVar[]block = new IntVar[3];
			block[0] = pairsrightline[0][j];
			block[1] = pairsleftline[0][j];
			block[2] = pairsbotcol[0][j];
			model.sum(block, "=", this.grid.cases[0][j].nbneighbors).post();
			
			//For north side
			// Each constraint : impose same value because this two pairs have to have same value because is the same link
			model.arithm(pairsrightline[0][j],"=",pairsleftline[0][j+1]).post();
			model.arithm(pairsleftline[0][j],"=",pairsrightline[0][j-1]).post();
			model.arithm(pairsbotcol[0][j],"=",pairstopcol[1][j]).post();
			this.defineConstraintType(this.grid.cases[0][j], model, pairstopcol[0][j], pairsrightline[0][j], pairsbotcol[0][j], pairsleftline[0][j]);
			
			//Same for south side
			model.arithm(pairsbotcol[this.grid.height-1][j],"=",0).post();
			block[0] = pairsrightline[this.grid.height-1][j];
			block[1] = pairsleftline[this.grid.height-1][j];
			block[2] = pairstopcol[this.grid.height-1][j];
			model.sum(block, "=", this.grid.cases[this.grid.height-1][j].nbneighbors).post();
			
			model.arithm(pairsrightline[this.grid.height-1][j],"=",pairsleftline[this.grid.height-1][j+1]).post();
			model.arithm(pairsleftline[this.grid.height-1][j],"=",pairsrightline[this.grid.height-1][j-1]).post();
			model.arithm(pairstopcol[this.grid.height-1][j],"=",pairsbotcol[this.grid.height-2][j]).post();
			this.defineConstraintType(this.grid.cases[this.grid.height-1][j], model, pairstopcol[this.grid.height-1][j], pairsrightline[this.grid.height-1][j], pairsbotcol[this.grid.height-1][j], pairsleftline[this.grid.height-1][j]);	
		}
		
		//Same idea for west and east borders
		for (int i = 1; i < this.grid.height-1; i++) {
			model.arithm(pairsleftline[i][0],"=",0).post();
			IntVar[]block = new IntVar[3];
			block[0] = pairsrightline[i][0];
			block[1] = pairstopcol[i][0];
			block[2] = pairsbotcol[i][0];
			model.sum(block, "=", this.grid.cases[i][0].nbneighbors).post();
			
			model.arithm(pairsrightline[i][0],"=",pairsleftline[i][1]).post();
			model.arithm(pairstopcol[i][0],"=",pairsbotcol[i-1][0]).post();
			model.arithm(pairsbotcol[i][0],"=",pairstopcol[i+1][0]).post();
			this.defineConstraintType(this.grid.cases[i][0], model, pairstopcol[i][0], pairsrightline[i][0], pairsbotcol[i][0], pairsleftline[i][0]);
			
			model.arithm(pairsrightline[i][this.grid.width-1],"=",0).post();
			block[0] = pairsleftline[i][this.grid.width-1];
			block[1] = pairstopcol[i][this.grid.width-1];
			block[2] = pairsbotcol[i][this.grid.width-1];
			model.sum(block, "=", this.grid.cases[i][this.grid.width-1].nbneighbors).post();
			model.arithm(pairsleftline[i][this.grid.width-1],"=",pairsrightline[i][this.grid.width-2]).post();
			model.arithm(pairstopcol[i][this.grid.width-1],"=",pairsbotcol[i-1][this.grid.width-1]).post();
			model.arithm(pairsbotcol[i][this.grid.width-1],"=",pairstopcol[i+1][this.grid.width-1]).post();
			this.defineConstraintType(this.grid.cases[i][this.grid.width-1], model, pairstopcol[i][this.grid.width-1], pairsrightline[i][this.grid.width-1], pairsbotcol[i][this.grid.width-1], pairsleftline[i][this.grid.width-1]);
		}
		
		//Same idea for others pieces in the grid
		for (int i = 1; i < this.grid.height-1; i++) {
			for (int j = 1; j < this.grid.width-1; j++) {
				
				IntVar[]block = new IntVar[4];
				block[0] = pairstopcol[i][j];
				block[1] = pairsrightline[i][j];
				block[2] = pairsbotcol[i][j];
				block[3] = pairsleftline[i][j];
				
				model.sum(block, "=", this.grid.cases[i][j].nbneighbors).post();
				model.arithm(pairsrightline[i][j],"=",pairsleftline[i][j+1]).post();
				model.arithm(pairsleftline[i][j],"=",pairsrightline[i][j-1]).post();
				model.arithm(pairstopcol[i][j],"=",pairsbotcol[i-1][j]).post();
				model.arithm(pairsbotcol[i][j],"=",pairstopcol[i+1][j]).post();
				this.defineConstraintType(this.grid.cases[i][j], model, pairstopcol[i][j], pairsrightline[i][j], pairsbotcol[i][j], pairsleftline[i][j]);
				
			}
		}
		
		//Constraints considered type of piece
		// Idea is to define with constraints shape of each piece considered
		/*for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				IntVar[]block = new IntVar[4];
				block[0] = pairstopcol[i][j];
				block[1] = pairsrightline[i][j];
				block[2] = pairsbotcol[i][j];
				block[3] = pairsleftline[i][j];
				switch (this.grid.cases[i][j].type) {
				
				case 2 :
					//Constraint to define a piece type 2
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=", 1));
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=", 1));
					break;
					
				case 5 :
					//Constraint to define a piece type 5
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=",0));
					model.ifOnlyIf(model.arithm(block[2],"=", 1), model.arithm(block[0],"=",0));
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=",0));
					model.ifOnlyIf(model.arithm(block[3],"=", 1), model.arithm(block[1],"=",0));
					break;
				}
			}
		}*/
		Solver solver = model.getSolver();
		System.out.println("Solution after solver : " + solver.solve());		
		this.translate(pairsrightline, pairsleftline, pairstopcol, pairsbotcol);
	}
	
	/**
	 * Define constraint for a piece considered its type
	 * Only necessary for type 2 and 5
	 * @param p
	 * @param model
	 * @param pairstopcol
	 * @param pairsrightline
	 * @param pairsbotcol
	 * @param pairsleftline
	 */
	public void defineConstraintType(Piece p, Model model, IntVar pairstopcol, IntVar pairsrightline, IntVar pairsbotcol, IntVar pairsleftline) {
		IntVar[]block = new IntVar[4];
		block[0] = pairstopcol;
		block[1] = pairsrightline;
		block[2] = pairsbotcol;
		block[3] = pairsleftline;
		if (p.type == 2) {
			model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=", 1));
			model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=", 1));
		}
		
		if (p.type == 5) {
			model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=",0));
			model.ifOnlyIf(model.arithm(block[2],"=", 1), model.arithm(block[0],"=",0));
			model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=",0));
			model.ifOnlyIf(model.arithm(block[3],"=", 1), model.arithm(block[1],"=",0));
		}
	}

	/**
	 * Method to traduce the pairs tab to cases of the grid 
	 * @param pairslinedroite
	 * @param pairslinegauche
	 * @param pairscolhaut
	 * @param pairscolbas
	 */
	public void translate(IntVar[][]pairslinedroite, IntVar[][]pairslinegauche, IntVar[][]pairscolhaut, IntVar[][]pairscolbas) {
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				//define links of a case
				this.grid.cases[i][j].links[0] = pairscolhaut[i][j].getValue();
				this.grid.cases[i][j].links[1] = pairslinedroite[i][j].getValue();
				
				this.grid.cases[i][j].links[2] = pairscolbas[i][j].getValue();
				this.grid.cases[i][j].links[3] = pairslinegauche[i][j].getValue();
				//find orientation considered links and type of the piece
				this.grid.cases[i][j].defineOrientation(this.grid.cases[i][j].type, this.grid.cases[i][j].links);
			}
		}
	}
	
	public static void main(String[] args) {
		LevelGenerator test = new LevelGenerator(20, 20);
		test.buildSolution();
		test.shuffleSolution();
		SolverGrid sol = new SolverGrid(test.grid);
		sol.solve();
		//sol.grid.displayInConsole();
		LevelDisplay display = new LevelDisplay(test, sol.grid);
	}
}	
	

