package org.usfirst.frc.team568.robot.powerup;

import org.usfirst.frc.team568.robot.PortMapper;
import org.usfirst.frc.team568.robot.steamworks.ControllerButtons;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class WestCoastDrive extends SubsystemBase {
	private DifferentialDrive drive;
	private Joystick joystick; 

	public WestCoastDrive(PortMapper source) {
		super(source);
		
		SpeedController fl = new Talon(port("leftFrontMotor"));
		SpeedController bl = new Talon(port("leftBackMotor"));
		SpeedController fr = new Talon(port("rightFrontMotor"));
		SpeedController br = new Talon(port("rightBackMotor"));
		
		drive = new DifferentialDrive(new SpeedControllerGroup(fl, bl), new SpeedControllerGroup(fr, br));
		joystick = new Joystick(0);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			
			{
				requires(WestCoastDrive.this);
			}
			
			@Override
			protected void execute() {
				drive.curvatureDrive(joystick.getY(), joystick.getX(), joystick.getRawButton(ControllerButtons.leftBumper));
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
			
		});
	}	

}
