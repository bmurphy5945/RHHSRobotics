/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    RobotDrive drive;
    Joystick leftStick,rightStick;
    double leftValue,rightValue;
    
    public void robotInit() {
        
        drive = new RobotDrive(1,2,3,4); //Spikes use relay (on/off motors, victor/jag is for speed control), set to DigitalModule1 and Relay1
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);

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
         leftValue = leftStick.getY();
         rightValue = rightStick.getY();
        
       
        if(leftStick.getTrigger()) {
           symetricalDrive(leftStick);
        } 
        else if (rightStick.getTrigger())
           drive.arcadeDrive(rightStick);
        
        else {
                drive.tankDrive(leftValue,rightValue);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
     public void symetricalDrive(Joystick stick){
        double offset=0;
        if (leftStick.getTrigger()){
        leftValue = stick.getY()-offset;
        rightValue = stick.getY()+offset;
        
        if(stick.getRawButton(4))
            offset -=.02;
        if(stick.getRawButton(5))
            offset +=.02;
        if(stick.getRawButton(3))
            offset=0;
        
            drive.tankDrive(leftValue,rightValue); //Relay set to forward to move it forward
        }
    }
}
