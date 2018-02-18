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

	DoubleSolenoid intakeSolenoidLeft;
	DoubleSolenoid intakeSolenoidRight;
	DoubleSolenoid shootingSolenoid;
	DoubleSolenoid openingSolenoid4;

	Compressor compressor;

	RobotDrive myRobot;
	
	PowerDistributionPanel powerDistributionPanel;
	ADXRS450_Gyro gyro;
	
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
		
		intakeMotorLeft = new Spark(5);
		intakeMotorRight = new Spark(6);

		climber = new Spark(8);	

		climberBase = new Spark(7);

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
			
	        double stickRightY = driveStickRight.getJoystickY();
	        double stickLeftY = driveStickLeft.getJoystickY();
	        
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
			controlShootingPiston();
			controlArmPistons();
							
			Timer.delay(0.005);
			
		}
	}
    
    public void controlIntakeMotors() {
    	
    	if (operatorController.getAButton() == true) {
    		
			intakeMotorLeft.set(1.0);
			intakeMotorRight.set(-1.0);
		
		} else if (operatorController.getBButton() == true) {
			
			intakeMotorLeft.set(-1.0);
			intakeMotorRight.set(1.0);
			
		} else {
			
		    intakeMotorLeft.set(0.0);
			intakeMotorRight.set(0.0);
			
		}
		
    }
    
    public void controlLiftArm() {
    	
    	if (operatorController.getRightTrigger() == true) {
    		
    		climberBase.set(0.7);
    		climber.set(-0.3);
    	} else if (operatorController.getLeftTrigger() == true) {
    		
    		climberBase.set(-0.7);
    		climber.set(0.3);
    		
    	} else {
    		
    		climberBase.set(0.0);
    		climber.set(0.0);
    	}
    	
    }
    
    public void controlShootingPiston(){
    	
    	if (operatorController.getXButton() == true){
    		
    		shootingSolenoid.set(DoubleSolenoid.Value.kForward);
    		
    	} else if (operatorController.getYButton() == true){
    		
    		shootingSolenoid.set(DoubleSolenoid.Value.kReverse);
    		
    	}
    
    }
    
    public void controlArmPistons(){
    	if (operatorController.getLeftBumper() == true){
    		
    		intakeSolenoidLeft.set(DoubleSolenoid.Value.kForward);
    		intakeSolenoidRight.set(DoubleSolenoid.Value.kForward);
    		
    	} else if (operatorController.getRightBumper() == true){
    		
    		intakeSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
    		intakeSolenoidRight.set(DoubleSolenoid.Value.kReverse);
    		
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
		
		
		myRobot.tankDrive(0.4, 0.4);
		
		Timer.delay(2.0);
		
		if (!isEnabled() || !isAutonomous()) { return; }
		
		myRobot.tankDrive(0.0,0.0);
		
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






