package frc.team568.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.cameraserver.CameraServer;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;

public class PanTiltCamera extends SubsystemBase {

	private Joystick joystick;

	private int cameraServerPort = 0;

	public Servo cameraVerticalServo;
	public Servo cameraHorizontalServo;

	public PanTiltCamera(RobotBase robot) {
		super(robot);

		// Joystick
		joystick = new Joystick(port("cameraJoystick"));

		// Camera servo setup
		cameraVerticalServo = new Servo(port("verticalServo"));
		cameraHorizontalServo = new Servo(port("horizontalServo"));
		cameraVerticalServo.set(0.5);
		cameraHorizontalServo.set(0.5);

		CameraServer.getInstance().startAutomaticCapture(cameraServerPort);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {

			{
				requires(PanTiltCamera.this);
			}

			@Override
			protected void initialize() {

			}

			@Override
			protected void execute() {

				// Rotate camera vertically
				cameraVerticalServo.setAngle(cameraVerticalServo.getAngle() + joystick.getRawAxis(Xinput.LeftStickY));

				// Rotate camera horizontally
				cameraHorizontalServo.setAngle(cameraHorizontalServo.getAngle() + joystick.getRawAxis(Xinput.LeftStickX));
			
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

}