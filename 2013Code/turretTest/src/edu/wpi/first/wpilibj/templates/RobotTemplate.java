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
public class RobotTemplate extends IterativeRobot 
{
    // Initial variables
    RobotDrive drive;
    Joystick leftStick, rightStick, turretStick;
    
    // Turret Motor
    Relay turretMotor;
    
    // Variables for precise turret rotation
    
    // After timing, the delay needed while the turret is on to rotate 180 degrees
    final double SECONDS_FOR_180 = 3.4;
    
    // Calculating the seconds per degree
    double secondsPerDegree = SECONDS_FOR_180 / 180;
    
    // Counters to count to 180 degrees for button press
    int degreeCounterClock = 0;
    int degreeCounterCounterClock = 0;
    
    // Counters to count to SECONDS_FOR_180 for button press
    double secCounterClock = 0;
    double secCounterCounterClock = 0;
    
    // Interval to count up with
    double secInterval = 0.1;
    
    public void rotateClock180() {
        /*if (degreeCounterClock <= 180) {
            turretMotor.set(Relay.Value.kForward);
            Timer.delay(secondsPerDegree);
            degreeCounterClock++;
            System.out.println("rotateClock180: We're counting @ "+degreeCounterClock);
        }*/
        if (secCounterClock <= SECONDS_FOR_180) {
            turretMotor.set(Relay.Value.kForward);
            Timer.delay(secInterval);
            secCounterClock += secInterval;
            System.out.println("rotateClock180: We're counting @ "+secCounterClock);
        }
    }
    public void rotateCounterClock180() {
        /*turretMotor.set(Relay.Value.kReverse);
        Timer.delay(3.4);
        turretMotor.set(Relay.Value.kOff);*/
        /*if (degreeCounterCounterClock <= 180) {
            turretMotor.set(Relay.Value.kForward);
            Timer.delay(secondsPerDegree);
            degreeCounterCounterClock++;
            System.out.println("rotateCounterClock180: We're counting @ "+degreeCounterCounterClock);
        }*/
        if (secCounterCounterClock <= SECONDS_FOR_180) {
            turretMotor.set(Relay.Value.kReverse);
            Timer.delay(secInterval);
            secCounterCounterClock += secInterval;
            System.out.println("rotateCounterClock180: We're counting @ "+secCounterCounterClock);
        }
    }
    
    public void robotInit() 
    {
        turretStick = new Joystick(3);
        turretMotor = new Relay(1);
       
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
        
        /*if (turretStick.getRawButton(4))
            
        {
            System.out.println("Going forward!");
            turretMotor.set(Relay.Value.kOn);
            turretMotor.set(Relay.Value.kForward);
        } 
        
        else if (turretStick.getRawButton(5))
        {
            System.out.println("Going backward!");
            turretMotor.set(Relay.Value.kOn);
            turretMotor.set(Relay.Value.kReverse); 
        }
        
        else  
        {
            System.out.println("Stopped!");
            turretMotor.set(Relay.Value.kOff);
        }*/
        
        if (turretStick.getRawButton(4))
            rotateClock180();
        else {
            //degreeCounterClock = 0;
            secCounterClock = 0;
            turretMotor.set(Relay.Value.kOff);
        }
        if (turretStick.getRawButton(5))
            rotateCounterClock180();
        else {
            //degreeCounterCounterClock = 0;
            secCounterCounterClock = 0;
            turretMotor.set(Relay.Value.kOff);
        }
    }
    
}
