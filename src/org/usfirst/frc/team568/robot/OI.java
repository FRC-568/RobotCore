package org.usfirst.frc.team568.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public Joystick leftStick = new Joystick(RobotMap.joy1Pos);
	public Joystick rightStick = new Joystick(RobotMap.joy2Pos);

	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);
	public Button one = new JoystickButton(rightStick, ControllerButtons.shootBallButton);
	public Button two = new JoystickButton(rightStick, ControllerButtons.obtainBallButton);
	public Button three = new JoystickButton(rightStick, ControllerButtons.aimPickUpBallButton);
	public Button four = new JoystickButton(rightStick, ControllerButtons.aimShootLowGoalButton);
	public Button five = new JoystickButton(rightStick, ControllerButtons.aimShootHighGoalButton);

	public Button liftFlipper = new JoystickButton(leftStick, ControllerButtons.liftFlipper);
	public Button lowerFlipper = new JoystickButton(leftStick, ControllerButtons.lowerFlipper);

	public Button crateOut = new JoystickButton(leftStick, ControllerButtons.crateOut);
	public Button crateIn = new JoystickButton(leftStick, ControllerButtons.crateIn);
	public Button liftGo = new JoystickButton(leftStick, ControllerButtons.lifterGo);
	public Button liftStop = new JoystickButton(leftStick, ControllerButtons.lifterStop);
	public Button lifterReverse = new JoystickButton(leftStick, ControllerButtons.lifterReverse);
	public Button Calabrate = new JoystickButton(leftStick, ControllerButtons.calabrateIMU);
	public Button Reset = new JoystickButton(leftStick, ControllerButtons.resetIMU);

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
