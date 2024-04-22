package frc.team568.robot.westDance.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    //=== motors ===
    private TalonSRX leftMotorLead;
    private TalonSRX leftFollower1;

    private TalonSRX rightMotorLead;
    private TalonSRX rightFollower1;

    private int leftMotorPort = 1;
    private int rightMotorPort = 2;

    boolean override = false;

    public DriveSubsystem() {
        leftMotorLead = new TalonSRX(leftMotorPort);
        leftFollower1 = new TalonSRX(leftMotorPort + 1); // Assuming follower 1 is connected to port next to the lead motor

        rightMotorLead = new TalonSRX(rightMotorPort);
        rightFollower1 = new TalonSRX(rightMotorPort + 1); // Assuming follower 1 is connected to port next to the lead motor

        // Set follower mode
        leftFollower1.follow(leftMotorLead);
        rightFollower1.follow(rightMotorLead);

        leftMotorLead.setInverted(true);
        rightMotorLead.setInverted(false);
    }

    /**
     * 
     * @param lSpeed rps
     * @param rSpeed rps
     */
    public void setSpeed(double drive, double turn) {
        double left = drive + turn;
        double right = drive - turn;
        var max = Math.max(Math.abs(left), Math.abs(right));
        if(max > 1){
            left /= max;
            right /= max;
        }
        
        leftMotorLead.set(ControlMode.PercentOutput, left);
        rightMotorLead.set(ControlMode.PercentOutput, right);
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
