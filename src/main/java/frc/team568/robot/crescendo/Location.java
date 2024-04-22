package frc.team568.robot.crescendo;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public enum Location {
	SPEAKER_TARGET(9, 218.42, 80.44),
	AMP_TARGET(72.5, 323, 35),
	AMP_START(38.06, 279.62, 0),
	CENTER_START(38.06, 148.22, 0),
	SOURCE_START(38.06, 79.22, 0),
	FIELD_CENTER(325.61, 161.62, 0);

	private final Translation3d blueTranslation;

	private Location(double xInches, double yInches, double zInches) {
		this.blueTranslation = new Translation3d(
				Units.inchesToMeters(xInches),
				Units.inchesToMeters(yInches),
				Units.inchesToMeters(zInches));
	}

	private Location(final Translation3d blueTranslation) {
		this.blueTranslation = blueTranslation;
	}

	public Translation3d getTranslation() {
		return getTranslation(DriverStation.getAlliance().orElse(Alliance.Blue));
	}

	public Translation3d getTranslation(Alliance forAlliance) {
		double x = blueTranslation.getX();
		if (forAlliance == Alliance.Red) {
			double cx = FIELD_CENTER.blueTranslation.getX();
			x = cx + cx - x;
		}
		return new Translation3d(x, blueTranslation.getY(), blueTranslation.getZ());
	}
}
