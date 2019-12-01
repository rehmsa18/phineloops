package fr.dauphine.javaavance.phineloops;

import java.util.ArrayList;

public class ConnectedComponent {

	int nbLinkPossible;
	ArrayList<Piece> pieces;

	public ConnectedComponent() {
		pieces = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return the number of pieces
	 */
	public int size() {
		return pieces.size();
	}

	/**
	 * Add piece to the connected component 
	 * @param piece
	 */
	public void addPiece(Piece piece) {
		pieces.add(piece);
		this.nbLink();
	}

	/**
	 * add all pieces from another connected componenet
	 * @param connectedComponent
	 */
	public void addAll(ConnectedComponent connectedComponent) {
		pieces.addAll(connectedComponent.pieces);
		this.nbLink();
	}
	
	/**
	 * Says if a piece is in the connected component
	 * @param piece
	 * @return true if contains piece
	 */
	public boolean contains(Piece piece) {
		return pieces.contains(piece);
	}
	
	/**
	 * 
	 * @param index
	 * @return piece
	 */
	public Piece get(int index) {
		return pieces.get(index);
	}
	
	/**
	 * calculate the number of links a connected componnent can make with other piece 
	 */
	public void nbLink() {
		nbLinkPossible = 0;
		for(Piece piece : pieces) {
			nbLinkPossible = nbLinkPossible + piece.links[1] + piece.links[2];
			nbLinkPossible = nbLinkPossible - piece.links[0] - piece.links[3];
		}
	}
	
}
