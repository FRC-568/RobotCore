package frc.team568.robot.recharge;

import static edu.wpi.first.wpilibj.XboxController.Button.kBack;
import static edu.wpi.first.wpilibj.XboxController.Button.kStart;
import static edu.wpi.first.wpilibj.XboxController.Button.kStickLeft;
import static edu.wpi.first.wpilibj.XboxController.Button.kStickRight;
import static edu.wpi.first.wpilibj.XboxController.Button.kX;

import java.util.Map;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.commands.DriveForTime;
import frc.team568.robot.commands.TalonSRXDriveDefaultCommand;
import frc.team568.robot.subsystems.DriveBase.Input;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Robot extends RobotBase {

	int drivingControllerPort = 0;
	int mechanismControllerPort = 1;

	Shooter shooter;
	Intake intake;
	GeneratorHanger hanger;
	
	DriveForTime autonomousDriveForTime;
	TalonSRXDrive drive;
	RobotContainer robotContainer;
	Gyro gyro = new ADXRS450_Gyro();
	Compressor compressor;
	Command autonomousCommand;
	XinputController driverController = new XinputController(drivingControllerPort);
	XinputController mechanismController = new XinputController(mechanismControllerPort); 

	PowerDistributionPanel pdp;

	public Robot() {
		super("Recharge");

		config("drive/leftMotors", new Integer[]{11, 10});
		config("drive/rightMotors", new Integer[] {9, 8});
		config("drive/leftInverted", false);
		config("drive/rightInverted", true);
		
		config("shooter/shooterL", 5);
		config("shooter/shooterR", 7);
		config("shooter/wheel", 3);
		config("shooter/rotator", 4);

		config("intake/intakeWheels", 2);
		config("intake/extenderLeftIn", 1);
		config("intake/extenderLeftOut", 6);
		config("intake/extenderRightIn", 7);
		config("intake/extenderRightOut", 0);

		config("hanger/hangerPuller", 5);

		config("spinner/spinner", 1);
		config("spinner/extendSpinner", 2);

		button("intakeExtenderToggle", mechanismControllerPort, Xinput.B);
		button("intake", mechanismControllerPort, Xinput.LeftBumper);
		button("outTake", mechanismControllerPort, Xinput.X);
		button("shoot", mechanismControllerPort, Xinput.RightBumper);
		button("hangerUp", mechanismControllerPort, Xinput.Y);
		button("hangerDown", mechanismControllerPort, Xinput.A);
		button("spin", mechanismControllerPort, Xinput.B);
		button("stopIntake", mechanismControllerPort, Xinput.Y);

		axis("rotateShooter", mechanismControllerPort, Xinput.LeftStickY);

		compressor = new Compressor();
		pdp = new PowerDistributionPanel();

		drive = addSubsystem(TalonSRXDrive::new).withGyro(gyro);
		//drive = addSubsystem(TalonSRXDrive::new);
		driverController.getButton(kBack).whenPressed(drive::toggleIsReversed);
		driverController.getButton(kStart).whenPressed(drive::toggleTankControls);
		driverController.getButton(kStickLeft)
			.and(driverController.getButton(kStickRight))
			.whileActiveOnce(new SequentialCommandGroup(
				new WaitCommand(5),
				new InstantCommand(drive::toggleSafeMode)
			));
		drive.setDefaultCommand(new TalonSRXDriveDefaultCommand(drive, Map.of(
			Input.FORWARD, () -> -driverController.getY(Hand.kLeft),
			Input.TURN, () -> driverController.getX(Hand.kRight),
			Input.TANK_LEFT, () -> -driverController.getY(Hand.kLeft),
			Input.TANK_RIGHT, () -> -driverController.getY(Hand.kRight)
		)));

		autonomousDriveForTime = new DriveForTime(2, 1, drive);

		//robotContainer = new RobotContainer(drive);

		shooter = addSubsystem(Shooter::new);
		//hanger = addSubsystem(GeneratorHanger::new);
		intake = addSubsystem(Intake::new);

		driverController.getButton(kX).whenHeld(new ShooterAlignCommand(shooter, drive)); //TODO check if this works
		//driverController.getButton(kY).whenPressed(robotContainer.getAutonomousCommand());
	
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		compressor.setClosedLoopControl(true);
		drive.resetSensors();
		gyro.reset();
	}

	@Override
	public void teleopPeriodic() {
		CommandScheduler.getInstance().run();
		System.out.println(shooter.distanceFromTarget());
	}

	@Override
	public void testPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {
		compressor.setClosedLoopControl(false);
	}

	@Override
	public void autonomousInit() {
		compressor.setClosedLoopControl(true);
		autonomousDriveForTime.schedule();
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

}