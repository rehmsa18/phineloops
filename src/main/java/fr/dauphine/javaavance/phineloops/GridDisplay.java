package fr.dauphine.javaavance.phineloops;
 
import java.awt.Graphics;

import javax.swing.JPanel;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
 
public class GridDisplay extends Application {
	
	private final static int DIM = 30;
	private int row;
	private int column;
	private int width;
	private int height;
	private Piece cases[][];
	
	
	public static void main(String[] args) {
    	Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

		GeneratorGrid gg = new GeneratorGrid(10,10);
		gg.buildSolution();	
		//gg.shuffleSolution();
		row = gg.height;
		column = gg.width;
		width = column*DIM;
		height = row*DIM;
		cases = gg.grid.cases;
		
        primaryStage.setTitle("Infinity loop");
        Canvas canvas = new Canvas(width, height);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        //drawGridLine(gc, row, column);
        drawShapes(gc, row, column, cases);
        
        Group root = new Group();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root,width, height));
        primaryStage.show();
        
        //Creating the mouse event handler 
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
           @Override 
           public void handle(MouseEvent e) { 
        	  int x = (int) (e.getX()/DIM);
        	  int y = (int) (e.getY()/DIM);
              gc.setStroke(Color.WHITE);
              gc.clearRect(x*DIM, y*DIM, DIM, DIM);
              gc.setStroke(Color.BLUE);
        	  cases[x][y].rotatePiece();
        	  draw(cases[x][y], x, y, gc);
           } 
        };  
        //Registering the event filter 
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);   

    }
    

    private void drawShapes(GraphicsContext gc, int row, int column, Piece cases[][]) {

        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(3);
        
		for(int y=0; y<row; y++) {
			for(int x=0; x<column; x++) {
				draw(cases[x][y], x, y,gc);
			}
		}
    }
    
    
    private void draw(Piece p, int a, int b, GraphicsContext gc) {
 
    	int x = a*DIM;
    	int y = b*DIM;
    	
		if (p.type==1) {
			if(p.orientation==0) {
				gc.strokeLine( x+(DIM/2), y, x+(DIM/2), y+(DIM/2));
				gc.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.orientation==1) {
				gc.strokeLine( x+(DIM/2), y+(DIM/2), x+DIM, y+(DIM/2));	
				gc.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.orientation==2) {
				gc.strokeLine( x+(DIM/2), y+(DIM/2), x+(DIM/2), y+DIM);
				gc.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
			else if(p.orientation==3) {
				gc.strokeLine( x , y+(DIM/2) , x+(DIM/2), y+(DIM/2));
				gc.fillOval(x+(DIM/4), y+(DIM/4), DIM/2, DIM/2);
			}
		}
		
		else if(p.type==2) {
			if(p.orientation==0) 
				gc.strokeLine(x+(DIM/2), y, x+(DIM/2), y+DIM);
			else if(p.orientation==1) 
				gc.strokeLine( x, y+(DIM/2), x+DIM, y+(DIM/2));
			
		}
		else if(p.type==3) {
			if(p.orientation==0) {
				gc.strokeLine( x , y+(DIM/2), x+DIM, y+DIM/2);
		        gc.strokeLine( x+(DIM/2), y, x+(DIM/2), y+(DIM/2));
			}
			else if(p.orientation==1) {
		        gc.strokeLine( x+(DIM/2), y+(DIM/2), x+DIM, y+(DIM/2));
		        gc.strokeLine( x+(DIM/2), y, x+(DIM/2), y+DIM);
			}				
			else if(p.orientation==2) {
		        gc.strokeLine( x , y+(DIM/2), x+DIM, y+(DIM/2));
		        gc.strokeLine( x+(DIM/2), y+(DIM/2), x+(DIM/2), y+DIM);			
			}
			else if(p.orientation==3) {
		        gc.strokeLine( x, y+(DIM/2), x+(DIM/2), y+(DIM/2));
		        gc.strokeLine( x+(DIM/2), y, x+(DIM/2), y+DIM);
			}				
			
		}
				
		else if(p.type==4) {
			gc.strokeLine( x, y+(DIM/2), x+DIM, y+(DIM/2));
		    gc.strokeLine( x+(DIM/2), y, x+(DIM/2), y+DIM);	
		}
		
		else if(p.type==5) {
			if(p.orientation==0) 
				gc.strokeArc( x+(DIM/2), y-(DIM/2), DIM, DIM, 180, 90, ArcType.OPEN);
			else if(p.orientation==1) 
				gc.strokeArc( x+(DIM/2), y+(DIM/2), DIM, DIM, 90, 90, ArcType.OPEN);				
			else if(p.orientation==2)
				gc.strokeArc( x-(DIM/2), y+(DIM/2), DIM, DIM, 0, 90, ArcType.OPEN);
			else if(p.orientation==3)
				gc.strokeArc( x-(DIM/2), y-(DIM/2), DIM, DIM, 270, 90, ArcType.OPEN);
			
		}
		        	      		
	}


	public void drawGridLine(GraphicsContext gc, int row, int column) {
        for (int i = 0; i < row; i++)
            gc.strokeLine( 0 , i*DIM, column*DIM, i*DIM);
        
        for (int i = 0; i < column; i++)
            gc.strokeLine( i*DIM, 0, i*DIM, row*DIM);
		
	}


	
}