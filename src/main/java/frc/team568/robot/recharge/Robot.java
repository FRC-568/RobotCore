package frc.team568.robot.recharge;

import static edu.wpi.first.wpilibj.XboxController.Button.kBack;
import static edu.wpi.first.wpilibj.XboxController.Button.kStart;
import static edu.wpi.first.wpilibj.XboxController.Button.kStickLeft;
import static edu.wpi.first.wpilibj.XboxController.Button.kStickRight;

import java.util.Map;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {
	RobotBase robot;
	Shooter shooter;
	Intake intake;
	TalonSRXDrive drive;
	//Gyro gyro = new ADXRS450_Gyro();
	Compressor compressor;
	Command autonomousCommand;
	XinputController driver = new XinputController(0);
	PowerDistributionPanel pdp;

	public Robot() {
		super("Recharge");

		int mainController = 0;
		int secondaryController = 1;

		config("drive/leftMotors", new Integer[]{11, 10});
		config("drive/rightMotors", new Integer[] {9, 8});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);
		
		config("shooter/shooterL", 5);
		config("shooter/shooterR", 6);
		config("shooter/wheel", 7);
		config("shooter/rotator", 8);

		config("intake/intakeWheels", 9);
		config("intake/extenderLeftIn", 10);
		config("intake/extenderLeftOut", 11);
		config("intake/extenderRightIn", 12);
		config("intake/extenderRightOut", 13);

		config("hanger/extenderHangLeftIn", 14);
		config("hanger/extenderHangLeftOut", 15);
		config("hanger/extenderHangRightIn", 16);
		config("hanger/extenderHangRightOut", 17);
		config("hanger/hangerPullerL", 18);
		config("hanger/hangerPullerR", 19);
		config("hanger/shifterWheel", 20);

		button("intakeExtenderToggle", mainController, Xinput.B);
		button("intake", mainController, Xinput.LeftBumper);
		button("shoot", mainController, Xinput.RightBumper);
		button("rotateShooterUp", mainController, Xinput.Y);
		button("rotateShooterDown", mainController, Xinput.A);
		button("hangerUp", secondaryController, Xinput.Y);
		button("hangerDown", secondaryController, Xinput.A);

		//compressor = new Compressor();
		pdp = new PowerDistributionPanel();

		//drive = addSubsystem(TalonSRXDrive::new).withGyro(gyro);
		drive = addSubsystem(TalonSRXDrive::new);
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

		//shooter = addSubsystem(Shooter::new);
		//intake = addSubsystem(Intake::new);

		//driver.getButton(kX).whenHeld(new ShooterAlignCommand(shooter, drive)); //TODO check if this works
	
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		//compressor.setClosedLoopControl(true);
		drive.resetSensors();
		//gyro.reset();
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
		//compressor.setClosedLoopControl(false);
	}

	@Override
	public void autonomousInit() {
		//compressor.setClosedLoopControl(true);
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}
}