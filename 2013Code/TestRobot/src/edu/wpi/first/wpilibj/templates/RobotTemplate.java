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
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    RobotDrive drive;
    Joystick leftStick,rightStick;
    
    int currentMotor = 1;
    
    Jaguar motor1 = new Jaguar(1);
    Jaguar motor2 = new Jaguar(2);
    Jaguar motor3 = new Jaguar(9);
    Jaguar motor4 = new Jaguar(10);
    
    public void robotInit() {
        //drive = new RobotDrive(1, 2, 9, 10);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        System.out.println("Test running!");
        //drive.tankDrive(0.8, 0.8);
        motor1.set(1);
        motor2.set(1);
        motor3.set(1);
        motor4.set(1);
        Timer.delay(1);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
       
    }
    
    /**
     * This function is called periodically during test mode
     */
    
    public void testPeriodic() {
        if (currentMotor == 1) {
            System.out.println("[testPeriodic] Testing motor 1!");
            motor4.set(0);
            motor1.set(1);
            currentMotor = 2;
        } else if (currentMotor == 2) {
            System.out.println("[testPeriodic] Testing motor 2!");
            motor1.set(0);
            motor2.set(1);
            currentMotor = 3;
        } else if (currentMotor == 3) {
            System.out.println("[testPeriodic] Testing motor 3!");
            motor2.set(0);
            motor3.set(1);
            currentMotor = 4;
        } else {
            System.out.println("[testPeriodic] Testing motor 4!");
            motor3.set(0);
            motor4.set(1);
            currentMotor = 1;
        }
        Timer.delay(1);
    }
    
}
