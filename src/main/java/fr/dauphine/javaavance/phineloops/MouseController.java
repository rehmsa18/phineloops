package fr.dauphine.javaavance.phineloops;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseController implements MouseListener{

	private static final int DIM  = 30;
	Piece cases[][];
	GridPanel gridPanel;
	LevelDisplay levelDisplay;

	public MouseController(GridPanel gridPanel, LevelDisplay levelDisplay) {
		this.gridPanel = gridPanel;
		this.levelDisplay = levelDisplay;
		cases = gridPanel.cases;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int a = (int) (e.getX()/DIM);
   	  	int b = (int) (e.getY()/DIM);
   	  	b --;

		for(int i=0; i<gridPanel.height; i++) {
			for(int j=0; j<gridPanel.width; j++) {		
				if(j == a && i == b) {
					//System.out.print(cases[i][j] + " -> ");
					cases[i][j].rotatePiece();
					//System.out.println(cases[i][j]);
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
