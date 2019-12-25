package fr.dauphine.javaavance.phineloops.modelTest;
import fr.dauphine.javaavance.phineloops.model.Piece;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;


class PieceTest {

	private Piece p;

	@Test
	public void defineNbNeighborsTest() {		
		int i;
		for(i=0; i<5; i++) {
			p = new Piece(i,0);
			assertEquals(i,p.getNbneighbors());
		}
		
		p = new Piece(i,0);
		assertEquals(2,p.getNbneighbors());
	}
	
	@Test
	public void defineOrientationTest() {
		int links[] = {1,1,0,0};
		
		for(int i=0; i<6; i++) {
			p = new Piece(i,0);
			p.defineOrientation(i, links);
			if((p.getType() == 0) || (p.getType() == 4) || (p.getType() == 3 && p.getLinks()[2] == 0) || (p.getLinks()[0] == 1 && (p.getType() == 1 || p.getType() == 2 || (p.getType() == 5 && p.getLinks()[1] == 1)))) {
				assertEquals(0,p.getOrientation());
			}
			if((p.getType() == 3 && p.getLinks()[3] == 0) || (p.getLinks()[1] == 1 && (p.getType() == 1 || p.getType() == 2 || (p.getType() == 5 && p.getLinks()[2] == 1)))) {
				assertEquals(1,p.getOrientation());
			}
			if((p.getType() == 3 && p.getLinks()[0] == 0) || (p.getLinks()[2] == 1 && (p.getType() == 1 || (p.getType() == 5 && p.getLinks()[3] == 1)))) {
				assertEquals(2,p.getOrientation());
			}
			if((p.getType() == 3 && p.getLinks()[1] == 0) || (p.getLinks()[3] == 1 && (p.getType() == 1 || (p.getType() == 5 && p.getLinks()[0] == 1)))) {
				assertEquals(3,p.getOrientation());
			}
		}
		
	}
	
	@Test
	public void isOrientationChoiceTest() {
		p = new Piece(5,3);
		assertSame(true,p.isOrientationChoice(0));
	}
	
	@RepeatedTest(4)
	public void rotatePieceTest() {
		p = new Piece(5,2);
		p.rotatePiece();
	}
	
	public static int sum(int links []) {
		int s=0;
		for(int i = 0; i<links.length;i++)
			s += links[i]; 
		return s;
	}
	
	@Test
	public void defineLinksTest() {
		for(int i=0; i<4; i++) {
			p = new Piece(5,i);
			p.defineLinks();
			assertTrue(p.getNbneighbors() == sum(p.getLinks()));
		}
	}
	
	@Test
	public void isNeighborTest() {
		p = new Piece(2,5,3,0);
		assertEquals(1,p.isNeighbor(new Piece(2,6,3,0)));
	}
	
	@Test
	public void isLinkedTest() {
		p = new Piece(2,5,3,1);
		assertSame(true, p.isLinked(new Piece(2,6,3,3)));
	}
	
	@Test
	public void noLinkNorthTest() {
		p = new Piece(3,0);
		assertSame(false, p.noLinkNorth());
	}
	
	@Test
	public void noLinkEastTest() {
		p = new Piece(2,0);
		assertSame(true, p.noLinkEast());
	}
	
	@Test
	public void noLinkSouthTest() {
		p = new Piece(1,2);
		assertSame(false, p.noLinkSouth());
	}
	
	@Test
	public void noLinkWestTest() {
		p = new Piece(1,2);
		assertSame(true, p.noLinkWest());
	}
	
	@RepeatedTest(10)
	public void shufflePieceTest() {
		p = new Piece(5,3);
		p.shufflePiece();
	}
	
	@Test
	public void defineNbRotationTest(){
		p = new Piece(2,0);
		assertSame(2, p.getNbRotation());
	}

}
