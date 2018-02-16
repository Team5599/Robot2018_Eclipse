package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.Timer;
 
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

	DifferentialDrive myRobot;
 
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
		
		myRobot = new DifferentialDrive(driveTrainLeft, driveTrainRight);
		
		intakeMotorLeft = new Spark(5);
		
		intakeMotorRight = new Spark(6);

		climber = new Spark(8);	

		climberBase = new Spark(9);

		intakeSolenoid1 = new DoubleSolenoid(0,1);
		intakeSolenoid2 = new DoubleSolenoid(2,3);
		shootingSolenoid3 = new DoubleSolenoid(4,5);
		openingSolenoid4 = new DoubleSolenoid(6,7);
	
		
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

			if (operatorController.getAButton() == true) {
				intakeMotorLeft.set(1.0);
				intakeMotorRight.set(1.0);
			
			}
			else {
			    intakeMotorLeft.set(0.0);
				intakeMotorRight.set(0.0);
				
			}
			
			if (operatorController.getBButton() == true) {
				intakeMotorLeft.set(-1.0);
				intakeMotorRight.set(-1.0);
				
			}
			else {
			    intakeMotorLeft.set(0.0);
				intakeMotorRight.set(0.0);
			}
							
			Timer.delay(0.005);
			
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

		// compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}






