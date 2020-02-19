package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;
import frc.team568.robot.subsystems.TalonSRXDrive;

public class Shooter extends SubsystemBase {

	// Camera setup

	public static final int CAMERA_WIDTH =  640;
	public static final int CAMERA_HEIGHT =  480;
	public static final double CAMERA_CENTER = CAMERA_HEIGHT / 2;
    public static final double OFFSET_TO_FRONT = 0; //39 1/8; 34 5/8
	public static final double WIDTH_BETWEEN_TARGET = 39.125; //29.375; // inches //TODO need to find to width of the vision target
	public static final double HEIGHT_OF_TARGET = 98.25; // height of target in inches
	public static final double DISTANCE_CONSTANT = WIDTH_BETWEEN_TARGET * CAMERA_WIDTH / 0.2361111111 / 2; //5760  // 5738;
	public static final double INITIAL_VELOCITY = 100; //TODO find initial velocity in inches per second (by testing?)
	public static final double SHOOTER_RADIUS = 10; //TODO find shooter radius in inches
	public static final double SHOOTER_MOUNTED_HEIGHT = 10; //TODO find shooter height from ground to edge of shooter
	private static final double GRAVITY = 386.09; // 386.09 inches per second per second
	
	private double shooterHeight = SHOOTER_MOUNTED_HEIGHT + (SHOOTER_RADIUS - SHOOTER_RADIUS * Math.cos(getShooterAngle())); // assumes getShooterAngle to be in radius and units inches
	private double simulatedHeight = HEIGHT_OF_TARGET - shooterHeight; //calcuate height of target from the shooter height because shooter is off the ground;
	private double distanceFromTarget;
	private double distanceFromCenterY;
	private double shooterAngle = 0; //TODO get shooter angle using encoders (in radians)
	private double calculatedAngle = Math.asin(Math.sqrt(2 * GRAVITY * simulatedHeight) / INITIAL_VELOCITY); // calculated angle in radians
	private double calculatedAngle1;
	private double calculatedAngle2;

	private double v = INITIAL_VELOCITY;
	private double d = getHorizontalDistanceFromTarget();
	private double g = GRAVITY;

	private double targetX = d;
	private double targetY  = getActualHeight();

	NetworkTable res = NetworkTableInstance.getDefault().getTable("Resolution");
	NetworkTable coords = NetworkTableInstance.getDefault().getTable("Coordinates");

	NetworkTableEntry resX;
	NetworkTableEntry resY;
	
	NetworkTableEntry ymin;
	NetworkTableEntry ymax;
	NetworkTableEntry xmin;
	NetworkTableEntry xmax;

	NetworkTableEntry centerX;
	NetworkTableEntry centerY;

	NetworkTableEntry boxWidth;
	NetworkTableEntry boxHeight;
	
	NetworkTable angleData = NetworkTableInstance.getDefault().getTable("Angle Calculation Data");
	NetworkTableEntry optimalAngleEntry = angleData.getEntry("Optimal Shooting Angle");
	NetworkTableEntry potentialAngle1 = angleData.getEntry("Potential Angle 1");
	NetworkTableEntry potentialAngle2 = angleData.getEntry("Potential Angle 2");
	
	double resWidth;
	double resHeight;

	// Motor setup

	SpeedControllerGroup shooter;
	VictorSP shooterL;
	VictorSP shooterR;
	WPI_TalonSRX wheel;
	WPI_TalonSRX shooterRotator;

	final double INTAKE_SPEED = 0.5;
	final double SHOOT_SPEED = 1;
	final double WHEEL_SPEED = 0.5;
	final double ROTATOR_SPEED = 0.2;

	// PID setup
	private PIDController pidShooterRotate;
	private double Kp = 0.03;
	private double Ki = 0.003;
	private double Kd = 0.003;

	// Drivetrain setup

	TalonSRXDrive drive;

	public Shooter(RobotBase robot) {

		super(robot);
		drive = new TalonSRXDrive(robot);

		shooterL = new VictorSP(configInt("shooterL"));
		shooterR = new VictorSP(configInt("shooterR"));
		shooterL.setInverted(true);
		shooterR.setInverted(false);
		shooter = new SpeedControllerGroup(shooterL, shooterR);
		
		wheel = new WPI_TalonSRX(configInt("wheel"));

		shooterRotator = new WPI_TalonSRX(configInt("rotator"));

		pidShooterRotate = new PIDController(Kp, Ki, Kd);

		initDefaultCommand();
	}
	
	public void rotateShooterSpeed(double speed) {

		shooterRotator.set(speed);

	}

	public void shootShooter() {

	}

