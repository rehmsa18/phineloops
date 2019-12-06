package fr.dauphine.javaavance.phineloops;
 
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

public class LevelDisplay extends JFrame {
	
	int DIM = 30;

	public LevelDisplay(LevelGenerator generator, Grid grid) {
		GridPanel gridPanel = new GridPanel(grid);
		grid.addObserver(gridPanel);
		MouseController mc = new MouseController(gridPanel, this);
		this.addMouseListener(mc);
		
		ButtonPanel buttonPanel = new ButtonPanel(generator, this);

		Container container = this.getContentPane();
	    container.setLayout(new BorderLayout());
	    container.add(gridPanel, BorderLayout.CENTER);
	    container.add(buttonPanel, BorderLayout.SOUTH);
	    
		this.setTitle("Infinity loop");
		this.setSize(new Dimension(grid.width*DIM + 1*DIM, grid.height*DIM + 3*DIM));
		this.setVisible(true);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	

    public static void main(String[] args) throws IOException {
    	
    	int width = 10;
    	int height = 7; 
    	int maxConnectedComponent = 25;
		
    
		LevelGenerator generator = new LevelGenerator(height, width, maxConnectedComponent);
		
		//LevelGenerator generator = new LevelGenerator(width, height);
		
		
		if(maxConnectedComponent > (width*height)/2) {
			System.err.println("Nombre de composantes connexes impossible");
		}
		
		generator.buildSolution();
		Grid grid = generator.grid;
		
		//grid.writeFile("a");
		//Grid grid = Grid.readFile("a");
		
		LevelDisplay ld = new LevelDisplay(generator, grid);

		
				
    }
  
}
 
