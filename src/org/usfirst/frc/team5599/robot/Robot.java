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

	JoystickController driveStickRight;
	JoystickController driveStickLeft;
	XBoxController operatorController;
 
	Spark leftFrontWheel;
    Spark rightFrontWheel;
    Spark leftRearWheel;
    Spark rightRearWheel;
    
    SpeedControllerGroup driveTrainLeft;
    SpeedControllerGroup driveTrainRight;

    Spark intakeMotorLeft;
	Spark intakeMotorRight;

	Spark climber;
	Spark climberBase;

	DoubleSolenoid intakeSolenoid1;
	DoubleSolenoid intakeSolenoid2;
	DoubleSolenoid shootingSolenoid3;
	DoubleSolenoid openingSolenoid4;

	Compressor compressor;

	RobotDrive myRobot;
	
	PowerDistributionPanel powerDistributionPanel;
	
	public Robot() {
 
		driveStickRight = new JoystickController(0);
		driveStickLeft = new JoystickController(1);                                                                                                                    
		operatorController = new XBoxController(2);

		rightFrontWheel = new Spark(4);

		rightRearWheel = new Spark(1);
		
		leftFrontWheel = new Spark(3);
		
		leftRearWheel = new Spark(2);
		
		driveTrainLeft = new SpeedControllerGroup(leftFrontWheel, leftRearWheel);
		driveTrainRight = new SpeedControllerGroup(rightFrontWheel, leftFrontWheel);
		
		// myRobot = new RobotDrive(driveTrainLeft, driveTrainRight);
		myRobot = new RobotDrive(leftFrontWheel, leftRearWheel, rightFrontWheel, rightRearWheel);
		powerDistributionPanel = new PowerDistributionPanel();
		
		intakeMotorLeft = new Spark(5);
		
		intakeMotorRight = new Spark(6);

		climber = new Spark(8);	

		climberBase = new Spark(7);

		intakeSolenoid1 = new DoubleSolenoid(0,1);
		intakeSolenoid2 = new DoubleSolenoid(2,3);
		shootingSolenoid3 = new DoubleSolenoid(4,5);
		openingSolenoid4 = new DoubleSolenoid(6,7);
		
		powerDistributionPanel.clearStickyFaults();
		
		String[] autonomousModes = {"None", "Baseline", "Left", "Center", "Right"};
		SmartDashboard.putStringArray("autonomous/modes", autonomousModes);
	
	}

	 /*
        ========================================================================================================
                                Tele-Operated Code
        ========================================================================================================
   	 */
		
    public void operatorControl() {

    	while (isEnabled() && isOperatorControl()) {
    		
    		System.out.println("Teleop mode!");
			
	        double stickRightY = driveStickRight.getJoystickY();
	        double stickLeftY = driveStickLeft.getJoystickY();
	        
	        myRobot.tankDrive(stickLeftY, stickRightY);

			controlIntakeMotors();
			controlLiftArm();
							
			Timer.delay(0.005);
			
		}
	}
    
    public void controlIntakeMotors() {
    	
    	if (operatorController.getAButton() == true) {
    		
			intakeMotorLeft.set(1.0);
			intakeMotorRight.set(1.0);
		
		} else if (operatorController.getBButton() == true) {
			
			intakeMotorLeft.set(-1.0);
			intakeMotorRight.set(-1.0);
			
		} else {
			
		    intakeMotorLeft.set(0.0);
			intakeMotorRight.set(0.0);
			
		}
		
    }
    
    public void controlLiftArm() {
    	
    	if (operatorController.getRightBumper() == true) {
    		
    		climberBase.set(0.8);
    		climber.set(-0.2);
    	} else if (operatorController.getLeftBumper() == true) {
    		
    		climberBase.set(-0.8);
    		climber.set(0.2);
    		
    	} else {
    		
    		climberBase.set(0.0);
    		climber.set(0.0);
    	}
    	
    }
    
    /*
    ========================================================================================================
                            Autonomous Code
    ========================================================================================================
    */
    
    public void autonomous() {
    	
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		String selected = SmartDashboard.getString("autonomous/selected", "Error");
		
		System.out.println("Autonomous Mode Selected: " + selected);
		
		char allianceSwitch = gameData.charAt(0);
		char centerScale = gameData.charAt(1);
		char opposingSwitch = gameData.charAt(2);

		/*
		if (allianceSwitch == 'L') {
			
			for (int count = 0; count <= 30; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(0.5);
				leftRearWheel.set(1.0);
				rightRearWheel.set(0.0);
				Timer.delay(0.1);
		 	}
	
			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(1.0);
				leftRearWheel.set(1.0);
				rightRearWheel.set(1.0);
				Timer.delay(0.1);
			}
	
			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(0.0); 
				rightFrontWheel.set(0.0);
				leftRearWheel.set(0.0);
				rightRearWheel.set(0.0);	
				Timer.delay(0.1);
			}
	
			for (int count = 0; count <= 30; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(0.5);
				leftRearWheel.set(1.0);
				rightRearWheel.set(0.0);		
				Timer.delay(0.1);
			}
	
	
			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(-1.0); 
				rightFrontWheel.set(-1.0);
				leftRearWheel.set(-1.0);
				rightRearWheel.set(-1.0);
				Timer.delay(0.1);
			}
	
			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(0.0); 
				rightFrontWheel.set(0.0);
				leftRearWheel.set(0.0);
				rightRearWheel.set(0.0);
				Timer.delay(0.1);
			}
	
		} else {
	
			for (int count = 0; count <= 30; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(1.0);
				leftRearWheel.set(1.0);
				rightRearWheel.set(1.0);
				Timer.delay(0.1);
			}
			
	 	}
	 	
	 	*/
	}
	
	public void leftStart(){
		
	}
	
	public void rightStart(){
		
	}
	
	public void centerStart(){
		
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

		// compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}






