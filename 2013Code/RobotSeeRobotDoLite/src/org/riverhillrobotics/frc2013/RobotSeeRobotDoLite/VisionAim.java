/*
 * RobotSeeRobotDoLite - code for adjusting the turretRelayMotor to align with the targets, "lite"
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

package org.riverhillrobotics.frc2013.RobotSeeRobotDoLite;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VisionAim class is a class designed to handle aiming for the target.
 * @author Albert Huang (Team 4067)
 * @param vision Instantiated VisionTarget class to track environment with.
 * @param turretRelayMotorRelayMotor turretRelayMotor Relay motor controller.
 * @param shooterElevatorRelayMotorRelayMotor Shooter elevator Relay motor controller.
 */

public class VisionAim {
    Relay turretRelayMotor, shooterElevatorRelayMotor;
    VisionTarget vision;
    
    double[] currentRect;
    
    // Analogous to __init__ in Python - this is run to set the variables when this
    // class is initialized.
    VisionAim(VisionTarget vision, Relay turretRelayMotor, Relay shooterElevatorRelayMotor) {
        this.vision = vision;
        this.turretRelayMotor = turretRelayMotor;
        this.shooterElevatorRelayMotor = shooterElevatorRelayMotor;
    }
    
    /**
     * Update the target rectangle for aiming.
     * 
     * @param None
     * @return None
     */
    public void updateTarget() {
        currentRect = vision.findRectangle();
    }
    
    /**
     * Check if the shooter is aligned to the target or not.
     * 
     * @param None
     * @return Boolean if the shooter is on the target or not.
     */
    public boolean onTarget() {
        updateTarget();
        
        if (VisionTarget.isNull(currentRect)) {
            return false;
        }
        
        if ( (Math.abs(currentRect[0]) <= 0.05) &&
            (Math.abs(currentRect[1]) <= 0.05) )
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
        
        if (VisionTarget.isNull(currentRect)) {
            // Just rotate clockwise and try again.
            turretRelayMotor.set(Relay.Value.kForward);
            Timer.delay(0.1);
            turretRelayMotor.set(Relay.Value.kOff);
            return true;
        }
        
        if (currentRect[0] > 0.05) {
            turretRelayMotor.set(Relay.Value.kForward);
            Timer.delay(0.1);
            turretRelayMotor.set(Relay.Value.kOff);
        } else if (currentRect[0] < -0.05){
            turretRelayMotor.set(Relay.Value.kReverse);
            Timer.delay(0.1);
            turretRelayMotor.set(Relay.Value.kOff);
        }
        if (currentRect[1] > 0.05) {
            shooterElevatorRelayMotor.set(Relay.Value.kReverse);
            Timer.delay(0.1);
            shooterElevatorRelayMotor.set(Relay.Value.kOff);
        } else if (currentRect[1] < -0.05) {
            shooterElevatorRelayMotor.set(Relay.Value.kForward);
            Timer.delay(0.1);
            shooterElevatorRelayMotor.set(Relay.Value.kOff);
        }
        if ( (Math.abs(currentRect[0]) <= 0.05) && (Math.abs(currentRect[1]) <= 0.05) )
            return false;
        else
            return true;
    }
}
