package fr.dauphine.javaavance.phineloops.view;
 
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

import fr.dauphine.javaavance.phineloops.controller.ButtonPanel;
import fr.dauphine.javaavance.phineloops.controller.MouseController;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelGenerator;
import fr.dauphine.javaavance.phineloops.model.Grid;

public class LevelDisplay extends JFrame {

	private static final long serialVersionUID = 1L;
	private int DIM = 30;

	public int getDIM() {
		return DIM;
	}


	public LevelDisplay(LevelGenerator generator, Grid grid) {	    
		
		GridPanel gridPanel = new GridPanel(grid, DIM);
		grid.addObserver(gridPanel);
		MouseController mc = new MouseController(gridPanel, this);
		this.addMouseListener(mc);
		
		ButtonPanel buttonPanel = new ButtonPanel(generator, grid, this);
		Container container = this.getContentPane();
	    container.setLayout(new BorderLayout());
	    container.add(gridPanel, BorderLayout.CENTER);
	    container.add(buttonPanel, BorderLayout.SOUTH);
	    
		this.setTitle("Infinity loop");
		this.setSize(new Dimension(grid.getWidth()*DIM + 1*DIM, grid.getHeight()*DIM + 4*DIM));
		this.setVisible(true);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
}
 
