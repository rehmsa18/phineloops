package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class LevelSolverThread {
	
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
	private int mobilePiece = 0;
		
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

	public LevelSolverThread(Grid grid) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
	}	
	
	public Stack<Stack<Piece>> sorting(Stack<Stack<Piece>> original) {
        Stack<Stack<Piece>> temporaryStack = new Stack<>();
        while(!original.isEmpty()) {
        	Stack<Piece> x = original.pop();
            while(!temporaryStack.isEmpty() && temporaryStack.peek().size() < x.size()) {
                original.push(temporaryStack.pop());
            }
            temporaryStack.push(x);
        }
        return temporaryStack;
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
	
	
	public boolean depthFirstOrder(){ // visit the node farthest from the root node => visit branch by branch
		Stack<Stack<Piece>> stacks = new Stack<>();
		for( int i = this.grid.getHeight()-1; i >= 0 ; i-- ) {
			for( int j = this.grid.getWidth()-1; j >= 0 ; j-- ) {
				if(grid.getCases()[i][j].getLock()==0) {
					//System.out.println(grid.cases[i][j] + " " +grid.cases[i][j].possibleOrientations);
					Stack<Piece> stack = new Stack<>();
					for (int k = grid.getCases()[i][j].getPossibleOrientations().size()-1; k >= 0; k--) {
						Piece p = new Piece(i,j, grid.getCases()[i][j].getType(), grid.getCases()[i][j].getPossibleOrientations().get(k));
						stack.push(p);
					}
					stacks.push(stack);
				}
			}
		} 
		
		//stacks = sorting(stacks);
		
		for(int i=0; i<stacks.size(); i++) {
			//System.out.println(stacks.get(i));
		}

		Node root = new Node(new Piece(-1,-1,-1,-1));
		root.parcours(stacks, root, " ");
		int hauteur = root.hauteur()-1;
		
		//Node.printTree(root, " ");
		
		while(root.getChild()!=null) {
			Piece p = root.getLeafNode().getPiece();
			grid.getCases()[p.getI()][p.getJ()] = p;
			root.leafDelete(root); 
		}	
		
		if( hauteur == mobilePiece)
			return true;
		else 
			return false;
	}

	
	public boolean solve() {
		int tmp = -1;
		while(lockedPiece!= tmp){
			tmp = lockedPiece;
			lockedPiece = this.lockPiece();
		}
		
		mobilePiece = totalPiece - lockedPiece;

	    System.out.println("total pieces : " + totalPiece + "\n");
		System.out.println("locked pieces : " + lockedPiece + "\n");
		System.out.println("mobiles pieces : " + mobilePiece + "\n");
		
		if( lockedPiece == totalPiece ) {
			return true;
		}
		else {
			return depthFirstOrder(); 
		}		
	}
	

	public static void main(String[] args) throws IOException {
		LevelGenerator test = new LevelGenerator(25, 25);
		test.buildSolution();
		test.shuffleSolution();
		//Grid grid = test.grid;
		//grid.writeFile("file5");
	   	//Grid grid2 = Grid.readFile("file5");
	   	
	    long debut = System.currentTimeMillis();
	   
		LevelSolverThread sol = new LevelSolverThread(test.getGrid());
		System.out.println(sol.solve());
		
	    long fin = System.currentTimeMillis();
	    System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

		
		//sol.grid.displayInConsole();
		@SuppressWarnings("unused")
		LevelDisplay display = new LevelDisplay(test, sol.grid);
	}

}
