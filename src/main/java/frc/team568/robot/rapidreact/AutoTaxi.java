package frc.team568.robot.rapidreact;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

//created taxi class
public class AutoTaxi extends SequentialCommandGroup {
	protected MecanumSubsystem subsystem;

public AutoTaxi(MecanumSubsystem subsystem) {
		this.subsystem = subsystem;
		this.addCommands(
				new WaitUntilCommand(10),
				new RunCommand(() -> {
					// to move
					/*
					 * subsystem.motorFL.set(1);
					 * subsystem.motorFR.set(1);
					 * subsystem.motorBL.set(1);
					 * subsystem.motorBR.set(1);
					 */
					subsystem.getMecanumDrive().driveCartesian(0, 1, 0);
				}).withTimeout(1), new InstantCommand(() -> {
					// to stop
					/*
					 * subsystem.motorFL.set(0);
					 * subsystem.motorFR.set(0);
					 * subsystem.motorBL.set(0);
					 * subsystem.motorBR.set(0);
					 */
					subsystem.getMecanumDrive().driveCartesian(0, 0, 0);
				}));
		addRequirements(subsystem);
	}

	@Override
	public void end(boolean interrupted) {
	}

	// made motors move for one second (reformated)
	/*
	 * @Override
	 * ublic void execute() {
	 * subsystem.motorFL.set(1);
	 * subsystem.motorFR.set(1);
	 * subsystem.motorBL.set(1);
	 * subsystem.motorBR.set(1);
	 * 
	 * try {
	 * wait(1000);
	 * } catch (InterruptedException e) {
	 * // TODO Auto-generated catch block
	 * e.printStackTrace();
	 * }
	 * subsystem.motorFL.set(0);
	 * subsystem.motorFR.set(0);
	 * subsystem.motorBL.set(0);
	 * subsystem.motorBR.set(0);
	 * 
	 * }
	 */

	@Override
	public void initialize() {
	}

	@Override
	public boolean isFinished() {
		return true;
		// this is a one line comment.
		/*
		 * this is a multiline commment
		 * return false;
		 */
	}
}
