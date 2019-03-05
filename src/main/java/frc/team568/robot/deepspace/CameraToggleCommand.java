package frc.team568.robot.deepspace;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.Xinput;

public class CameraToggleCommand extends Command {
	Joystick controller1;
	Camera camera;

	NetworkTable cameraInput = NetworkTableInstance.getDefault().getTable("Camera Input");
	NetworkTableEntry cameraInputPort;

	int flag = 0;
	
	
	public CameraToggleCommand() {

	}

	@Override
	public void initialize() {
			System.out.println("Starting Camera Toggle Command");
			if(flag == 0) {
				cameraInputPort = cameraInput.getEntry("Camera Port");
				cameraInputPort.setDouble(0);
	
				flag = 1;
			} else {
				cameraInputPort = cameraInput.getEntry("Camera Port");
				cameraInputPort.setDouble(1);
	
				flag = 0;
			}	
			
	}
	@Override
	public void execute() {
		
		
		
	}
	@Override
	public boolean isFinished() {
		// if (tracker.returnGetAngle() <= 2 && tracker.returnGetAngle() >= -2) {
		// 	return true;
		// } else {
		// 	return false;
		// }
		return false;
	}

}