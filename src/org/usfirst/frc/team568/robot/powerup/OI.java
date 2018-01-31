package org.usfirst.frc.team568.robot.powerup;

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

	public Button intake = new JoystickButton(joyStick1, ControllerButtons.RightStickIn);
	public Button outtake = new JoystickButton(joyStick1, ControllerButtons.LeftStickIn);

}
