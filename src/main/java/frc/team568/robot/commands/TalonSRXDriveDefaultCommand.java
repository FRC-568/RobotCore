package frc.team568.robot.commands;

import java.util.Map;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class TalonSRXDriveDefaultCommand extends CommandBase {
	final TalonSRXDrive drive;
	Map<Input, DoubleSupplier> inputMap;

	private double kP = 0.05;

	public TalonSRXDriveDefaultCommand(final TalonSRXDrive drive, Map<Input, DoubleSupplier> inputMap) {
		this.drive = drive;
		this.inputMap = inputMap;
		addRequirements(drive);
	}

	@Override
	public void execute() {
		if (drive.getTankControls()) {
			drive.tankDrive(
				axis(Input.TANK_LEFT),
				axis(Input.TANK_RIGHT));
		} else {
			drive.arcadeDrive(
				axis(Input.FORWARD),
				axis(Input.TURN) - kP * drive.getTurnRate());
		}
	}

	private final double axis(Input input) {
		return inputMap.get(input).getAsDouble();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("P", () -> kP, p -> this.kP = p);
	}
}