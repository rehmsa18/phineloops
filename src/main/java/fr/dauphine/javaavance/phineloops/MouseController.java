package fr.dauphine.javaavance.phineloops;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public class MouseController implements MouseListener{

	private static final int DIM  = 30;
	Piece cases[][];
	GridPanel d;
	LevelDisplay levelDisplay;

	public MouseController(GridPanel gridPanel, LevelDisplay levelDisplay) {
		this.d = gridPanel;
		this.levelDisplay = levelDisplay;
		cases = gridPanel.cases;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int a = (int) (e.getX()/DIM);
   	  	int b = (int) (e.getY()/DIM);
   	  	b --;
   	  	int row = d.row;
   	  	int column = d.column;
   	  	
   	  	//System.out.println(a + " " + e.getX());
   	  	//System.out.println(b + " "+ e.getY());

   	  	
   	  	System.out.println("case clicked");
	    
		for(int y=0; y<row; y++) {
			for(int x=0; x<column; x++) {		
				if(x == a && y == b) {
					System.out.print(cases[x][y] + " -> ");
					cases[x][y].rotatePiece();
					System.out.print(cases[x][y]);
					levelDisplay.repaint();				
				}
			}
		}			
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
