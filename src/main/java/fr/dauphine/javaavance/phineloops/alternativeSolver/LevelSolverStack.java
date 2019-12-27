package fr.dauphine.javaavance.phineloops.alternativeSolver;

import java.io.IOException;
import java.util.Stack;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class LevelSolverStack {
	
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
    private String pieceNbNeighbors = "min";
	private int maxThreads = 1;
	private int nbOrientations;
	private int nbThreads;
	private Stack<Stack<Piece>> originalStack;
	private Stack<Piece> chosenPiece;
	
	public LevelSolverStack(Grid grid) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
	}
	
	public LevelSolverStack(Grid grid, int maxThreads) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.maxThreads = maxThreads;
	}
	
	public LevelSolverStack(Grid grid, String pieceNbNeighbors) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.pieceNbNeighbors = pieceNbNeighbors;
	}
	
	public LevelSolverStack(Grid grid, String pieceNbNeighbors, int maxThreads) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.pieceNbNeighbors = pieceNbNeighbors;
		this.maxThreads = maxThreads;
	}	
	
	/**
	 * Get the grid
	 * @return Grid
	 */
	public Grid getGrid() {
		return grid;
	}
	
	/**
	 * Return piece with the least orientation possible 
	 * @param originalStack
	 * @return Piece
	 */
	public Stack<Piece> getPieceWithLeastOrientation(Stack<Stack<Piece>> originalStack) {
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
	 * Put all pieces in stack
	 */
	public void piecesStacked() {
		originalStack = new Stack<>();
		for( int i = this.grid.getHeight()-1; i >= 0 ; i-- ) {
			for( int j = this.grid.getWidth()-1; j >= 0 ; j-- ) {
				if(grid.getCases()[i][j].getLock()==0) {
					Stack<Piece> stack = new Stack<>();
					for (int k = grid.getCases()[i][j].getPossibleOrientations().size()-1; k >= 0; k--) {
						Piece p = new Piece(i,j, grid.getCases()[i][j].getType(), grid.getCases()[i][j].getPossibleOrientations().get(k));
						stack.push(p);
					}
					originalStack.push(stack);
				}
			}
		} 
	}
	
	/**
	 * choose the piece to fixe
	 */
	public void choosePiece() {
		if( pieceNbNeighbors.equals("min") ) {
			chosenPiece = this.getPieceWithLeastOrientation(originalStack);
		}
		else if( pieceNbNeighbors.equals("max") ) {
			chosenPiece = this.getPieceWithMostOrientation(originalStack);
		}				
		originalStack.remove(chosenPiece);
	}
	
	public void setThreads() {
		nbOrientations = chosenPiece.size();
		nbThreads = 0; 
		if(nbOrientations == maxThreads) {
			nbThreads = nbOrientations - 1;
		}
		else if(nbOrientations < maxThreads) {
			nbThreads = nbOrientations - 1;
		}
		else if(nbOrientations > maxThreads){
			nbThreads = maxThreads - 1;
		}
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
		
		if( lockedPiece == totalPiece ) {
			return true;
		}
		else {
			
			piecesStacked();
			choosePiece();
			setThreads();

			// use of child thread
			StackThread[] threads = new StackThread[nbThreads];
	        for (int i = 0; i < nbThreads; i++) {
				Piece root = chosenPiece.get(i);
				threads[i] = new StackThread(originalStack,root);
				threads[i].start();
	        }
	        try {
	            for (int i = 0; i < nbThreads; i++) {
	                threads[i].join();
	    			if(threads[i].getSolutionFound()) {
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
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        // use main thread
			for(int k = nbThreads; k <chosenPiece.size(); k++) {
				Piece root = chosenPiece.get(k);
				Node node = new Node(root);
    			if(node.DepthFirstSearch(originalStack, node, " ")) {
	        		while(node.getChild()!=null) {
	        			Piece p = node.getLeafNode().getPiece();
	        			grid.getCases()[p.getI()][p.getJ()] = p;
	        			node.leafDelete(node); 
					}
        			Piece p = node.getLeafNode().getPiece();
        			grid.getCases()[p.getI()][p.getJ()] = p;
					return true;
    			}
			}
			return false;		 
		}		
	}
}