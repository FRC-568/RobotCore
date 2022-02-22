package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.MecanumControllerCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Autonomous extends SequentialCommandGroup {
	public Autonomous(Trajectory trajectory, MecanumSubsystem subsystem, double MaxSpeed) {
		addCommands(
			new MecanumControllerCommand(
				trajectory,
				subsystem::getPose,
				subsystem.m_kinematics,
				new PIDController(1, 0, 0),
				new PIDController(1, 0, 0),
				new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(MaxSpeed, 1/* max accleration */)),
				MaxSpeed, //max wheel speeds
				(wheelSpeeds) -> {
					subsystem.motorBR.set(ControlMode.Velocity, subsystem.convertToEncoderUnits(wheelSpeeds.rearRightMetersPerSecond));
					subsystem.motorBL.set(ControlMode.Velocity, subsystem.convertToEncoderUnits(wheelSpeeds.rearLeftMetersPerSecond));
					subsystem.motorFR.set(ControlMode.Velocity, subsystem.convertToEncoderUnits(wheelSpeeds.frontRightMetersPerSecond));
					subsystem.motorFL.set(ControlMode.Velocity, subsystem.convertToEncoderUnits(wheelSpeeds.frontLeftMetersPerSecond));
				},
				subsystem));
	}
}
