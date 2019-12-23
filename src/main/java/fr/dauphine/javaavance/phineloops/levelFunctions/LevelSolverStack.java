package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class LevelSolverStack {
	
	private Grid grid;
	private int totalPiece;
	private int lockedPiece = 0;
	private boolean argumentGiven;
	private int maxThreads;
	
	public LevelSolverStack(Grid grid, boolean argumentGiven, int maxThreads) {
		this.grid = grid;
		this.totalPiece = grid.getHeight()*grid.getWidth();
		this.argumentGiven = argumentGiven;
		this.maxThreads = maxThreads;
	}	
	
	/**
	 * Sort the stack, on top the closest pieces of the chosen piece
	 * @param chosenPiece
	 * @param originalStack
	 * @return Stack
	 */
	public Stack<Piece> sortOriginalStack(Piece chosenPiece, Stack<Piece> originalStack) {
		ArrayList<Piece> tmpList = new ArrayList<>();
		int hauteur = 1;
		while(!originalStack.isEmpty()) {	
			Iterator<Piece> iterator = originalStack.iterator();
			while (iterator.hasNext()) {
			   Piece  p = iterator.next();
			   if( Math.sqrt( Math.pow(p.getI()-chosenPiece.getI(),2) + Math.pow(p.getJ()-chosenPiece.getJ(),2)) <= hauteur ) {
				   tmpList.add(p);
				   iterator.remove();
			   }
			}
			hauteur++;
		}
		Collections.reverse(tmpList);
		Stack<Piece> sortedStack = new Stack<>();
		sortedStack.addAll(tmpList);
		return sortedStack;
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
	 * Says if piece respects links with others
	 * @param child
	 * @param pieces
	 * @return true if the links with others pieces are respected
	 */
	public static boolean respectAncestors(Piece child, Stack<Piece> pieces) {
		for(int i=0; i<pieces.size(); i++) {
    	   if( !child.linkedNeighborOrNoNeighbor(pieces.get(i) )) {
				return false;
    	   }
        } 
		return true;	
	}
	
	/**
	 * Says if a solution is found
	 * @param originalStack
	 * @param finalStack
	 * @return true if solution found
	 */
	public static Stack<Piece> stack(Stack<Piece> originalStack, Piece root) {
		Stack<Piece> finalStack = new Stack<>();
		finalStack.push(root);
		while(!originalStack.isEmpty()) {
			Piece p = originalStack.pop();
			while( !respectAncestors(p,finalStack) && (p.getIndex()<p.getPossibleOrientations().size()-1)) {
				p.rotatePossibleOrientation();
			}
			if(respectAncestors(p,finalStack)) {
				finalStack.push(p);
			}
			else {	
				boolean stop = false;
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
					return finalStack;
				}
			}
		}
		return finalStack;
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
		//System.out.println("mobiles pieces : " + mobilePiece + "\n");
		
		if( lockedPiece == totalPiece ) {
			return true;
		}
		else {
			
			Stack<Piece> originalStack = new Stack<>();
			for( int i = this.grid.getHeight()-1; i>=0; i-- ) {
				for( int j = this.grid.getWidth()-1; j>=0; j-- ) {
					if(grid.getCases()[i][j].getLock()==0) {
						Piece p = grid.getCases()[i][j];
						//Collections.sort(p.getPossibleOrientations());
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
			
			System.out.println(chosenPiece + " " +chosenPiece.getPossibleOrientations().size());
						
			originalStack.remove(chosenPiece);
			originalStack = sortOriginalStack(chosenPiece, originalStack);
			

			int nbOrientations = chosenPiece.getPossibleOrientations().size();
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
				Piece root = new Piece(chosenPiece.getI(),chosenPiece.getJ(),chosenPiece.getType(),chosenPiece.getPossibleOrientations().get(i));
				ArrayList<Integer> orientationPossible = new ArrayList<>();
				orientationPossible.add(chosenPiece.getPossibleOrientations().get(i));
				root.setPossibleOrientations(orientationPossible);
				Stack<Piece> originalStack2 = new Stack<>();
				originalStack2.addAll(originalStack);
				threads[i] = new StackThread(originalStack2,root);
	            threads[i].start();
	        }

	        try {
	            for (int i = 0; i < nbThreads; i++) {
	                threads[i].join();
	    			Stack<Piece> finalS = threads[i].getFinalStack();
	    			if(finalS.size() == mobilePiece) {
		                System.out.println("solution "+ threads[i].getName()+" " + threads[i].getFinalStack());
						while(!finalS.isEmpty()) {
							Piece p = (Piece) finalS.pop();
							grid.getCases()[p.getI()][p.getJ()] = p;
						}
	    			return true;
	            	}	
	    			else {
		                System.out.println("not solution "+ threads[i].getName()+" " + threads[i].getFinalStack());
	    			}

	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        
	        //main thread
			for(int k=nbThreads; k <chosenPiece.getPossibleOrientations().size(); k++) {
				Piece root = new Piece(chosenPiece.getI(),chosenPiece.getJ(),chosenPiece.getType(),chosenPiece.getPossibleOrientations().get(k));
				ArrayList<Integer> orientationPossible = new ArrayList<>();
				orientationPossible.add(chosenPiece.getPossibleOrientations().get(k));
				root.setPossibleOrientations(orientationPossible);
				Stack<Piece> finalS = stack(originalStack, root);
    			if(finalS.size() == mobilePiece) {
	                System.out.println("solution Thread "+ Thread.currentThread().getName() + "-" + k + " " + finalS);
					while(!finalS.isEmpty()) {
						Piece p = (Piece) finalS.pop();
						grid.getCases()[p.getI()][p.getJ()] = p;
					}
					return true;
    			}
    			else {
	                System.out.println("not solution Thread "+ Thread.currentThread().getName() + "-" + k + " " + finalS);
    			}
			}
			return false;		 
		}		
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		LevelGenerator test = new LevelGenerator(100, 100);
		test.buildSolution();
		test.shuffleSolution();
		Grid grid = test.getGrid();
		//Write.writeFile("file", grid);
	   	//Grid grid2 = Read.readFile("file");
	   	
	    long debut = System.currentTimeMillis();
	   
	    boolean argumentGiven = false;
		LevelSolverStack sol = new LevelSolverStack(grid, argumentGiven, 4);
		System.out.println(sol.solve());
		
	    long fin = System.currentTimeMillis();
	    System.out.println("Méthode exécutée en " + Long.toString(fin - debut) + " millisecondes");

	    LevelDisplay display2 = new LevelDisplay(test, sol.grid); 
	    
    	LevelChecker levelChecker = new LevelChecker(sol.grid);
    	
    	System.out.println("check " +levelChecker.check());
	    
	}

}
