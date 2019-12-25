package fr.dauphine.javaavance.phineloops.levelFunctionsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelGenerator;
import fr.dauphine.javaavance.phineloops.model.Piece;

class LevelGeneratorTest {
	LevelGenerator test;
	
	@Test
	public void respectLinkNeighborsTest() {
		test = new LevelGenerator(2,2);
		Piece p1 = new Piece(0,0,5,1);
		test.getGrid().add(p1);
		Piece p2 = new Piece(0,1,1,3);
		test.getGrid().add(p2);
		Piece p3 = new Piece(1,0,1,0);
		test.getGrid().add(p3);
		assertSame(true,test.respectLinkNeighbors(0, 0, p1));
	}
	
	@Test
	public void pieceType1NeighborsTest() {	
		test = new LevelGenerator(2,2);
		Piece p1 = new Piece(0,0,1,1);
		test.getGrid().add(p1);
		Piece p2 = new Piece(0,1,1,3);
		test.getGrid().add(p2);
		assertSame(true,test.pieceType1Neighbors(0, 1, p2));
	}

	@Test
	public void pieceAlternativeTest(){
		test = new LevelGenerator(2,2);
		Piece p1 = new Piece(0,0,5,1);
		test.getGrid().add(p1);
		Piece p2 = new Piece(0,1,1,3);
		test.getGrid().add(p2);
		Piece p3 = new Piece(1,0,1,0);
		test.getGrid().add(p3);
		Piece p = new Piece(0,0);
		ArrayList<Piece> l = test.pieceAlternative(1, 1);
		for(Piece pp : l ) {
			assertEquals(p.getType(),pp.getType());
		}
	}
	
	
	
	@Test
	public void associatePieceToGridTest() {
		test = new LevelGenerator(2,2);
		Piece p1 = new Piece(0,0,5,1);
		test.getGrid().add(p1);
		Piece p2 = new Piece(0,1,1,3);
		test.getGrid().add(p2);
		Piece p3 = new Piece(1,0,1,0);
		test.getGrid().add(p3);
		Piece p = new Piece(0,0);
		assertEquals(p.getType(),test.associatePieceToGrid(1, 1).getType());
	}

}
