package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.team568.robot.RobotBase;

public class Robot extends RobotBase {
	Command autonomousCommand;
	//motor ports
	private static final int lfMotorPort = 1;
  	private static final int lbMotorPort = 2;
  	private static final int rfMotorPort = 3;
	private static final int rbMotorPort = 4;
	//joystick inputs
//	private static final int JoystickInput = 1;
	//private static final int rightJoystickInput = 1;

	public DifferentialDrive drive;
	
	public Joystick m_leftStick;
  public Joystick m_rightStick;

	public Timer timer;
	
	public Robot() {

		WPI_TalonSRX frontLeft = new WPI_TalonSRX(lfMotorPort);	
		WPI_TalonSRX rearLeft = new WPI_TalonSRX(lbMotorPort);
		WPI_TalonSRX frontRight = new WPI_TalonSRX(rfMotorPort);
		WPI_TalonSRX rearRight = new WPI_TalonSRX(rbMotorPort);


		drive = new DifferentialDrive(new SpeedControllerGroup(frontLeft, rearLeft), new SpeedControllerGroup(frontRight, rearRight));
		frontLeft.setInverted(true);
		rearLeft.setInverted(true);
		frontRight.setInverted(true);
		rearRight.setInverted(true);
		//rightStick = new Joystick(JoystickInput);
		//leftStick = new Joystick(rightJoystickInput);
		m_leftStick = new Joystick(0);
    	m_rightStick = new Joystick(1);
	}
	
	@Override
	public void robotInit() {


	}

	@Override
	public void robotPeriodic() {
		

	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		drive.tankDrive(0.5, 0.5);
	}

	@Override
	public void teleopInit() {

	}

	@Override
	// called every 20 milliseconds in teleop 
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		drive.tankDrive(m_leftStick.getRawAxis(1), m_leftStick.getRawAxis(5));
	}

	@Override
	public void testInit() {

	}

	@Override
	public void testPeriodic() {

		Scheduler.getInstance().run();

	}

	@Override
	public void disabledInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
			autonomousCommand = null;
		}

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
}
