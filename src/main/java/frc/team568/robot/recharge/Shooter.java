package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.DriveBase;
import frc.team568.robot.subsystems.SubsystemBase;

@SuppressWarnings("unused")
public class Shooter extends SubsystemBase {

	// Camera setup

	public static final int CAMERA_WIDTH =  640;
	public static final int CAMERA_HEIGHT =  480;
	public static final double CAMERA_CENTER = CAMERA_HEIGHT / 2;
    public static final double OFFSET_TO_FRONT = 0; //39 1/8; 34 5/8
	public static final double WIDTH_BETWEEN_TARGET = 39.125; //29.375; // inches //TO-DO need to find to width of the vision target
	public static final double HEIGHT_OF_TARGET = 98.25; // height of target in inches
	public static final double DISTANCE_CONSTANT = WIDTH_BETWEEN_TARGET * CAMERA_WIDTH / 0.2361111111 / 2; //5760  // 5738;
	public static final double INITIAL_VELOCITY = 500; //TO-DO find initial velocity in inches per second (by testing?)
	public static final double SHOOTER_RADIUS = 8;
	public static final double SHOOTER_MOUNTED_HEIGHT = 10; //TO-DO find shooter height from ground to edge of shooter
	private static final double GRAVITY = 386.09; // 386.09 inches per second per second

	//calcuation formulas
	private final double RANDIAN_TO_DEGREES = 180 / Math.PI;
	private final double DEGREES_TO_RADIANS = Math.PI / 180;
	
	private final double ENCODER_ZERO_POS = 0;
	private final double TICKS_PER_DEGREE = 10; //TO-DO get tick per degree
	private final double DEGREES_PER_TICK = 1 / TICKS_PER_DEGREE;

	private double shooterHeight;
	private double simulatedHeight = HEIGHT_OF_TARGET - shooterHeight; //calcuate height of target from the shooter height because shooter is off the ground;
	private double distanceFromTarget;
	private double distanceFromCenterY;
	private double calculatedAngle = Math.asin(Math.sqrt(2 * GRAVITY * simulatedHeight) / INITIAL_VELOCITY); // calculated angle in radians
	private double calculatedAngle1;
	private double calculatedAngle2;

	private double v = INITIAL_VELOCITY;
	private double d;
	private double g = GRAVITY;

	private double targetX = d;
	private double targetY;

	//calculations with air resistance
	private double pi = 3.414592654;
	private double e = 2.718281828;
	private double a; //angle
	private double m = 5; // mass in grams
	private double p = 1.225; //density of fluid in kg/m^3
	private double asubv = 0.25; //projected surface area in the y direction
	private double asubh = 0.25; //projecdtd surface areas in the x direction
	private double csubv = 0.47; //drag coefficient in y direction use reference sheet / trial and error
	private double csubh = 0.47; //drag coefficient in the x direction use reference sheet / trial and error

	private double n = (pi / 180) * a;
	private double ksubv = 0.5 * p * asubv * csubv;
	private double ksubh = 0.5 * p * asubh * csubh;
	private double t = (m / (ksubh * v * Math.cos(n))) * (Math.pow(e, ((ksubh * 5))));

	NetworkTable res = NetworkTableInstance.getDefault().getTable("resolution");
	NetworkTable coords = NetworkTableInstance.getDefault().getTable("coordinates");

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

	WPI_TalonSRX shooterL;
	WPI_TalonSRX shooterR;
	VictorSPX wheel;
	WPI_TalonSRX shooterRotator;

	final double INTAKE_SPEED = 0.5;
	final double SHOOT_SPEED = -1;
	final double WHEEL_SPEED = -1;

	// PID setup
	private PIDController pidShooterRotate;
	private double Kp = 0.03;
	private double Ki = 0.003;
	private double Kd = 0.003;

	// PDP
	private PowerDistribution pdp;
	private double shooterLCompensation = 0;
	private double shooterRCompensation = 0;

	// Drivetrain setup
	private DriveBase drive;

	public Shooter(RobotBase robot, DriveBase drive) {
		super(robot);

		this.drive = drive;
		shooterL = new WPI_TalonSRX(configInt("shooterL"));
		shooterR = new WPI_TalonSRX(configInt("shooterR"));
		shooterL.setInverted(true);
		shooterR.setInverted(false);
		shooterL.setSensorPhase(true);
		shooterR.setSensorPhase(true);

		wheel = new VictorSPX(configInt("wheel"));

		shooterRotator = new WPI_TalonSRX(configInt("rotator"));
		shooterRotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);

		pidShooterRotate = new PIDController(Kp, Ki, Kd);

		pdp = new PowerDistribution();

		d = getHorizontalDistanceFromTarget();
		targetY  = getActualHeight();
		shooterHeight = SHOOTER_MOUNTED_HEIGHT + (SHOOTER_RADIUS - SHOOTER_RADIUS * Math.cos(getShooterAngle())); // assumes getShooterAngle to be in radius and units inches

