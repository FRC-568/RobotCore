package frc.team568.robot.rechargemodified;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.XinputController;
import frc.team568.robot.subsystems.TwoMotorDrive;

public class Robot extends RobotBase {

	final int drivingControllerPort = 0;
	
	TwoMotorDrive drive;
	Gyro gyro = new ADXRS450_Gyro();
	Shooter shooter;
	Compressor compressor;
	Command autonomousCommand;
	XinputController driverController = new XinputController(drivingControllerPort);

	public Robot() {
		
		super("Recharge");

		// Mapping the ports
		port("leftMotor", 12);
		port("rightMotor", 13);
		port("leftShooter", 14);
		port("rightShooter", 15);

		// Mapping the controls
		button("safeModeToggle", () -> button(0, Xinput.LeftStickIn) && button(0, Xinput.RightStickIn));
		button("shoot", drivingControllerPort, Xinput.RightBumper);
		axis("forward", drivingControllerPort, Xinput.LeftStickY);
		axis("turn", drivingControllerPort, Xinput.RightStickX);

		// Subsystems
		drive = addSubsystem(TwoMotorDrive::new);
		shooter = addSubsystem(Shooter::new);
		compressor = new Compressor();
	
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		compressor.setClosedLoopControl(true);
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
		compressor.setClosedLoopControl(false);
	}

	@Override
	public void autonomousInit() {
		compressor.setClosedLoopControl(true);
	}

	@Override
	public void autonomousPeriodic() {
		CommandScheduler.getInstance().run();
	}

}