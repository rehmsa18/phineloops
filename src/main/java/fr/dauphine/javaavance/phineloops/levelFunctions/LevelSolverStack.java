package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class LevelSolverStack {
	
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
	private int mobilePiece = 0;
	private boolean argumentGiven;
		
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

	public int getMobilePiece() {
		return mobilePiece;
	}

	public void setMobilePiece(int mobilePiece) {
		this.mobilePiece = mobilePiece;
	}

	public LevelSolverStack(Grid grid, boolean argumentGiven) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.argumentGiven = argumentGiven;
	}	
	
	/**
	 * Says if piece respects links with others
	 * @param child
	 * @param pieces
	 * @return true if the links with others pieces are respected
	 */
	public boolean respectAncestors(Piece child, Stack<Piece> pieces) {
		for(int i=0; i<pieces.size();i++) {
    	   if( !child.linkedNeighborOrNoNeighbor(pieces.get(i) )) {
				return false;
    	   }
       } 
		return true;	
	}
	
	
	/**
	 * Return piece with the least orientation possible
	 * @param originalStack
	 * @return Piece
	 */
	public Piece getPieceWithMostNeighborsFixed(Stack<Piece> originalStack) {
		int min = 5;
		int i = 0;
		Piece chosenPiece = null;
		boolean foundMinOrientation = false;
		while(!foundMinOrientation && i<originalStack.size()) {
			Piece p = originalStack.get(i);
			if(p.getPossibleOrientations().size() < min) {
				min = p.getPossibleOrientations().size();
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
	public Piece getPieceWithMostOrientation(Stack<Piece> originalStack) {
		int max = 0;
		int i = 0;
		Piece chosenPiece = null;
		boolean foundMaxOrientation = false;
		while(!foundMaxOrientation && i<originalStack.size()) {
			Piece p = originalStack.get(i);
			if(p.getPossibleOrientations().size()>max) {
				max = p.getPossibleOrientations().size();
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
	 * Lock the max pieces possible
	 * @return number of fixed pieces
	 */
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
					else if (orientationPossible.size() == 0) {
						countLockedPiece = -2;
						return countLockedPiece;
					}
					else 
						grid.getCases()[i][j].setPossibleOrientations(orientationPossible);	
				}
				countLockedPiece += grid.getCases()[i][j].getLock();
			}			
		}
		return countLockedPiece;
	}
	
	
	
	/**
	 * Says if a solution is found
	 * @param originalStack
	 * @param finalStack
	 * @return true if solution found
	 */
	public boolean stack(Stack<Piece> originalStack, Stack<Piece> finalStack) {
		
		while(!originalStack.isEmpty()) {
			Piece p = originalStack.pop();
			while( !this.respectAncestors(p,finalStack) && (p.getIndex()<p.getPossibleOrientations().size()-1)) {
				p.rotatePossibleOrientation();
			}
			if(this.respectAncestors(p,finalStack)) {
				finalStack.push(p);
			}
			else {	
				boolean stop =false;
				originalStack.add(p);
				while (!stop && !finalStack.isEmpty()) {
					originalStack.peek().initializeIndex();
					originalStack.peek().setOrientation(originalStack.peek().getPossibleOrientations().get(0));
					originalStack.peek().defineLinks();
					originalStack.push(finalStack.pop());
					if(originalStack.peek().getIndex()<originalStack.peek().getPossibleOrientations().size()-1) {
						originalStack.peek().rotatePossibleOrientation();
						stop = true;
					}
				}
				if(finalStack.isEmpty()) {
					originalStack.pop();
					return false;
				}
			}
		}	
		return true;
	}

	/**
	 * Solve the level
	 * @return true if a solution found
	 */
	public boolean solve() {
		int tmp = -1;
		while(lockedPiece!= tmp){
			tmp = lockedPiece;
			lockedPiece = this.lockPiece();
			if (lockedPiece == -2) {
				return false;
			}
		}
		
		mobilePiece = totalPiece - lockedPiece;

	    //System.out.println("total pieces : " + totalPiece + "\n");
		//System.out.println("locked pieces : " + lockedPiece + "\n");
		//System.out.println("mobiles pieces : " + mobilePiece + "\n");
		
		if( lockedPiece == totalPiece ) {
			return true;
		}
		else {
			
			Stack<Piece> originalStack = new Stack<>();
			Stack<Piece> finalStack = new Stack<>();
			for( int i = this.grid.getHeight()-1; i>=0; i-- ) {
				for( int j = this.grid.getWidth()-1; j>=0; j-- ) {
					if(grid.getCases()[i][j].getLock()==0) {
						Piece p = grid.getCases()[i][j];
						Collections.sort(p.getPossibleOrientations());
						p.setOrientation(p.getPossibleOrientations().get(0));
						p.defineLinks();
						originalStack.push(p);
					}
				}
			}
			
			Piece chosenPiece;
			
			if(argumentGiven) {
				chosenPiece = this.getPieceWithMostNeighborsFixed(originalStack);
			}
			else {
				chosenPiece = this.getPieceWithMostOrientation(originalStack);
			}
			
			originalStack.remove(chosenPiece);
			
			//System.out.println(chosenPiece);
			
			boolean solutionFound = false;
			int k = 0;
			while( !solutionFound &&  k<chosenPiece.getPossibleOrientations().size()) {
				Piece root = new Piece(chosenPiece.getI(),chosenPiece.getJ(),chosenPiece.getType(),chosenPiece.getPossibleOrientations().get(k));
				ArrayList<Integer> orientationPossible = new ArrayList<>();
				orientationPossible.add(chosenPiece.getPossibleOrientations().get(k));
				root.setPossibleOrientations(orientationPossible);
				finalStack.push(root);
				solutionFound = stack(originalStack,finalStack);
				System.out.println(k + " " + root + " " + solutionFound);
				k++;
			}			
			if( solutionFound ) {
				while(!finalStack.isEmpty()) {
					Piece p = finalStack.pop();
					grid.getCases()[p.getI()][p.getJ()] = p;
				}
				return true;
			}
			else {
				return false;
			}
			 
		}		
	}
	
	public static void main(String[] args) throws IOException {
		LevelGenerator test = new LevelGenerator(70,70);
		test.buildSolution();
		test.shuffleSolution();
		Grid grid = test.getGrid();
		//grid.writeFile("file6");
	   	//Grid grid2 = Grid.readFile("file6");
	   	
	    long debut = System.currentTimeMillis();
	   
	    boolean argumentGiven = true;
		LevelSolverStack sol = new LevelSolverStack(grid, argumentGiven);
		System.out.println(sol.solve());
		
	    long fin = System.currentTimeMillis();
	    System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

	    LevelDisplay display2 = new LevelDisplay(test, sol.grid); 

	}

}
