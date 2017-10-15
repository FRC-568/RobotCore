package org.usfirst.frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public Joystick joyStick1 = new Joystick(RobotMap.joy1Pos);
	public Joystick joyStick3 = new Joystick(RobotMap.joy3Pos);
	public Joystick joyStick2 = new Joystick(RobotMap.joy2Pos);

	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);
	public Button shootSix = new JoystickButton(joyStick2, ControllerButtons.joy2B6);
	public Button shootSeven = new JoystickButton(joyStick2, ControllerButtons.joy2B7);
	public Button shootFour = new JoystickButton(joyStick2, ControllerButtons.joy2B4);
	public Button shootFive = new JoystickButton(joyStick2, ControllerButtons.joy2B5);
	public Button shootEleven = new JoystickButton(joyStick2, ControllerButtons.joy2B11);
	public Button shootOne = new JoystickButton(joyStick2, ControllerButtons.joy2B1);
	public Button shootTwo = new JoystickButton(joyStick2, ControllerButtons.joy2B2);
	public Button shootThree = new JoystickButton(joyStick2, ControllerButtons.joy2B3);

	public Button armsUp = new JoystickButton(joyStick2, ControllerButtons.joy2B10);
	public Button armsDown = new JoystickButton(joyStick2, ControllerButtons.joy2B11);

	public Button trigger = new JoystickButton(joyStick1, ControllerButtons.joy1B1);

	public Button liftFlipper = new JoystickButton(joyStick1, ControllerButtons.liftFlipper);
	public Button lowerFlipper = new JoystickButton(joyStick1, ControllerButtons.lowerFlipper);

	public Button crateOut = new JoystickButton(joyStick1, ControllerButtons.crateOut);
	public Button crateIn = new JoystickButton(joyStick1, ControllerButtons.crateIn);
	public Button liftGo = new JoystickButton(joyStick1, ControllerButtons.lifterGo);
	public Button liftStop = new JoystickButton(joyStick1, ControllerButtons.lifterStop);
	public Button lifterReverse = new JoystickButton(joyStick1, ControllerButtons.lifterReverse);
	public Button Calabrate = new JoystickButton(joyStick3, ControllerButtons.calabrateIMU);
	public Button Reset = new JoystickButton(joyStick3, ControllerButtons.resetIMU);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
}
