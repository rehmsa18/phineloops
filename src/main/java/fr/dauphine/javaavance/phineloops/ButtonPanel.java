package fr.dauphine.javaavance.phineloops;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;


public class ButtonPanel extends JPanel {

	public ButtonPanel(LevelGenerator levelGenerator, Grid grid, LevelDisplay levelDisplay ) {

		this.setLayout(new FlowLayout());
		
		JButton btnCheck = new JButton("Check"); // Check if a solution is true
		JButton btnShuffle = new JButton("Shuffle"); // Shuffle a level
		JButton btnSolve = new JButton("Solve"); // Resolve a level
		JButton btnNew = new JButton("New"); // Get a new level
		
		this.add(btnCheck);
		this.add(btnSolve);
		this.add(btnShuffle);
		this.add(btnNew);

		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				LevelChecker levelChecker = new LevelChecker(grid);
				try {
					System.out.println("Solution : " + levelChecker.check() + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	      
		btnShuffle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				levelGenerator.shuffleSolution();
				levelDisplay.repaint();
			}
		});
	      
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SolverGrid sol = new SolverGrid(grid);
				sol.solve();
				sol.grid.displayInConsole();
				levelDisplay.repaint();
	        }
		});
		
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				levelGenerator.buildSolution();
				levelGenerator.shuffleSolution();
				levelDisplay.repaint();
	        }
		});
	           
	}
}
