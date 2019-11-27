package fr.dauphine.javaavance.phineloops;
 
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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
	

    public static void main(String[] args) {
    	
    	int row = 16;
    	int column = 16;
    	
		LevelGenerator generator = new LevelGenerator(row,column);
		generator.buildSolution();
		//generator.shuffleSolution();
		Grid grid = generator.grid;
    
    	LevelDisplay ld = new LevelDisplay(generator, grid);
				
    }
  
}
 
