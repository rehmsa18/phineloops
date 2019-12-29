package fr.dauphine.javaavance.phineloops.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.model.Piece;


public class GridPanel extends JPanel implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int DIM;
	private int width;
	private int height;
	private Piece cases[][];

	public GridPanel(Grid grid, int DIM) {
		height = grid.getHeight();
		width = grid.getWidth();
		cases = grid.getCases();
		grid.addObserver(this);
		this.DIM = DIM;
	}

	public Piece[][] getCases() {
		return cases;
	}

	protected void paintComponent(Graphics g) {
		
		 this.setBackground(Color.white);
    	 super.paintComponent(g);
    	 Graphics2D g2 = (Graphics2D) g;
    	 g2.setStroke(new BasicStroke(3));	
    	 g.setColor(Color.BLUE);
    	
		 for(int i=0; i<height; i++) {
			 for(int j=0; j<width; j++) {
				 cases[i][j].setGridX(j*DIM);
				 cases[i][j].setGridY(i*DIM);
				 cases[i][j].draw(g, DIM);
			}
		}
    }
	
	
	@Override
	public void update(Observable o, Object arg) {
		Graphics g = this.getGraphics();    
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				cases[i][j].draw(g, DIM);
			}
		}
		
	}
	
	public void drawGridLine(Graphics g) {
    	g.setColor(Color.RED);
    	
        for (int i = 0; i < height; i++)
            g.drawLine( 0 , i*DIM, width*DIM, i*DIM);
        
        for (int i = 0; i < width; i++)
            g.drawLine( i*DIM, 0, i*DIM, height*DIM);
	}

}
