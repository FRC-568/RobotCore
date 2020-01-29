package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;

public class MechyDrive extends SubsystemBase {

	private Joystick joystick;
	private WPI_TalonSRX fl;
	private WPI_TalonSRX bl;
	private WPI_TalonSRX fr;
	private WPI_TalonSRX br;

	public MechyDrive(RobotBase robot) {
		super(robot);

		initMotors();
		joystick = new Joystick(port("mainJoystick"));

		reset();
	}

	@Override
	public void periodic() {
		
	}

	protected void initMotors() {
		
		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		addChild("FL Motor", fl);
		addChild("BL Motor", bl);
		addChild("FR Motor", fr);
		addChild("BR Motor", br);

		fl.setInverted(true);
		bl.setInverted(true);
		fr.setInverted(false);
		br.setInverted(false);
		
		fl.configNominalOutputForward(0, 0);
		fl.configNominalOutputReverse(0, 0);
		fl.configPeakOutputForward(1, 0);
		fl.configPeakOutputReverse(-1, 0);

		fr.configNominalOutputForward(0, 0);
		fr.configNominalOutputReverse(0, 0);
		fr.configPeakOutputForward(1, 0);
		fr.configPeakOutputReverse(-1, 0);
		
	}

	public void stop() {

	}

	public void reset() {
		
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {

			{
				requires(MechyDrive.this);
			}

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {

				// Mecanum drive
				double r = Math.hypot(joystick.getRawAxis(Xinput.LeftStickX), joystick.getRawAxis(Xinput.LeftStickY));
				double robotAngle = Math.atan2(-joystick.getRawAxis(Xinput.LeftStickY), joystick.getRawAxis(Xinput.LeftStickX)) - Math.PI / 4;
				double rightX = joystick.getRawAxis(Xinput.RightStickX);
				final double v1 = -r * Math.cos(robotAngle) - rightX;
				final double v2 = -r * Math.sin(robotAngle) + rightX;
				final double v3 = -r * Math.sin(robotAngle) - rightX;
				final double v4 = -r * Math.cos(robotAngle) + rightX;
				fl.set(v1 * 0.6);
				fr.set(v2 * 0.6);
				bl.set(v3 * 0.6);
				br.set(v4 * 0.6);

			}

			@Override
			protected boolean isFinished() {
				return false;
			}

		}); // End set default command

	} // End init default command

}
