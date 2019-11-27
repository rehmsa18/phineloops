package fr.dauphine.javaavance.phineloops;

import java.awt.Graphics;

public class DrawablePiece {

	Piece p;
	int x;
	int y;
	int DIM = 30;
	
    public DrawablePiece(Piece p) {
		this.p = p;
		this.x = p.gridX;
		this.y = p.gridY;
	}

	public void paintComponent(Graphics g) {
		
		if (p.type==1) {
			if(p.orientation==0) {
				g.drawLine( x+(DIM/2), y, x+(DIM/2), y+(DIM/2));
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.orientation==1) {
				g.drawLine( x+(DIM/2), y+(DIM/2), x+DIM, y+(DIM/2));	
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.orientation==2) {
				g.drawLine( x+(DIM/2), y+(DIM/2), x+(DIM/2), y+DIM);
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.orientation==3) {
				g.drawLine( x , y+(DIM/2) , x+(DIM/2), y+(DIM/2));
				g.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
		}
		
		else if(p.type==2) {
			if(p.orientation==0) 
				g.drawLine(x+(DIM/2), y, x+(DIM/2), y+DIM);
			else if(p.orientation==1) 
				g.drawLine( x, y+(DIM/2), x+DIM, y+(DIM/2));
			
		}
		else if(p.type==3) {
			if(p.orientation==0) {
				g.drawLine( x , y+(DIM/2), x+DIM, y+DIM/2);
		        g.drawLine( x+(DIM/2), y, x+(DIM/2), y+(DIM/2));
			}
			else if(p.orientation==1) {
		        g.drawLine( x+(DIM/2), y+(DIM/2), x+DIM, y+(DIM/2));
		        g.drawLine( x+(DIM/2), y, x+(DIM/2), y+DIM);
			}				
			else if(p.orientation==2) {
		        g.drawLine( x , y+(DIM/2), x+DIM, y+(DIM/2));
		        g.drawLine( x+(DIM/2), y+(DIM/2), x+(DIM/2), y+DIM);			
			}
			else if(p.orientation==3) {
		        g.drawLine( x, y+(DIM/2), x+(DIM/2), y+(DIM/2));
		        g.drawLine( x+(DIM/2), y, x+(DIM/2), y+DIM);
			}				
			
		}
				
		else if(p.type==4) {
			g.drawLine( x, y+(DIM/2), x+DIM, y+(DIM/2));
		    g.drawLine( x+(DIM/2), y, x+(DIM/2), y+DIM);	
		}
		
		else if(p.type==5) {
			if(p.orientation==0) 
				g.drawArc( x+(DIM/2), y-(DIM/2), DIM, DIM, 180, 90);
			else if(p.orientation==1) 
				g.drawArc( x+(DIM/2), y+(DIM/2), DIM, DIM, 90, 90);				
			else if(p.orientation==2)
				g.drawArc( x-(DIM/2), y+(DIM/2), DIM, DIM, 0, 90);
			else if(p.orientation==3)
				g.drawArc( x-(DIM/2), y-(DIM/2), DIM, DIM, 270, 90);
			
		}
  
    }
	
}