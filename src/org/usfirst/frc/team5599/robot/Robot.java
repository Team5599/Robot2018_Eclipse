package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.SampleRobot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.CameraServer;

@SuppressWarnings("deprecation")
// This neat little line tells Eclipse to ignore all warnings regarding 'deprecation'
// ie to ignore the dozens of RobotDrive and SampleRobot warnings on the sidebar.
// Unfortunately, it ignores *Everything* that is deprecated, not just RobotDrive and SampleRobot.

public class Robot extends SampleRobot {
 	 	
	/*
        ========================================================================================================
                                Initialization
        ========================================================================================================
    */

	JoystickController driveStickRight;
	JoystickController driveStickLeft;
	XBoxController operatorController;
 
	Spark frontLeftWheel;
    Spark frontRightWheel;
    Spark rearLeftWheel;
    Spark rearRightWheel;

    Spark intakeMotorLeft;
	Spark intakeMotorRight;

	Spark climber;
	Spark climberBase;
	Spark tension;

	DoubleSolenoid intakeSolenoidLeft;
	DoubleSolenoid intakeSolenoidRight;
	DoubleSolenoid shootingSolenoid;
	//DoubleSolenoid openingSolenoid4;
	DoubleSolenoid handPiston;

	Compressor compressor;
	
	CameraServer server;
	

	RobotDrive myRobot;
	
	PowerDistributionPanel powerDistributionPanel;
	ADXRS450_Gyro gyro;
	
	/*
	 * Preferences for Autonomous set by the Dashboard
	 * Do not modify
	 */
	
	String startPosition = "";
	
	boolean cutAcrossAlliance = false;
	String cutAcrossAlliance_Direction = ""; // Left or Right?
	
	boolean placeCube = false; // Do we want to deploy the cube when we get to the switch/scale
	boolean useVisionTracking = false; // Fuck.

	
	
	// Constructor
	public Robot() {
 
		driveStickRight = new JoystickController(0);
		driveStickLeft = new JoystickController(1);                                                                                                                    
		operatorController = new XBoxController(2);

		frontRightWheel = new Spark(4);

		rearRightWheel = new Spark(1);
		
		frontLeftWheel = new Spark(3);
		
		rearLeftWheel = new Spark(2);
		
		myRobot = new RobotDrive(frontLeftWheel, rearLeftWheel, frontRightWheel, rearRightWheel);
		
		intakeMotorLeft = new Spark(5);
		intakeMotorRight = new Spark(6);

		climber = new Spark(8);	

		climberBase = new Spark(7);
		
		tension = new Spark(9); //This relies on motor 7

		intakeSolenoidLeft = new DoubleSolenoid(4,5);
		intakeSolenoidRight = new DoubleSolenoid(6,7);
		shootingSolenoid = new DoubleSolenoid(0,1);
		//openingSolenoid4 = new DoubleSolenoid(6,7);
		handPiston = new DoubleSolenoid(3,2);
		
		powerDistributionPanel = new PowerDistributionPanel();
		gyro = new ADXRS450_Gyro();
		
		// Operating the Camera
        server = CameraServer.getInstance();
        server.addAxisCamera("10.55.99.11");
		
	}
	
	public void robotInit() {
		
		/*
		
		Robot-wide initialization code should go here.
		Users should override this method for default Robot-wide initialization
		which will be called when the robot is first powered on.
		It will be called exactly one time.

		Warning: the Driver Station "Robot Code" light and FMS "Robot Ready" indicators will
		be off until RobotInit() exits. Code in RobotInit() that waits for enable will cause
		the robot to never indicate that the code is ready, causing the robot to be bypassed
		in a match.
		
		*/
		
		powerDistributionPanel.clearStickyFaults();
		
		String[] autonomousModes = {"None", "Baseline", "Switch", "Scale", "Switch->Baseline"};
		SmartDashboard.putStringArray("autonomous/modes", autonomousModes);
		
		updateDashboard(true);
		
		System.out.println("Robot Initiliazed! Ready to go!");
		
	}
	
	int dashboardTick = 0;
	
