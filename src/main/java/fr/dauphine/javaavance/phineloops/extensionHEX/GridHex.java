package fr.dauphine.javaavance.phineloops.extensionHEX;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

public class GridHex  extends Observable {
	int height;
	int width;
	PieceHex cases[][];
	
	public GridHex(int height, int width){
		this.height = height;
		this.width = width;
		cases = new PieceHex[this.height][this.width];
	}
	
	public void add (PieceHex p) {
		cases[p.x][p.y] = p;
	}
	
	/**
	 * 
	 * @return all pieces of the grid
	 */
	public PieceHex[][] getCase() {
		return cases;
	}
}
