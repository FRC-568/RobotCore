
package frc.team568.robot.deepspace;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.team568.robot.subsystems.DriveBase;
import frc.team568.robot.subsystems.SubsystemBase;
import frc.team568.robot.RobotBase;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team568.robot.Xinput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Drive extends DriveBase {
	
	private static final int lMotorPort = 1;
	private static final int rMotorPort = 2;

	public DifferentialDrive drive;
	public Joystick controller1;	

	DoubleSolenoid solenoidShifter;

	boolean isShifted;
	boolean shiftIsHeld;

	DriveTrain2019 driveTrain;
	SubsystemBase subSystem;

	public Drive(final RobotBase robot) {
		super(robot);

		
	}

	public void initDrive() {
		WPI_TalonSRX leftMotor = new WPI_TalonSRX(lMotorPort);	
		WPI_TalonSRX Rightmotor = new WPI_TalonSRX(rMotorPort);

		controller1 = new Joystick(0);

		drive = new DifferentialDrive(leftMotor, Rightmotor);

		leftMotor.setInverted(true);
		Rightmotor.setInverted(true);
	}
	
	public void gearShifter() {
		if(controller1.getRawButton(Xinput.Y) ) {
			if(!shiftIsHeld) {
				if(isShifted) {
					solenoidShifter.set(DoubleSolenoid.Value.kReverse); //high gear
				} else {			
					solenoidShifter.set(DoubleSolenoid.Value.kForward); //low gear
				}
				isShifted = !isShifted;
			}
			shiftIsHeld = true;
		} else {
			shiftIsHeld = false;
		}		
	}

	@Override
	public void resetSensors() {
		
	}
	@Override
	public void tankDrive(double leftValue, double rightValue, boolean squareInputs) {
		tankDrive(controller1.getRawAxis(1), controller1.getRawAxis(5), squareInputs);
	}
	@Override
	public void arcadeDrive(double moveValue, double rotateValue, boolean squareInputs) {
		arcadeDrive(moveValue, rotateValue, squareInputs);
	}

	public void driveArcade() {
		arcadeDrive(controller1.getRawAxis(1), controller1.getRawAxis(5), false);
	}

	public void driveTank() {
		tankDrive(controller1.getRawAxis(1), controller1.getRawAxis(5), false);
	}

	public void driveToTapeCommand() {
		new JoystickButton(controller1, Xinput.X).whileHeld(new TapeTrackerCommand(driveTrain));
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			
			{
				requires(Drive.this);
			}
			
			@Override
			protected void initialize() {
				System.out.println("Starting Default Command");
			}

			@Override
			protected void execute() {
				gearShifter();
				driveTank();
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

	@Override
	public double getDistance() {
		return 0;
	}

	@Override
	public double getDistance(Side side) {
		return 0;
	}

	@Override
	public double getHeading() {
		return 0;
	}

	@Override
	public double getVelocity() {
		return 0;
	}

	@Override
	public double getVelocity(Side side) {
		return 0;
	}
}