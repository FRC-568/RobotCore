package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

final class OI {
	static final CommandXboxController driverController = new CommandXboxController(0);
	
	static final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");

	static final class Button{
		//public static final Trigger pneumaticup = driverController.leftBumper();
		//public static final Trigger pneumaticdown = driverController.rightBumper();
		public static final Trigger pneumaticsubsystem = driverController.rightBumper();
		//public static final Trigger test = driverController.x();
	}
}
