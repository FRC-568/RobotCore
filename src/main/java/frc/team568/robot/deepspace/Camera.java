package frc.team568.robot.deepspace;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Servo;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.Joystick;

public class Camera extends SubsystemBase {

	private Joystick joystick;

	public Servo cameraVerticalServo;
	public Servo cameraHorizontalServo;

	public Camera(RobotBase robot) {
		super(robot);

		// Joystick
		joystick = new Joystick(port("cameraJoystick"));

		// Camera servo setup
		cameraVerticalServo = new Servo(port("verticalServo"));
		cameraHorizontalServo = new Servo(port("horizontalServo"));
		cameraVerticalServo.set(0.5);
		cameraHorizontalServo.set(0.5);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {

			{
				requires(Camera.this);
			}

			@Override
			protected void initialize() {

			}

			@Override
			protected void execute() {

				// Rotate camera vertically
				cameraVerticalServo.setAngle(cameraVerticalServo.getAngle() + joystick.getRawAxis(ControllerIDs.LeftStickY));

				// Rotate camera horizontally
				cameraHorizontalServo.setAngle(cameraHorizontalServo.getAngle() + joystick.getRawAxis(ControllerIDs.LeftStickX));
			
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

}