package axelrod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
 
//REPETIONS,ITERATIONS,CHECKPOINT,TYPE,ROWS,COLS,FEATURES,TRAITS,RADIUS,MUTATION,SELECTION_ERROR
public class Controller
{
	
    public void open() throws FileNotFoundException
    {
    	// This is a pool of threads of the size of the cores of the computer
    	ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	// Keep the tasks in a list. Don't start until the entire file is read.
        ArrayList<Callable<String>> tasks = new ArrayList<Callable<String>>();
    	
        //Get scanner instance
        Scanner scanner = new Scanner(new File("sample.csv"));
        //skip the column titles
        scanner.nextLine();
        scanner.useDelimiter(",");
         
        //Start reading the file
        while (scanner.hasNext())
        {
        	int repetitions = Integer.parseInt(scanner.next());
        	if (repetitions > 0) {
	        	World world = new World();
	        	world.ITERATIONS = Integer.parseInt(scanner.next());
	        	world.CHECKPOINT = Integer.parseInt(scanner.next());
	        	world.TYPE = scanner.next();
	        	world.BUFFERED_SIZE = Integer.parseInt(scanner.next());
	        	world.ROWS = Integer.parseInt(scanner.next());
	        	world.COLS = Integer.parseInt(scanner.next());
	        	world.FEATURES = Integer.parseInt(scanner.next());
	        	world.TRAITS = Integer.parseInt(scanner.next());
	        	world.RADIUS = Integer.parseInt(scanner.next());
	        	world.MUTATION = Float.parseFloat(scanner.next());
	        	world.SELECTION_ERROR = Float.parseFloat(scanner.next());
	        	tasks.add(world);
	        	
	        	// Generate tasks per repetitions
	        	for (int r = 1; r < repetitions; r++) {
		        	tasks.add(world.clone());
	        	}
	        	
	        	if (scanner.hasNextLine()) {
	        		scanner.nextLine();
	        	}
        	}
        }
         
        //Do not forget to close the scanner 
        scanner.close();
        
        
		try {
			//Invoke the tasks
			List<Future<String>> results = exec.invokeAll(tasks);
	        exec.shutdown();
	        
	        // Write the results to the file
        	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("results.csv"), "utf-8"));
            writer.write(World.header());
            for(Future<String> f : results) {
            	f.get();
				writer.write(f.get());
	        }
            writer.close();
	        

		} catch (InterruptedException e) {
			System.out.println("Error generated in exec.invokeAll(tasks)");
			e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("Error generated in f.get()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in I/O");
			e.printStackTrace();
	    }

        
    }
    
}