package fr.dauphine.javaavance.phineloops.modelTest;

import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;



class GridTest {

	private Grid g;

	@Test
	public void existsPieceTest() {
			g = new Grid(2,2);
			assertSame(true,g.existsPiece(1, 1));
	}

	@Test
	public void addTest () {
		g = new Grid(1,2);
		Piece p = new Piece(0,1,1,0);
		g.add(p);
		assertEquals(p,g.getCases()[0][1]);
	}
	
	@Test
	public void allLinkedTest() {
		g = new Grid(2,2);
		Piece p1 = new Piece(0,0,5,1);
		g.add(p1);
		Piece p2 = new Piece(0,1,1,3);
		g.add(p2);
		Piece p3 = new Piece(1,0,1,0);
		g.add(p3);
		assertSame(true,g.allLinked(p1));
	}
	
	@Test
	public void detectImpossibleTest() {
		g = new Grid(1,1);
		Piece p = new Piece(0,0,1,1);
		g.add(p);
		assertSame(false,g.detectImpossible());
	}
	
	@Test
	public void detectCircledByType0Test() {
		g = new Grid(3,3);
		Piece p1 = new Piece(0,0,5,1);
		g.add(p1);
		Piece p2 = new Piece(0,1,0,0);
		g.add(p2);
		Piece p3 = new Piece(1,0,0,0);
		g.add(p3);
		assertSame(false,g.detectCircledByType0());
	}
	
	/*@Test
	public void lockPiece() {
		g = new Grid(2,2);
		Piece p1 = new Piece(0,0,5,1);
		g.add(p1);
		Piece p2 = new Piece(0,1,1,3);
		g.add(p2);
		Piece p3 = new Piece(1,0,1,0);
		g.add(p3);
		assertEquals(3,g.lockPiece());
	}*/

}
