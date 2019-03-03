package frc.team568.robot.deepspace;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Camera {
	Joystick controller1;	
	TalonSRXDrive driveTrain;

	NetworkTable dataToSendTable = NetworkTableInstance.getDefault().getTable("dataToSend");
	NetworkTableEntry recieveDistanceFromTarget;
	NetworkTableEntry recieveCenterX;
	NetworkTableEntry recieveGetAngle;

	NetworkTableEntry dummyDistanceFromTarget;
	NetworkTableEntry dummyCenterX;
	NetworkTableEntry dummyGetAngle;

	Double centerX;
	Double distanceFromTarget;
	Double getAngle;

	public Camera() {

	}

	public void initCamera() {
		controller1 = new Joystick(0);
	}

	public void imageProcess() {
	
	}

	public void driveToTapeCommand() {
		new JoystickButton(controller1, Xinput.X).whileActive(new TapeTrackerCommand(driveTrain));
	}


	public Double returnCenterX() {
		// dummyCenterX = dataToSendTable.getEntry("centerx");
		// dummyCenterX.setDouble(10);

		recieveCenterX = dataToSendTable.getEntry("centerX");
		centerX = recieveCenterX.getDouble(-40);
		return centerX;
	}

	public Double returnDistanceFromTarget() {
		// dummyDistanceFromTarget = dataToSendTable.getEntry("distanceFromTarget");
		// dummyDistanceFromTarget.setDouble(20);

		recieveDistanceFromTarget = dataToSendTable.getEntry("distanceFromTarget");
		distanceFromTarget = recieveDistanceFromTarget.getDouble(-30);
		
		return distanceFromTarget;
	}

	public Double returnGetAngle() {
		// dummyGetAngle = dataToSendTable.getEntry("distanceFromTarget");
		// dummyGetAngle.setDouble(30);

		recieveGetAngle = dataToSendTable.getEntry("getAngle");
		getAngle = recieveGetAngle.getDouble(-20);
		
		return getAngle;
	}
}