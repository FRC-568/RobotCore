package frc.team568.robot.tankbot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends TimedRobot {
	public DifferentialDrive drive;

	VictorSP fl = new VictorSP(1);
	VictorSP bl = new VictorSP(2);
	VictorSP fr = new VictorSP(3);
	VictorSP br = new VictorSP(4);

	XboxController ds1 = new XboxController(0);

	public Robot() {
		drive = new DifferentialDrive(new SpeedControllerGroup(fl, bl), new SpeedControllerGroup(fr, br));
		drive.setRightSideInverted(false);

		fl.setInverted(false);
		bl.setInverted(false);
		fr.setInverted(true);
		br.setInverted(true);
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void teleopPeriodic() {
		drive.tankDrive(-ds1.getY(Hand.kLeft), -ds1.getY(Hand.kRight));
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {
	}

}
