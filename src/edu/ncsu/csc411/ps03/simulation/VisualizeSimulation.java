package edu.ncsu.csc411.ps03.simulation;

import edu.ncsu.csc411.ps03.utils.ConfigurationLoader;
import edu.ncsu.csc411.ps03.utils.InputManager;
import edu.ncsu.csc411.ps03.environment.Environment;
import edu.ncsu.csc411.ps03.utils.ColorPalette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * A Visual Guide toward testing whether your robot
 * agent is operating correctly. This visualization
 * will run for 1000 time steps. Unlike PS01 and PS02,
 * this simulation will run through all 1000 iterations
 * before quitting.
 * You are free to modify this file for testing.
 * @author Adam Gaweda
 */
public class VisualizeSimulation extends JFrame {
	private static final long serialVersionUID = 1L;
	private EnvironmentPanel envPanel;
	private ScorePanel scorePanel;
	private Environment env;
	private String mapFile = "inputs/public/input02.txt";
	private String configFile = "config/configNormal.txt";
	
	/* Builds the environment; while not necessary for this problem set,
	 * this could be modified to allow for different types of environments.
	 * 
	 * The map variable allows you to customize the environment to any configuration.
	 * Each row in the array represents a value associated to a given task in the 
	 * environment, and each column represents a value associated to a given worker.
	 */
	public VisualizeSimulation() {
		// Currently uses the first public test case
		Properties properties = ConfigurationLoader.loadConfiguration(configFile);
		int ITERATIONS = Integer.parseInt(properties.getProperty("ITERATIONS", "200"));
		int TILESIZE = Integer.parseInt(properties.getProperty("TILESIZE", "50"));
		int DELAY = Integer.parseInt(properties.getProperty("DELAY", "200"));
		boolean DEBUG = Boolean.parseBoolean(properties.getProperty("DEBUG", "true"));
		
		// Currently loads the first public test case, but you can change the map file
		// or make your own!
		int[][] map = InputManager.loadMap(mapFile);
		
		this.env = new Environment(map);
		envPanel = new EnvironmentPanel(this.env, ITERATIONS, TILESIZE, DELAY, DEBUG);
		scorePanel = new ScorePanel();
		this.add(envPanel, BorderLayout.CENTER);	
		this.add(scorePanel, BorderLayout.SOUTH);
		setResizable(false);
	}
	
	public static void main(String[] args) {
	    JFrame frame = new VisualizeSimulation();

	    frame.setTitle("CSC 411 - Problem Set 03");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
    }
	
	// Inner Class Environment Panel
	@SuppressWarnings("serial")
	class EnvironmentPanel extends JPanel{
		private Environment env;
		private Color[] workerColors;
		private Timer timer;
		private int ITERATIONS;
		public int TILESIZE;
		public int DELAY; // milliseconds
		public boolean DEBUG;
		
