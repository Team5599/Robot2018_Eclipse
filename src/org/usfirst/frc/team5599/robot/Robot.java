package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;
import edu.wpi.first.wpilibj.DriverStation;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

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
	Spark intakeRotator;

	DigitalInput limitSwitch;
	//DigitalInput otherSwitch;

	Compressor compressor;

	RobotDrive myRobot;
	
	int IMG_WIDTH = 640;
	int IMG_HEIGHT = 480;
	

	AxisCamera camera;
	VisionThread visionThread;
	
	double centerX = 0.0;
	Object imgLock = new Object();
	
	PowerDistributionPanel powerDistributionPanel;
	ADXRS450_Gyro gyro;
	
	public Robot() {

		operatorController = new JoystickController(0);                                                                                                                  
		
		driverController = new XBoxController(1);

		rightFrontWheel = new Spark(2); //2 4

		rightRearWheel = new Spark(0); //7 1
		
		leftFrontWheel = new Spark(3); //3 
		
		leftRearWheel = new Spark(1); //5 2
		
		/*driveTrainLeft = new SpeedControllerGroup(leftFrontWheel, leftRearWheel);
		driveTrainRight = new SpeedControllerGroup(rightFrontWheel, leftFrontWheel);*/
		
		// myRobot = new RobotDrive(driveTrainLeft, driveTrainRight);
		myRobot = new RobotDrive(leftFrontWheel, leftRearWheel, rightFrontWheel, rightRearWheel);
		
		intakeMotorLeft = new Spark(4);
		intakeMotorRight = new Spark(5);

		cubeArmBack = new Spark(6);
		cubeArmFront = new Spark(7);
		cubeArmBase = new Spark(8);
		intakeRotator = new Spark(9);//maybe 7and 6
		
		limitSwitch = new DigitalInput(0);
		//otherSwitch = new DigitalInput(1);
		
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
		
		powerDistributionPanel = new PowerDistributionPanel();
		gyro = new ADXRS450_Gyro();
		
		powerDistributionPanel.clearStickyFaults();
		
		String[] autonomousModes = {"None", "Baseline", "Left", "Center", "Right"};
		SmartDashboard.putStringArray("autonomous/modes", autonomousModes);
	}
	
	public void robotInit() {
		
		SmartDashboard.putNumber("automode", 0.0);
		SmartDashboard.putNumber("RobotStartPosition", 0.0);
		SmartDashboard.putString("selectedAuto", "Not Set");
		
		// camera = CameraServer.getInstance().addAxisCamera("10.55.99.11");
		
		/*
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
	    
	    */
	    
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

	        controlIntakeMotors();
			controlLiftArm();
			controlIntakeRotator();
			controlArm();
		
			System.out.println("Limit switch" + limitSwitch.get());
							
			Timer.delay(0.01);
			
		}
	}

    
    public void controlIntakeMotors() {
    	
    	if (operatorController.getButtonSeven() || driverController.getLeftTrigger() == true) {
    		System.out.println("AAaaaaaa");
    		
			intakeMotorLeft.set(0.75);
			intakeMotorRight.set(-0.75);
		
		} else if (operatorController.getButtonEight() || driverController.getRightTrigger() == true) {
			
			intakeMotorLeft.set(-0.9);
			intakeMotorRight.set(0.9);
			
		} else {
			
		    intakeMotorLeft.set(0.0);
			intakeMotorRight.set(0.0);
			
		}
		
    }
    
 public void controlIntakeRotator() {
    	
    	if (operatorController.getButtonNine() == true) { 
    		System.out.println("Damn");
    		
			intakeRotator.set(0.8);
		
		} else if (operatorController.getButtonTen() == true) {
			
			intakeRotator.set(-0.7);
			
		} else {
			
		    intakeRotator.set(0.0);
			
		}
		
    }
 /*   
    public void controlclimber() {
    	
    	if (operatorController.get() == true) {
    		
			climberBase.set();
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
    public void controlLiftArm() {  

    
    	
    	if (operatorController.getButtonEleven() == true) {
    		System.out.println("BOIIIIIII");
    		
    		cubeArmBase.set(0.6);
    		/*if (limitSwitch.get() == true) {
    			System.out.println("Cube Arm Base is stalling");
    			cubeArmBase.set(0.3);
    		}
    		else {
    			System.out.println("Cube Arm Base is going up");
    			cubeArmBase.set(0.7);
    		}*/
    	} else if (operatorController.getButtonTwelve() == true) {
    		
    		cubeArmBase.set(-0.5);

    	} else {
    		
    		cubeArmBase.set(0.0);
    	}
    	
    }
    
    
    public void controlArm() {   

    
    	if (operatorController.getJoystickY() >= 0.1) {
    		System.out.println("Yikes!!!!");
    		
    		if (limitSwitch.get() == true) {
    			cubeArmBack.set(0.0);
        		cubeArmFront.set(0.0);
        		System.out.println("limitSwitch is stalling arm");
    		} else {
    			
    			cubeArmBack.set(0.9);
        		cubeArmFront.set(0.9);
        		System.out.println("Arm is going up");
    		}
    	

    	} else if (operatorController.getJoystickY() <= -0.1) {
    		
    		cubeArmBack.set(-0.15);
    		cubeArmFront.set(-0.15);

    	} else {
    		
    		cubeArmBack.set(0.0);
    		cubeArmFront.set(0.0);
    	}
    	
    }
    
    
    
    /*
    ========================================================================================================
                            Autonomous Code
    ========================================================================================================
    */
