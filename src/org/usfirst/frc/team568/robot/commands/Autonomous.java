package org.usfirst.frc.team568.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Autonomous extends CommandGroup {

	public Autonomous() {

	}
}
/*
 * if (Robot.getInstance().whichOne == 1 || Robot.getInstance().whichOne == 2 ||
 * Robot.getInstance().whichOne == 3 || Robot.getInstance().whichOne == 4 ||
 * Robot.getInstance().whichOne == 5) { addSequential(new
 * GoForwardToObsticles(5)); if (Robot.getInstance().over == true) {
 * 
 * if (Robot.getInstance().whichOne == 1) { addSequential(new
 * GoForwardToObsticles(3));
 * 
 * } else if (Robot.getInstance().whichOne == 2) { addSequential(new
 * GoForwardToObsticles(4)); }
 * 
 * else if (Robot.getInstance().whichOne == 3) { addSequential(new
 * GoForwardToObsticles(6)); }
 * 
 * else if (Robot.getInstance().whichOne == 4) { addSequential(new
 * GoForwardToObsticles(1)); }
 * 
 * else if (Robot.getInstance().whichOne == 5) { addSequential(new
 * GoForwardToObsticles(2)); } } else { addSequential(new Stop()); } } else {
 * addSequential(new Stop()); }
 * 
 * } }
 */