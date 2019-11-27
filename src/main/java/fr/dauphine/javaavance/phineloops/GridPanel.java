package fr.dauphine.javaavance.phineloops;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;


public class GridPanel extends JPanel implements Observer {
	
	final static int DIM = 30;
	int row;
	int column;
	int width;
	int height;
	Piece cases[][];
	Grid grid;

	public GridPanel(Grid grid) {
		row = grid.height;
		column = grid.width;
		width = column*DIM;
		height = row*DIM;
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
    	
		for(int y=0; y<row; y++) {
			for(int x=0; x<column; x++) {
				cases[x][y].gridX = x*DIM;
				cases[x][y].gridY =y*DIM;
				cases[x][y].draw(g);
			}
		}
    }
	
	
	@Override
	public void update(Observable o, Object arg) {
		Graphics g = this.getGraphics();    
		for(int y=0; y<row; y++) {
			for(int x=0; x<column; x++) {
				cases[x][y].draw(g);
			}
		}
		
	}
	
	public void drawGridLine(Graphics g) {
    	g.setColor(Color.RED);
    	
        for (int i = 0; i < row; i++)
            g.drawLine( 0 , i*DIM, column*DIM, i*DIM);
        
        for (int i = 0; i < column; i++)
            g.drawLine( i*DIM, 0, i*DIM, row*DIM);
	}

}
