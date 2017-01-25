package org.usfirst.frc.team568.util;

public class PID {
	/*
	 * double Pan; double Tilt; double BoxSize; double KI; double KP; double KD;
	 * double TiltKP; double TiltKD; double TiltKI; double ErrSum; double Err2;
	 * double Err; double Pow; boolean LL; boolean LR; double tiltErr; double
	 * tiltErr2; double tiltPow;
	 */
	// AimingPID aim;

	/*
	 * double Pan; double Tilt; double BoxSize;
	 * 
	 * double KI; double KP; double KD; double TiltKP; double TiltKD; double
	 * TiltKI; double ErrSum; double Err2; double Err; double Pow; boolean LL;
	 * boolean LR;
	 * 
	 * double tiltErr; double tiltErr2; double tiltPow;
	 */
	// aim = new AimingPID(0.001, 0, 0);
	/*
	 * SmartDashboard.putNumber("P", 2.00); SmartDashboard.putNumber("I",
	 * 0.700); SmartDashboard.putNumber("D", 0);
	 * SmartDashboard.putNumber("TP", 5.000); SmartDashboard.putNumber("TI",
	 * 0); SmartDashboard.putNumber("TD", 0);
	 * SmartDashboard.putNumber("encoderValue", encoder.getDistance());
	 */
	// SmartDashboard.putNumber("Heading", referanceFrame2.getAngle());
			/*
			 * time.reset(); time.start(); if (SmartDashboard.getNumber(
			 * "Autonomous #") == 1) {
			 * 
			 * if (time.get() < .75) { shooter.rightTilt.set(-.5);
			 * shooter.leftTilt.set(.5); } else { shooter.rightTilt.set(0);
			 * shooter.leftTilt.set(0); } if (time.get() < 10) {
			 * 
			 * arcadeDrive.forwardWithGyro(.6); } else { arcadeDrive.halt(); }
			 * 
			 * } else if (SmartDashboard.getNumber("Autonomous #") == 2) { if
			 * (time.get() < 10) { arcadeDrive.goBackwards(.6); } else {
			 * arcadeDrive.halt(); }
			 * 
			 * }
			 */
	// SmartDashboard.putNumber("POS X", referanceFrame2.getPos().x);
			// SmartDashboard.putNumber("POS Y", referanceFrame2.getPos().y);
			/// SmartDashboard.putNumber("Heading", referanceFrame2.getHeading());
			// SmartDashboard.putNumber("Acel Y", referanceFrame2.getAcel().y);
			// SmartDashboard.putNumber("Acel X", referanceFrame2.getAcel().x);
			// SmartDashboard.putNumber("Encoder", encoder.getDistance());
			/*
			 * KP = SmartDashboard.getNumber("P"); KI =
			 * SmartDashboard.getNumber("I"); KD = SmartDashboard.getNumber("D");
			 * TiltKP = SmartDashboard.getNumber("TP"); TiltKI =
			 * SmartDashboard.getNumber("TI"); TiltKD =
			 * SmartDashboard.getNumber("TD");
			 */
	/*
	 * SmartDashboard.putNumber("POS X", referanceFrame2.getPos().x);
	 * SmartDashboard.putNumber("POS Y", referanceFrame2.getPos().y);
	 * SmartDashboard.putNumber("Heading", referanceFrame2.getHeading());
	 * SmartDashboard.putNumber("Acel Y", referanceFrame2.getAcel().y);
	 * SmartDashboard.putNumber("Acel X", referanceFrame2.getAcel().x);
	 * SmartDashboard.putNumber("Encoder", encoder.getDistance());
	 * 
	 * KP = SmartDashboard.getNumber("P"); KI =
	 * SmartDashboard.getNumber("I"); KD = SmartDashboard.getNumber("D");
	 * TiltKP = SmartDashboard.getNumber("TP"); TiltKI =
	 * SmartDashboard.getNumber("TI"); TiltKD =
	 * SmartDashboard.getNumber("TD");
	 */

	/*
	 * NetworkTable server = NetworkTable.getTable("SmartDashboard"); try {
	 * System.out.println(server.getNumber("COG_X", 0.0));
	 * System.out.println(server.getNumber("COG_Y", 0.0)); } catch
	 * (TableKeyNotDefinedException ex) {
	 * 
	 * }
	 * 
	 * if (rightStick.getRawButton(3)) { Pan =
	 * SmartDashboard.getNumber("COG_X"); Tilt =
	 * SmartDashboard.getNumber("COG_Y"); BoxSize =
	 * SmartDashboard.getNumber("COG_BOX_SIZE"); if (Pan > 0) { Err2 = Err;
	 * Err = Pan - 320; ErrSum += Err; if (ErrSum > 1000) ErrSum = 1000; if
	 * (ErrSum < -1000) ErrSum = -1000; Pow = Err * (KP / 1000) + Pow * (KI
	 * / 1000) + (Err - Err2) * (KD / 1000);// Pos // turn
	 * Robot.tankDrive(-Pow, Pow); if (Err < 0) { LL = false; LR = true; }
	 * else { LL = true; LR = false; } SmartDashboard.putNumber("ERR", Err);
	 * SmartDashboard.putNumber("Pow", Pow);
	 * SmartDashboard.putNumber("Size", BoxSize);
	 * 
	 * if (Math.abs(Err) < 100) { // ErrSum = 0; if (BoxSize < 100)
	 * DarwinsRobot.tankDrive(-.5, -.5);
	 * 
	 * tiltErr = 240 - Tilt; tiltErr2 = tiltErr; tiltPow = tiltErr * (TiltKP
	 * / 1000) + tiltPow * (TiltKI / 1000)+ (tiltErr - tiltErr2) * (TiltKD /
	 * 1000); bob.set(tiltPow); sam.set(tiltPow);
	 * SmartDashboard.putNumber("TiltPow", tiltPow);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * else { bob.set(0); sam.set(0); if (LL) { DarwinsRobot.tankDrive(-.55,
	 * .55); } if (LR) { DarwinsRobot.tankDrive(.55, -.55); } }
	 * 
	 * }
	 */
	// NIVision.IMAQdxStopAcquisition(session);


}
