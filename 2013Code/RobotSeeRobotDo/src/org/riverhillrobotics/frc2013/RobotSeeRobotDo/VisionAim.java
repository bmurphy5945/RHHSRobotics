/*
 * RobotSeeRobotDo - code for adjusting the turret to align with the targets
 * Copyright (C) 2013 Albert Huang
 * Based on the 2013 Vision Sample Project by FIRST, (C) 2008, FIRST BSD license
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
 * 
 * This class is designed to contain all the Vision Tracking related code,
 * therefore making the robot code much more organized, and easier to work with.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDo;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VisionAim class is a class designed to handle aiming for the target.
 * @author Albert Huang (Team 4067)
 * @param vision Instantiated VisionTarget class to track environment with.
 * @param turretJaguarMotor Turret Relay motor controller.
 * @param shooterElevatorRelayMotor Shooter elevator Relay motor controller.
 */

public class VisionAim {
    Jaguar turretJaguarMotor;
    Relay shooterElevatorRelayMotor1, shooterElevatorRelayMotor2;
    VisionTarget vision;
    
    // NOTE: Will be replaced with angle interval in the future
    final static double turretRotationDelayInterval = 0.2;
    final static double shooterAdjustDelayInterval = 0.1;
    
    // Least difference in order to shoot
    final static double canShootThreshold = 0.1;
    
    // Global
    double GoalCenterXNormAbs = 2.0;
    double GoalCenterYNormAbs = 2.0;
    
    double bestGoalCenterXNorm = 2.0;
    double bestGoalCenterYNorm = 2.0;
    
    double offset_x = 0;
    double offset_y = 0;
    
    public Rectangle currentRect = new Rectangle();
    
    // Analogous to __init__ in Python - this is run to set the variables when this
    // class is initialized.
    /**
     * Create a VisionAim object.
     * 
     * @param vision VisionTarget object.
     * @param turretJaguarMotor Jaguar motor for the turret.
     * @param shooterElevatorRelayMotor Relay motor for the shooter elevator.
     * @return None
     */
    VisionAim(VisionTarget vision, Jaguar turretJaguarMotor, Relay shooterElevatorRelayMotor1, Relay shooterElevatorRelayMotor2) {
        this.vision = vision;
        this.turretJaguarMotor = turretJaguarMotor;
        this.shooterElevatorRelayMotor1 = shooterElevatorRelayMotor1;
        this.shooterElevatorRelayMotor2 = shooterElevatorRelayMotor2;
    }
    
    /**
     * Create a VisionAim object with an offset.
     * 
     * @param vision VisionTarget object.
     * @param turretJaguarMotor Relay motor for the turret.
     * @param shooterElevatorRelayMotor Relay motor for the shooter elevator.
     * @param offset_x X offset for aiming.
     * @param offset_y Y offset for aiming.
     * @return None
     */
    VisionAim(VisionTarget vision, Jaguar turretJaguarMotor, Relay shooterElevatorRelayMotor1, Relay shooterElevatorRelayMotor2, double offset_x, double offset_y) {
        this.vision = vision;
        this.turretJaguarMotor = turretJaguarMotor;
        this.shooterElevatorRelayMotor1 = shooterElevatorRelayMotor1;
        this.shooterElevatorRelayMotor2 = shooterElevatorRelayMotor2;
        this.offset_x = offset_x;
        this.offset_y = offset_y;
    }
    
    /**
     * Update the target rectangle for aiming.
     * 
     * @param None
     * @return None
     */
    public void updateTarget() {
        currentRect = vision.findBestRectangle();
        
        // Apply offsets here
        currentRect.center_norm_x += offset_x;
        currentRect.center_norm_y += offset_y;
    }
    
    /**
     * Check if the shooter is aligned to the target or not.
     * 
     * @param None
     * @return Boolean if the shooter is on the target or not.
     */
    public boolean onTarget() {
        updateTarget();
        
        if (currentRect.isNull()) {
            return false;
        }
        
        if ( (Math.abs(currentRect.center_norm_x) <= canShootThreshold) &&
            (Math.abs(currentRect.center_norm_y) <= canShootThreshold) )
            return true;
        else
            return false;
    }
    
