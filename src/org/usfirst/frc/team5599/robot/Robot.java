package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CameraServerJNI;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Robot extends SampleRobot {
 	 	
	/*
        ========================================================================================================
                                Initialization
        ========================================================================================================
    */

	JoystickController operatorController;
	XBoxController driverController;
 
	Spark leftFrontWheel;
    Spark rightFrontWheel;
    Spark leftRearWheel;
    Spark rightRearWheel;
    
    SpeedControllerGroup driveTrainLeft;
    SpeedControllerGroup driveTrainRight;

    Spark intakeMotorLeft;
	Spark intakeMotorRight;

	//Spark climber;
	//Spark climberBase;
	Spark cubeArmBack;
	Spark cubeArmFront;
	Spark cubeArmBase;

	DoubleSolenoid intakeSolenoidLeft;	
	DoubleSolenoid intakeSolenoidRight;
	DoubleSolenoid shootingSolenoid;
	DoubleSolenoid openingSolenoid4;

	Compressor compressor;

	RobotDrive myRobot;
	
	PowerDistributionPanel powerDistributionPanel;
	ADXRS450_Gyro gyro;
	
	// Auto variables
	
	String startPosition = "";
	boolean cutAcrossAlliance = false;
	
	// Resized values from GRIP
	int IMG_WIDTH = 640;
	int IMG_HEIGHT = 480;
	

	AxisCamera camera;
	VisionThread visionThread;
	
	double centerX = 0.0;
	Object imgLock = new Object();
	
	// Constructor
	
	public Robot() {

		operatorController = new JoystickController(0);                                                                                                                  
		
		driverController = new XBoxController(1);

		rightFrontWheel = new Spark(2); //2 4

		rightRearWheel = new Spark(0); //7 1
		
		leftFrontWheel = new Spark(3); //3 
		
		leftRearWheel = new Spark(1); //5 2
		
		myRobot = new RobotDrive(leftFrontWheel, leftRearWheel, rightFrontWheel, rightRearWheel);
		
		intakeMotorLeft = new Spark(4);
		intakeMotorRight = new Spark(5);

		cubeArmBack = new Spark(6);
		cubeArmFront = new Spark(7);
		cubeArmBase = new Spark(8); //maybe 7and 6
		//cubeArmBaseRight = new Spark();
		
		//climber = new Spark();
		//climberForward = new Spark();
		//climberBase = new Spark(8);

/* 
		operatorController = new JoystickController(11);                                                                                                                  
		
		driverController = new XBoxController(10);

		rightFrontWheel = new Spark(2); //2 4

		rightRearWheel = new Spark(0); //7 1
*/		
		intakeSolenoidLeft = new DoubleSolenoid(0,5);
		intakeSolenoidRight = new DoubleSolenoid(4,3);
		shootingSolenoid = new DoubleSolenoid(1,2);
		openingSolenoid4 = new DoubleSolenoid(6,7);
		
		powerDistributionPanel = new PowerDistributionPanel();
		gyro = new ADXRS450_Gyro();
		
		powerDistributionPanel.clearStickyFaults();
		
		String[] autonomousModes = {"None", "Baseline", "Switch", "Scale"};
		SmartDashboard.putStringArray("autonomous/modes", autonomousModes);
	
	}
	
	public void robotInit() {

		camera = CameraServer.getInstance().addAxisCamera("10.55.99.11");
	    camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
	    
	    visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
	    	System.out.println("Found " + pipeline.findBlobsOutput().toArray().length + " blob(s).");
	        if (pipeline.findBlobsOutput().toArray().length > 0) {
	            Rect r = Imgproc.boundingRect(new MatOfPoint(pipeline.findBlobsOutput().toList().get(0).pt));
	            synchronized (imgLock) {
	                centerX = r.x + (r.width / 2);
	            }
	        }
	    });
	    visionThread.start();
	    
	}
	
	public void updateDashboard(){
		
		SmartDashboard.putNumber("drive/navx/yaw", gyro.getAngle());
		
	}

	 /*
        ========================================================================================================
                                Tele-Operated Code
        ========================================================================================================
   	 */
		
    public void operatorControl() {

    	while (isEnabled() && isOperatorControl()) {
    		
    		System.out.println("Teleop mode!");
			
	        double stickLeftY = driverController.getLeftThumbstickY();
	        double stickRightY = driverController.getRightThumbstickY();

	         myRobot.tankDrive(stickLeftY, stickRightY);
	        
	      /*
			
			// Drivetrain from townsend harris for high-grip turning
			
	        
	        if (Math.abs(stickLeftY) > 0.1){
		        leftFrontWheel.set(stickLeftY);
		        rightRearWheel.set(-stickLeftY);
	        }
	        
	        if (Math.abs(stickRightY) > 0.1){
	        leftRearWheel.set(stickRightY);
	        rightFrontWheel.set(-stickRightY);
	        }
	        
	       */
	         
	        controlIntakeMotors();
			controlLiftArm();
			// controlShootingPiston();
			// controlArmPistons();
			
							
			Timer.delay(0.01);
			
		}
	}

    
    public void controlIntakeMotors() {
    	
    	if (driverController.getLeftTrigger() == true) {
    		System.out.println("AAaaaaaa");
    		
			intakeMotorLeft.set(0.75);
			intakeMotorRight.set(-0.75);
		
		} else if (driverController.getRightTrigger() == true) {
			
			intakeMotorLeft.set(-0.75);
			intakeMotorRight.set(0.75);
			
		} else {
			
		    intakeMotorLeft.set(0.0);
			intakeMotorRight.set(0.0);
			
		}
		
    }
 /*   
    public void controlclimber() {
    	
    	if (operatorController.get() == true) {
    		
			climberBase.set(1.0);
			climber.set(1.0);
		
		} else if (operatorrController.get() == true) {
			
			climberBase.set(-1.0);
			climber.set(-1.0);
			
		} else {
			
		    climberBase.set(0.0);
			climber.set(0.0);
			
		}
		
    }
   */ 
    public void controlLiftArm() {   //tanoy said he want one elevator motor going one way and the other going another way, and change speed together -Jeff

    
    	
    	if (driverController.getLeftBumper() == true) {
    		System.out.println("BOIIIIIII");
    		
    		cubeArmBase.set(0.0);
    		cubeArmBack.set(0.7);
    		cubeArmFront.set(0.7);

    	} else if (driverController.getRightBumper() == true) {
    		
    		cubeArmBase.set(0.0);
    		cubeArmBack.set(-0.15);
    		cubeArmFront.set(-0.15);

    	} else {
    		
    		cubeArmBase.set(0.0);
    		cubeArmBack.set(0.0);
    		cubeArmFront.set(0.0);
    	}
    	
    }
    
    /*public void controlShootingPiston(){
    	
    	if (operatorController.getButton1() == true){
    		
    		shootingSolenoid.set(DoubleSolenoid.Value.kForward);
    		
    	} else if (operatorController.getButton2() == true){
    		
    		shootingSolenoid.set(DoubleSolenoid.Value.kReverse);
    		
    	}
    	
    	public void controlElevator() {
    		
    		if (operatorController.getDPadUp() == true) {
        		
        		cubeArmBase.set();
        		cubeArm.set();
        	} else if (operatorController.getDPadDown() == true) {
        		
        		cubeArmBase.set();
        		cubeArm.set();
        		
        	} else {
        		
        		cubeArmBase.set(0.0);
        		cubeArm.set(0.0);
        	}
    	}
    
    }
    
    public void controlArmPistons(){
    	if (operatorController.getButton9() == true){
    		
    		intakeSolenoidLeft.set(DoubleSolenoid.Value.kForward);
    		intakeSolenoidRight.set(DoubleSolenoid.Value.kForward);
    		
    	} else if (operatorController.getButton10() == true){
    		
    		intakeSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
    		intakeSolenoidRight.set(DoubleSolenoid.Value.kReverse);
    		
    	}


    }
    */
    
    public void test() {
    	
    	System.out.println("Beginning test code");
    	
    	while (isTest() && isEnabled()) {
    		
    		visionAlignWithCube();
    		
    		Timer.delay(0.05);
    	}
    }
    /*
    ========================================================================================================
                            Autonomous Code
    ========================================================================================================
    */
    
    
    public void autonomous() {
    	
		String selected = SmartDashboard.getString("autonomous/selected", "Error");
		
		System.out.println("Autonomous Mode Selected: " + selected);
		
		// Get these values from the network table (Placed there by the driver station)
		startPosition = SmartDashboard.getString("autonomous/StartPosition", "Left");
		cutAcrossAlliance = SmartDashboard.getBoolean("autonomous/CutAcrossAlliance", false);
		
		/*
		cutAcrossAlliance_Direction = SmartDashboard.getString("autonomous/CutAcrossAlliance_Direction", "None");
		placeCube = SmartDashboard.getBoolean("autonomous/PlaceCube", false);
		useVisionTracking = SmartDashboard.getBoolean("autonomous/UseVisionTracking", false);
		*/
		
		System.out.println("AutoMode. Starting on the " + startPosition + " doing " + selected + ". Field Cutting: " + cutAcrossAlliance);
		
		// System.out.println("Autonomous[StartPosition="+startPosition+", cutAcrossAlliance="+cutAcrossAlliance+"|"+cutAcrossAlliance_Direction+", placeCube=" + placeCube + ", useVisionTracking=" + useVisionTracking + "]");
		
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		char allianceSwitch = gameData.charAt(0);
		char centerScale = gameData.charAt(1);
		char opposingSwitch = gameData.charAt(2);
		
		System.out.println("Switch Status [AllianceSwitch="+allianceSwitch+", CenterScale=" + centerScale + ", opposingSwitch="+opposingSwitch+"]");
		
		
		if (selected == "None") {
			
			System.out.println("We chose to do nothing for this round");
			
		} else if (selected == "Baseline") {
			
			auto_driveForward(1.0, 3.0);
			
		} else if (selected == "Switch") {
			
			if (startPosition == "Left") {
				
				if (allianceSwitch == 'R') {
					
					if (cutAcrossAlliance == true) {
						
						
						System.out.println("Switch is on other side. Cutting across alliance Right -> Left");
						
						// Place the cube on the RIGHT switch starting from the LEFT side
						
						
					} else {
						
						System.out.println("Switch is on other side. Crossing baseline instead.");
						
						auto_driveForward(1.0, 3.0);
						
					}
					
				} else {
					
					System.out.println("Placing on switch. Left -> Left");
					
					// Place the cube on the LEFT Switch starting from the LEFT side
					
					auto_driveForward(1.0, 3.0);
					auto_turnRight(0.4, 2.0);
					auto_driveForward(0.4, 1.0);
					auto_raiseArm(0.7, 1.0);
					auto_deployCube();
					
				}
				
			} else if (startPosition == "Right") {
				
				if (allianceSwitch == 'L') {
					
					if (cutAcrossAlliance == true) {
						
						
						System.out.println("Switch is on other side. Cutting across alliance Left -> Right");
						
						// Place the cube on the LEFT switch starting from the RIGHT side
						
					} else {
						
						System.out.println("Switch is on other side. Crossing baseline instead.");
						auto_driveForward(1.0, 3.0);
						
					}
					
				} else {
					
					System.out.println("Placing on switch. Right -> Right");
					
					// Place the cube on the RIGHT Switch starting from the RIGHT side
					
					auto_driveForward(1.0, 3.0);
					auto_turnLeft(0.4, 2.0);
					auto_driveForward(0.4, 1.0);
					auto_raiseArm(0.7, 1.0);
					auto_deployCube();
				}
				
			} else if (startPosition == "Center") {
				
				if (allianceSwitch == 'R') {
					
					System.out.println("Placing on switch. Center -> Right");
					
					// Place the cube on the RIGHT Switch starting from the CENTER
					
					auto_driveForward(0.4, 2.0);
					auto_turnRight(0.4, 1.0);
					auto_driveForward(0.4, 1.0);
					auto_raiseArm(0.7, 1.0);
					auto_deployCube();
					
				} else if (allianceSwitch == 'L') {
					
					System.out.println("Placing on switch. Center -> Left");
					
					// Place the cube on the LEFT Switch starting from the CENTER
					
					auto_driveForward(0.4, 2.0);
					auto_turnLeft(0.4, 1.0);
					auto_driveForward(0.4, 1.0);
					auto_raiseArm(0.7, 1.0);
					auto_deployCube();
					
				}
				
			} else {
				System.out.println("Error. No Invalid Start Position: " + startPosition);
			}
			
		} else if (selected == "Scale") {
			
			if (startPosition == "Left") {
				
				if (centerScale == 'R') {
					
					if (cutAcrossAlliance == true) {
						
						
						System.out.println("Scale is on other side. Cutting across field Right -> Left");
						
						// Place the cube on the RIGHT scale starting from the LEFT side
						
					} else {
						
						System.out.println("Scale is on other side. Crossing baseline instead.");
						auto_driveForward(1.0, 3.0);
						
					}
					
				} else {
					
					System.out.println("Placing on scale. Left -> Left");
					
					// Place the cube on the LEFT Scale starting from the LEFT side
					
					auto_driveForward(1.0, 4.0);
					auto_turnRight(0.4, 1.0);
					auto_driveForward(0.4, 1.0);
					auto_raiseArm(0.7, 3.0);
					auto_deployCube();
				}
				
			} else if (startPosition == "Right") {
				
				if (centerScale == 'L') {
					
					if (cutAcrossAlliance == true) {
						
						
						System.out.println("Scale is on other side. Cutting across field Left -> Right");
						
						// Place the cube on the LEFT Scale starting from the RIGHT side
						
					} else {
						
						System.out.println("Scale is on other side. Crossing baseline instead.");
						auto_driveForward(1.0, 3.0);
						
					}
					
				} else {
					
					System.out.println("Placing on scale. Right -> Right");
					
					// Place the cube on the RIGHT Scale starting from the RIGHT side
					
					auto_driveForward(1.0, 4.0);
					auto_turnLeft(0.4, 1.0);
					auto_driveForward(0.4, 1.0);
					auto_raiseArm(0.7, 3.0);
					auto_deployCube();
				}
				
			} else if (startPosition == "Center") {
				
				if (cutAcrossAlliance == true) {
					
					if (allianceSwitch == 'R') {
						
						System.out.println("Placing on scale. Center -> Right");
						
						// Place the cube on the RIGHT Scale starting from the CENTER
						
					} else if (allianceSwitch == 'L') {
						
						System.out.println("Placing on scale. Center -> Left");
						
						// Place the cube on the LEFT Scale starting from the CENTER
						
					}
					
				} else {
					
					System.out.println("Cannot place on scale, we're stuck in the center.");
				}
				
			} else {
				
				System.out.println("Error. No Invalid Start Position: " + startPosition);
			}
				
		} else {
			System.out.println("Invalid Autonomous Mode: No code designated for '" + selected + "'");
		}
		
    }
    
    
    // Driving condensed into smaller chunks
    
    public void auto_driveForward(double speed, double time) {
    	
    	myRobot.tankDrive(speed, speed);
		
		Timer.delay(time);
		
		myRobot.tankDrive(0.0, 0.0);
    	
    }
    
    public void auto_raiseArm(double speed, double time) {
    	
    	cubeArmBase.set(0.0);
		cubeArmBack.set(speed);
		cubeArmFront.set(speed);
		
		Timer.delay(time);
		
		cubeArmBase.set(0.0);
		cubeArmBack.set(0.0);
		cubeArmFront.set(0.0);
    	
    }
    
    public void auto_driveForwardWhileRaisingArm(double speed, double waitTime, double armSpeed, double armRaiseTime) {
    	
    	myRobot.tankDrive(speed, speed);
    	
    	Timer.delay(waitTime);
    	
    	cubeArmBase.set(0.0);
		cubeArmBack.set(armSpeed);
		cubeArmFront.set(armSpeed);
		
		Timer.delay(armRaiseTime);
		
		myRobot.tankDrive(0.0, 0.0);
		cubeArmBase.set(0.0);
		cubeArmBack.set(0.0);
		cubeArmFront.set(0.0);
    		
    }
    
    public void auto_deployCube() {
    	
    	// Code that makes the arm release the cube
    	
    }
    
    public void auto_runIntakeArms(double speed) {
    	
    	intakeMotorLeft.set(-speed);
		intakeMotorRight.set(speed);
		
    }
    
    public void auto_turnLeft(double speed, double time) {

    	myRobot.tankDrive(-speed, speed);
    	
    	Timer.delay(time);
    	
    	myRobot.tankDrive(0.0, 0.0);
    	
    }
    
    public void auto_turnRight(double speed, double time) {

    	myRobot.tankDrive(speed, -speed);
    	
    	Timer.delay(time);
    	
    	myRobot.tankDrive(0.0, 0.0);
    	
    }
    
    public void auto_stop() {
    	
    	myRobot.tankDrive(0.0, 0.0);
    	
    	cubeArmBase.set(0.0);
		cubeArmBack.set(0.0);
		cubeArmFront.set(0.0);
		
		intakeMotorLeft.set(0.0);
		intakeMotorRight.set(0.0);
    	
    }
    
    public void visionAlignWithCube() {
    	
    	for (int i = 0; i < 300; i++) {
    		double centerX;

	        synchronized (imgLock) {
	            centerX = this.centerX;
	        }
	        
	        double turn = centerX - (IMG_WIDTH / 2);
	        myRobot.arcadeDrive(-0.6, turn * 0.005);
	        
	        Timer.delay(0.05);
	        
    	}
        
    }

	  //////////////////////////////////////////////////////////////////////////////////
     // Disabling Code ////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

	public void disabled(){

		System.out.println("Disabling robot . . .");

		myRobot.tankDrive(0.0, 0.0);

		leftFrontWheel.set(0.0);
		rightFrontWheel.set(0.0);
		leftRearWheel.set(0.0);
		rightRearWheel.set(0.0);  
		intakeMotorLeft.set(0.0);
		intakeMotorRight.set(0.0);
		cubeArmBase.set(0.0);
		cubeArmFront.set(0.0);
		cubeArmBack.set(0.0);
		compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}






