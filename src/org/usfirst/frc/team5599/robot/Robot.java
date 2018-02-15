package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
 
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

    Spark intakeMotorLeft;
	Spark intakeMotorRight;

	Spark climber;
	Spark climberBase;

	DoubleSolenoid intakeSolenoid1;
	DoubleSolenoid intakeSolenoid2;
	DoubleSolenoid shootingSolenoid3;
	DoubleSolenoid openingSolenoid4;
	DoubleSolenoid climbingSolenoid5;

	Compressor compressor;

	RobotDrive myRobot;
 
	 public Robot() {
 
		driveStickRight = new JoystickController(0);
		driveStickLeft = new JoystickController(1);                                                                                                                    
		operatorController = new XBoxController(2);

		
		rightFrontWheel = new Spark(0);

		rightRearWheel = new Spark(1);
		
		leftFrontWheel = new Spark(2);
		
		leftRearWheel = new Spark(3);
		
		myRobot = new RobotDrive(leftFrontWheel, leftRearWheel, rightFrontWheel, rightRearWheel);
		
		intakeMotorLeft = new Spark(4);
		
		intakeMotorRight = new Spark(5);

		climber = new Spark(8);	

		climberBase = new Spark(9);

		intakeSolenoid1 = new DoubleSolenoid(0,1);
		intakeSolenoid2 = new DoubleSolenoid(2,3);
		shootingSolenoid3 = new DoubleSolenoid(4,5);
		openingSolenoid4 = new DoubleSolenoid(6,7);
		climbingSolenoid5 = new DoubleSolenoid(8,9);
		
		
		
	}

/*
        ========================================================================================================
                                Tele-Operated Code
        ========================================================================================================
   	 */
		
    public void operatorControl() {

    	while (isEnabled() && isOperatorControl()) {
			
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
							
			Timer.delay(0.005);

			}
		}
	}

 /*
        ========================================================================================================
                                Autonomous Code
        ========================================================================================================
    */

	public void autonomous() {

		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.charAt(0) == 'L') {
			
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
				Timer.delay(.1);
			}

			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(0.0); 
				rightFrontWheel.set(0.0);
				leftRearWheel.set(0.0);
				rightRearWheel.set(0.0);	
				Timer.delay(.1);
			}

			for (int count = 0; count <= 30; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(0.5);
				leftRearWheel.set(1.0);
				rightRearWheel.set(0.0);		
				Timer.delay(.1);
			}


 			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(-1.0); 
				rightFrontWheel.set(-1.0);
				leftRearWheel.set(-1.0);
				rightRearWheel.set(-1.0);
				Timer.delay(.1);
			}

			for (int count = 0; count <= 300; count++) {
				leftFrontWheel.set(0.0); 
				rightFrontWheel.set(0.0);
				leftRearWheel.set(0.0);
				rightRearWheel.set(0.0);
				Timer.delay(.1);
			 }

			}

			else {

			for (int count = 0; count <= 30; count++) {
				leftFrontWheel.set(1.0); 
				rightFrontWheel.set(1.0);
				leftRearWheel.set(1.0);
				rightRearWheel.set(1.0);
				Timer.delay(.1);
			}
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

		
		compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}






