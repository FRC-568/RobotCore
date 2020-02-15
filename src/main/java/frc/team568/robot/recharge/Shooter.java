package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.TalonSRXDrive;
import frc.team568.robot.subsystems.SubsystemBase;

public class Shooter extends SubsystemBase {

	// Camera setup

	public static final int CAMERA_WIDTH =  640;
	public static final int CAMERA_HEIGHT =  480;
	public static final double CAMERA_CENTER = CAMERA_HEIGHT / 2;
    public static final double OFFSET_TO_FRONT = 0; //39 1/8; 34 5/8
	public static final double WIDTH_BETWEEN_TARGET = 39.125; //29.375; // inches //TODO need to find to width of the vision target
	public static final double DISTANCE_CONSTANT = WIDTH_BETWEEN_TARGET * CAMERA_WIDTH / 0.2361111111 / 2; //5760  // 5738;

	private double distanceFromTarget;
	private double distanceFromCenterY;
	
	private NetworkTable res = NetworkTableInstance.getDefault().getTable("Resolution");
	private NetworkTable coords = NetworkTableInstance.getDefault().getTable("Coordinates");

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
	
	double resWidth;
	double resHeight;

	// Motor setup

	SpeedControllerGroup shooter;
	VictorSP shooterL;
	VictorSP shooterR;
	WPI_TalonSRX wheel;
	WPI_TalonSRX cheetoRotator;

	final double INTAKE_SPEED = 0.5;
	final double SHOOT_SPEED = 1;
	final double WHEEL_SPEED = 0.5;

	// Drivetrain setup

	Joystick controller0;
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

		cheetoRotator = new WPI_TalonSRX(configInt("rotator"));
	
	}

	public void rotateShooterSpeed(double speed) {

		cheetoRotator.set(speed);

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

		return 0;

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

	public void alignToTarget(RobotBase robot) {
		new JoystickButton(controller0, Xinput.X).whileActive(new ShooterAlignCommand(robot, drive));	
	}

	@Override
	public void initDefaultCommand() {

		setDefaultCommand(new Command() {

			{
				requires(Shooter.this);
			}

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {
				
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

			}

			@Override
			protected boolean isFinished() {
				return false;
			}

		});

	}

}
