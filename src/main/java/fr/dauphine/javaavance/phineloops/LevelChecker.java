package fr.dauphine.javaavance.phineloops;

import java.io.IOException;

import fr.dauphine.javaavance.phineloops.model.Grid;

public class LevelChecker {
	
	Grid grid;
	
	public LevelChecker(Grid grid) {
		this.grid = grid;
	}
	
	public boolean check() throws IOException {
		
		for(int i=0; i<grid.getHeight(); i++){
			for(int j=0; j<grid.getWidth(); j++){
								
				//verify the pieces in the northern border
				if (!grid.existsPiece(i-1,j) && grid.getCases()[i][j].getLinks()[0] == 1){
					return false;
				}
				
				//check the pieces on the eastern border
				if(!grid.existsPiece(i,j+1) && grid.getCases()[i][j].getLinks()[1] == 1) {
					return false;
				}
			
				//check the pieces on the southern border
				if(!grid.existsPiece(i+1,j) && grid.getCases()[i][j].getLinks()[2] == 1) {
					return false;
				}
				
				//check the pieces on the western border
				if(!grid.existsPiece(i,j-1) && grid.getCases()[i][j].getLinks()[3] == 1) {
					return false;
				}
				
				//check the pieces in the center
				if(!grid.allLinked(grid.getCases()[i][j])) {
					return false;
				}
			}			
		}
		return true;
	}

}
