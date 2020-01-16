package frc.team568.robot.deepspace;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class CameraToggleCommand extends InstantCommand {
	NetworkTable cameraTable = NetworkTableInstance.getDefault().getTable("Camera Input");
	NetworkTableEntry cameraInputPort = cameraTable.getEntry("Camera Port");

	@Override
	public void initialize() {
		double cPort = cameraInputPort.getDouble(0);
		cameraInputPort.setDouble(cPort == 0 ? 1 : 0);	
	}

}