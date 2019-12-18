package fr.dauphine.javaavance.phineloops;

import java.awt.Color;
import java.awt.Graphics;

import fr.dauphine.javaavance.phineloops.model.Piece;

public class DrawablePiece {

	Piece p;
	int x;
	int y;
	int DIM;
	
    public DrawablePiece(Piece p, int DIM) {
		this.p = p;
		this.x = p.getGridX();
		this.y = p.getGridY();
		this.DIM = DIM;
	}

    /**
     * Draw piece in graphic interface 
     * @param g
     */
	public void paintComponent(Graphics g) {
		
		if(p.getLock()==0)
		   	 g.setColor(Color.green);
		else
		   	 g.setColor(Color.blue);
		
		if (p.getType()==1) {
			if(p.getOrientation()==0) {
				g.drawLine( x+(DIM/2), y, x+(DIM/2), y+(DIM/2));
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.getOrientation()==1) {
				g.drawLine( x+(DIM/2), y+(DIM/2), x+DIM, y+(DIM/2));	
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.getOrientation()==2) {
				g.drawLine( x+(DIM/2), y+(DIM/2), x+(DIM/2), y+DIM);
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.getOrientation()==3) {
				g.drawLine( x , y+(DIM/2) , x+(DIM/2), y+(DIM/2));
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
		}
		
		else if(p.getType()==2) {
			if(p.getOrientation()==0) 
				g.drawLine(x+(DIM/2), y, x+(DIM/2), y+DIM);
			else if(p.getOrientation()==1) 
				g.drawLine( x, y+(DIM/2), x+DIM, y+(DIM/2));
			
		}
		else if(p.getType()==3) {
			if(p.getOrientation()==0) {
				g.drawLine( x , y+(DIM/2), x+DIM, y+DIM/2);
		        g.drawLine( x+(DIM/2), y, x+(DIM/2), y+(DIM/2));
			}
			else if(p.getOrientation()==1) {
		        g.drawLine( x+(DIM/2), y+(DIM/2), x+DIM, y+(DIM/2));
		        g.drawLine( x+(DIM/2), y, x+(DIM/2), y+DIM);
			}				
			else if(p.getOrientation()==2) {
		        g.drawLine( x , y+(DIM/2), x+DIM, y+(DIM/2));
		        g.drawLine( x+(DIM/2), y+(DIM/2), x+(DIM/2), y+DIM);			
			}
			else if(p.getOrientation()==3) {
		        g.drawLine( x, y+(DIM/2), x+(DIM/2), y+(DIM/2));
		        g.drawLine( x+(DIM/2), y, x+(DIM/2), y+DIM);
			}				
			
		}
				
		else if(p.getType()==4) {
			g.drawLine( x, y+(DIM/2), x+DIM, y+(DIM/2));
		    g.drawLine( x+(DIM/2), y, x+(DIM/2), y+DIM);	
		}
		
		else if(p.getType()==5) {
			if(p.getOrientation()==0) 
				g.drawArc( x+(DIM/2), y-(DIM/2), DIM, DIM, 180, 90);
			else if(p.getOrientation()==1) 
				g.drawArc( x+(DIM/2), y+(DIM/2), DIM, DIM, 90, 90);				
			else if(p.getOrientation()==2)
				g.drawArc( x-(DIM/2), y+(DIM/2), DIM, DIM, 0, 90);
			else if(p.getOrientation()==3)
				g.drawArc( x-(DIM/2), y-(DIM/2), DIM, DIM, 270, 90);
			
		}
  
    }
	
}