	public double calcuateAngle() {
		//angle in degrees
		calculatedAngle1 = Math.atan(Math.pow(v, 2) + Math.sqrt(Math.pow(v, 4) - g * (g * Math.pow(targetX, 2) + 2 * targetY * Math.pow(v, 2))));
		calculatedAngle2 = Math.atan(Math.pow(v, 2) - Math.sqrt(Math.pow(v, 4) - g * (g * Math.pow(targetX, 2) + 2 * targetY * Math.pow(v, 2))));

		potentialAngle1.setDouble(calculatedAngle1);
		potentialAngle2.setDouble(calculatedAngle2);
	
		if(calculatedAngle1 < 0 || calculatedAngle1 > 90) {
			optimalAngleEntry.setDouble(-100);
			return -100;
		} if (calculatedAngle2 < 0 || calculatedAngle2 > 90) {
			optimalAngleEntry.setDouble(-100);
			return -100;

		} else {
			if(calculatedAngle1 < calculatedAngle2) {
				optimalAngleEntry.setDouble(calculatedAngle1);
	
				return calculatedAngle1; 

			} else {
				optimalAngleEntry.setDouble(calculatedAngle2);
	
				return calculatedAngle2;
			}
		}
		
	}
	
	public double getHorizontalDistanceFromTarget() {
		return Math.cos(getShooterAngle()) * distanceFromTarget();
	}

	public double getActualHeight() {
		return simulatedHeight;
	}

	public double getResX() {
		resY = res.getEntry("Width");
		resWidth = resX.getDouble(-1);
		
		return resWidth;
	}

	public double getResY() {
		resX = res.getEntry("Height");
		resHeight = resY.getDouble(-1);
		
		return resHeight;
	}

	public double getCenterX() {
		centerX = coords.getEntry("centerX");
		double returnCenterX = centerX.getDouble(-1);
		
		return returnCenterX;
	}

	public double getCenterY() {
		centerY = coords.getEntry("centerX");
		double returnCenterY = centerY.getDouble(-1);

		return returnCenterY;
	}

	public double distanceFromTarget() {
		// distance constant divided by length between centers of contours
		distanceFromTarget = DISTANCE_CONSTANT / getCenterX();
		return distanceFromTarget - OFFSET_TO_FRONT;
	}

	public double distanceFromYPixels() {
		
		if(getCenterY() > CAMERA_CENTER) {
			distanceFromCenterY = getCenterY() - CAMERA_CENTER;
		} else {
			distanceFromCenterY = CAMERA_CENTER - getCenterY();
		}
 		
		return distanceFromCenterY;
	}

	public double getShooterAngle() {
		return shooterAngle;
	}

	public void setShooterAngle(double angle) {

		pidShooterRotate.reset();
		pidShooterRotate.setSetpoint(angle);
		pidShooterRotate.setTolerance(5);

		do 
			wheel.set(pidShooterRotate.calculate(getShooterAngle())); 
		while (!pidShooterRotate.atSetpoint());
		wheel.set(0);

	}

	public double getAngle() {
		// 13.3133853031in is for the distance from center to center from goal, then
		// divide by lengthBetweenCenters in pixels to get proportion
		double constant = WIDTH_BETWEEN_TARGET / getCenterX();
		double angleToGoal = 0;

		// this calculates the distance from the center of goal to center of webcam
		double distanceFromCenterPixels = ((getCenterX() / 2) - (CAMERA_WIDTH / 2));
		// Converts pixels to inches using the constant from above.
		double distanceFromCenterInch = distanceFromCenterPixels * constant;
		// math brought to you buy Chris and Jones
		angleToGoal = Math.atan(distanceFromCenterInch / distanceFromTarget());
		angleToGoal = Math.toDegrees(angleToGoal);
		// prints angle
		// System.out.println("Angle: " + angleToGoal);
			
		//SmartDashboard.putNumber("angleToGoal", angleToGoal);
		return angleToGoal;
	}

	public void initDefaultCommand() {

		setDefaultCommand(new CommandBase() {

			{
				addRequirements(Shooter.this);
			}

			@Override
			public void execute() {
				
				// Manually move shooter
				if (button("intake")) {

					shooter.set(INTAKE_SPEED);
					wheel.set(WHEEL_SPEED);

				} else if (button("shoot")) {

					shooter.set(SHOOT_SPEED);
					wheel.set(WHEEL_SPEED);

				} else {

					shooter.set(0);
					wheel.set(0);

				}

				if (button("rotateShooterUp"))
					shooterRotator.set(ROTATOR_SPEED);
				else if (button("rotateShooterDown"))
					shooterRotator.set(-ROTATOR_SPEED);
				else
					shooterRotator.set(0);

			}

		});

	}

}