public void auto_driveForward(double speed, double time) {
	
	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		
		count++;
		myRobot.tankDrive(-speed, -speed);
		Timer.delay(0.001);
	}
	
	myRobot.tankDrive(0.0, 0.0);
	
}

public void auto_raiseArm(double speed, double time) {
	
	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		
		count++;
		intakeRotator.set(0.7);
		Timer.delay(0.001);
	}
	
	
	intakeRotator.set(0.0);
	
	
}

/*
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
*/

public void auto_deployCube() {
	
	int count = 0;
	while (isEnabled() && isAutonomous() && count < 1000) {
		
		count++;
		intakeMotorLeft.set(-0.85);
		intakeMotorRight.set(0.85);
		Timer.delay(0.001);
	
}
	intakeMotorLeft.set(0.0);
	intakeMotorRight.set(0.0);
	
}


public void auto_rotateCube() {
	
	int count = 0;
	while (isEnabled() && isAutonomous() && count < 1000) {
		
		count++;
		intakeRotator.set(0.7);
		Timer.delay(0.001);
	
}
	intakeRotator.set(0.0);
	
}


public void auto_runIntakeArms(double speed, double time) {
	
	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		count++;
		intakeMotorLeft.set(0.75);
		intakeMotorRight.set(-0.75);
		Timer.delay(0.001);
	}
	
	intakeMotorLeft.set(0.0);
	intakeMotorRight.set(0.0);
	
}

public void auto_turnLeft(double speed, double time) {

	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		count++;
		myRobot.tankDrive(-speed, speed);
		Timer.delay(0.001);
	}
	
	myRobot.tankDrive(0.0, 0.0);
	
}

public void auto_stationaryTurnLeft(double speed, double time) {

	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		count++;
		myRobot.tankDrive(0.0, speed);
		Timer.delay(0.001);
	}
	
	myRobot.tankDrive(0.0, 0.0);
	
}

public void auto_turnRight(double speed, double time) {

	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		count++;
		myRobot.tankDrive(speed, -speed);
		Timer.delay(0.001);
	}
	
	myRobot.tankDrive(0.0, 0.0);

	
}

