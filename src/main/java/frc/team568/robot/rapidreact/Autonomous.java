package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj2.command.MecanumControllerCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

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
		switch (autoType) {
			case "Outtake":
				addCommands(new ChargeAndScore(subsystem, intake, param));
				break;
			case "Taxi":
				addCommands(new AutoTaxi(subsystem, param.taxiTime()));
				break;
			case "Do Nothing":
				addCommands(new WaitCommand(100));
				break;
			default:
				addCommands(new WaitCommand(100));
		}
	}

	@Override
	public void end(boolean interrupted) {
		subsystem.getMecanumDrive().stopMotor();
		super.end(interrupted);
	}
}
