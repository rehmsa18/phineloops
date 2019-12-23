package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.util.Stack;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class StackThread extends Thread{

	private static Stack<Piece> originalStack;
	private Stack<Piece> finalStack;
	private Piece root;
    private final static Object monitor = new Object();
	
	public StackThread(Stack<Piece> originalStack, Piece root) {
		StackThread.originalStack = originalStack;
		this.root = root;
	}
	
	/**
	 * Get the stack formed with one orientation of the chosen piece
	 * @return stack of pieces
	 */
	public Stack<Piece> getFinalStack() {
		return finalStack;
	}
	
	public void run() {
		synchronized(monitor) {
			//System.out.println("\nd " + currentThread().getName()+ " "+root);
			//System.out.println("d2 " + currentThread().getName()+ " "+originalStack);
		    finalStack = LevelSolverStack.stack(originalStack, root);
			//System.out.println("f " + currentThread().getName()+ " "+finalStack);
			//System.out.println("f2 " + currentThread().getName()+ " "+originalStack+"\n");
		}
	}
}
