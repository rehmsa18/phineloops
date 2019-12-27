package fr.dauphine.javaavance.phineloops.alternativeSolver;

import java.util.Stack;

import fr.dauphine.javaavance.phineloops.model.Piece;

public class Node {
	private Piece piece;
	private Node parent = null;
	private Node child = null;

	public Node(Piece piece){
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	} 

	public Node getParent() {
		return parent;
	}	

	public Node getChild() {
		return child;
	}	

	/**
	 * Add a child to the node
	 * @param child
	 * @return Node
	 */
    public Node addChild(Node child){
    	child.parent = this;
        this.child = child;
		return child;
    }

    /**
     * Get the lead of a node
     * @return Node
     */
    public Node getLeafNode() {
    	if (this.child == null) {
    		return this;
		} 
		else {
			return this.child.getLeafNode();
		}
    }	 

    /**
     * Delete leaf of node
     * @param root
     * @return Node
     */
	public Node leafDelete(Node root) { 
		if (root == null) { 
			return null; 
		} 
		if (root.child == null ) { 
		    return null; 
		} 
		root.child = leafDelete(root.child); 
		return root; 
	} 

	/**
	 * Says if the links are respected in a tree
	 * @param parent
	 * @param child
	 * @param test
	 * @return boolean
	 */
    public int RespectParent(Node parent, Node child, Node test) {
		if(parent.piece.linkedNeighborOrNoNeighbor(test.piece)) {
			if(parent.parent!=null)
				return + RespectParent(parent.parent, parent,test);
			return + 0;
		}
		else {
			return + 1; 
		}
    }

    /**
     * Search a solution of the level by depth first serach
     * @param stacks
     * @param parent
     * @param espace
     * @return boolean
     */
	public boolean DepthFirstSearch(Stack<Stack<Piece>> stacks, Node parent, String espace) {
		Boolean solutionFound = false;
		Stack<Piece> stack = stacks.pop();
		Stack<Piece> tmp = new Stack<>();
		while(!stack.isEmpty() && !solutionFound) {
			Piece p = stack.pop();
			Node node = new Node(p);
			if( RespectParent(parent, null, node ) == 0){
				parent.addChild(node);	
				if(stacks.isEmpty()) {
					return true;
				}
				else {
					solutionFound = DepthFirstSearch(stacks, node, espace + " ");		
				}
			}
			tmp.push(p);
		}
		stacks.push(tmp);
		return solutionFound;
	}

}
