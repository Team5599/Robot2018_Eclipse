package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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

	DoubleSolenoid intakeSolenoidLeft;	
	DoubleSolenoid intakeSolenoidRight;
	DoubleSolenoid shootingSolenoid;
	DoubleSolenoid openingSolenoid4;

	Compressor compressor;

	RobotDrive myRobot;
	
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
		
		String[] autonomousModes = {"None", "Baseline", "Left", "Center", "Right"};
		SmartDashboard.putStringArray("autonomous/modes", autonomousModes);
	
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
	        
	      /*  if (Math.abs(stickLeftY) > 0.1){
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
			
							
			Timer.delay(0.005);
			
		}
	}

	/*public void controlWheels() { //Figure out inputs

		if (driverController.getLeftThumbStickX == 1.0) {

		}

		else if (driverController.getLeftThumbStickX == -1.0) {

		}

		else if (driverController.getLeftThumbStickY == 1.0) {

		}

		else if (driverController.getLeftThumbStickY == -1.0) {

		}

		else if (driverController.getRightThumbStickX == 1.0) {

		}

		else if (driverController.getRightThumbStickX == -1.0) {

		}

		else if (driverController.getRightThumbStickY == 1.0) {

		}

		else if (driverController.getRightThumbStickY == -1.0) {

		}

		else {

		}

	}
	*/

    
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
    /*
    ========================================================================================================
                            Autonomous Code
    ========================================================================================================
    */
    
    public void autonomous() {
    	System.out.println("Running autonomous...");
    	myRobot.tankDrive(-1.0, -1.0);
    	//Timer.delay(1.0);
    	System.out.println("Autonomous run completed. "); }
    	
		//String gameData = DriverStation.getInstance().getGameSpecificMessage();
		String gameData = "LRL";
		String selected = SmartDashboard.getString("autonomous/selected", "Error")
		
		//System.out.println("Autonomous Mode Selected: " + selected);
		
		//char allianceSwitch = gameData.charAt(0);
		//char scale = gameData.charAt(1);
		//char opposingSwitch = gameData.charAt(2);
	
		
		
		//myRobot.tankDrive(1.0, 1.0); 
		
		
   /* for (int time = 0; time <= 18; time++) {
    	leftFrontWheel.set(1.0);
    	rightFrontWheel.set(1.0);
    	leftRearWheel.set(1.0);
    	rightRearWheel.set(1.0); 
    	Timer.delay(0.1);	}
    }
		*/
		//Timer.delay(1.0); 
		
	/*	if (!isEnabled() || !isAutonomous()) { return; }
		
		myRobot.tankDrive(0.0, 0.0);
		
		Timer.delay(0.2);
		
		if (!isEnabled() || !isAutonomous()) { return; }
		
		climberBase.set(-0.5);
		climber.set(0.2);
		
		Timer.delay(2.0);
		
		if (!isEnabled() || !isAutonomous()) { return; }
		
		shootingSolenoid.set(DoubleSolenoid.Value.kForward);
		
		Timer.delay(0.1);
		
		if (!isEnabled() || !isAutonomous()) { return; }
		
		climberBase.set(0.0);
		climber.set(0.0);

		for (int count = 0; count <= 30; count++) {
	*/	
		
	
		
	if (allianceSwitch == 'L'); {
		System.out.println("Robot Is at L!"); 
			//if (!isEnabled() || !isAutonomous()) { return; }
			
			for (int a = 0; a <= 100; a++) {
				leftFrontWheel.set(-1.0); 
				rightFrontWheel.set(-1.0);
				leftRearWheel.set(-1.0);
				rightRearWheel.set(-1.0);
				Timer.delay(0.1);
		 	}
	
			for (int b = 0; b <= 100; b++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(1.0);
				leftRearWheel.set(1.0);
				rightRearWheel.set(1.0);
				Timer.delay(0.1);
			}
	
			for (int c = 0; c <= 100; c++) {
				leftFrontWheel.set(-1.0); 
				rightFrontWheel.set(-1.0);
				leftRearWheel.set(-1.0);
				rightRearWheel.set(-1.0);	
				Timer.delay(0.1);
				
				
			}
			
			else  {
				
				for (int count = 0; count <= 30; count++) {
					leftFrontWheel.set(0.0); 
					rightFrontWheel.set(0.0);
					leftRearWheel.set(0.0);
					rightRearWheel.set(0.0);
					Timer.delay(0.1);
				}
			}
	}
			
			
		 
			/*if (!isEnabled() || !isAutonomous()) { return; }
			
			cubeArmFront.set(0.0);
			cubeArmBack.set(0.0);
			cubeArmBase.set(1.0);
			Timer.delay(2.0);
			
			if (!isEnabled() || !isAutonomous()) { return; }
			
			shootingSolenoid.set(DoubleSolenoid.Value.kForward);
			
			Timer.delay(0.1);
			
			
			if (!isEnabled() || !isAutonomous()) { return; }
			*/
			//cubeArmBase.set(0.0);
			//cubeArmFront.set(0.0);
			
			/*for (int d = 0; d <= 30; d++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(1.0);
				leftRearWheel.set(1.0);
				rightRearWheel.set(1.0);		
				Timer.delay(0.1);
			}
	*/
	
			/*for (int a = 0; a <= 300; a++) {
				leftFrontWheel.set(-1.0); 
				rightFrontWheel.set(-1.0);
				leftRearWheel.set(-1.0);
				rightRearWheel.set(-1.0);
				Timer.delay(0.1);
			}
	
			for (int a = 0; a <= 300; a++) {
				leftFrontWheel.set(0.0); 
				rightFrontWheel.set(0.0);
				leftRearWheel.set(0.0);
				rightRearWheel.set(0.0);
				Timer.delay(0.1);}}
			}

			//if (!isEnabled() || !isAutonomous()) { return; }
			
			//myRobot.tankDrive(0.0, 0.0); }
			
	
		/* else {
	
			for (int count = 0; count <= 30; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(1.0);
				leftRearWheel.set(1.0);
				rightRearWheel.set(1.0);
				Timer.delay(0.1);
			}
			*/
	 	

	 	
	 	/* void autonomous() {

	 	String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		String selected = SmartDashboard.getString("autonomous/selected", "Error");
		
		System.out.println("Autonomous Mode Selected: " + selected);
		
		char allianceSwitch = gameData.charAt(0);
		char scale = gameData.charAt(1);
		char opposingSwitch = gameData.charAt(2);
		
		myRobot.tankDrive(0.5, 0.5);
		Timer.delay(2.0);
		myRobot.tankDrive(0.0, 0.0);
		*/
		
		//if (scale == 'L' || !isEnabled() || !isAutonomous()) { return ;} 
		//System.out.println("Robot is driving Left!"); }
	

	


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
		// compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}






