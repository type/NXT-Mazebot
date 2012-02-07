package nxt;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Maze Solver
 * 
 * 
 */
public class MazeSolver {

	/**
	 * This is responsible for moving the robot
	 */
	private DifferentialPilot myPilot;
	/**
	 * Our light sensors
	 */
	private LightSensor myFrontSensor, myRightSensor;
	/**
	 * 12 Inches, what we think is the length of one tile
	 */
	private static final double travelDist = 30.48f;

	/**
	 * Default constructor, initializes the motors and sensors
	 */
	public MazeSolver() {
		// Set up motors and sensors
		myPilot = new DifferentialPilot(2.5f, -5.5f, Motor.B, Motor.A);
		myFrontSensor = new LightSensor(SensorPort.S1);
		myRightSensor = new LightSensor(SensorPort.S2);

		// Calibrate?
		doCalibration();
	}

	/**
	 * Runs the maze solving algorithm
	 */
	public void solve() {

		// Start moving
		myPilot.travel(travelDist, true);

		// While we are moving
		while (myPilot.isMoving()) {
			// Check for a change in the light value on the right
			if (myRightSensor.getLightValue() > 40) {
				// Stop momentarily
				myPilot.stop();

				// Determine what to do
				// myPilot.rotateRight();
				myPilot.rotate(90);
			} else if (myFrontSensor.getLightValue() > 40) {
				// If we can't go forward
				// Stop momentarily
				myPilot.stop();

				// Determine what to do
				// myPilot.rotateRight();
				myPilot.rotate(90);
			}

			// Move some more, and return right away
			myPilot.travel(travelDist, true);
		}

		// We should have completed by now
		System.out.println("Done");
		Button.waitForPress();

	}

	public static void main(String[] args) {
		// Create and start the solver
		MazeSolver mySolver = new MazeSolver();
		mySolver.solve();
	}

	/**
	 * Calibration utility method, not sure if we need it
	 */
	private void doCalibration() {
		// Prep sensor
		myFrontSensor.setFloodlight(true);
		myRightSensor.setFloodlight(true);
		myFrontSensor.calibrateHigh();
		myRightSensor.calibrateHigh();
		myFrontSensor.calibrateLow();
		myRightSensor.calibrateLow();
	}

}
