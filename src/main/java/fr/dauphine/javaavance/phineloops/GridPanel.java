package fr.dauphine.javaavance.phineloops;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;


public class GridPanel extends JPanel implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int DIM = 30;
	int width;
	int height;
	Piece cases[][];
	Grid grid;

	public GridPanel(Grid grid) {
		height = grid.height;
		width = grid.width;
		cases = grid.cases;
		grid.addObserver(this);
	}
	
	protected void paintComponent(Graphics g) {
		
		 this.setBackground(Color.white);
    	 super.paintComponent(g);
    	 Graphics2D g2 = (Graphics2D) g;
    	 g2.setStroke(new BasicStroke(3));
    	 //this.drawGridLine(g2);	
    	 g.setColor(Color.BLUE);
    	
		 for(int i=0; i<height; i++) {
			 for(int j=0; j<width; j++) {
				 cases[i][j].gridX = j*DIM;
				 cases[i][j].gridY = i*DIM;
				 cases[i][j].draw(g);
			}
		}
    }
	
	
	@Override
	public void update(Observable o, Object arg) {
		Graphics g = this.getGraphics();    
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				cases[i][j].draw(g);
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
