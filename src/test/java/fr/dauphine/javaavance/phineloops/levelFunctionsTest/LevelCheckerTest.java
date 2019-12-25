package fr.dauphine.javaavance.phineloops.levelFunctionsTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.dauphine.javaavance.phineloops.levelFunctions.LevelChecker;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;

class LevelCheckerTest {
	LevelChecker test;

	@Test
	public void checkTest() throws IOException {
		Grid g = new Grid(2,2);
		test = new LevelChecker(g);
		Piece p1 = new Piece(0,0,5,1);
		g.add(p1);
		Piece p2 = new Piece(0,1,1,3);
		g.add(p2);
		Piece p3 = new Piece(1,0,1,0);
		g.add(p3);
		Piece p4 = new Piece(1,1,0,0);
		g.add(p4);
		assertSame(true,test.check());
	}

}
