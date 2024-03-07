package edu.ncsu.csc411.ps03.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.ncsu.csc411.ps03.environment.Environment;

/**
	Represents a linear assignment problem where N workers must be assigned
	to N tasks. Each worker/task combination is further associated with some
	value. The goal of this task is the produce an optimal configuration that
	maximizes (or minimizes) the sum of the assigned worker/task values.
*/

public class ConfigurationSolver {
	private Environment env;
	private int[] configuration;
	private int[] bestConfiguration;
	
	/** Initializes a Configuration Solver for a specific environment. */
	public ConfigurationSolver (Environment env) { 
		this.env = env;
		this.configuration = new int[this.env.getNumWorkers()];
		// Initializing by assigning work to an arbitrary task
		for(int i = 0; i < this.configuration.length; i++) {
			this.configuration[i] = i;
		}
		// Need to clone because of how Java handles assigning arrays
		// as values (relative referencing). Recall this is similar to
		// that one lecture in CSC 116 where if we don't duplicate the second
		// array, it will constantly change as this.configuration changes.
		// Cloning the array resolves this issue.
		this.bestConfiguration = this.configuration.clone();
	}
	
	/**
    	Problem Set 03 - For this Problem Set, you will be exploring search
    	methods for optimal configurations. In this exercise, you are given
    	a linear assignment problem, where you must determine the appropriate
    	configuration assignments for persons to tasks. Specifically, you are
    	seeking to MAXIMIZE the fitness score of this configuration. While brute
    	forcing your search will provide you with the optimal solution, you run
    	into the issue that there are N! possible permutations, which can increase
    	your search space as N increases. Instead, utilize one of the search methods
    	presented in class to tackle this problem.
    	
    	For the updateSearch(), design an algorithm that will iterate through an
    	iterative optimization algorithm, updating the current configuration as it
    	traverses its search space. The updateSearch() method should also return this
    	configuration back to the environment. For example, in an N=5 problem space, 
    	updateSearch() may return {4,1,2,3,0}, where Worker #4 is assigned Task #3.
    	
    	NOTE - simply doing random search, while it "will" work, is not permitted. If we
    	see you implementation is just random search, you will receive a -40% penalty to
    	your submission. Instead, think about the meta-heuristics presented in class, which
    	use "controlled" randomization.
	 */

	/**
		For this implementation, I use simulated annealing to traverse the search space.
	 	The algorithm starts with a high temperature and gradually decreases it until it reaches a low temperature.
	 	In each iteration, it randomly selects two workers and swaps their assignments in the candidateConfiguration array.
	 	It then calculates the scores of the current and candidate configurations using the calcScore() method.
	 	If the candidate score is higher than the current score, the candidate configuration becomes the new current configuration.
	 	Otherwise, it accepts the candidate configuration with a certain probability based on the difference in scores and the current temperature.
	 */
	public int[] updateSearch () {
		double temperature = 1000;
		double coolingRate = 0.95;

		int[] currentConfiguration = this.configuration.clone();
		int[] candidateConfiguration = this.configuration.clone();

		while (temperature > 1.0) {
			int worker1 = randomizeWorker();
			int worker2 = randomizeWorker();
			swap(candidateConfiguration, worker1, worker2);
			int currentScore = env.calcScore(currentConfiguration);
			int candidateScore = env.calcScore(candidateConfiguration);
			if (candidateScore > currentScore) {
				currentConfiguration = candidateConfiguration.clone();
			} else {
				double acceptanceProbability = Math.exp((candidateScore - currentScore) / temperature);
				if (Math.random() < acceptanceProbability) {
					currentConfiguration = candidateConfiguration.clone();
				}
			}
			if (currentScore > env.calcScore(bestConfiguration)) {
				bestConfiguration = currentConfiguration.clone();
			}
			temperature *= coolingRate;
		}
		return currentConfiguration;
	}

	private int randomizeWorker() {
		return (int) (Math.random() * this.configuration.length);
	}

	private void swap(int[] configuration, int worker1, int worker2) {
		int temp = configuration[worker1];
		configuration[worker1] = configuration[worker2];
		configuration[worker2] = temp;
	}


	/**
	 * In addition to updateSearch, you should also track you BEST OBSERVED CONFIGURATION.
	 * While your search may move to worse configurations (since that's what the algorithm
	 * needs to do), getBestConfiguration will return the best observed configuration by your
	 * agent.
	 * You do not need to change this method. Update bestConfiguration in updateSearch.
	*/
	public int[] getBestConfiguration() {
		return this.bestConfiguration;
	}
	
}