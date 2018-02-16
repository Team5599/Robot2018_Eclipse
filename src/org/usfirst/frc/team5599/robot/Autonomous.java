package org.usfirst.frc.team5599.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class Autonomous extends Robot {
	
	 /*
	    ========================================================================================================
	                            Autonomous Code
	    ========================================================================================================
	*/
	
	public void autonomous() {
	
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		char allianceSwitch = gameData.charAt(0);
		char centerScale = gameData.charAt(1);
		char opposingSwitch = gameData.charAt(2);
	
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
	}
	
	public void leftStart(){
		
	}
	
	public void rightStart(){
		
	}
	
	public void centerStart(){
		
	}
}