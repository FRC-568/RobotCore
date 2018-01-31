package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive2018 extends SubsystemBase {
	private SpeedControllerGroup leftDrive;
	private SpeedControllerGroup rightDrive;
	private DifferentialDrive drive;
	private Joystick joystick;

	public Drive2018(RobotBase robot) {
		super(robot);

		WPI_TalonSRX fl = new WPI_TalonSRX(port("leftFrontMotor"));
		WPI_TalonSRX bl = new WPI_TalonSRX(port("leftBackMotor"));
		WPI_TalonSRX fr = new WPI_TalonSRX(port("rightFrontMotor"));
		WPI_TalonSRX br = new WPI_TalonSRX(port("rightBackMotor"));

		leftDrive = new SpeedControllerGroup(fl, bl);
		leftDrive.setInverted(true);
		rightDrive = new SpeedControllerGroup(fr, br);
		drive = new DifferentialDrive(leftDrive, rightDrive);

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
				drive.arcadeDrive((joystick.getRawAxis(1) * 1), (-joystick.getRawAxis(4) * .6));
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}
}
