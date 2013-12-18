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
public class Mapper extends IterativeRobot {
    Joystick one,two,three;
    
    public void robotInit() {
        one = new Joystick(1);
        two = new Joystick(2);
        three = new Joystick(3);
        


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
        
        for(int i =1; i<=11;i++){
            if(one.getRawButton(i)){
                System.out.println("One: "+ i);
            }
        }
        for(int i =1; i<=11;i++){
            if(two.getRawButton(i)){
                System.out.println("two: "+ i);
            }
        }
        for(int i =1; i<=11;i++){
            if(three.getRawButton(i)){
                System.out.println("three: "+ i);
            }
        }        

        
    }
    
}
