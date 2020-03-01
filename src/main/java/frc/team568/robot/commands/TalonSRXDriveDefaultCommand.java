package frc.team568.robot.commands;

import java.util.Map;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class TalonSRXDriveDefaultCommand extends CommandBase {
	final TalonSRXDrive drive;
	Map<Input, DoubleSupplier> inputMap;

	private double kP = 0.05;
	private double prevAngle = 0;
	private PIDController pidDrive;

	public TalonSRXDriveDefaultCommand(final TalonSRXDrive drive, Map<Input, DoubleSupplier> inputMap) {
		this.drive = drive;
		this.inputMap = inputMap;

		prevAngle = drive.getHeading();
		pidDrive = new PIDController(kP, 0, 0);
		addRequirements(drive);
	}

	@Override
	public void execute() {

		if (drive.getTankControls()) {

			drive.tankDrive(
				axis(Input.TANK_LEFT),
				axis(Input.TANK_RIGHT));
	
		} else {
/*
			// pid calculation
			pidDrive.setSetpoint(prevAngle);
			double correction = pidDrive.calculate(drive.getHeading());

			// resets current gyro heading and pid if turning or not moving
			if (Math.abs(axis(Input.TURN)) > 0.05) {
			
				pidDrive.reset();
				prevAngle = drive.getHeading();
				correction = 0;

			} else if (Math.abs(axis(Input.FORWARD)) < 0.05 && Math.abs(axis(Input.TURN)) < 0.05) {

				pidDrive.reset();
				prevAngle = drive.getHeading();
				correction = 0;

			}*/

			drive.arcadeDrive(
				axis(Input.FORWARD),
				axis(Input.TURN) * 0.6);// + correction);
		
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