	public void addMotorToDashboard(String name, Spark motorController) {
		SmartDashboard.putNumber(name+"/speed", motorController.get());
		SmartDashboard.putNumber(name+"/current",powerDistributionPanel.getCurrent(motorController.getChannel()));
	}
	
	public void updateDashboard(boolean initialStart){
		
		if ((dashboardTick < 5) && (!initialStart)) {
			dashboardTick++;
		} else {
			
			
			/*
			SmartDashboard.putNumber("gyroAngle", gyro.getAngle());
			SmartDashboard.putNumber("gyroRate", gyro.getRate());
			
			addMotorToDashboard("driveTrain/frontLeftWheel", frontLeftWheel);
			addMotorToDashboard("driveTrain/frontRightWheel", frontRightWheel);
			addMotorToDashboard("driveTrain/rearLeftWheel", rearLeftWheel);
			addMotorToDashboard("driveTrain/rearRightWheel", frontRightWheel);
			
			addMotorToDashboard("intakeArm/left", intakeMotorLeft);
			addMotorToDashboard("intakeArm/right", intakeMotorRight);
			
			addMotorToDashboard("cimber/climberBase", climberBase);
			addMotorToDashboard("climber/climberHand", climber);

			SmartDashboard.putString("solenoid/shootingSolenoid", shootingSolenoid.get().name());
			SmartDashboard.putString("solenoid/intakeSolenoidLeft", intakeSolenoidLeft.get().name());
			SmartDashboard.putString("solenoid/intakeSolenoidRight", intakeSolenoidRight.get().name());
			
			SmartDashboard.putNumber("powerDistributionPanel/inputVoltage", powerDistributionPanel.getVoltage());
			SmartDashboard.putNumber("powerDistributionPanel/temperature", powerDistributionPanel.getTemperature());
			SmartDashboard.putNumber("powerDistributionPanel/totalCurrent", powerDistributionPanel.getTotalCurrent());
			SmartDashboard.putNumber("powerDistributionPanel/totalEnergy", powerDistributionPanel.getTotalEnergy());
			SmartDashboard.putNumber("powerDistributionPanel/totalPower", powerDistributionPanel.getTotalPower());

			SmartDashboard.putBoolean("compressor/enabled", compressor.enabled());
			SmartDashboard.putBoolean("compressor/lowPressure", compressor.getPressureSwitchValue());
			SmartDashboard.putNumber("compressor/current", compressor.getCompressorCurrent());
			
			
			*/
			
			dashboardTick = 0;
			
		}
		
	}

	 /*
        ========================================================================================================
                                Tele-Operated Code
        ========================================================================================================
   	 */
		
    public void operatorControl() {

    	while (isEnabled() && isOperatorControl()) {
    		
    		System.out.println("Teleop mode!");
			
	        double stickLeftY = driveStickLeft.getJoystickY();
	        double stickRightY = driveStickRight.getJoystickY();
	        
	        myRobot.tankDrive(stickLeftY, stickRightY);
	        
	        /*
	        if (Math.abs(stickLeftY) > 0.1){
		        frontLeftWheel.set(stickLeftY);
		        rearRightWheel.set(-stickLeftY);
	        }
	        
	        if (Math.abs(stickRightY) > 0.1){
		        rearLeftWheel.set(stickRightY);
		        frontRightWheel.set(-stickRightY);
	        }
	        */
	        
	        
	        controlIntakeMotors();
			controlLiftArm();
			controlShootingPiston();
			//controlArmPistons();
			controlHandPiston();
			
			updateDashboard(false);
			
			Timer.delay(0.005);
			
		}
	}
    
    public void controlIntakeMotors() {
    	
    	if (operatorController.getAButton() == true) {
    		
    		System.out.println("Running Intake Motor");
    		
			intakeMotorLeft.set(0.7);
			intakeMotorRight.set(-0.7);
		
		} else if (operatorController.getBButton() == true) {
			
			System.out.println("Running Intake Motor Inverted");
			
			intakeMotorLeft.set(-0.7);
			intakeMotorRight.set(0.7);
			
		} else {
			
		    intakeMotorLeft.set(0.0);
			intakeMotorRight.set(0.0);
			
		}
		
    }
    
