package simulator;

import java.io.File;
import java.io.FileNotFoundException;

import simulator.control.Controller;
import simulator.control.ControllerBatch;
import simulator.control.Printable;


public class Main {
	private static Printer printer;
	

	public static void main(String[] args) {
		printer = new Main.Printer();

		ControllerBatch controller = new ControllerBatch(printer);
		
	
		try {
			controller.load_tasks("sample.csv");
		} catch (FileNotFoundException e) {
			System.out.println("WARNING: Sample experimental file not found (" + "sample.csv" + ")");
			e.printStackTrace();
		}
		
		File dir = new File(Controller.WORKSPACE_DIR + Controller.RESULTS_DIR);
		
		String result_dir = Controller.RESULTS_DIR.substring(0, Controller.RESULTS_DIR.length() - 1);
		for( int i = 0; dir.exists(); i++) {
			dir = new File(result_dir + i + "/");	
		}

		String results_dir = dir.getAbsolutePath() + "/";
		controller.set_RESULTS_DIR(results_dir);
		(new File(results_dir + Controller.ITERATIONS_DIR)).mkdirs();
		 
		
		controller.start();
	}
	
	private static class Printer implements  Printable {
		public void print(String str){
			System.out.print(str);			
		} 
	}

}
