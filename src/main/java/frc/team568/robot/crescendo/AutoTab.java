package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoTab {
	
	public SendableChooser<Command> chooser = new SendableChooser<>();
	
	public AutoTab(RobotContainer container) {
		//var dSolenoid = container.dSolenoid;

		//Command downpneumatic = new DownPneumatic(dSolenoid);
		//Command uppneumatic = new UpPneumatic(dSolenoid);
		
		chooser.setDefaultOption("Wait", null);
		//chooser.addOption("Down Pneumatic", downpneumatic);
		//chooser.addOption("Up Pneumatic", uppneumatic);

		OI.autoTab.add("Auto Program", chooser);

	}
}