		// Designs a GUI Panel based on the dimensions of the Environment and implements 
		// a Timer object to run the simulation. This timer will iterate through time-steps
		// with a 100ms delay (or wait 100ms before updating again).
		public EnvironmentPanel(Environment env, int iterations, int tilesize, int delay, boolean debug) {
			this.env = env;
			ITERATIONS = iterations;
			TILESIZE = tilesize;
			DELAY = delay;
			DEBUG = debug;
			// Height needs a 5% increase otherwise the last task gets cut off
		    setPreferredSize(new Dimension( (env.getNumWorkers()+1) * ((int)(TILESIZE * 1)),
		    								(env.getNumTasks()+1) * ((int)(TILESIZE * 1.05)))
		    				);
			// Only using 15 colors, visualization will recycle colors for larger data sets
			this.workerColors = new Color[]{ColorPalette.RED, ColorPalette.ORANGE, ColorPalette.GREEN, 
											ColorPalette.BLUE, ColorPalette.INDIGO, ColorPalette.YELLOW, 
											ColorPalette.BROWN, ColorPalette.WHITE, ColorPalette.CONCRETE, 
											ColorPalette.LIGHTRED, ColorPalette.LIGHTORANGE, ColorPalette.LIGHTYELLOW, 
											ColorPalette.LIGHTGREEN, ColorPalette.LIGHTBLUE, ColorPalette.LIGHTINDIGO
											};
			
			this.timer = new Timer(DELAY, new ActionListener() {
				int timeStepCount = 0;
				public void actionPerformed(ActionEvent e) {
					// Update the Environment
					try {
						// Wrapped in try/catch in case the Robot's decision results
						// in a crash; we'll treat that the same as Action.DO_NOTHING
						env.updateConfiguration();
					} catch (Exception ex) {
						if (DEBUG) {
							String error = "[ERROR AGENT CRASH AT TIME STEP %03d] %s\n";
							System.out.printf(error, timeStepCount, ex);
						}
					}
					scorePanel.updateScore();
					// Redraw the Visualization
					repaint();
					// Increment how many steps have been taken
					timeStepCount++;
					
					// 2) The simulation has iterated through the passed number of iterations
					if (timeStepCount == ITERATIONS) {
						String line = "Configuration after %4d iterations:";
						String line2 = "Best Configuration after %4d iterations:";
						String msg = String.format(line, ITERATIONS);
						String msg2 = String.format(line2, ITERATIONS);
						String configuration = Arrays.toString(env.getCurrentConfiguration());
						String best = Arrays.toString(env.getBestConfiguration());
						System.out.println(msg);
						System.out.println(configuration);
						System.out.println("Score: " + env.calcScore(env.getCurrentConfiguration()));
						System.out.println(msg2);
						System.out.println(best);
						System.out.println("Score: " + env.calcScore(env.getBestConfiguration()));
						env.writeConfigurationsToFile();
						timer.stop();
					}
				}				
			});
			this.timer.start();
		}
		
