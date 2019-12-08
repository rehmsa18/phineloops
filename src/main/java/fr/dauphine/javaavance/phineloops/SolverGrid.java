package fr.dauphine.javaavance.phineloops;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import fr.dauphine.javaavance.phineloops.Grid;


public class SolverGrid {
	Grid grid;
	public SolverGrid(Grid grid) {
		this.grid = grid;
	}
	
	
	//old test solution for a 2*2 keep for the moment for tests
	/*public void solve0() {
		Model model = new Model("choco solver");

		IntVar[][]pairslinedroite = new IntVar[2][2];
		IntVar[][]pairslinegauche = new IntVar[2][2];
		IntVar[][]pairscolhaut = new IntVar[2][2];
		IntVar[][]pairscolbas = new IntVar[2][2];
				
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				IntVar a = model.intVar(new int[]{0,1});
				IntVar b = model.intVar(new int[]{0,1});
				IntVar c = model.intVar(new int[]{0,1});
				IntVar d = model.intVar(new int[]{0,1});
				pairslinedroite[i][j] = a;
				pairslinegauche[i][j] = b;
				pairscolhaut[i][j] = c;
				pairscolbas[i][j] = d;
			}	
		}
		model.arithm(pairscolhaut[0][0],"=",0).post();
		model.arithm(pairslinegauche[0][0],"=",0).post();
		IntVar[]bloc = new IntVar[2];
		bloc[0] = pairslinedroite[0][0];
		bloc[1] = pairscolbas[0][0];
		model.sum(bloc, "=", this.grid.cases[0][0].nbneighbors).post();
		model.arithm(pairslinedroite[0][0],"=",pairslinegauche[0][1]).post();
		model.arithm(pairscolbas[0][0],"=",pairscolhaut[1][0]).post();
		
		model.arithm(pairscolhaut[0][1],"=",0).post();
		model.arithm(pairslinedroite[0][1],"=",0).post();
		bloc = new IntVar[2];
		bloc[0] = pairslinegauche[0][1];
		bloc[1] = pairscolbas[0][1];//AZZZEEEE
		model.sum(bloc, "=", this.grid.cases[0][1].nbneighbors).post();
		model.arithm(pairslinegauche[0][1],"=",pairslinedroite[0][0]).post();
		model.arithm(pairscolbas[0][1],"=",pairscolhaut[1][1]).post();
		
		model.arithm(pairscolbas[1][0],"=",0).post();
		model.arithm(pairslinegauche[1][0],"=",0).post();
		bloc = new IntVar[2];
		bloc[0] = pairslinedroite[1][0];
		bloc[1] = pairscolhaut[1][0];
		model.sum(bloc, "=", this.grid.cases[1][0].nbneighbors).post();
		model.arithm(pairslinedroite[1][0],"=",pairslinegauche[1][1]).post();
		model.arithm(pairscolhaut[1][0],"=",pairscolbas[0][0]).post();
		
		model.arithm(pairscolbas[1][1],"=",0).post();
		model.arithm(pairslinedroite[1][1],"=",0).post();
		bloc = new IntVar[2];
		bloc[0] = pairslinegauche[1][1];
		bloc[1] = pairscolhaut[1][1];
		model.sum(bloc, "=", this.grid.cases[1][1].nbneighbors).post();
		model.arithm(pairslinegauche[1][1],"=",pairslinedroite[1][0]).post();
		model.arithm(pairscolhaut[1][1],"=",pairscolbas[0][1]).post();

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				IntVar[]block = new IntVar[4];
				block[0] = pairscolhaut[i][j];
				block[1] = pairslinedroite[i][j];
				block[2] = pairscolbas[i][j];
				block[3] = pairslinegauche[i][j];
				System.out.println(this.grid.cases[i][j].type);
				switch (this.grid.cases[i][j].type) {
				case 0 :
					//model.sum(block,"=",0).post();
					model.arithm(pairscolbas[i][j],"=",0).post();
					model.arithm(pairscolhaut[i][j],"=",0).post();
					model.arithm(pairslinedroite[i][j],"=",0).post();
					model.arithm(pairslinegauche[i][j],"=",0).post();
					break;
				case 4 :
					model.sum(block,"=",4).post();
					break;
				case 1 : 
					IntVar[] set = new IntVar[3];
					set[0] = pairslinedroite[i][j];
					set[1] = pairscolbas[i][j];
					set[2] = pairscolbas[i][j];
					model.ifThen(model.arithm(pairscolhaut[i][j],"=", 1), model.sum(set,"=",0));
					
					set = new IntVar[3];
					set[0] = block[0];
					set[1] = block[2];
					set[2] = block[3];
					model.ifThen(model.arithm(block[1],"=", 1), model.sum(set,"=",0));
					
					IntVar[] set11 = new IntVar[3];
					set11[0] = block[0];
					set11[1] = block[1];
					set11[2] = block[3];
					model.ifThen(model.arithm(block[2],"=", 1), model.sum(set11,"=",0));
					
					IntVar[] set111 = new IntVar[3];
					set111[0] = block[0];
					set111[1] = block[1];
					set111[2] = block[2];
					model.ifThen(model.arithm(block[3],"=", 1), model.sum(set111,"=",0));
					break;
					
				case 2 : 
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=", 1));
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=", 1));
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[2],"=", 0));
					model.ifOnlyIf(model.arithm(block[1],"=", 0), model.arithm(block[3],"=", 0));
					
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[1],"=", 0));
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[3],"=", 0));
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[1],"=", 1));
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[3],"=", 1));
					break;
					
				case 3 : 
					IntVar[] set3 = new IntVar[3];
					set3[0] = block[1];
					set3[1] = block[2];
					set3[2] = block[3];
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.sum(set3,"=",3));
					
					set3 = new IntVar[3];
					set3[0] = block[0];
					set3[1] = block[2];
					set3[2] = block[3];
					model.ifOnlyIf(model.arithm(block[1],"=", 0), model.sum(set3,"=",3));
					
					set3 = new IntVar[3];
					set3[0] = block[0];
					set3[1] = block[1];
					set3[2] = block[3];
					model.ifOnlyIf(model.arithm(block[2],"=", 0), model.sum(set3,"=",3));
					
					set3 = new IntVar[3];
					set3[0] = block[0];
					set3[1] = block[1];
					set3[2] = block[2];
					model.ifOnlyIf(model.arithm(block[3],"=", 0), model.sum(set3,"=",3));
					break;

				case 5 :
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=",0));
					model.ifOnlyIf(model.arithm(block[2],"=", 1), model.arithm(block[0],"=",0));
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=",0));
					model.ifOnlyIf(model.arithm(block[3],"=", 1), model.arithm(block[1],"=",0));
					
					
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[2],"=",1));
					model.ifOnlyIf(model.arithm(block[2],"=", 0), model.arithm(block[0],"=",1));
					model.ifOnlyIf(model.arithm(block[1],"=", 0), model.arithm(block[3],"=",1));
					model.ifOnlyIf(model.arithm(block[3],"=", 0), model.arithm(block[1],"=",1));
					
					IntVar[] set0 = new IntVar[2];
					IntVar[] set5 = new IntVar[2];
					set0[0] = block[0];
					set0[1] = block[1];
					set5[0] = block[2];
					set5[1] = block[3];
					model.ifOnlyIf(model.sum(set0,"=", 0), model.sum(set5,"=",2));
					
					IntVar[] set01 = new IntVar[2];
					IntVar[] set1111 = new IntVar[2];
					set01[0] = block[1];
					set01[1] = block[2];
					set1111[0] = block[3];
					set1111[1] = block[0];
					model.ifOnlyIf(model.sum(set01,"=", 0), model.sum(set1111,"=",2));
					
					IntVar[] set011 = new IntVar[2];
					IntVar[] set11111 = new IntVar[2];
					set011[0] = block[2];
					set011[1] = block[3];
					set11111[0] = block[0];
					set11111[1] = block[1];
					model.ifOnlyIf(model.sum(set011,"=", 0), model.sum(set11111,"=",2));
					
					IntVar[] set0111 = new IntVar[2];
					IntVar[] set111111 = new IntVar[2];
					set0111[0] = block[3];
					set0111[1] = block[0];
					set111111[0] = block[1];
					set111111[1] = block[2];
					model.ifOnlyIf(model.sum(set0111,"=", 0), model.sum(set111111,"=",2));
					
					IntVar[] set01111 = new IntVar[2];
					IntVar[] set1111111 = new IntVar[2];
					set01111[0] = block[0];
					set01111[1] = block[1];
					set1111111[0] = block[2];
					set1111111[1] = block[3];
					model.ifOnlyIf(model.sum(set01111,"=", 2), model.sum(set1111111,"=",0));
					
					IntVar[] set011111 = new IntVar[2];
					IntVar[] set11111111 = new IntVar[2];
					set011111[0] = block[1];
					set011111[1] = block[2];
					set11111111[0] = block[3];
					set11111111[1] = block[0];
					model.ifOnlyIf(model.sum(set011111,"=", 2), model.sum(set11111111,"=",0));
					
					IntVar[] set0111111 = new IntVar[2];
					IntVar[] set111111111 = new IntVar[2];
					set0111111[0] = block[2];
					set0111111[1] = block[3];
					set111111111[0] = block[0];
					set111111111[1] = block[1];
					model.ifOnlyIf(model.sum(set0111111,"=", 2), model.sum(set111111111,"=",0));
					
					IntVar[] set01111111 = new IntVar[2];
					IntVar[] set1111111111 = new IntVar[2];
					set01111111[0] = block[3];
					set01111111[1] = block[0];
					set1111111111[0] = block[1];
					set1111111111[1] = block[2];
					model.ifOnlyIf(model.sum(set01111111,"=", 2), model.sum(set1111111111,"=",0));
					break;
				}
			}
		}
		
		Solver solver = model.getSolver();
		//solver.setSearch();
		solver.solve();
		System.out.println(solver.solve());
		
		this.translate(pairslinedroite, pairslinegauche, pairscolhaut, pairscolbas);
	}*/
	
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
	 * @return 
	 */
	public boolean solve() {
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
		
		//Same idea for piece (0,1),(1,0),(1,1)
		model.arithm(pairstopcol[0][this.grid.width-1],"=",0).post();
		model.arithm(pairsrightline[0][this.grid.width-1],"=",0).post();
		bloc[0] = pairsleftline[0][this.grid.width-1];
		bloc[1] = pairsbotcol[0][this.grid.width-1];
		model.sum(bloc, "=", this.grid.cases[0][this.grid.width-1].nbneighbors).post();
		
		model.arithm(pairsleftline[0][this.grid.width-1],"=",pairsrightline[0][this.grid.width-2]).post();
		model.arithm(pairsbotcol[0][this.grid.width-1],"=",pairstopcol[1][this.grid.width-1]).post();
		
		model.arithm(pairsbotcol[this.grid.height-1][0],"=",0).post();
		model.arithm(pairsleftline[this.grid.height-1][0],"=",0).post();
		bloc[0] = pairsrightline[this.grid.height-1][0];
		bloc[1] = pairstopcol[this.grid.height-1][0];
		model.sum(bloc, "=", this.grid.cases[this.grid.height-1][0].nbneighbors).post();
		
		model.arithm(pairsrightline[this.grid.height-1][0],"=",pairsleftline[this.grid.height-1][1]).post();
		model.arithm(pairstopcol[this.grid.height-1][0],"=",pairsbotcol[this.grid.height-2][0]).post();

		model.arithm(pairsbotcol[this.grid.height-1][this.grid.width-1],"=",0).post();
		model.arithm(pairsrightline[this.grid.height-1][this.grid.width-1],"=",0).post();
		bloc[0] = pairsleftline[this.grid.height-1][this.grid.width-1];
		bloc[1] = pairstopcol[this.grid.height-1][this.grid.width-1];
		model.sum(bloc, "=", this.grid.cases[this.grid.height-1][this.grid.width-1].nbneighbors).post();
		
		model.arithm(pairsleftline[this.grid.height-1][this.grid.width-1],"=",pairsrightline[this.grid.height-1][this.grid.width-2]).post();
		model.arithm(pairstopcol[this.grid.height-1][this.grid.width-1],"=",pairsbotcol[this.grid.height-2][this.grid.width-1]).post();
		
		//constraints for border pieces west and east
		for (int j = 1; j < this.grid.width-1; j++) {
			
			// define no links with west neighbor
			model.arithm(pairstopcol[0][j],"=",0).post();
			
			// constraint to impose the numbers of links necessited considered type of piece 
			IntVar[]block = new IntVar[3];
			block[0] = pairsrightline[0][j];
			block[1] = pairsleftline[0][j];
			block[2] = pairsbotcol[0][j];
			model.sum(block, "=", this.grid.cases[0][j].nbneighbors).post();
			
			//For west side
			// Each constraint : impose same value because this two pairs have to have same value because is the same link
			model.arithm(pairsrightline[0][j],"=",pairsleftline[0][j+1]).post();
			model.arithm(pairsleftline[0][j],"=",pairsrightline[0][j-1]).post();
			model.arithm(pairsbotcol[0][j],"=",pairstopcol[1][j]).post();
			
			//Same for east side
			model.arithm(pairsbotcol[this.grid.height-1][j],"=",0).post();
			block[0] = pairsrightline[this.grid.height-1][j];
			block[1] = pairsleftline[this.grid.height-1][j];
			block[2] = pairstopcol[this.grid.height-1][j];
			model.sum(block, "=", this.grid.cases[this.grid.height-1][j].nbneighbors).post();
			
			model.arithm(pairsrightline[this.grid.height-1][j],"=",pairsleftline[this.grid.height-1][j+1]).post();
			model.arithm(pairsleftline[this.grid.height-1][j],"=",pairsrightline[this.grid.height-1][j-1]).post();
			model.arithm(pairstopcol[this.grid.height-1][j],"=",pairsbotcol[this.grid.height-2][j]).post();
		}
		
		//Same idea for north and south borders
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
			
			model.arithm(pairsrightline[i][this.grid.width-1],"=",0).post();
			block[0] = pairsleftline[i][this.grid.width-1];
			block[1] = pairstopcol[i][this.grid.width-1];
			block[2] = pairsbotcol[i][this.grid.width-1];
			model.sum(block, "=", this.grid.cases[i][this.grid.width-1].nbneighbors).post();
			model.arithm(pairsleftline[i][this.grid.width-1],"=",pairsrightline[i][this.grid.width-2]).post();
			model.arithm(pairstopcol[i][this.grid.width-1],"=",pairsbotcol[i-1][this.grid.width-1]).post();
			model.arithm(pairsbotcol[i][this.grid.width-1],"=",pairstopcol[i+1][this.grid.width-1]).post();
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
				
			}
		}
		
		//Constraints considered type of piece
		// Idea is to define with constraints shape of each piece considered
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
				IntVar[]block = new IntVar[4];
				block[0] = pairstopcol[i][j];
				block[1] = pairsrightline[i][j];
				block[2] = pairsbotcol[i][j];
				block[3] = pairsleftline[i][j];
				switch (this.grid.cases[i][j].type) {
				case 0 :
					//constraint to define no link
					model.sum(block,"=",0).post();
					break;
				case 4 :
					//constraint to define four links
					model.sum(block,"=",4).post();
					break;
				case 1 :
					//constraints to define unique link idea if a pair = 1 all the other pairs = 0
					IntVar[] set = new IntVar[3];
					
					set[0] = block[1];
					set[1] = block[2];
					set[2] = block[3];
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.sum(set,"=",0));
					
					set[0] = block[0];
					set[1] = block[2];
					set[2] = block[3];
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.sum(set,"=",0));
					
					set[0] = block[0];
					set[1] = block[1];
					set[2] = block[3];
					model.ifOnlyIf(model.arithm(block[2],"=", 1), model.sum(set,"=",0));
					
					set[0] = block[0];
					set[1] = block[1];
					set[2] = block[2];
					model.ifOnlyIf(model.arithm(block[3],"=", 1), model.sum(set,"=",0));
					break;
					
				case 2 :
					//same idea as piece type 1 to define a -- or | shape
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=", 1));
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=", 1));
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[2],"=", 0));
					model.ifOnlyIf(model.arithm(block[1],"=", 0), model.arithm(block[3],"=", 0));
					
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[1],"=", 0));
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[3],"=", 0));
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[1],"=", 1));
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[3],"=", 1));
					break;
					
				case 3 :
					//same idea
					IntVar[] set3 = new IntVar[3];
					set3[0] = block[1];
					set3[1] = block[2];
					set3[2] = block[3];
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.sum(set3,"=",3));
					
					set3[0] = block[0];
					set3[1] = block[2];
					set3[2] = block[3];
					model.ifOnlyIf(model.arithm(block[1],"=", 0), model.sum(set3,"=",3));
					
					set3[0] = block[0];
					set3[1] = block[1];
					set3[2] = block[3];
					model.ifOnlyIf(model.arithm(block[2],"=", 0), model.sum(set3,"=",3));
					
					set3[0] = block[0];
					set3[1] = block[1];
					set3[2] = block[2];
					model.ifOnlyIf(model.arithm(block[3],"=", 0), model.sum(set3,"=",3));
					break;

				case 5 :
					//same idea
					model.ifOnlyIf(model.arithm(block[0],"=", 1), model.arithm(block[2],"=",0));
					model.ifOnlyIf(model.arithm(block[2],"=", 1), model.arithm(block[0],"=",0));
					model.ifOnlyIf(model.arithm(block[1],"=", 1), model.arithm(block[3],"=",0));
					model.ifOnlyIf(model.arithm(block[3],"=", 1), model.arithm(block[1],"=",0));
					
					
					model.ifOnlyIf(model.arithm(block[0],"=", 0), model.arithm(block[2],"=",1));
					model.ifOnlyIf(model.arithm(block[2],"=", 0), model.arithm(block[0],"=",1));
					model.ifOnlyIf(model.arithm(block[1],"=", 0), model.arithm(block[3],"=",1));
					model.ifOnlyIf(model.arithm(block[3],"=", 0), model.arithm(block[1],"=",1));
					
					IntVar[] set0 = new IntVar[2];
					IntVar[] set1 = new IntVar[2];
					set0[0] = block[0];
					set0[1] = block[1];
					set1[0] = block[2];
					set1[1] = block[3];
					model.ifOnlyIf(model.sum(set0,"=", 0), model.sum(set1,"=",2));
					
					set0[0] = block[1];
					set0[1] = block[2];
					set1[0] = block[3];
					set1[1] = block[0];
					model.ifOnlyIf(model.sum(set0,"=", 0), model.sum(set1,"=",2));
					
					set0[0] = block[2];
					set0[1] = block[3];
					set1[0] = block[0];
					set1[1] = block[1];
					model.ifOnlyIf(model.sum(set0,"=", 0), model.sum(set1,"=",2));
					
					set0[0] = block[3];
					set0[1] = block[0];
					set1[0] = block[1];
					set1[1] = block[2];
					model.ifOnlyIf(model.sum(set0,"=", 0), model.sum(set1,"=",2));
					
					
					set0[0] = block[0];
					set0[1] = block[1];
					set1[0] = block[2];
					set1[1] = block[3];
					model.ifOnlyIf(model.sum(set0,"=", 2), model.sum(set1,"=",0));
					
					set0[0] = block[1];
					set0[1] = block[2];
					set1[0] = block[3];
					set1[1] = block[0];
					model.ifOnlyIf(model.sum(set0,"=", 2), model.sum(set1,"=",0));
					
					set0[0] = block[2];
					set0[1] = block[3];
					set1[0] = block[0];
					set1[1] = block[1];
					model.ifOnlyIf(model.sum(set0,"=", 2), model.sum(set1,"=",0));
					
					set0[0] = block[3];
					set0[1] = block[0];
					set1[0] = block[1];
					set1[1] = block[2];
					model.ifOnlyIf(model.sum(set0,"=", 2), model.sum(set1,"=",0));
					break;
				}
			}
		}
		Solver solver = model.getSolver();
		solver.solve();		
		this.translate(pairsrightline, pairsleftline, pairstopcol, pairsbotcol);
		return solver.solve();
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
		LevelGenerator test = new LevelGenerator(10, 10);
		test.buildSolution();
		test.shuffleSolution();
		SolverGrid sol = new SolverGrid(test.grid);
		sol.solve();
		sol.grid.displayInConsole();
		//LevelDisplay display = new LevelDisplay(test, sol.grid);
	}
}	
	

