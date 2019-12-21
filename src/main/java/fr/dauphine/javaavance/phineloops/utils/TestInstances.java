package fr.dauphine.javaavance.phineloops.utils;

import java.io.File;
import java.io.IOException;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelSolverIA;
import fr.dauphine.javaavance.phineloops.model.Grid;

public class TestInstances {

	public static void test() throws IOException {
		final File folder = new File(".\\instances\\public");
		long totalTime = 0;
	    for (final File fileName : folder.listFiles()) {
	    	System.out.println( );
	    	Grid grid = Read.readFile(fileName.toString());
	    	
			long debut = System.currentTimeMillis();
			LevelSolverIA sol = new LevelSolverIA(grid, 1);
			//LevelSolverStack sol = new LevelSolverStack(grid, true);
			boolean response = sol.solve();
			long fin = System.currentTimeMillis();
			long time = fin-debut;
			
			System.out.println(fileName + " " + response + " " + time);
			totalTime += time;
	    }  
	    System.out.println(totalTime);
	}
	public static void main(String[] args) throws IOException {
		TestInstances.test();
	}

}
