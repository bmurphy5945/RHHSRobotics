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
    
    Joystick RobotDrive = new Joystick(1);
    RobotDrive drive;
    double leftValue,rightValue;
    Joystick leftStick,rightStick,turretStick;
    
    public void robotInit() 
    {
    leftStick = new Joystick(1);
    rightStick = new Joystick(2);
    
    drive = new RobotDrive(1,2,3,4);
    
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
            if (leftStick.getRawButton(8)) 
            {
            drive.tankDrive(leftStick.getY(), leftStick.getY());
        }
        else
        {
            drive.tankDrive(leftStick.getY(), rightStick.getY());
        }
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
