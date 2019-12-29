package fr.dauphine.javaavance.phineloops.model;

import java.util.ArrayList;

public class ConnectedComponent {

	private int nbLinkPossible;
	private ArrayList<Piece> pieces;

	public ConnectedComponent() {
		pieces = new ArrayList<>();
	}
	
	public int getNbLinkPossible() {
		return nbLinkPossible;
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
	 * add all pieces from another connected component
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
	 * calculate the number of links a connected component can make with other piece 
	 */
	public void nbLink() {
		nbLinkPossible = 0;
		for(Piece piece : pieces) {
			nbLinkPossible = nbLinkPossible + piece.getLinks()[1] + piece.getLinks()[2];
			nbLinkPossible = nbLinkPossible - piece.getLinks()[0] - piece.getLinks()[3];
		}
	}
	
}
