package fr.dauphine.javaavance.phineloops.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import fr.dauphine.javaavance.phineloops.model.Piece;
import fr.dauphine.javaavance.phineloops.view.GridPanel;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class MouseController implements MouseListener{

	private int DIM;
	private Piece cases[][];
	private GridPanel gridPanel;
	private LevelDisplay levelDisplay;

	public int getDIM() {
		return DIM;
	}

	public void setDIM(int dIM) {
		DIM = dIM;
	}

	public Piece[][] getCases() {
		return cases;
	}

	public void setCases(Piece[][] cases) {
		this.cases = cases;
	}

	public GridPanel getGridPanel() {
		return gridPanel;
	}

	public void setGridPanel(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}

	public LevelDisplay getLevelDisplay() {
		return levelDisplay;
	}

	public void setLevelDisplay(LevelDisplay levelDisplay) {
		this.levelDisplay = levelDisplay;
	}

	public MouseController(GridPanel gridPanel, LevelDisplay levelDisplay) {
		this.gridPanel = gridPanel;
		this.levelDisplay = levelDisplay;
		this.DIM = levelDisplay.getDIM();
		cases = gridPanel.getCases();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int a = (int) (e.getX()/DIM);
   	  	int b = (int) (e.getY()/DIM);
   	  	b --;

		for(int i=0; i<gridPanel.getHeight(); i++) {
			for(int j=0; j<gridPanel.getWidth(); j++) {		
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
