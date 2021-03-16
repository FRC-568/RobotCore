package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.Limelight;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Robot extends RobotBase {

	final int drivingControllerPort = 0;

	TwoMotorDrive drive;
	Gyro gyro = new ADXRS450_Gyro();
	Shooter shooter;
	Limelight limelight;

	XinputController driverController = new XinputController(drivingControllerPort);

	AutoNav auto;

	public Robot() {

		super("Recharge2");

		// Mapping the ports
		port("leftMotor", 3);
		port("rightMotor", 2);
		port("leftShooter", 0);
		port("rightShooter", 1);
		port("lifter", 13);
		port("hatch", 0);
		port("aimer", 12);

		// Mapping the controls
		pov("extendAimer", drivingControllerPort, Xinput.Up);
		pov("retractAimer", drivingControllerPort, Xinput.Down);
		button("safeModeToggle", () -> button(0, Xinput.LeftStickIn) && button(0, Xinput.RightStickIn));
		button("shoot", drivingControllerPort, Xinput.RightBumper);
		button("toggleHatch", drivingControllerPort, Xinput.B);
		button("lift", drivingControllerPort, Xinput.Y);
		button("lower", drivingControllerPort, Xinput.A);
		axis("forward", drivingControllerPort, Xinput.LeftStickY);
		axis("turn", drivingControllerPort, Xinput.RightStickX);

		// Subsystems
		drive = addSubsystem(TwoMotorDrive::new);
		limelight = addSubsystem(Limelight::new);
		shooter = addSubsystem(Shooter::new);

		// Initialize autonomous
		auto = new AutoNav(drive);

	}

	@Override
	public void teleopInit() {

		if (auto != null)
			auto.cancel();
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
		auto.schedule();
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

}