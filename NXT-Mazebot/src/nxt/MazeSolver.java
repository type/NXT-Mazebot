package nxt;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassSensor;
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
	* Our compass sensor
	*/
	private CompassSensor myCompass;

	/**
	* 12 Inches, what we think is the length of one tile
	*/
	private static final double travelDist = 01.50f;
	private static final double travelDist2 = 1.00f;
	/**
	* The wheel diameter
	*/
	private static final double wheelDiam = 2.5f;
	/**
	* The space between wheels
	*/
	private static final double axleLength = 9.6f;
	/**
	* Light Threshold
	*/
	private static final int lightThreshold = 40;
	/**
	* End of maze threshold
	*/
	private static final int endThreshold = 52;
	/**
	* Speed to rotate
	*/
	private static final double rotateSpeed = 30.0f;

	/**
	* Default constructor, initializes the motors and sensors
	*/
	public MazeSolver() {
		// Set up motors and sensors
		myPilot = new DifferentialPilot(wheelDiam, axleLength, Motor.B,
				Motor.A, true);
		myFrontSensor = new LightSensor(SensorPort.S1);
		myRightSensor = new LightSensor(SensorPort.S2);
		myCompass = new CompassSensor(SensorPort.S3);

		// Set rotate speed
		myPilot.setRotateSpeed(rotateSpeed);
		myPilot.setAcceleration(70);

		// Calibrate
		doCalibration();
	}

	/**
	* Program Entry Point
	* 
	* @param args
	*            Command line arguments
	*/
	public static void main(String[] args) {
		// Create and start the solver
		MazeSolver mySolver = new MazeSolver();
		mySolver.solve();
		// mySolver.testLights();
	}

	/**
	* Runs the maze solving algorithm
	*/
	public void solve() {

		// While we haven't solved the maze
		while (!atTarget()) {

			// Check for a change in the light value on the right
			if (rightIsClear()) {
				// Turn right
				goForward(1.2, travelDist);
				turnRight();

				// Check for result
				if (atTarget()) {
					break;
				}

				// Move forward since the front will be clear if the right was
				// clear
				goForward(1, travelDist2);

			} else {
				// Check if front is clear
				if (frontIsClear()) {
					goForward(1, travelDist2);
				} else {
					// If front and right are blocked, turn left (keep hand
					// against wall)
					turnLeft();
				}
			}

		}

		// We are done
		mazeSolved();
	}

	/**
	* Calibration utility method, not sure if we need it
	*/
	private void doCalibration() {
		// Prep sensor
		System.out.println("Port 1: Front Sensor");
		System.out.println("Port 2: Right Sensor");
		System.out.println("Port 3: Compass\n");

		// Calibrate front
		// System.out.println("Calibrate High Front");
		// Button.waitForPress();
		// myFrontSensor.calibrateHigh();
		System.out.print("Value: ");
		System.out.println(myFrontSensor.getLightValue());

		// Calibrate right
		// System.out.println("Calibrate High Right");
		// Button.waitForPress();
		// myRightSensor.calibrateHigh();
		System.out.print("Value: ");
		System.out.println(myRightSensor.getLightValue());
		Button.waitForPress();
	}

	/**
	* Right turn method Uses compass
	*/
	private void turnRight() {
		System.out.println("R");
		/*
		* // Stop momentarily myPilot.stop();
		* 
		* // Get current bearing float x = myCompass.getDegrees(); float y = (x
		* - 90f) % 360;
		* 
		* // Get us within a threshold of the degree that we want while (x < y
		* - 3) { myPilot.rotate(-5); x = myCompass.getDegrees();
		* System.out.println("Bearing: " + x); }
		*/

		// Turn Right
		// goForward(4);
		// goBack();
		myPilot.rotate(-76);
		goForward(6.5, travelDist);
	}

	/**
	* Turns the robot left using compass
	*/
	private void turnLeft() {
		System.out.println("L");
		/*
		* // Stop momentarily myPilot.stop();
		* 
		* // Get current bearing float x = myCompass.getDegrees(); float y = (x
		* + 90f) % 360;
		* 
		* // Get us within a threshold of the degree that we want while (x < y
		* - 3) { myPilot.rotate(-5); x = myCompass.getDegrees();
		* System.out.println("Bearing: " + x); }
		*/

		// Turn Left
		goBack();
		// goBack();
		myPilot.rotate(76);
		goForward(2, travelDist);
	}

	/**
	* Go forward method
	*/
	private void goForward(double numTimes, double dist) {
		System.out.println("F");
		// Move some more, and return right away
		myPilot.travel(dist * numTimes);
	}

	private void goBack() {
		System.out.println("B");
		// Move some more, and return right away
		myPilot.travel(travelDist * -2.1);
	}

	/**
	* Right is clear method
	* 
	* @return True if right is clear
	*/
	private boolean rightIsClear() {
		//
		// System.out.print("Right Light Value: ");
		System.out.print("R: ");
		System.out.println(myRightSensor.getLightValue());
		return myRightSensor.getLightValue() > lightThreshold;
	}

	/**
	* Front is clear method
	* 
	* @return True if front is clear
	*/
	private boolean frontIsClear() {
		//
		// System.out.print("Front Light Value: ");
		System.out.print("F: ");
		System.out.println(myFrontSensor.getLightValue());
		return myFrontSensor.getLightValue() > lightThreshold;
	}

	/**
	* Checks for a bright object in front to indicate that we are at the target
	* 
	* @return True if we are at target
	*/
	private boolean atTarget() {
		return myFrontSensor.getLightValue() > endThreshold;
	}

	/**
	* Maze solved method
	*/
	private void mazeSolved() {
		// We should have completed by now
		System.out.println("Done");
		MyTune();
		Button.waitForPress();
		System.out.println("X");
		// Play Victorious Sound!
	}

	public void testLights() {
		for (int i = 0; i < 10; i++) {
			// goForward();
			rightIsClear();
			frontIsClear();
			Button.waitForPress();
		}
	}

	public void MyTune() {

		// NOTE: This tune was generated from a midi using Guy
		// Truffelli's Brick Music Studio www.aga.it/~guy/lego
		short[] note = { 2349, 115, 0, 5, 1760, 165, 0, 35, 1760, 28, 0, 13,
				1976, 23, 0, 18, 1760, 18, 0, 23, 1568, 15, 0, 25, 1480, 103,
				0, 18, 1175, 180, 0, 20, 1760, 18, 0, 23, 1976, 20, 0, 20,
				1760, 15, 0, 25, 1568, 15, 0, 25, 2217, 98, 0, 23, 1760, 88, 0,
				33, 1760, 75, 0, 5, 1760, 20, 0, 20, 1760, 20, 0, 20, 1976, 18,
				0, 23, 1760, 18, 0, 23, 2217, 225, 0, 15, 2217, 218 };
		for (int i = 0; i < note.length; i += 2) {
			final short w = note[i + 1];
			final int n = note[i];
			if (n != 0)
				Sound.playTone(n, w * 10);
			try {
				Thread.sleep(w * 2);
			} catch (InterruptedException e) {
			}
		}

	}
}

