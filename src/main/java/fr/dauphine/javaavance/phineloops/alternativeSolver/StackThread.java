package fr.dauphine.javaavance.phineloops.alternativeSolver;

import java.util.Stack;
import fr.dauphine.javaavance.phineloops.model.Piece;

public class StackThread extends Thread{

	private static Stack<Stack<Piece>> originalStack;
	private Piece root;
	private Node node;
	private boolean solutionFound;
	private final static Object monitor = new Object();
	
	public StackThread(Stack<Stack<Piece>> originalStack, Piece root) {
		StackThread.originalStack = originalStack;
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
	 * Says if a solutution is found
	 * @return boolean
	 */
	public boolean getSolutionFound() {
		return solutionFound;
	}
	
	/**
	 * search a solution with the orientation of the chosen piece
	 */
	public void run() {
		synchronized(monitor) {
			node = new Node(root);
		    solutionFound = node.DepthFirstSearch(originalStack, node, " ");
		}
	}
}