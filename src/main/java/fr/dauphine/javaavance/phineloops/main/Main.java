package fr.dauphine.javaavance.phineloops.main; 

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fr.dauphine.javaavance.phineloops.levelFunctions.LevelChecker;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelGenerator;
import fr.dauphine.javaavance.phineloops.levelFunctions.LevelSolverIA;
import fr.dauphine.javaavance.phineloops.model.Grid;
import fr.dauphine.javaavance.phineloops.utils.IncorrectArgumentException;
import fr.dauphine.javaavance.phineloops.utils.Read;
import fr.dauphine.javaavance.phineloops.utils.Write;
import fr.dauphine.javaavance.phineloops.view.LevelDisplay;

public class Main {
    private static String inputFile = null;  
    private static String outputFile = null;
    private static Integer width = -1;
    private static Integer height = -1;
    private static Integer maxcc = -1; 
    private static Integer threads = -1;

    private static void generate(int width, int height, String outputFile) throws IOException{
	// generate grid and store it to outputFile...
    	if(width<=0 || height<=0){
            throw new IncorrectArgumentException("height and width must be positive");
        }
    	if(outputFile == null){
			throw new IncorrectArgumentException("please type the name of the output file");
        }
		LevelGenerator generator = new LevelGenerator(height, width);
		generator.buildSolution();
		generator.shuffleSolution();
		Grid grid = generator.getGrid();
		Write.writeFile(outputFile, grid);
    }
    
    private static void generate(int width, int height, String outputFile, int maxcc) throws IOException{
	// generate grid and store it to outputFile...
    	if(width<=0 || height<=0){
            throw new IncorrectArgumentException("height and width must be positive");
        }
    	if(maxcc<1){
            throw new IncorrectArgumentException("the number of connected components must be higher 1");
        }
    	if(outputFile == null){
			throw new IncorrectArgumentException("please type the name of the output file");
        }
		LevelGenerator generator = new LevelGenerator(height, width, maxcc);
		generator.buildSolution();
		generator.shuffleSolution();
		Grid grid = generator.getGrid();
		Write.writeFile(outputFile, grid);
    }

    private static boolean solve(String inputFile, String outputFile, int threads) throws IOException{
	// load grid from inputFile, solve it and store result to outputFile...
    	if(outputFile == null || inputFile == null){
			throw new IncorrectArgumentException("please type the name of the output/input file");
        }
    	Grid grid = Read.readFile(inputFile);
		LevelSolverIA sol = new LevelSolverIA(grid, threads);
		sol.solve();
		Grid grid2 = sol.getGrid();
		Write.writeFile(outputFile, grid2);

    	return sol.solve();
    }

    private static boolean check(String inputFile) throws IOException{
	// load grid from inputFile and check if it is solved... 
    	if(inputFile == null){
			throw new IncorrectArgumentException("please type the name of the input file");
        }
    	Grid grid = Read.readFile(inputFile);
    	LevelChecker levelChecker = new LevelChecker(grid);
    	
    	return levelChecker.check();
    }
    
    private static void gui(String inputFile) throws IOException{
    	if(inputFile == null){
			throw new IncorrectArgumentException("please type the name of the input file");
        }
		Grid grid = Read.readFile(inputFile);
		LevelGenerator generator = new LevelGenerator(grid.getHeight(), grid.getWidth());
		generator.setGrid(grid);
		@SuppressWarnings("unused")
		LevelDisplay ld = new LevelDisplay(generator, grid);
		
    }
    
    public static void main(String[] args) throws IOException {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        
        options.addOption("g", "generate ", true, "Generate a grid of size height x width.");
        options.addOption("c", "check", true, "Check whether the grid in <arg> is solved.");        
        options.addOption("s", "solve", true, "Solve the grid stored in <arg>.");   
        options.addOption("o", "output", true, "Store the generated or solved grid in <arg>. (Use only with --generate and --solve.)");
        options.addOption("t", "threads", true, "Maximum number of solver threads. (Use only with --solve.)");
        options.addOption("x", "nbcc", true, "Maximum number of connected components. (Use only with --generate.)");
        options.addOption("G", "gui", true, "Run with the graphic user interface.");
        options.addOption("h", "help", false, "Display this help");
        
        try {
            cmd = parser.parse( options, args);         
        } catch (ParseException e) {
            System.err.println("Error: invalid command line format.");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "phineloops", options );
            System.exit(1);
        }       
                
		try{ 
			
		    if( cmd.hasOption( "g" ) ) {
				System.out.println("Running phineloops generator.");
				String[] gridformat = cmd.getOptionValue( "g" ).split("x");
				width = Integer.parseInt(gridformat[0]);
				height = Integer.parseInt(gridformat[1]); 
				if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");
				outputFile = cmd.getOptionValue( "o" );
				
				if( cmd.hasOption( "x" ) ) {
					maxcc = Integer.parseInt(cmd.getOptionValue( "x" )); 
					generate(width, height, outputFile, maxcc); 
				}
				else {
					generate(width, height, outputFile); 
				}
		    }
		    
		    else if( cmd.hasOption( "s" ) ) {
				System.out.println("Running phineloops solver.");
				inputFile = cmd.getOptionValue( "s" );
				if(! cmd.hasOption("o")) throw new ParseException("Missing mandatory --output argument.");      
				outputFile = cmd.getOptionValue( "o" );
				if( cmd.hasOption( "t" ) ) {
					threads = Integer.parseInt(cmd.getOptionValue( "t" )); 
				}
				boolean solved = solve(inputFile, outputFile, threads); 
				System.out.println("SOLVED: " + solved);            
		    } 
		    
		    else if( cmd.hasOption( "c" )) {
				System.out.println("Running phineloops checker.");
				inputFile = cmd.getOptionValue( "c" );
				boolean solved = check(inputFile); 
				System.out.println("SOLVED: " + solved);           
		    }
		    
		    else if( cmd.hasOption( "G" )) {
				System.out.println("Running phineloops gui.");
				inputFile = cmd.getOptionValue( "G" );         		
				gui(inputFile);         
		    }
		    
		    else {
		    	throw new ParseException("You must specify at least one of the following options: -generate -check -solve ");           
		    }
		    
		} catch (ParseException e) {
	            System.err.println("Error parsing commandline : " + e.getMessage());
	            HelpFormatter formatter = new HelpFormatter();
	            formatter.printHelp( "phineloops", options );         
	            System.exit(1); // exit with error      
		}


        //System.exit(0); // exit with success   

    }
}