		initDefaultCommand();
		
	}

	public void rotateShooterSpeed(double speed) {

		shooterRotator.set(speed);

	}

	public double calcuateAngle() {
		//angle in degrees
		calculatedAngle1 = Math.atan(Math.pow(v, 2) + Math.sqrt(Math.pow(v, 4) - g * (g * Math.pow(targetX, 2) + 2 * targetY * Math.pow(v, 2))));
		calculatedAngle2 = Math.atan(Math.pow(v, 2) - Math.sqrt(Math.pow(v, 4) - g * (g * Math.pow(targetX, 2) + 2 * targetY * Math.pow(v, 2))));

		//potentialAngle1.setDouble(calculatedAngle1);
		//potentialAngle2.setDouble(calculatedAngle2);
	
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
		//return 47;
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
		// // distance constant divided by length between centers of contours
		// distanceFromTarget = DISTANCE_CONSTANT / getCenterX();
		
		double width = 60; //inches // width of the target box

		boxWidth = coords.getEntry("boxWidth");
		double boxWidthentry = boxWidth.getDouble(-1);
		distanceFromTarget = (width * 333.82) / boxWidthentry; // 333.82 is the focal length of the microsoft lifecam in px
		
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
		return shooterRotator.getSelectedSensorPosition() * DEGREES_PER_TICK;
	}

	public void setShooterZero() {

		pidShooterRotate.reset();
		pidShooterRotate.setSetpoint(ENCODER_ZERO_POS);
		pidShooterRotate.setTolerance(3);

		do 
			wheel.set(ControlMode.PercentOutput, pidShooterRotate.calculate(getShooterAngle())); 
		while (!pidShooterRotate.atSetpoint());
		wheel.set(ControlMode.PercentOutput, 0);

	}

	public void setShooterAngle(double angle) {

		angle *= DEGREES_TO_RADIANS;

		pidShooterRotate.reset();
		pidShooterRotate.setSetpoint(angle);
		pidShooterRotate.setTolerance(3);

		do 
			wheel.set(ControlMode.PercentOutput, pidShooterRotate.calculate(getShooterAngle())); 
		while (!pidShooterRotate.atSetpoint());
		wheel.set(ControlMode.PercentOutput, 0);

	}

	public double getAngle() {
		// double h = (CAMERA_WIDTH / 2) / Math.tan(68.5 / 2);
		// double degreesPerPixel = Math.atan(1 / h);
		// double pixelsPerDegree = 1 / degreesPerPixel;

		// double angleToGoal = getCenterX() * degreesPerPixel;

		double angleToGoal = getCenterX();


		// // 13.3133853031in is for the distance from center to center from goal, then
		// // divide by lengthBetweenCenters in pixels to get proportion
		// double constant = WIDTH_BETWEEN_TARGET / getCenterX();
		// double angleToGoal = 0;

		// // this calculates the distance from the center of goal to center of webcam
		// double distanceFromCenterPixels = ((getCenterX() / 2) - (CAMERA_WIDTH / 2));
		// // Converts pixels to inches using the constant from above.
		// double distanceFromCenterInch = distanceFromCenterPixels * constant;
		// // math brought to you by Chris and Jones
		// angleToGoal = Math.toDegrees(Math.atan(distanceFromCenterInch / distanceFromTarget()));

		return angleToGoal;
	}

	public void initDefaultCommand() {

		setDefaultCommand(new Command() {

			{
				addRequirements(Shooter.this);
			}

			@Override
			public void execute() {
				
				double shooterLCurrent = pdp.getCurrent(configInt("shooterL"));
				double shooterRCurrent = pdp.getCurrent(configInt("shooterR"));

				// Manually move shooter
				if (button("intake")) {

					shooterL.set(INTAKE_SPEED);
					shooterR.set(INTAKE_SPEED);

					wheel.set(ControlMode.PercentOutput, WHEEL_SPEED);

				} else if (button("shoot")) {

					final double COMPENSATION_CHANGE = 0.01;
					final double SHOOTER_L_GOAL = 20;
					final double SHOOTER_R_GOAL = 20;

					if (shooterLCurrent > SHOOTER_L_GOAL)
						shooterLCompensation += COMPENSATION_CHANGE;
					else
						shooterLCompensation -= COMPENSATION_CHANGE;

					if (shooterRCurrent > SHOOTER_L_GOAL)
						shooterRCompensation += COMPENSATION_CHANGE;
					else
						shooterRCompensation -= COMPENSATION_CHANGE;

					shooterL.set(SHOOT_SPEED + shooterLCompensation);
					shooterR.set(SHOOT_SPEED + shooterRCompensation);

					//if ((Math.abs(shooterL.getSelectedSensorVelocity() - SHOOTER_L_GOAL) < 1 && (Math.abs(shooterR.getSelectedSensorVelocity() - SHOOTER_R_GOAL) < 1)))
						wheel.set(ControlMode.PercentOutput, -WHEEL_SPEED);

				} else {
					
					shooterL.set(0);
					shooterR.set(0);
					wheel.set(ControlMode.PercentOutput, 0);
					shooterLCompensation = 0;
					shooterRCompensation = 0;

				}

				shooterRotator.set(axis("rotateShooter") * 0.3);
				
				

			}

		});

	}

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);
		builder.addDoubleProperty("P", () -> Kp, (value) -> Kp = value);
		builder.addDoubleProperty("I", () -> Ki, (value) -> Ki = value);
		builder.addDoubleProperty("D", () -> Kd, (value) -> Kd = value);
		builder.addDoubleProperty("ShooterL Velocity", () -> shooterL.getSelectedSensorVelocity(), null);
		builder.addDoubleProperty("ShooterR Velocity", () -> shooterR.getSelectedSensorVelocity(), null);

	}

	@Override
	public String getConfigName() {
		return "shooter";
	}

}
