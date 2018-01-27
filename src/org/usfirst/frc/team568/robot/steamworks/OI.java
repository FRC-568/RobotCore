package org.usfirst.frc.team568.robot.steamworks;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick joyStick1 = new Joystick(0);
	public Joystick joyStick2 = new Joystick(1);

	public Button climb = new JoystickButton(joyStick2, ControllerButtons.RightBumper);
	public Button inverseClimb = new JoystickButton(joyStick2, ControllerButtons.leftBumper);
	public Button intake = new JoystickButton(joyStick1, ControllerButtons.RightStickIn);
	public Button outtake = new JoystickButton(joyStick1, ControllerButtons.LeftStickIn);
	// public Button stopShooting = new JoystickButton(joyStick2,
	// ControllerButtons.LeftStickIn);
	public Button openGearBox = new JoystickButton(joyStick1, ControllerButtons.A);
	public Button closeGearBox = new JoystickButton(joyStick1, ControllerButtons.B);
	public Button openRopeClamp = new JoystickButton(joyStick2, ControllerButtons.X);
	public Button closeRopeClamp = new JoystickButton(joyStick2, ControllerButtons.Y);

}
