package fr.dauphine.javaavance.phineloops.utils;

import java.io.File;
import java.io.IOException;

import fr.dauphine.javaavance.phineloops.alternativeSolver.LevelSolverStack;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelChecker;
import fr.dauphine.javaavance.phineloops.model.Grid;

public class TestInstances {

	public static void test() throws IOException, InterruptedException {
		final File folder = new File(".\\instances\\public");
		long totalTime = 0;
		int i = 1;
	    for (final File fileName : folder.listFiles()) {
			if(i!=7 &&i!=27 && i!=28 && i!=29 && i!=36 && i!=46 && i!=48) {
		    	Grid grid = Read.readFile(fileName.toString());
				long debut = System.currentTimeMillis();
				//LevelSolverIA sol = new LevelSolverIA(grid, 1);
				LevelSolverStack sol = new LevelSolverStack(grid, "max", 4);
				boolean response = sol.solve();
				long fin = System.currentTimeMillis();
				long time = fin-debut;	
				
				LevelChecker c = new LevelChecker(sol.getGrid()); 
				
				System.out.println(i + " " + fileName + " " + response + " " + c.check() + " " + time);
				
				totalTime += time;
			}
			else {
				System.out.println(i);
			}
			i++;
	    }  
	    System.out.println(totalTime);
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		TestInstances.test();
	}

}