		/*
		 * The paintComponent method draws all of the objects onto the
		 * panel. This is updated at each time step when we call repaint().
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			// Create Background
			for (int task = 0; task < this.env.getNumTasks()+1; task++) {
				for (int worker = 0; worker < this.env.getNumWorkers()+1; worker++) {
					// Get its respective tile and set the color for the 
					// corresponding status
					g.setColor(ColorPalette.SILVER);
					// Then draw that square, not the row and col are flipped
					// so that the visualization matches the String maps
			        g.fillRect( worker * TILESIZE, 
		                        task * TILESIZE,
		                        TILESIZE, TILESIZE);
			        
			        // Draw an outline to the tile
			        g.setColor(ColorPalette.BLACK);
		            g.drawRect( worker * TILESIZE, 
		            			task * TILESIZE,
		                        TILESIZE, TILESIZE);
				}
		    }
			
			// Paint the Workers
			char face = '\u263A';
			for (int worker = 0; worker < this.env.getNumWorkers(); worker++) {
				// Get its respective tile and set the color for the 
				// corresponding status
				Color selectedColor = this.workerColors[worker % this.workerColors.length];
				g.setColor(selectedColor);
				g.setFont(new Font("Arial", Font.PLAIN, (int)(.75*TILESIZE)));
				// Then draw that square, not the row and col are flipped
				// so that the visualization matches the String maps
				int x = (worker+1) * TILESIZE+TILESIZE/8;
				int y = (int) (TILESIZE/1.5);
		        g.drawString(""+face, x, y );
			}
			
			// Paint the Tasks
			// Using Fruit Emojis for visualization sake, but could be attributed to any task
			// Similar to colors, capped at 15, then visualization will recycle task icons
			String[] tasks = {
					"\ud83c\udf47", // grapes
					"\ud83c\udf49", // watermelon
					"\ud83c\udf4a", // orange
					"\ud83c\udf4c", // banana
					"\ud83c\udf4d", // pineapple
					"\ud83c\udf4e", // apple
					"\ud83c\udf50", // pear
					"\ud83c\udf51", // peach
					"\ud83c\udf52", // cherries
					"\ud83c\udf53", // strawberry
					"\ud83c\udf45", // tomato
					"\ud83e\udd65", // coconut
					"\ud83e\udd51", // avocado
					"\ud83e\udd55", // carrot
					"\ud83e\uddc4", // garlic
			        };
			for (int task = 0; task < this.env.getNumTasks(); task++) {
				// Get its respective icon
				String taskEmoji = tasks[task % tasks.length];
				g.setColor(ColorPalette.BLACK);
				g.setFont(getFont(taskEmoji));
				// Then draw that square, not the row and col are flipped
				// so that the visualization matches the String maps
				int x = TILESIZE/8;
				int y = (int) (((task+1) * TILESIZE) + (TILESIZE* .75));
				g.drawString(""+taskEmoji, x, y );
			}
			
			// Paint the Configurations
			int[] configuration = this.env.getCurrentConfiguration();
			for(int i = 0; i < configuration.length; i++) {
				Color selectedColor = this.workerColors[i % this.workerColors.length];
				Color box = new Color(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue(), 128);
				g.setColor(box);
				g.fillRect( ((i+1) * TILESIZE)+TILESIZE/5, 
			    			((configuration[i]+1) * TILESIZE)+TILESIZE/5,
				            (int)((TILESIZE/5)*3.3), (int)((TILESIZE/5)*3));
				
				// Draw an outline to the configuration
		        g.setColor(selectedColor);
	            g.drawRect( ((i+1) * TILESIZE)+TILESIZE/5, 
			    			((configuration[i]+1) * TILESIZE)+TILESIZE/5,
				            (int)((TILESIZE/5)*3.3), (int)((TILESIZE/5)*3));
			}
			
			// Add Values to Tiles
			int[][] values = this.env.getValues();
			for (int task = 0; task < this.env.getNumTasks(); task++) {
				for (int worker = 0; worker < this.env.getNumWorkers(); worker++) {
					// Get its respective tile and set the color for the 
					// corresponding status
					g.setColor(ColorPalette.BLACK);
					g.setFont(new Font("Arial", Font.PLAIN, (int)(.5*TILESIZE)));
					// Then draw that square, not the row and col are flipped
					// so that the visualization matches the String maps
					int x = (worker+1) * TILESIZE+TILESIZE/4;
					int y = (int) ((task+1) * TILESIZE+TILESIZE/1.5);
			        g.drawString(""+values[task][worker], x, y );
				}
		    }
		}
		
		// Method from StackOverflow to obtain the correct Emoji Font Family for an OS
		// https://stackoverflow.com/q/64733229/1558159
		// NOTE: Have not tested the GUI on a Mac, YMMV
		public Font getFont(String emoji) {
		    String os = System.getProperty("os.name");
		    String fontFamily = os.equals("Mac OS X") ? "Apple Color Emoji" : "Segoe UI Emoji";
		    Font font = new Font(fontFamily, Font.PLAIN, (int)(.6*TILESIZE));
		    return font;
		  }
	}
	
	// Inner Class Score Panel
	@SuppressWarnings("serial")
	class ScorePanel extends JPanel{
		private JLabel currentLabel;
		private JLabel bestLabel;
		// A small, simple JLabel to will output the currently highlighted configuration's score
		public ScorePanel() {
			setLayout(new FlowLayout());
			this.currentLabel = new JLabel();
			add(currentLabel);
			JLabel bufferLabel = new JLabel("------");
			add(bufferLabel);
			this.bestLabel = new JLabel();
			add(bestLabel);
		}
		
		public void updateScore() {
			repaint();
		}
		
		/*
		 * The paintComponent method draws all of the objects onto the
		 * panel. This is updated at each time step when we call repaint().
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Update the current and best score to the Environment's current configuration
			String fontFamily = "monospaced";
		    Font font = new Font(fontFamily, Font.PLAIN, 12);
			int currentScore = env.calcScore(env.getCurrentConfiguration());
			int bestScore = env.calcScore(env.getBestConfiguration());
			String currentLabel = String.format("Current Score: %4d", currentScore);
			String bestLabel = String.format("Best Score: %4d", bestScore);
			this.currentLabel.setText(currentLabel);
			this.currentLabel.setFont(font);
			this.bestLabel.setText(bestLabel);
			this.bestLabel.setFont(font);
		}
	}
}