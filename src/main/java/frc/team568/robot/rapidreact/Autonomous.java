package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.MecanumControllerCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class Autonomous extends SequentialCommandGroup {
	protected MecanumSubsystem subsystem;
	protected Intake intake;
	// double taxiTime = 1.5;
	// double lungeTime = 1, intakeTime = 3, lidDelay = 0.3;
	private BuiltInAccelerometer accel;

	public Autonomous(Trajectory trajectory, MecanumSubsystem subsystem, double MaxSpeed) {
		addCommands(
				new MecanumControllerCommand(
						trajectory,
						subsystem::getPose,
						subsystem.m_kinematics,
						new PIDController(1, 0, 0),
						new PIDController(1, 0, 0),
						new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(MaxSpeed, 1)),
						MaxSpeed,
						(wheelSpeeds) -> {
							subsystem.motorBR.set(ControlMode.Velocity,
									subsystem.convertToEncoderUnits(wheelSpeeds.rearRightMetersPerSecond));
							subsystem.motorBL.set(ControlMode.Velocity,
									subsystem.convertToEncoderUnits(wheelSpeeds.rearLeftMetersPerSecond));
							subsystem.motorFR.set(ControlMode.Velocity,
									subsystem.convertToEncoderUnits(wheelSpeeds.frontRightMetersPerSecond));
							subsystem.motorFL.set(ControlMode.Velocity,
									subsystem.convertToEncoderUnits(wheelSpeeds.frontLeftMetersPerSecond));
						},
						subsystem));
	}

	public Autonomous(String autoType, MecanumSubsystem subsystem, Intake intake, AutonomousParameters param) {
		this.subsystem = subsystem;
		this.intake = intake;
		addRequirements(subsystem, intake);
		if (autoType == "Outtake") {
			addCommands(
					new InstantCommand(() -> intake.setLidOpen(true)),
					new ParallelCommandGroup(
							new InstantCommand(() -> intake.setIntakeMotor(-0.7)),
							new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(0.7, 0, 0))
									.withTimeout(param.lungeTime.getDouble(1))),
					new WaitUntilCommand(this::hit).andThen(new InstantCommand(() -> intake.setLidOpen(false))),
					new WaitCommand(param.intakeTimeout.getDouble(3)),
					new InstantCommand(() -> subsystem.getMecanumDrive().driveCartesian(0, 0, 0)),
					new InstantCommand(() -> intake.setIntakeMotor(0.0)),
					new WaitUntilCommand(10),
					new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(-0.5, 0, 0)).withTimeout(param.taxiTimeout.getDouble(1.5)),
					new WaitCommand(100));
		} else if (autoType == "Taxi") {
			addCommands(
					new WaitCommand(10),
					new RunCommand(() -> subsystem.getMecanumDrive().driveCartesian(-0.5, 0, 0)).withTimeout(param.taxiTimeout.getDouble(1.5)),
					new WaitCommand(100));
		} else if (autoType == "Do Nothing") {
			addCommands(
					new WaitCommand(100));
		}
	}

	public boolean hit() {
		// Assuming that negative acceleration is the hit.
		return accel.getZ() > 0.5;
	}

	public Autonomous addAccelerometer(BuiltInAccelerometer accelerometer) {
		accel = accelerometer;
		return this;
	}

	@Override
	public void end(boolean interrupted) {
		subsystem.getMecanumDrive().stopMotor();
		super.end(interrupted);
	}
}
