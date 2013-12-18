/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Combine extends IterativeRobot {
    Jaguar rightSide;
    Jaguar leftSide;
    Jaguar shooter;
    
    Relay collect1;
    Relay collect2;
    
    RobotDrive wheels;
    
    Joystick left;
    Joystick right;
    Joystick extra;
    
    double leftValue;
    double rightValue;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    leftSide = new Jaguar(1);
    rightSide = new Jaguar(2);
    shooter = new Jaguar(3);
    
    collect1 = new Relay(1);
    collect2 = new Relay(3);
    
    wheels = new RobotDrive(leftSide, rightSide);
    
    left = new Joystick(1);
    right = new Joystick(3);
    extra = new Joystick(2);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        leftValue = left.getY();
        rightValue = right.getY();
        wheels.tankDrive(leftValue, rightValue);
        
        shooter.set(extra.getY());
        
        if(left.getRawButton(3))
        {
            collect1.set(Relay.Value.kForward);
            collect2.set(Relay.Value.kForward);
        }
        else if(left.getRawButton(2))
        {
            collect1.set(Relay.Value.kOff);
            collect2.set(Relay.Value.kOff);
        }
    }
    
}
