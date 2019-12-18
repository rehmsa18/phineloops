package fr.dauphine.javaavance.phineloops.levelFunctions;

import java.util.Stack;

import fr.dauphine.javaavance.phineloops.model.Piece;

public class Node {
	//https://www.javagists.com/java-tree-data-structure

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

    public Node addChild(Node child){
    	child.parent = this;
        this.child = child;
		return child;
    }
     
    public Node getLeafNode() {
    	if (this.child == null) {
    		return this;
		} 
		else {
			return this.child.getLeafNode();
		}
    }	 
	 
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
	 
	public int hauteur() {
		if (this.child == null) {
			return 1;
		} 
		else {
			return 1 + this.child.hauteur();
		}
	}
	
    public static void printTree(Node node, String appender) {
    	System.out.println(appender + node.getPiece());
		if(node.getChild()!=null)
			printTree(node.getChild(), appender + " ");
    }

    public int RespectParent(Node parent, Node child, Node test) {
		if(parent.piece.linkedNeighborOrNoNeighbor(test.piece)) {
			if(parent.parent!=null)
				return + this.RespectParent(parent.parent, parent,test);
			return + 0;
		}
		else {
			return + 1; 
		}
    }

	public boolean parcours(Stack<Stack<Piece>> stacks, Node parent, String espace) {
		//System.out.println("\n"+stacks);
		Boolean solutionFound = false;
		Stack<Piece> stack = stacks.pop();
		Stack<Piece> tmp = new Stack<>();
		while(!stack.isEmpty() && !solutionFound) {
			Piece p = stack.pop();
			Node node = new Node(p);
			//System.out.println(espace + node.getData());
			if( RespectParent(parent, null, node ) == 0){
				//System.out.println(espace + node.getData());
				parent.addChild(node);	
				if(stacks.isEmpty()) {
					return true;
				}
				else {
					solutionFound = parcours(stacks, node, espace + " ");		
				}
			}
			tmp.push(p);
		}
		stacks.push(tmp);
		return solutionFound;
	}
	
	
}
