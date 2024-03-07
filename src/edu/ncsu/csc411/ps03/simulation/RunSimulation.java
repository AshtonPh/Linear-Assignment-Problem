package edu.ncsu.csc411.ps03.simulation;

import java.util.Arrays;

import edu.ncsu.csc411.ps03.simulation.RunSimulation;
import edu.ncsu.csc411.ps03.environment.Environment;
import edu.ncsu.csc411.ps03.utils.InputManager;

/**
 * A non-GUI version of the simulation. This will allow
 * you to quickly test out your implementations without
 * having to wait for each time-step to occur.
 * You are free to modify the environment for testing.
 * @author Adam Gaweda
 */
public class RunSimulation {
	private Environment env;
	private int interations;
	private boolean displaySimErrors;
	
	// Build the simulation with the following parameters
		public RunSimulation(String mapFile, int iterations) {
			int[][] map = InputManager.loadMap(mapFile);
			this.env = new Environment(map);
			this.interations = iterations;
			this.displaySimErrors = true;
		}
	
	// Iterate through the simulation, updating the environment at each time step
	public void run() {		
		for (int i = 1; i <= interations; i++) {			
			try {
				// Wrapped in try/catch in case the Robot's decision results
				// in a crash; we'll treat that the same as Action.DO_NOTHING
				env.updateConfiguration();
			} catch (Exception ex) {
				if (this.displaySimErrors) {
					String error = "[ERROR AGENT CRASH AT TIME STEP %03d] %s\n";
					System.out.printf(error, i, ex);
				}
			}
		}
		
		String line = "Configuration after %4d iterations:";
		String line2 = "Best Configuration after %4d iterations:";
		String msg = String.format(line, interations);
		String msg2 = String.format(line2, interations);
		String configuration = Arrays.toString(env.getCurrentConfiguration());
		String best = Arrays.toString(env.getBestConfiguration());
		System.out.println(msg);
		System.out.println(configuration);
		System.out.println("Score: " + env.calcScore(env.getCurrentConfiguration()));
		System.out.println(msg2);
		System.out.println(best);
		System.out.println("Score: " + env.calcScore(env.getBestConfiguration()));
		env.writeConfigurationsToFile();
	}
	
	// Returns the fitness score of the current configuration 
	public int getScore() {
		return this.env.calcScore(this.env.getBestConfiguration());
	}
	
	public static void main(String[] args) {
		String mapFile = "inputs/public/input01.txt";
		int iterations = 200;
		RunSimulation sim = new RunSimulation(mapFile, iterations);
		sim.run();
    }
}