    /**
     * Check if the shooter is aligned to the target or not.
     * Same as onTarget(), but with an easier to read method name.
     * This method wraps around onTarget().
     * 
     * @param None
     * @return Boolean if the shooter is on the target or not.
     */
    public boolean readyToShoot() { return onTarget(); }
    
    /**
     * Set a value for the shooter elevator Relay motors.
     * 
     * @param val Relay value to use for the motors.
     * @return None
     */
    public void setShooterElevator(Relay.Value val) { shooterElevatorRelayMotor1.set(val); shooterElevatorRelayMotor2.set(val); }
    
    /**
     * Align the shooter to the target, if necessary.
     * If the shooter is not on the target (and has to move), this returns true.
     * If the shooter is on the target (and does not have to move), this returns false.
     * Beware that the return value is different from onTarget() or
     * readyToShoot()! This needs to be run in a loop, since it only aligns step by step.
     * 
     * @param None
     * @return Boolean if the shooter is on the target or not. False if on target, true if not on target.
     */
    public boolean aimForTarget() {
        updateTarget();
        
        if (currentRect.isNull()) {
            // Just rotate clockwise and try again.
            turretJaguarMotor.set(0.25);
            Timer.delay(turretRotationDelayInterval);
            turretJaguarMotor.set(0);
            return true;
        }
        
        bestGoalCenterXNorm = currentRect.center_norm_x;
        bestGoalCenterYNorm = currentRect.center_norm_y;
        
        // Do some sanity checking
        if (GoalCenterXNormAbs < Math.abs(bestGoalCenterXNorm)) {
            System.out.println("[RobotSeeRobotDo] [AUTONOMUS] WARNING: X normal value exceeds previous normal value! Setting normal to 0.0 to allow shooting.");
            bestGoalCenterXNorm = 0.0;
        }
        if (GoalCenterYNormAbs < Math.abs(bestGoalCenterYNorm)) {
            System.out.println("[RobotSeeRobotDo] [AUTONOMUS] WARNING: X normal value exceeds previous normal value! Setting normal to 0.0 to allow shooting.");
            bestGoalCenterYNorm = 0.0;
        }
        
        // Record the absolute value of the center normals
        GoalCenterXNormAbs = Math.abs(bestGoalCenterXNorm);
        GoalCenterYNormAbs = Math.abs(bestGoalCenterYNorm);
        
        // Now let's move!
        if (Math.abs(bestGoalCenterXNorm) > canShootThreshold) {
            if (bestGoalCenterXNorm > 0.0) {
                // Forward = clockwise = move rectangle to the left to 0.0, decreasing.
                System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Rotating turret clockwise!");
                turretJaguarMotor.set(1);
                Timer.delay(turretRotationDelayInterval);
                turretJaguarMotor.set(0);
            } else {
                // Reverse = counterclockwise = move rectangle to the right to 0.0, increasing.
                System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Rotating turret counterclockwise!");
                turretJaguarMotor.set(-1);
                Timer.delay(turretRotationDelayInterval);
                turretJaguarMotor.set(0);
            }
        }
        
        if (Math.abs(bestGoalCenterYNorm) > canShootThreshold) {
            if (bestGoalCenterYNorm > 0.0) {
                // Reverse = down = move rectangle up to 0.0.
                System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Moving turret down!");
                setShooterElevator(Relay.Value.kReverse);
                Timer.delay(shooterAdjustDelayInterval);
                setShooterElevator(Relay.Value.kOff);
            } else {
                // Forward = up = move rectangle down to 0.0.
                System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Moving turret up!");
                setShooterElevator(Relay.Value.kForward);
                Timer.delay(shooterAdjustDelayInterval);
                setShooterElevator(Relay.Value.kOff);
            }
        }
        
        if ( (Math.abs(bestGoalCenterXNorm ) <= canShootThreshold) && (Math.abs(bestGoalCenterYNorm ) <= canShootThreshold) ) {
            System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: We can now make a shot! Made the threshold " + canShootThreshold + ", with the current goal center norm value: (" + bestGoalCenterXNorm + ", " + bestGoalCenterYNorm + ")");
            
            // Since we're done, reset the absolute values to a high value, 2.0
            GoalCenterXNormAbs = 2.0;
            GoalCenterYNormAbs = 2.0;

            return false;
        } else {
            return true;
        }
    }
}
