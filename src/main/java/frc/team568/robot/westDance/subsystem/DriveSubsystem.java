package frc.team568.robot.westDance.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    //=== motors ===
    private TalonSRX leftMotorLead;
    private TalonSRX leftFollower1;
    private TalonSRX leftFollower2;

    private TalonSRX rightMotorLead;
    private TalonSRX rightFollower1;
    private TalonSRX rightFollower2;

    private int leftMotorPort = 1;
    private int rightMotorPort = 2;

    boolean override = false;

    public DriveSubsystem() {
        leftMotorLead = new TalonSRX(leftMotorPort);
        leftFollower1 = new TalonSRX(leftMotorPort + 1); // Assuming follower 1 is connected to port next to the lead motor
        leftFollower2 = new TalonSRX(leftMotorPort + 2); // Assuming follower 2 is connected to port two next to the lead motor

        rightMotorLead = new TalonSRX(rightMotorPort);
        rightFollower1 = new TalonSRX(rightMotorPort + 1); // Assuming follower 1 is connected to port next to the lead motor
        rightFollower2 = new TalonSRX(rightMotorPort + 2); // Assuming follower 2 is connected to port two next to the lead motor

        // Set follower mode
        leftFollower1.follow(leftMotorLead);
        leftFollower2.follow(leftMotorLead);

        rightFollower1.follow(rightMotorLead);
        rightFollower2.follow(rightMotorLead);

        leftMotorLead.setInverted(true);
        rightMotorLead.setInverted(false);
    }

    /**
     * 
     * @param lSpeed rps
     * @param rSpeed rps
     */
    public void setSpeed(double lSpeed, double rSpeed) {
        leftMotorLead.set(ControlMode.PercentOutput, lSpeed);
        rightMotorLead.set(ControlMode.PercentOutput, rSpeed);
    }

    @Override
    public void periodic() {
        // Update any periodic functionality here
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        // Add any Sendable data if needed
    }
}
