package org.usfirst.frc.team568.robot.commands;

import org.usfirst.frc.team568.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Autonomous extends CommandGroup {

	public Autonomous() {
		if (Robot.getInstance().whichOne == 1 || Robot.getInstance().whichOne == 2 || Robot.getInstance().whichOne == 3
				|| Robot.getInstance().whichOne == 4 || Robot.getInstance().whichOne == 5) {
			addSequential(new GoForwardToObsticles());
			if (Robot.getInstance().over == true) {

				if (Robot.getInstance().whichOne == 1) {

				} else if (Robot.getInstance().whichOne == 2) {

				}

				else if (Robot.getInstance().whichOne == 3) {

				}

				else if (Robot.getInstance().whichOne == 4) {

				}

				else if (Robot.getInstance().whichOne == 5) {
				}
			} else {
				addSequential(new Stop());
			}
		} else {
			addSequential(new Stop());
		}

	}
}