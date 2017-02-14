package org.usfirst.frc.team568.robot;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team568.robot.commands.AutoOne;
import org.usfirst.frc.team568.robot.commands.AutoThree;
import org.usfirst.frc.team568.robot.commands.AutoTwo;
import org.usfirst.frc.team568.robot.commands.Climb;
import org.usfirst.frc.team568.robot.subsystems.Climber;
import org.usfirst.frc.team568.robot.subsystems.DriveTrain;
import org.usfirst.frc.team568.robot.subsystems.GearBox;
import org.usfirst.frc.team568.robot.subsystems.ReferenceFrame2;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot implements PortMapper {
	private final Map<String, Integer> _portMap;

	int session;
	Timer time;
	// public double whichOne;
	// public boolean over;

	public double speed;
	public double pressure;

	// public double EncoderValue;

	protected static Robot instance;
	public OI oi;
	public DriveTrain driveTrain;
	public ReferenceFrame2 referanceFrame2;
	public ADIS16448_IMU imu;
	Command autonomousCommand;

	GearBox gearBox;

	public Climber climber;
	public Compressor compressor;

	public ControllerButtons buttons;
	public Climb climb;

	public Robot() {
		instance = this;
		oi = new OI();

		driveTrain = new DriveTrain();
		// gearBox = new GearBox();

		referanceFrame2 = new ReferenceFrame2();
		time = new Timer();
		imu = new ADIS16448_IMU();
		climber = new Climber();

		_portMap = new HashMap<String, Integer>(20);
		definePort("leftFrontMotor", 1);
		definePort("leftBackMotor", 2);
		definePort("rightFrontMotor", 3);
		definePort("rightBackMotor", 4);
		definePort("joy1Pos", 0);
		definePort("joy2Pos", 1);
		definePort("gearPneumatic1", 7);
		definePort("gearPneumatic2", 8);
		definePort("gearDetector", 7);
		definePort("laser1", 5);
		definePort("encoderYellow", 0);
		definePort("encoderWhite", 1);
		definePort("topClampIn", 1);
		definePort("bottomClampIn", 2);
		definePort("reacherIn", 3);
		definePort("reacherOut", 4);
		definePort("bottomClampOut", 5);
		definePort("topClampOut", 6);
	}

	@Override
	public void robotInit() {

		compressor = new Compressor();
		imu.reset();
		imu.calibrate();
		referanceFrame2.reset();
		referanceFrame2.calabrateGyro();

		SmartDashboard.putNumber("Autonomous #", 1);

		// SmartDashboard.putNumber("Kp", .15);

		/*
		 * //System.out.println("Robot Init"); //referanceFrame2.reset();
		 * //referanceFrame2.start(); //referanceFrame2.calabrateGyro();
		 * //imu.reset(); //imu.calibrate();
		 * //SmartDashboard.putBoolean("Forward?", true);
		 * //SmartDashboard.putNumber("Time?", 10);
		 * //SmartDashboard.putNumber("Speed", .60);
		 * //SmartDashboard.putNumber("Autonomous #", 1);
		 * //SmartDashboard.putString("Event:", "Robot init");
		 * //SmartDashboard.putNumber("Degrees", 90);
		 * //SmartDashboard.putNumber("Count", 0);
		 */
	}

	@Override
	public void disabledInit() {
		compressor.stop();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		referanceFrame2.motorEncoder.reset();
		imu.reset();

		if (SmartDashboard.getNumber("Autonomous #", 0) == 1) {
			autonomousCommand = new AutoOne();
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 2) {
			autonomousCommand = new AutoTwo();
		} else if (SmartDashboard.getNumber("Autonomous #", 0) == 3) {
			autonomousCommand = new AutoThree();
		}
		referanceFrame2.reset();

		autonomousCommand.start();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("MotorEncoderTicks:", referanceFrame2.motorEncoder.get());
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		referanceFrame2.motorEncoder.reset();
		imu.reset();
		referanceFrame2.reset();
		compressor.enabled();

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		SmartDashboard.putNumber("MotorEncoderTicks:", referanceFrame2.motorEncoder.get());
		SmartDashboard.putNumber("GYRO", referanceFrame2.getAngle());

	}

	@Override
	public void testPeriodic() {

	}

	public static Robot getInstance() {
		return instance;
	}

	public int getPort(String name) {
		if (_portMap.containsKey(name) == false)
			throw new RuntimeException("Port \"" + name + "\" is not mapped for " + this.getClass().getName() + ".");
		return _portMap.get(name);
	}

	protected void definePort(String name, int number) {
		_portMap.put(name, number);
	}
}
