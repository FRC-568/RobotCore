package frc.team568.robot.recharge;

import static edu.wpi.first.wpilibj.XboxController.Button.*;

import java.util.Map;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.TalonSRXDrive;
import frc.team568.robot.subsystems.DriveBase.Input;

public class Robot extends RobotBase {
	RobotBase robot;
	Shooter shooter;
	TalonSRXDrive drive;
	Gyro gyro = new ADXRS450_Gyro();
	Command autonomousCommand;
	XinputController driver = new XinputController(0);

	public Robot() {
		super("Recharge");

		int mainController = 0;

		config("drive/leftMotors", new Integer[]{2, 1});
		config("drive/rightMotors", new Integer[] {4, 3});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);
		
		config("shooter/shooterL", 5);
		config("shooter/shooterR", 6);
		config("shooter/wheel", 7);
		config("shooter/rotator", 8);

		button("intake", mainController, Xinput.LeftBumper);
		button("shoot", mainController, Xinput.RightBumper);
		button("rotateShooterUp", mainController, Xinput.Y);
		button("rotateShooterDown", mainController, Xinput.A);

		drive = addSubsystem(TalonSRXDrive::new).withGyro(gyro);
		driver.getButton(kBack).whenPressed(drive::toggleIsReversed);
		driver.getButton(kStart).whenPressed(drive::toggleTankControls);
		driver.getButton(kStickLeft)
			.and(driver.getButton(kStickRight))
			.whileActiveOnce(new SequentialCommandGroup(
				new WaitCommand(5),
				new InstantCommand(drive::toggleSafeMode)
			));
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driver.getY(Hand.kLeft),
			Input.TURN, () -> driver.getX(Hand.kRight),
			Input.TANK_LEFT, () -> -driver.getY(Hand.kLeft),
			Input.TANK_RIGHT, () -> -driver.getY(Hand.kRight)
		)));

		shooter = addSubsystem(Shooter::new);
		driver.getButton(kX).whenHeld(new ShooterAlignCommand(drive, robot)); //TODO check if this works
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		drive.resetSensors();
		gyro.reset();
	}

	@Override
	public void teleopPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		
	}

	@Override
	public void autonomousInit() {
		
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}
}