package frc.team568.robot.deepspace;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Camera {
	Joystick controller0;

	TalonSRXDrive driveTrain;

	NetworkTable dataToSendTable = NetworkTableInstance.getDefault().getTable("dataToSend");
	NetworkTableEntry recieveDistanceFromTarget;
	NetworkTableEntry recieveCenterX;
	NetworkTableEntry recieveGetAngle;

	NetworkTable cameraInput = NetworkTableInstance.getDefault().getTable("Camera Input");
	NetworkTableEntry cameraInputPort;

	Double centerX;
	Double distanceFromTarget;
	Double getAngle;

	boolean isShifted;
	boolean shiftIsHeld;

	public Camera() {

	}

	public void initCamera() {
		controller0 = new Joystick(0);
	}
	
	public void driveToTapeCommand() {
		new JoystickButton(controller0, Xinput.X).whileTrue(new TapeTrackerCommand(driveTrain));
	}
	public void toggleCameraCommand() {
		new JoystickButton(controller0, Xinput.Back).onTrue(new CameraToggleCommand());
	}

	public void toggleCamera() {
		
	}

	public void toggleCamera0() {
		cameraInputPort = cameraInput.getEntry("Camera Port");
		cameraInputPort.setNumber(0);
	}

	public Double returnCenterX() {
		recieveCenterX = dataToSendTable.getEntry("centerX");
		centerX = recieveCenterX.getDouble(-40);
		
		return centerX;
	}

	public Double returnDistanceFromTarget() {
		recieveDistanceFromTarget = dataToSendTable.getEntry("distanceFromTarget");
		distanceFromTarget = recieveDistanceFromTarget.getDouble(-30);
		
		return distanceFromTarget;
	}

	public Double returnGetAngle() {
		recieveGetAngle = dataToSendTable.getEntry("getAngle");
		getAngle = recieveGetAngle.getDouble(-20);
		
		return getAngle;
	}
}