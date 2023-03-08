package frc.team568.robot.chargedup;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;


public class PIDConfig implements Sendable{
	private double entryP, entryI, entryD, entryKs, entryKv;
	private double lastP = -1, lastI = -1, lastD = -1, lastKs = -1, lastKv = -1;

	private PIDConfig(double p, double i, double d, double kS, double kV) {
		this.entryP = p;
		this.entryI = i;
		this.entryD = d;
		this.entryKs = kS;
		this.entryKv = kV;
	}

	public double getP() {
		return entryP;
	}

	public boolean setP(double value) {
		if (value == getP())
			return false;

		entryP = value;
		return true;
	}

	public double getI() {
		return entryI;
	}

	public void setI(double value) {
		entryI = value;
	}

	public double getD() {
		return entryD;
	}

	public void setD(double value) {
		entryD = value;
	}

	public double getKs() {
		return entryKs;
	}

	public void setKs(double value) {
		entryKs = value;
	}

	public double getKv() {
		return entryKv;
	}

	public void setKv(double value) {

		entryKv = value;
	}

	private boolean isDirty() {
		return lastP != getP() || lastI != getI() || lastD != getD() || lastKs != getKs() || lastKv != getKv();
	}

	private void clearDirtyFlag() {
		lastP = getP();
		lastI = getI();
		lastD = getD();
		lastKs = getKs();
		lastKv = getKv();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		
	}
}