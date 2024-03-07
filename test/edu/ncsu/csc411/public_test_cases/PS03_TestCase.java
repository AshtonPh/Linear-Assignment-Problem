package edu.ncsu.csc411.public_test_cases;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Timeout;

import edu.ncsu.csc411.ps03.simulation.RunSimulation;

/**
 * This JUnit test suite uses JUnit5. In order to run these 
 * test cases, you will need to have JUnit5 installed on your
 * local machines. You can set your Project to use JUnit5 by
 * right-clicking on the project and selecting "Properties", then
 * selecting "Java Build Path". Finally, selecting "Add Library..."
 * will allow you to select "JUnit" and specify the version.
 * DO NOT MODIFY.
 * @author Adam Gaweda
 */ 
public class PS03_TestCase {
	private final int NUM_TRIALS = 100;
	private final int ITERATIONS = 1000;
	private int successfulTrials = 0;
	private boolean displayExceptions = false;
	private int DURATION = 5000;
	private String line = "Test %02d success rate: %.2f after %d trials";
	private String error = "Error on iteration %02d: %s";
	
	@Before
	public void setUp() {
		successfulTrials = 0;
	}

	// This Problem Set utilizes the timeout feature with JUnit test cases.
	// This means that after 5000 milliseconds (5 seconds) a trial will fail.
	// This is to prevent time expensive brute force decisions.
	@Test
	public void testEnvironment01() {
		String map = "inputs/public/input01.txt";
		
		// Each test's success threshold is slightly above the median, which has been
		// included for your reference.
		int threshold = 110; // median 100
		
		// Each environment will be executed 100 times
		for (int trial = 0; trial < NUM_TRIALS; trial++) {
			RunSimulation sim = new RunSimulation(map, ITERATIONS);
			try {
				// CompletableFuture will run an iteration's simulation
				// asynchronously for 5000 milliseconds (5 seconds) before timing out.
				// This is to prevent infinite loops or inefficient implementations
				// that are brute forcing solutions.
				Runnable simulation = () -> sim.run();
				CompletableFuture.runAsync(simulation).get(DURATION, TimeUnit.MILLISECONDS);
				if(sim.getScore() >= threshold) {
					successfulTrials++;
				}
			} catch (InterruptedException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (ExecutionException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (TimeoutException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			}
		}

		String msg = String.format(line, 1, successfulTrials/(NUM_TRIALS*1.0)*100, NUM_TRIALS);
		System.out.println(msg);
		// To pass the test case, you must successfully complete 70% of the trials
		assertTrue(successfulTrials/(NUM_TRIALS*1.0) >= 0.7, msg);
	}
	
	// Environment 02 is a Sanity Check with a clear optimal configuration 
	@Test
	public void testEnvironment02() {
		String map = "inputs/public/input02.txt";
		
		// Your code should be able to reach the optimal configuration in this test
		int threshold = 450;
		
		for (int trial = 0; trial < NUM_TRIALS; trial++) {
			RunSimulation sim = new RunSimulation(map, ITERATIONS);
			try {
				// CompletableFuture will run an iteration's simulation
				// asynchronously for 5000 milliseconds (5 seconds) before timing out.
				// This is to prevent infinite loops or inefficient implementations
				// that are brute forcing solutions.
				Runnable simulation = () -> sim.run();
				CompletableFuture.runAsync(simulation).get(DURATION, TimeUnit.MILLISECONDS);
				if(sim.getScore() >= threshold) {
					successfulTrials++;
				}
			} catch (InterruptedException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (ExecutionException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (TimeoutException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			}
		}

		String msg = String.format(line, 2, successfulTrials/(NUM_TRIALS*1.0)*100, NUM_TRIALS);
		System.out.println(msg);
		assertTrue(successfulTrials/(NUM_TRIALS*1.0) >= 0.7, msg);
	}
	
	// Tests 03-05 increase the difficulty to brute force solutions by increasing the
	// number of possible permutations to 9!, or 362,880
	@Test
	public void testEnvironment03() {
		String map = "inputs/public/input03.txt";
		int threshold = 560; // median 518
		
		for (int trial = 0; trial < NUM_TRIALS; trial++) {
			RunSimulation sim = new RunSimulation(map, ITERATIONS);
			try {
				// CompletableFuture will run an iteration's simulation
				// asynchronously for 5000 milliseconds (5 seconds) before timing out.
				// This is to prevent infinite loops or inefficient implementations
				// that are brute forcing solutions.
				Runnable simulation = () -> sim.run();
				CompletableFuture.runAsync(simulation).get(DURATION, TimeUnit.MILLISECONDS);
				if(sim.getScore() >= threshold) {
					successfulTrials++;
				}
			} catch (InterruptedException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (ExecutionException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (TimeoutException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			}
		}

		String msg = String.format(line, 3, successfulTrials/(NUM_TRIALS*1.0)*100, NUM_TRIALS);
		System.out.println(msg);
		assertTrue(successfulTrials/(NUM_TRIALS*1.0) >= 0.7, msg);
	}
	
	@Test
	public void testEnvironment04() {
		String map = "inputs/public/input04.txt";
		int threshold = 500; // median 462
		
		for (int trial = 0; trial < NUM_TRIALS; trial++) {
			RunSimulation sim = new RunSimulation(map, ITERATIONS);
			try {
				// CompletableFuture will run an iteration's simulation
				// asynchronously for 5000 milliseconds (5 seconds) before timing out.
				// This is to prevent infinite loops or inefficient implementations
				// that are brute forcing solutions.
				Runnable simulation = () -> sim.run();
				CompletableFuture.runAsync(simulation).get(DURATION, TimeUnit.MILLISECONDS);
				if(sim.getScore() >= threshold) {
					successfulTrials++;
				}
			} catch (InterruptedException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (ExecutionException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (TimeoutException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			}
		}

		String msg = String.format(line, 4, successfulTrials/(NUM_TRIALS*1.0)*100, NUM_TRIALS);
		System.out.println(msg);
		assertTrue(successfulTrials/(NUM_TRIALS*1.0) >= 0.7, msg);
	}
	
	@Test
	public void testEnvironment05() {
		String map = "inputs/public/input05.txt";
		int threshold = 525; // median 478
		
		for (int trial = 0; trial < NUM_TRIALS; trial++) {
			RunSimulation sim = new RunSimulation(map, ITERATIONS);
			try {
				// CompletableFuture will run an iteration's simulation
				// asynchronously for 5000 milliseconds (5 seconds) before timing out.
				// This is to prevent infinite loops or inefficient implementations
				// that are brute forcing solutions.
				Runnable simulation = () -> sim.run();
				CompletableFuture.runAsync(simulation).get(DURATION, TimeUnit.MILLISECONDS);
				if(sim.getScore() >= threshold) {
					successfulTrials++;
				}
			} catch (InterruptedException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (ExecutionException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			} catch (TimeoutException e) {
				if (displayExceptions)
					System.out.printf(error, trial, e.getClass());
			}
		}

		String msg = String.format(line, 5, successfulTrials/(NUM_TRIALS*1.0)*100, NUM_TRIALS);
		System.out.println(msg);
		assertTrue(successfulTrials/(NUM_TRIALS*1.0) >= 0.7, msg);
	}
	
}