    public void controlLiftArm() {
    	
    	if (operatorController.getRightTrigger() == true) {
    		
    		System.out.println("Raising Lift Arm");
    		
    		//This raises it
    		climberBase.set(0.8);
    		climber.set(0.3);
    		tension.set(0.6);
    		
    	} else if (operatorController.getLeftTrigger() == true) {
    		
    		System.out.println("Lowering Lift Arm");
    		
    		//This lowers it
    		climberBase.set(-0.1);
    		climber.set(-0.3);
    		tension.set(-0.2);
    		
    	} else {
    		
    		climberBase.set(0.0);
    		climber.set(0.0);
    		tension.set(0.0);
    	}
    	
    }
    
    public void controlShootingPiston(){
    	
    	if (operatorController.getXButton() == true){
    		
    		System.out.println("Deploying Shooting Piston");
    		
    		handPiston.set(DoubleSolenoid.Value.kReverse);
    		shootingSolenoid.set(DoubleSolenoid.Value.kForward);
    		
    		
    	} else if (operatorController.getYButton() == true){
    		
    		System.out.println("Retracting Shooting Piston");
    		
    		shootingSolenoid.set(DoubleSolenoid.Value.kReverse);
    		
    	}
    
    }
    
    public void controlHandPiston(){
    	
    	if (operatorController.getLeftBumper() == true){
    		
    		System.out.println("Deploying Hand Piston");
    		
    		handPiston.set(DoubleSolenoid.Value.kForward);
    		
    	} else if (operatorController.getRightBumper() == true){
    		
    		System.out.println("Retracting Hand Piston");
    		
    		handPiston.set(DoubleSolenoid.Value.kReverse);
    		
    	}
    
    }
    
