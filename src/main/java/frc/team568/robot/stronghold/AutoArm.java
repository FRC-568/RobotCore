package frc.team568.robot.stronghold;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoArm extends CommandBase {
	private final Timer timer = new Timer();
	private final Arms arm;
	
	public AutoArm(Arms arm) {
		this.arm = arm;
	}

	@Override
	public void initialize() {
		timer.reset();
		timer.start();
	}

	@Override
	public void execute() {
		arm.goDown();
	}

	@Override
	public void end(boolean interrupted) {
		arm.stop();
	}

	@Override
	public boolean isFinished() {
		return timer.hasElapsed(2);
	}

}
