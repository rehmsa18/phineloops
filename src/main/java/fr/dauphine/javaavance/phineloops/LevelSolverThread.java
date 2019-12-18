package fr.dauphine.javaavance.phineloops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class LevelSolverThread {
	
	Grid grid;
	int totalPiece;
	int lockedPiece = 0;
	int mobilePiece = 0;
		
	public LevelSolverThread(Grid grid) {
		this.grid = grid;
		this.totalPiece = grid.height*grid.width;
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
		for (int i = 0; i < this.grid.height; i++) {
			for (int j = 0; j < this.grid.width; j++) {
		
				if(grid.cases[i][j].lock==0) {

					ArrayList<Integer> orientationPossible = new ArrayList<>();
					
					if (grid.northWestSide(i, j)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) { 
							if( grid.cases[i][j].links[0]==0 && grid.cases[i][j].links[3]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}	
					else if (grid.northEastSide(i, j)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[0]==0 && grid.cases[i][j].links[1]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}					
					else if (grid.southWestSide(i, j)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[2]==0 && grid.cases[i][j].links[3]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}
					else if (grid.southEastSide(i, j)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[2]==0 && grid.cases[i][j].links[1]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}			
					else if (grid.northBorder(i)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[0]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}
					else if (grid.southBorder(i)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[2]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}
					else if (grid.westBorder(j)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[3]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}	
					else if (grid.eastBorder(j)) {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if( grid.cases[i][j].links[1]==0 && !grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}
					else {
						for(int k=0; k<grid.cases[i][j].nbRotation; k++) {
							if(!grid.noRespectedLockPiece(grid.cases[i][j])) 
								orientationPossible.add(grid.cases[i][j].orientation);
							grid.cases[i][j].rotatePiece();
						}
					}
					if( orientationPossible.size() == 1) {
						grid.cases[i][j].orientation = orientationPossible.get(0);
						grid.cases[i][j].defineLinks();
						grid.cases[i][j].lock = 1;
					}
					else 
						grid.cases[i][j].possibleOrientations = orientationPossible;	
				}
				countLockedPiece += grid.cases[i][j].lock;
			}			
		}
		return countLockedPiece;
	}
	
	
	public boolean depthFirstOrder(){ // visit the node farthest from the root node => visit branch by branch
		Stack<Stack<Piece>> stacks = new Stack<>();
		for( int i = this.grid.height-1; i >= 0 ; i-- ) {
			for( int j = this.grid.width-1; j >= 0 ; j-- ) {
				if(grid.cases[i][j].lock==0) {
					//System.out.println(grid.cases[i][j] + " " +grid.cases[i][j].possibleOrientations);
					Stack<Piece> stack = new Stack<>();
					for (int k = grid.cases[i][j].possibleOrientations.size()-1; k >= 0; k--) {
						Piece p = new Piece(i,j, grid.cases[i][j].type, grid.cases[i][j].possibleOrientations.get(k));
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
		
		while(root.child!=null) {
			Piece p = root.getLeafNode().getPiece();
			grid.cases[p.x][p.y] = p;
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
		LevelGenerator test = new LevelGenerator(30, 30);
		test.buildSolution();
		test.shuffleSolution();
		//Grid grid = test.grid;
		//grid.writeFile("file5");
	   	//Grid grid2 = Grid.readFile("file5");
	   	
	    long debut = System.currentTimeMillis();
	   
		LevelSolverThread sol = new LevelSolverThread(test.grid);
		System.out.println(sol.solve());
		
	    long fin = System.currentTimeMillis();
	    System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

		
		//sol.grid.displayInConsole();
		@SuppressWarnings("unused")
		LevelDisplay display = new LevelDisplay(test, sol.grid);
	}

}