public void auto_stationaryTurnRight(double speed, double time) {

	int count = 0;
	while (isEnabled() && isAutonomous() && count < time*1000) {
		count++;
		myRobot.tankDrive(speed, 0.0);
		Timer.delay(0.001);
	}
	
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


/* public void visionAlignWithCube() {
	
	System.out.println("Cube started");
	
	// for (int i = 0; i < 100; i++) {
	while (isEnabled() && isAutonomous()) {
		double centerX;

        synchronized (imgLock) {
            centerX = this.centerX;
        }
        
        System.out.println("centerX: " + centerX);
        
        if (centerX > (IMG_WIDTH / 2)) {
        	System.out.println("Cube found, turning right?");
        	
        } else if (centerX != 0.0) {
        	System.out.println("Cube found, turning left?");
        	
        }
        
        Timer.delay(0.05);
        
	}
	
	System.out.println("Cube Done");
    
}

	public void auto_getCube() {
		
		visionAlignWithCube();
		
	} 
	*/
	
	public void auto_crossBaseline() {
		auto_driveForward(1.0, 1.0);
	}

public void autonomous() {
	
    System.out.println("Autonomous mode running");
    
	String selected = "";
	String startPosition = "";
	
	Double getStart = SmartDashboard.getNumber("RobotStartPosition", 0.0);
	
	if (getStart == 0.0) {
		startPosition = "Error";
	} else if (getStart == 1.0) {
		startPosition = "Left";
	} else if (getStart == 2.0) {
		startPosition = "Center";
	} else if (getStart == 3.0) {
		startPosition = "Right";
	} else {
		System.out.println("Invalid start position " + startPosition);
		startPosition = "Left";
	}
	double auto = SmartDashboard.getNumber("automode", 0.0);
	
	if (auto == 0.0) {
		selected = "None";
	} else if (auto == 1.0) {
		selected = "Baseline";
	} else if (auto == 2.0) {
		selected = "Switch";
	} else if (auto == 3.0) {
		selected = "Scale";
	} else if (auto == 9.0) {
		selected = "GetCube";
	} else {
		System.out.println("I blame ananta " + selected);
		selected = "Baseline";
	}
	
	SmartDashboard.putString("selectedAuto", selected);
	
	boolean cutAcrossAlliance = false;
	boolean placeCube = false;
	boolean useVisionTracking = false;
	
	System.out.println("Autonomous Mode Selected: " + selected);
	
	// Get these values from the network table (Placed there by the driver station)
	startPosition = SmartDashboard.getString("autonomous/StartPosition", "Left");
	cutAcrossAlliance = SmartDashboard.getBoolean("autonomous/CutAcrossAlliance", false);
	
	placeCube = SmartDashboard.getBoolean("autonomous/PlaceCube", false);
	useVisionTracking = SmartDashboard.getBoolean("autonomous/UseVisionTracking", false);
	
	
	System.out.println("AutoMode. Starting on the " + startPosition + " doing " + selected + ". Field Cutting: " + cutAcrossAlliance);
    
	String gameData = DriverStation.getInstance().getGameSpecificMessage();
	char allianceSwitch = gameData.charAt(0);
	char centerScale = gameData.charAt(1);
	char opposingSwitch = gameData.charAt(2);
	
	System.out.println("Switch Status [AllianceSwitch="+allianceSwitch+", CenterScale=" + centerScale + ", opposingSwitch="+opposingSwitch+"]");
	

	if (selected == "None") {
		
		System.out.println("We chose to do nothing for this round");
		
	} else if (selected == "Baseline") {
		
		auto_crossBaseline();
		
	} else if (selected == "Switch") {
		
		if (startPosition == "Left") {
			
			if (allianceSwitch == 'R') {
				
				if (cutAcrossAlliance == true) {
					
					
					System.out.println("Switch is on other side. Cutting across alliance Right -> Left");
					
					// Place the cube on the RIGHT switch starting from the LEFT side
					
					auto_driveForward(-0.5, 3.0);
					
					
					
				} else {
					
					System.out.println("Switch is on other side. Crossing baseline instead.");
					
					auto_crossBaseline();
					
				}
				
			} else {
				
				System.out.println("Placing on switch. Left -> Left");
				
				// Place the cube on the LEFT Switch starting from the LEFT side
				
				auto_stationaryTurnLeft(0.3, 1.0);
				auto_driveForward(0.4, 2.0);
				auto_rotateCube();
				
			}
			
		} else if (startPosition == "Right") {
			
			if (allianceSwitch == 'L') {
				
				if (cutAcrossAlliance == true) {
					
					
					System.out.println("Switch is on other side. Cutting across alliance Left -> Right");
					
					// Place the cube on the LEFT switch starting from the RIGHT side
					auto_driveForward(-1.0, 3.0);
					
				} else {
					
					System.out.println("Switch is on other side. Crossing baseline instead.");
					auto_crossBaseline();
					
				}
				
			} else {
				
				System.out.println("Placing on switch. Right -> Right");
				
				// Place the cube on the RIGHT Switch starting from the RIGHT side
				
				auto_driveForward(-0.5, 3.0);
				auto_turnLeft(0.2, 0.2);
				auto_driveForward(0.2, 0.5);
				auto_raiseArm(0.7, 1.0);
				auto_deployCube();
			}
			
		} else if (startPosition == "Center") {
			
			if (allianceSwitch == 'R') {
				
				System.out.println("Placing on switch. Center -> Right");
				
				// Place the cube on the RIGHT Switch starting from the CENTER
				
				auto_stationaryTurnRight(0.3, 1.0);
				auto_driveForward(0.4, 2.0);
				auto_rotateCube();
				
			} else if (allianceSwitch == 'L') {
				
				System.out.println("Placing on switch. Center -> Left");
				
				// Place the cube on the LEFT Switch starting from the CENTER
				
				auto_stationaryTurnLeft(0.3, 1.0);
				auto_driveForward(0.4, 2.0);
				auto_rotateCube();
				
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
					auto_driveForward(-1.0, 3.0);
					auto_turnRight(0.4, 1.0);
					auto_driveForward(1.0, 1.0);
					auto_raiseArm(0.7, 3.0);
					
				} else {
					
					System.out.println("Scale is on other side. Crossing baseline instead.");
					auto_crossBaseline();
					
				}
				
			} else {
				
				System.out.println("Placing on scale. Left -> Left");
				
				// Place the cube on the LEFT Scale starting from the LEFT side
				
				auto_driveForward(-1.0, 5.0);
				auto_raiseArm(0.7, 3.0);
				auto_deployCube();
			}
			
		} else if (startPosition == "Right") {
			
			if (centerScale == 'L') {
				
				if (cutAcrossAlliance == true) {
					
					
					System.out.println("Scale is on other side. Cutting across field Left -> Right");
					
					// Place the cube on the LEFT Scale starting from the RIGHT side
					auto_driveForward(1.0, 3.0);
					auto_turnLeft(0.4, 1.0);
					auto_driveForward(0.5, 1.0);
					auto_raiseArm(0.7, 3.0);
					auto_deployCube();
					
				} else {
					
					System.out.println("Scale is on other side. Crossing baseline instead.");
					auto_crossBaseline();
					
				}
				
			} else {
				
				System.out.println("Placing on scale. Right -> Right");
				
				// Place the cube on the RIGHT Scale starting from the RIGHT side
				
				auto_driveForward(1.0, 4.0);
				auto_raiseArm(0.7, 3.0);
				auto_deployCube();
			}
			
		} else if (startPosition == "Center") {
			
			if (cutAcrossAlliance == true) {
				
				if (allianceSwitch == 'R') {
					
					System.out.println("Placing on scale. Center -> Right");
					
					// Place the cube on the RIGHT Scale starting from the CENTER
					
					auto_driveForward(0.5, 2.0);
					auto_turnRight(0.3, 1.0);
					auto_driveForward(1.0, 2.0);
					auto_turnLeft(0.4, 1.0);
					auto_driveForward(1.0, 3.0);
					auto_raiseArm(0.7, 3.0);
					auto_deployCube();
					
				} else if (allianceSwitch == 'L') {
					
					System.out.println("Placing on scale. Center -> Left");
					
					// Place the cube on the LEFT Scale starting from the CENTER
					auto_driveForward(0.5, 2.0);
					auto_turnLeft(0.4, 1.0);
					auto_driveForward(1.0, 2.0);
					auto_turnRight(0.4, 1.0);
					auto_driveForward(1.0, 3.0);
					auto_raiseArm(0.7, 3.0);
					auto_deployCube();
					
					
				}
				
			} else {
				
				System.out.println("Cannot place on scale, we're stuck in the center.");
			}
			
		} else {
			
			System.out.println("Error. No Invalid Start Position: " + startPosition);
		}
			
	} else if (selected == "GetCube") {
		System.out.println("wtf why did you put 9");
	} else {
		System.out.println("Invalid Autonomous Mode: No code designated for '" + selected + "'");
	}
	
}

	//Disabling Code

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
		intakeRotator.set(0.0);
		// compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}






