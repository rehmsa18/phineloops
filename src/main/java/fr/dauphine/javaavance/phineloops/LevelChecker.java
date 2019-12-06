package fr.dauphine.javaavance.phineloops;

import java.io.IOException;

public class LevelChecker {
	
	public static boolean checker() throws IOException {
		
		Grid  grid= Grid.readFile("a");
		
		System.out.println("hello");

		grid.displayInConsole();		
		
		for(int i=0; i<grid.height; i++){
			for(int j=0; j<grid.width; j++){
				
				//verify the pieces in the northern border
				if (!grid.existsPiece(i-1,j) && grid.cases[i][j].links[0] == 1){
					return false;
				}
				
				//check the pieces on the eastern border
				if(!grid.existsPiece(i,j+1) && grid.cases[i][j].links[1] == 1) {
					return false;
				}
			
				//check the pieces on the southern border
				if(!grid.existsPiece(i+1,j) && grid.cases[i][j].links[2] == 1) {
					return false;
				}
				
				//check the pieces on the western border
				if(!grid.existsPiece(i,j-1) && grid.cases[i][j].links[3] == 1) {
					return false;
				}
				
				//check the pieces in the center
				if(!grid.allLinked(grid.cases[i][j])) {
					return false;
				}
			}
			
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(LevelChecker.checker());
	}

}