package frc.team568.robot.powerup;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.team568.robot.Xinput;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick joyStick1 = new Joystick(0);
	public Joystick joyStick2 = new Joystick(1);
	public Joystick arcadeBox = new Joystick(2);

	public Button blockIn = new JoystickButton(joyStick1, Xinput.A);
	public Button blockOut = new JoystickButton(joyStick1, Xinput.B);
	public Button blockGrab = new JoystickButton(joyStick1, Xinput.X);
	public Button blockSpinR = new JoystickButton(joyStick1, Xinput.Y);

	public Button blockOut2 = new JoystickButton(joyStick2, Xinput.B);
	public Button blockIn2 = new JoystickButton(joyStick2, Xinput.A);

	public Button armOut = new JoystickButton(joyStick1, Xinput.RightBumper);
	public Button armIn = new JoystickButton(joyStick1, Xinput.LeftBumper);

	public Button liftUp = new JoystickButton(joyStick2, Xinput.X);
	public Button liftDown = new JoystickButton(joyStick2, Xinput.Y);

	public Button climb = new JoystickButton(joyStick2, Xinput.LeftBumper);
	public Button unClimb = new JoystickButton(joyStick2, Xinput.RightBumper);

	public Button climbA = new JoystickButton(arcadeBox, 12);
	public Button unClimbA = new JoystickButton(arcadeBox, 11);
	public Button liftUpA = new JoystickButton(arcadeBox, 7);
	public Button liftDownA = new JoystickButton(arcadeBox, 10);
	public Button blockLiftIn = new JoystickButton(arcadeBox, 8);
	public Button blockLiftOut = new JoystickButton(arcadeBox, 9);

}
