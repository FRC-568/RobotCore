package org.usfirst.frc.team568.robot.powerup;

import java.util.function.IntFunction;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.steamworks.ControllerButtons;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class WestCoastDrive extends SubsystemBase {
	private DifferentialDrive drive;
	private Joystick joystick;

	public WestCoastDrive(RobotBase robot, IntFunction<SpeedController> motorProvider) {
		super(robot);

		SpeedController fl = motorProvider.apply(port("leftFrontMotor"));
		SpeedController bl = motorProvider.apply(port("leftBackMotor"));
		SpeedController fr = motorProvider.apply(port("rightFrontMotor"));
		SpeedController br = motorProvider.apply(port("rightBackMotor"));

		joystick = new Joystick(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			{
				requires(WestCoastDrive.this);
			}

			@Override
			protected void execute() {
				drive.curvatureDrive(-joystick.getRawAxis(1), joystick.getRawAxis(4),
						joystick.getRawButton(ControllerButtons.leftBumper));
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

}