    public void controlArmPistons(){
    	
    	if (operatorController.getDPadUp() == true){
    		
    		System.out.println("Deploying Side-Intake Pistons");
    		
    		intakeSolenoidLeft.set(DoubleSolenoid.Value.kForward);
    		intakeSolenoidRight.set(DoubleSolenoid.Value.kForward);
    		
    	} else if (operatorController.getDPadDown() == true){
    		
    		System.out.println("Retracting Side-Intake Pistons");
    		
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
    	
		String selected = SmartDashboard.getString("autonomous/selected", "Error");
		
		System.out.println("Autonomous Mode Selected: " + selected);
		
		// Get these values from the network table (Placed there by the driver station)
		startPosition = SmartDashboard.getString("autonomous/StartPosition", "Left");
		cutAcrossAlliance = SmartDashboard.getBoolean("autonomous/CutAcrossAlliance", false);
		cutAcrossAlliance_Direction = SmartDashboard.getString("autonomous/CutAcrossAlliance_Direction", "None");
		placeCube = SmartDashboard.getBoolean("autonomous/PlaceCube", false);
		useVisionTracking = SmartDashboard.getBoolean("autonomous/UseVisionTracking", false);
		
		System.out.println("Autonomous[StartPosition="+startPosition+", cutAcrossAlliance="+cutAcrossAlliance+"|"+cutAcrossAlliance_Direction+", placeCube=" + placeCube + ", useVisionTracking=" + useVisionTracking + "]");
		
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		char allianceSwitch = gameData.charAt(0);
		char centerScale = gameData.charAt(1);
		char opposingSwitch = gameData.charAt(2);
		
		System.out.println("Switch Status [AllianceSwitch="+allianceSwitch+", CenterScale=" + centerScale + ", opposingSwitch="+opposingSwitch+"]");
		
		
		
		/*
		 Every Possible Autonomous Configuration:
		 
		 - Do Nothing
		 
		 - Cross Baseline
		 	- Left/Right Start --> Just go forward
		 	- Center Start [Cross Left]
		 	- Center Start [Cross Right]
		 	
		 - Go to Switch
		 	? Deploy Cube ?
		 	- Left/Right Start --> Should we cut across alliances if the switch is on the opposing side?
		 	- Center Start --> Easy placement
		 	? Cross baseline after deploying cube?
		 		- Left/Right Start
		 			- Did we cut across?
		 				- Will we hit other robots? --> Don't go
		 				- It should be clear --> Navigate to baseline
		 			- We didn't have to cut across? --> Navigate to baseline
		 		- Center Start
		 			- Cross Left
		 				- Will we hit other robots? --> Don't go
		 				- It should be clear --> Navigate to baseline
		 			- Cross Right
		 				- Will we hit other robots? --> Don't go
		 				- It should be clear --> Navigate to baseline
		 	
		 - Go Scale
		 	? Deploy Cube ?
		 	- Left/Right Start --> Just navigate to the scale
		 	- Center Start [Cross Left]
		 	- Center Start [Cross Right]

		 
		 */
		
		if (selected == "None") {
			System.out.println("We chose to do nothing for this round");
			
		} else if (selected == "Baseline") {
			
			autonomous_crossBaseline();
			
		} else if (selected == "Switch") {
			
		} else if (selected == "Scale") {
			
		} else if (selected == "Switch->Baseline") {
			
		} else {
			System.out.println("Invalid Autonomous Mode: No code designated for '" + selected + "'");
		}
		
		
		myRobot.tankDrive(0.4, 0.4);
		
		Timer.delay(2.0);
		
		if (!isEnabled() || !isAutonomous()) { return; }
		
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

		/*
		if (allianceSwitch == 'L') {
			
			for (int count = 0; count <= 30; count++) {
				frontLeftWheel.set(1.0); 
				frontRightWheel.set(0.5);
				rearLeftWheel.set(1.0);
				rearRightWheel.set(0.0);
				Timer.delay(0.1);
		 	}
	
			for (int count = 0; count <= 300; count++) {
				frontLeftWheel.set(1.0); 
				frontRightWheel.set(1.0);
				rearLeftWheel.set(1.0);
				rearRightWheel.set(1.0);
				Timer.delay(0.1);
			}
	
			for (int count = 0; count <= 300; count++) {
				frontLeftWheel.set(0.0); 
				frontRightWheel.set(0.0);
				rearLeftWheel.set(0.0);
				rearRightWheel.set(0.0);	
				Timer.delay(0.1);
			}
	
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
				frontLeftWheel.set(1.0); 
				frontRightWheel.set(0.5);
				rearLeftWheel.set(1.0);
				rearRightWheel.set(0.0);		
				Timer.delay(0.1);
			}
	
	
			for (int count = 0; count <= 300; count++) {
				frontLeftWheel.set(-1.0); 
				frontRightWheel.set(-1.0);
				rearLeftWheel.set(-1.0);
				rearRightWheel.set(-1.0);
				Timer.delay(0.1);
			}
	
			for (int count = 0; count <= 300; count++) {
				frontLeftWheel.set(0.0); 
				frontRightWheel.set(0.0);
				rearLeftWheel.set(0.0);
				rearRightWheel.set(0.0);
				Timer.delay(0.1);
			}

			if (!isEnabled() || !isAutonomous()) { return; }
			
			myRobot.tankDrive(0.0, 0.0);
			
	
		} else {
	
			for (int count = 0; count <= 30; count++) {
				frontLeftWheel.set(1.0); 
				frontRightWheel.set(1.0);
				rearLeftWheel.set(1.0);
				rearRightWheel.set(1.0);
				Timer.delay(0.1);
			}
			
	 	}
	 	*/

		if (!isEnabled() || !isAutonomous()) { return; }
		
		myRobot.tankDrive(0.0, 0.0);
		
	}
    
    public void autonomous_crossBaseline() {
    	
    	myRobot.tankDrive(0.4, 0.4);
		
		Timer.delay(2.0);
		
		if (!isEnabled() || !isAutonomous()) { return; }
		
		myRobot.tankDrive(0.0, 0.0);
		
    }

	//Disabling Code

	public void disabled(){

		System.out.println("Disabling robot . . .");

		myRobot.tankDrive(0.0, 0.0);

		frontLeftWheel.set(0.0);
		frontRightWheel.set(0.0);
		rearLeftWheel.set(0.0);
		rearRightWheel.set(0.0);  
		intakeMotorLeft.set(0.0);
		intakeMotorRight.set(0.0);

		// compressor.stop();
		
		System.out.println("The Robot has been disabled!");
	}
}

