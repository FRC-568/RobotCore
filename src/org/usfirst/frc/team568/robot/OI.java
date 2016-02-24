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
	public Joystick shootStick = new Joystick(RobotMap.joy3Pos);

	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);
	public Button shootFour = new JoystickButton(shootStick, ControllerButtons.shootBallButton);
	public Button shootFive = new JoystickButton(shootStick, ControllerButtons.obtainBallButton);
	public Button shootSix = new JoystickButton(shootStick, ControllerButtons.stopShooterButton);
	public Button shootOne = new JoystickButton(shootStick, ControllerButtons.nudge);
	public Button rightTrigger = new JoystickButton(rightStick, ControllerButtons.safty);
	public Button leftTrigger = new JoystickButton(leftStick, ControllerButtons.safty);

	public Button shootTwo = new JoystickButton(shootStick, ControllerButtons.bringShooterDown);
	public Button shootThree = new JoystickButton(shootStick, ControllerButtons.bringShooterUp);

	public Button liftFlipper = new JoystickButton(leftStick, ControllerButtons.liftFlipper);
	public Button lowerFlipper = new JoystickButton(leftStick, ControllerButtons.lowerFlipper);

	public Button crateOut = new JoystickButton(leftStick, ControllerButtons.crateOut);
	public Button crateIn = new JoystickButton(leftStick, ControllerButtons.crateIn);
	public Button liftGo = new JoystickButton(leftStick, ControllerButtons.lifterGo);
	public Button liftStop = new JoystickButton(leftStick, ControllerButtons.lifterStop);
	public Button lifterReverse = new JoystickButton(leftStick, ControllerButtons.lifterReverse);
	public Button Calabrate = new JoystickButton(rightStick, ControllerButtons.calabrateIMU);
	public Button Reset = new JoystickButton(rightStick, ControllerButtons.resetIMU);

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
