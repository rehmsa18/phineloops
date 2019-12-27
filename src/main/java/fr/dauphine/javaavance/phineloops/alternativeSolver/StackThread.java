package fr.dauphine.javaavance.phineloops.alternativeSolver;

import java.util.Stack;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class StackThread extends Thread{

	private static Stack<Stack<Piece>> originalStack;
	private Piece root;
	private Node node;
	private boolean solutionFound;
    private final static Object monitor = new Object();
	
	@SuppressWarnings("static-access")
	public StackThread(Stack<Stack<Piece>> originalStack, Piece root) {
		this.originalStack = originalStack;
		this.root = root;
	}
	
	/**
	 * Get the stack formed with one orientation of the chosen piece
	 * @return stack of pieces
	 */
	public Node getNode() {
		return node;
	}
	
	/**
	 * Get the stack formed with one orientation of the chosen piece
	 * @return stack of pieces
	 */
	public boolean getSolutionFound() {
		return solutionFound;
	}
	
	public void run() {
		synchronized(monitor) {
			//System.out.println("\nd " + currentThread().getName()+ " "+root);
			//System.out.println("d2 " + currentThread().getName()+ " "+originalStack);
			node = new Node(root);
		    solutionFound = node.DepthFirstSearch(originalStack, node, " ");
			//System.out.println("f " + currentThread().getName()+ " "+finalStack);
			//System.out.println("f2 " + currentThread().getName()+ " "+originalStack+"\n");
		}
	}
}