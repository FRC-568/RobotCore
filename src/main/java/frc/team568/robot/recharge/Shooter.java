package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.DriveBase;
import frc.team568.robot.subsystems.SubsystemBase;

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
	public static final double SHOOTER_RADIUS = 8;
	public static final double SHOOTER_MOUNTED_HEIGHT = 10; //TODO find shooter height from ground to edge of shooter
	private static final double GRAVITY = 386.09; // 386.09 inches per second per second
	
	private double shooterHeight = SHOOTER_MOUNTED_HEIGHT + (SHOOTER_RADIUS - SHOOTER_RADIUS * Math.cos(getShooterAngle())); // assumes getShooterAngle to be in radius and units inches
	private double simulatedHeight = HEIGHT_OF_TARGET - shooterHeight; //calcuate height of target from the shooter height because shooter is off the ground;
	private double distanceFromTarget;
	private double distanceFromCenterY;
	private double shooterAngle = 0; //TODO get shooter angle using encoders (in radians)
	//private double calculatedAngle = Math.asin(Math.sqrt(2 * GRAVITY * simulatedHeight) / INITIAL_VELOCITY); // calculated angle in radians
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

	WPI_TalonSRX shooterL;
	WPI_TalonSRX shooterR;
	VictorSPX wheel;
	WPI_TalonSRX shooterRotator;

	final double INTAKE_SPEED = 0.5;
	final double SHOOT_SPEED = -1;
	final double WHEEL_SPEED = 1;
	final double ROTATOR_SPEED = 0.5;

	// PID setup
	private PIDController pidShooterRotate;
	private double Kp = 0.03;
	private double Ki = 0.003;
	private double Kd = 0.003;

	// PDP
	private PowerDistributionPanel pdp;
	private double shooterCompensation = 0;

	// Drivetrain setup
	private DriveBase drive;

	public Shooter(RobotBase robot) {
		super(robot);

		this.drive = robot.getSubsystem(DriveBase.class);
		shooterL = new WPI_TalonSRX(configInt("shooterL"));
		shooterR = new WPI_TalonSRX(configInt("shooterR"));
		shooterL.setInverted(true);
		shooterR.setInverted(false);
		shooterL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		shooterL.setSensorPhase(true);
		shooterR.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		shooterR.setSensorPhase(true);

		wheel = new VictorSPX(configInt("wheel"));

		shooterRotator = new WPI_TalonSRX(configInt("rotator"));

		pidShooterRotate = new PIDController(Kp, Ki, Kd);

		pdp = new PowerDistributionPanel();

		d = getHorizontalDistanceFromTarget();
		targetY  = getActualHeight();

		initDefaultCommand();
		
	}

	public void rotateShooterSpeed(double speed) {

		shooterRotator.set(speed);

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
			wheel.set(ControlMode.PercentOutput, pidShooterRotate.calculate(getShooterAngle())); 
		while (!pidShooterRotate.atSetpoint());
		wheel.set(ControlMode.PercentOutput, 0);

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
		// math brought to you by Chris and Jones
		angleToGoal = Math.toDegrees(Math.atan(distanceFromCenterInch / distanceFromTarget()));

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

					shooterL.set(INTAKE_SPEED);
					shooterR.set(INTAKE_SPEED);

					wheel.set(ControlMode.PercentOutput, WHEEL_SPEED);

				} else if (button("shoot")) {

					final double COMPENSATION_CHANGE = 0.001;
					final double SHOOTER_GOAL = 100;

					if (shooterL.getSelectedSensorVelocity() > shooterR.getSelectedSensorVelocity())
						shooterCompensation -= COMPENSATION_CHANGE;
					else
						shooterCompensation += COMPENSATION_CHANGE;

					shooterL.set(SHOOT_SPEED - shooterCompensation);
					shooterR.set(SHOOT_SPEED + shooterCompensation);

					if ((Math.abs(shooterL.getSelectedSensorVelocity()) < SHOOTER_GOAL) && (Math.abs(shooterR.getSelectedSensorVelocity()) < SHOOTER_GOAL))
						wheel.set(ControlMode.PercentOutput, -WHEEL_SPEED);

				} else {

					shooterL.set(0);
					shooterR.set(0);
					wheel.set(ControlMode.PercentOutput, 0);
					shooterCompensation = 0;

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
