/*
 * RobotSeeRobotDo - code for adjusting the turret to align with the targets
 * Copyright (C) 2013 Albert Huang
 * Based on the IterativeRobot template and the 2013 Vision Sample Project
 * by FIRST, (C) 2008, FIRST BSD license
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDo;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotSeeRobotDo extends IterativeRobot {
    // Initialize the AxisCamera object
    AxisCamera camera;
    
    /*************************************************************************
     * Robot control variables
     *************************************************************************/
    // Initial variables
    RobotDrive drive;
    Joystick leftStick, rightStick, turretStick;
    
    // Turret Motor
    Jaguar turretMotor;
    
    // Shooter elevator motors
    Relay shooterElevatorMotor1, shooterElevatorMotor2;
    
    // Frisbee push motor (subject to change)
    Relay frisbeePushMotor;
    
    // Shooter motors
    Jaguar shooterMotor1, shooterMotor2;
    
    Joystick xboxShooter;
    
    // Variables for precise turret rotation
    
    // After timing, the delay needed while the turret is on to rotate 180 degrees
    final double SECONDS_FOR_180 = 3.4;
    
    // Counters to count to SECONDS_FOR_180 for button press
    double secCounterClock = 0;
    double secCounterCounterClock = 0;
    
    // Interval to count up with
    double secInterval = 0.1;
    
    // Initialize the VisionTarget class
    VisionTarget vision;
    
    // Initialize the VisionAim class
    VisionAim aim;
    
    // Initialize the FrisbeeShooter class
    FrisbeeShooter frisbeeShooter;
    
    // Are we shooting or not? This is used to run the shooting code.
    boolean shooting = false;
    
    public void robotInit() 
    {
        // Initialize the Turret Joystick
        //turretStick = new Joystick(3);
        xboxShooter = new Joystick(3);
        // Initialize the turret motor at Relay 1
        turretMotor = new Jaguar(7);
        
        // Initialize the shooter elevator motors at Relay 2 and 3
        shooterElevatorMotor1 = new Relay(1);
        shooterElevatorMotor2 = new Relay(8);
        
        // Initialize the frisbee pushing motor at PWM 4
        frisbeePushMotor = new Relay(7);
    
        // Initialize the actual shooter motors at PWM 5 and 6
        shooterMotor1 = new Jaguar(5);
        shooterMotor2 = new Jaguar(6);
        
        // Initialize the camera by getting an instance of it
        camera = AxisCamera.getInstance();
        camera.writeRotation(AxisCamera.RotationT.k180);
        
        // Set the threshold for light green
        //HSVThreshold thres = new HSVThreshold(103, 124, 225, 255, 234, 255, true);
        // GREEN: HSVThreshold thres = new HSVThreshold(86, 119, 61, 255, 131, 255, true);
        HSVThreshold thres = new HSVThreshold(157, 176, 114, 255, 110, 255, true);
        vision = new VisionTarget(camera, thres);
        // OR using a custom threshold:
        // HSVThreshold thres = new HSVThreshold(0, 0, 60, 0, 0, 100);
        // vision = new VisionTarget(camera, thres);
        // Can be updated with vision.changeThreshold(newHSVThresholdObj)
        
        aim = new VisionAim(vision, turretMotor, shooterElevatorMotor1, shooterElevatorMotor2);
        // OR specifying a x and y offset:
        // aim = new VisionAim(vision, turretMotor, shooterElevatorMotor1, shooterElevatorMotor2, -0.5, 0.1);
        
        frisbeeShooter = new FrisbeeShooter(aim, shooterMotor1, shooterMotor2, frisbeePushMotor);
    }
    
    public void rotateClock180() {
        if (secCounterClock <= SECONDS_FOR_180) {
            turretMotor.set(1);
            Timer.delay(secInterval);
            secCounterClock += secInterval;
            System.out.println("rotateClock180: We're counting @ "+secCounterClock);
        }
    }
    
    public void rotateCounterClock180() {
        if (secCounterCounterClock <= SECONDS_FOR_180) {
            turretMotor.set(-1);
            Timer.delay(secInterval);
            secCounterCounterClock += secInterval;
            System.out.println("rotateCounterClock180: We're counting @ "+secCounterCounterClock);
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    
    public void autonomousPeriodic() 
    {
        frisbeeShooter.shoot();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        // Driving code
        if (leftStick.getTrigger())
            // Just pass leftStick.getY() to both arguments so that both sides get
            // the same value, hence forward/backward with one stick.
            // Technically, you could do:
            // drive.tankDrive(leftStick, leftStick);
            // ...but this makes more sense. :P DEAL WITH IT!!!!1!
            drive.tankDrive(leftStick.getY(), leftStick.getY());
        else
            drive.tankDrive(leftStick.getY(), rightStick.getY());
        
        // Shooting code
        if (turretStick.getTrigger())
            shooting = true;
        if (shooting)
            // The statement inside the if() will always execute (assuming
            // that shooting is true), so we can just stick that inside
            // the if statement, WITHOUT creating a new boolean to store
            // the result, to check whether shooting is done or not.
            // Optimizations are cool :)
            // 
            // Here, we use shootNB(), the non-blocking version of the
            // shooting code, since we still want to be able to drive.
            if (frisbeeShooter.shootNB())
                shooting = false;
        
        // Manual turret rotation code
        if (turretStick.getRawButton(4))
            rotateClock180();
        else {
            secCounterClock = 0;
            turretMotor.set(0);
        }
        if (turretStick.getRawButton(5))
            rotateCounterClock180();
        else {
            secCounterCounterClock = 0;
            turretMotor.set(0);
        }
        
        // Manual shooting code
        if (turretStick.getRawButton(6))
            frisbeeShooter.actuallyShoot();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {        
        aim.aimForTarget();
        turretMotor.set((.5)*(xboxShooter.getZ()));
         if(Math.abs(xboxShooter.getRawAxis(4))>.5)
            {
                if(xboxShooter.getRawAxis(4)>.0)
                {
                    shooterElevatorMotor1.set(Relay.Value.kForward);
                    shooterElevatorMotor2.set(Relay.Value.kForward);
                }else if(xboxShooter.getRawAxis(4)<.0) {
                    shooterElevatorMotor1.set(Relay.Value.kReverse);
                    shooterElevatorMotor2.set(Relay.Value.kReverse);
                }
            }
                else{
                    shooterElevatorMotor1.set(Relay.Value.kOff);
                    shooterElevatorMotor2.set(Relay.Value.kOff);
                }
    }
    
}
