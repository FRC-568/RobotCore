package frc.team568.robot.bombbot;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.TalonSRXDrive;
import frc.team568.util.Utilities;

public class Robot extends RobotBase {

	int mode = 0;

	TalonSRXDrive drive;

	WPI_TalonSRX claw1, claw2, claw3;

	XinputController driverController;

	public Robot() {
		super("BombBot");

		config("drive/leftMotors", new Integer[] { 1 });
		config("drive/rightMotors", new Integer[] { 2 });
		config("drive/leftInverted", false);
		config("drive/rightInverted", false);

		claw1 = new WPI_TalonSRX(4);
		claw2 = new WPI_TalonSRX(3);
		claw3 = new WPI_TalonSRX(5);

		driverController = new XinputController(0);

		drive = new TalonSRXDrive(this);
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
				Input.FORWARD, () -> driverController.getLeftY(),
				Input.TURN, () -> -driverController.getRightX(),
				Input.TANK_LEFT, () -> -driverController.getLeftY(),
				Input.TANK_RIGHT, () -> -driverController.getRightY())));

		driverController.getButton(Button.kLeftBumper).whenReleased(() -> cycleMode(false));
		driverController.getButton(Button.kRightBumper).whenReleased(() -> cycleMode(true));
	}

	public void robotInit() {
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void teleopPeriodic() {
		if (!driverController.getLeftBumper() && !driverController.getRightBumper()) {
			switch (mode) {
				case 0:
					claw1.set(Utilities.applyDeadband(driverController.getLeftTriggerAxis(), 0.05)
							- Utilities.applyDeadband(driverController.getRightTriggerAxis(), 0.05));
					break;
				case 1:
					claw2.set(Utilities.applyDeadband(driverController.getLeftTriggerAxis(), 0.05)
							- Utilities.applyDeadband(driverController.getRightTriggerAxis(), 0.05));
					break;
				case 2:
					claw3.set(Utilities.applyDeadband(driverController.getLeftTriggerAxis(), 0.05)
							- Utilities.applyDeadband(driverController.getRightTriggerAxis(), 0.05));
					break;
			}
			if (driverController.getAButton()) {
				claw1.set(1);
			} else if (driverController.getXButton()) {
				claw1.set(-1);
			} else {
				claw1.set(0);
			}
			if (driverController.getYButton()) {
				claw2.set(1);
			} else if (driverController.getBButton()) {
				claw2.set(-1);
			} else {
				claw2.set(0);
			}
			if (driverController.getDownButton()) {
				claw3.set(1);
			} else if (driverController.getUpButton()) {
				claw3.set(-1);
			} else
				claw3.set(0);
		}
	}

	void cycleMode(Boolean add) {
		if (add && mode < 3)
			mode++;
		else if (mode > 0)
			mode--;
	}

	@Override
	public void disabledInit() {
	}
}