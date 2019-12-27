package fr.dauphine.javaavance.phineloops.alternativeSolver;

import java.io.IOException;
import java.util.Stack;

import fr.dauphine.javaavance.phineloops.levelFunctions.LevelChecker;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelGenerator;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;
import fr.dauphine.javaavance.phineloops.utils.Read;

public class LevelSolverStack {
	
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
	private String chosenPieceNbNeighbors = "min";
	private int maxThreads = 1;
	
	public LevelSolverStack(Grid grid) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
	}
	
	public LevelSolverStack(Grid grid, int maxThreads) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.maxThreads = maxThreads;
	}	
	
	public LevelSolverStack(Grid grid, String chosenPieceNbNeighbors) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.chosenPieceNbNeighbors = chosenPieceNbNeighbors;
	}	
	
	
	public LevelSolverStack(Grid grid, String chosenPieceNbNeighbors, int maxThreads) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.chosenPieceNbNeighbors = chosenPieceNbNeighbors;
		this.maxThreads = maxThreads;
	}	
	
	
	public Grid getGrid() {
		return grid;
	}
	
	
	/**
	 * Return piece with the least orientation possible
	 * @param originalStack
	 * @return Piece
	 */
	public Stack<Piece> getPieceWithMostNeighborsFixed(Stack<Stack<Piece>> originalStack) {
		int min = 5;
		int i = 0;
		Stack<Piece> chosenPiece = null;
		boolean foundMinOrientation = false;
		while(!foundMinOrientation && i<originalStack.size()) {
			Stack<Piece> p = originalStack.get(i);
			if(p.size() < min) {
				min = p.size();
				chosenPiece = p;
				if(min == 2) {
					foundMinOrientation = true;
				}
			}
			i++;
		}
		return chosenPiece;
	}
	
	/**
	 * Return piece with the most orientation possible
	 * @param originalStack
	 * @return Piece
	 */
	public  Stack<Piece> getPieceWithMostOrientation(Stack<Stack<Piece>> originalStack) {
		int max = 0;
		int i = 0;
		Stack<Piece> chosenPiece = null;
		boolean foundMaxOrientation = false;
		while(!foundMaxOrientation && i<originalStack.size()) {
			Stack<Piece> p = originalStack.get(i);
			if(p.size()>max) {
				max = p.size();
				chosenPiece = p;
				if(max == 4) {
					foundMaxOrientation = true;
				}
			}
			
			i++;
		}
		return chosenPiece;
	}


	/**
	 * Solve the level
	 * @return true if a solution found
	 * @throws IOException 
	 */
	public boolean solve() throws IOException {
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
		
		int mobilePiece = totalPiece - lockedPiece;
	    //System.out.println("total pieces : " + totalPiece + "\n");
		//System.out.println("locked pieces : " + lockedPiece + "\n");
		System.out.println("mobiles pieces : " + mobilePiece + "\n");
		
		if( lockedPiece == totalPiece ) {
			return true;
		}
		else {
			
			Stack<Stack<Piece>> originalStack = new Stack<>();
			for( int i = this.grid.getHeight()-1; i >= 0 ; i-- ) {
				for( int j = this.grid.getWidth()-1; j >= 0 ; j-- ) {
					if(grid.getCases()[i][j].getLock()==0) {
						//System.out.println(grid.cases[i][j] + " " +grid.cases[i][j].possibleOrientations);
						Stack<Piece> stack = new Stack<>();
						for (int k = grid.getCases()[i][j].getPossibleOrientations().size()-1; k >= 0; k--) {
							Piece p = new Piece(i,j, grid.getCases()[i][j].getType(), grid.getCases()[i][j].getPossibleOrientations().get(k));
							stack.push(p);
						}
						originalStack.push(stack);
					}
				}
			} 
			
			Stack<Piece> chosenPiece;
			
			if(chosenPieceNbNeighbors.equals("min")) {
				chosenPiece = this.getPieceWithMostNeighborsFixed(originalStack);
			}
			else {
				chosenPiece = this.getPieceWithMostOrientation(originalStack);
			}
			
			System.out.println(chosenPiece + " " +chosenPiece.size());
						
			originalStack.remove(chosenPiece);			

			int nbOrientations = chosenPiece.size();
			int nbThreads = 0; 

			if(nbOrientations == maxThreads) {
				nbThreads = nbOrientations - 1;
			}
			else if(nbOrientations < maxThreads) {
				nbThreads = nbOrientations - 1;
			}
			else if(nbOrientations > maxThreads){
				nbThreads = maxThreads - 1;
			}

			StackThread[] threads = new StackThread[nbThreads];
			
			// threads created
	        for (int i = 0; i < nbThreads; i++) {
				Piece root = chosenPiece.get(i);
				threads[i] = new StackThread(originalStack,root);
				threads[i].start();
				/*Piece root = new Piece(chosenPiece.getI(),chosenPiece.getJ(),chosenPiece.getType(),chosenPiece.getPossibleOrientations().get(i));
				ArrayList<Integer> orientationPossible = new ArrayList<>();
				orientationPossible.add(chosenPiece.getPossibleOrientations().get(i));
				root.setPossibleOrientations(orientationPossible);
				Stack<Piece> originalStack2 = new Stack<>();
				originalStack2.addAll(originalStack);
				threads[i] = new StackThread(originalStack2,root);
	            threads[i].start();*/
	        }

	        try {
	            for (int i = 0; i < nbThreads; i++) {
	                threads[i].join();
	    			if(threads[i].getSolutionFound()) {
		                System.out.println("solution "+ threads[i].getName());
		                Node node = threads[i].getNode();
		        		while(node.getChild()!=null) {
		        			Piece p = node.getLeafNode().getPiece();
		        			grid.getCases()[p.getI()][p.getJ()] = p;
		        			node.leafDelete(node); 
						}
	        			Piece p = node.getLeafNode().getPiece();
	        			grid.getCases()[p.getI()][p.getJ()] = p;
						return true;
	            	}	
	    			else {
		                System.out.println("no solution "+ threads[i].getName());
	    			}

	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }


			for(int k = nbThreads; k <chosenPiece.size(); k++) {
				Piece root = chosenPiece.get(k);
				Node node = new Node(root);
    			if(node.DepthFirstSearch(originalStack, node, " ")) {
	                //System.out.println("solution Thread "+ Thread.currentThread().getName() + "-" + k);
	        		while(node.getChild()!=null) {
	        			Piece p = node.getLeafNode().getPiece();
	        			grid.getCases()[p.getI()][p.getJ()] = p;
	        			node.leafDelete(node); 
					}
        			Piece p = node.getLeafNode().getPiece();
        			grid.getCases()[p.getI()][p.getJ()] = p;
					return true;
    			}
    			else {
	                //System.out.println("not solution Thread "+ Thread.currentThread().getName() + "-" + k);
    			}
			}
			return false;		 
		}		
	}

	public static void main(String[] args) throws IOException {
		LevelGenerator test = new LevelGenerator(100,100);
		test.buildSolution();
		test.shuffleSolution();
		//Grid grid = test.getGrid();
		//Write.writeFile("file3", grid);
	    Grid grid2 = Read.readFile("file2");

	    long debut = System.currentTimeMillis();

	    LevelSolverStack sol = new LevelSolverStack(grid2);
		System.out.println(sol.solve());

	    long fin = System.currentTimeMillis();
	    System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");
	    //LevelDisplay display2 = new LevelDisplay(test, sol.grid); 
	    
	    LevelChecker ch = new LevelChecker(sol.grid);
	    
	    System.out.println(ch.check());

	}

}