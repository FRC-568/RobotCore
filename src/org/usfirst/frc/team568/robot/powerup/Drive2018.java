package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive2018 extends SubsystemBase {

	private DifferentialDrive drive;
	private SpeedControllerGroup left;
	private SpeedControllerGroup right;
	private Joystick joystick;
	int kTimeoutMs;

	public Drive2018(RobotBase robot) {
		super(robot);

		WPI_TalonSRX fl = new WPI_TalonSRX(port("leftFrontMotor"));
		WPI_TalonSRX bl = new WPI_TalonSRX(port("leftBackMotor"));
		WPI_TalonSRX fr = new WPI_TalonSRX(port("rightFrontMotor"));
		WPI_TalonSRX br = new WPI_TalonSRX(port("rightBackMotor"));

		right = new SpeedControllerGroup(fr, br);
		left = new SpeedControllerGroup(fl, bl);
		left.setInverted(true);
		right.setInverted(true);

		// bl.follow(fl);
		// br.follow(fr);
		// fl.setInverted(true);
		// fl.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
		// 10);
		// fr.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
		// 10);

		// int flVelocity = fl.getSelectedSensorVelocity(0);

		drive = new DifferentialDrive(left, right);

		joystick = new Joystick(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			{
				requires(Drive2018.this);

			}

			@Override
			protected void execute() {
				/*
				 * drive.curvatureDrive((joystick.getRawAxis(1) * 1), (-joystick.getRawAxis(4) *
				 * .6), joystick.getRawButton(ControllerButtons.RightBumper));
				 */
				drive.arcadeDrive(joystick.getRawAxis(1) * .75, -joystick.getRawAxis(4) * .5);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}
}
