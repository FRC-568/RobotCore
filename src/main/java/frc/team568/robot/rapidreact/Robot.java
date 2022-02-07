package frc.team568.robot.rapidreact;

import java.util.Map;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	MecanumSubsystem drive;

	XinputController driverController = new XinputController(0/* driving Controller Port */);

	public Robot() {
		super("RapidReact");
		drive = new MecanumSubsystem(this);

		drive.setDefaultCommand(new MecanumSubsystemDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driverController.getLeftY(),
			Input.STRAFE, () -> driverController.getLeftX(),
			Input.TURN, () -> driverController.getRightX()
		)));
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}
}