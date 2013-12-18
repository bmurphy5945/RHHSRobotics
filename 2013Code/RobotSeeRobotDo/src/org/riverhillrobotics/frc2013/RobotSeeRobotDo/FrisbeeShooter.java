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
 * This class is designed to contain the frisbee shooter code.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDo;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay;

/**
 * The FrisbeeShooter class is a class designed to handle frisbee shooting.
 * @author Albert Huang (Team 4067)
 * @param vision Instantiated VisionTarget class to track environment with.
 * @param shooterJaguar1 First shooter Jaguar motor controller.
 * @param shooterJaguar2 Second shooter Jaguar motor controller.
 * @param frisbeePushRelayMotor Frisbee pushing Relay motor controller.
 */

public class FrisbeeShooter {
    Jaguar shooterJaguar1, shooterJaguar2;
    Relay frisbeePushRelayMotor;
    VisionAim aim;
    
    // Delay to allow shooter to spin up
    final static double shooterStartupDelay = 1.5;
    
    // Delay to allow frisbee to escape
    final static double frisbeeEscapeDelay = 0.3;
    
    // Custom speeds for different goals
    final static double shooterHighGoalSpeed = 1.0;
    final static double shooterMiddleGoalSpeed = 0.7;
    final static double shooterLowGoalSpeed = 0.5;
    
    // Does goal height affect speed?
    final static boolean GOAL_HEIGHT_AFFECTS_SPEED = false;
    
    // Analogous to __init__ in Python - this is run to set the variables when this
    // class is initialized.
    FrisbeeShooter(VisionAim aim, Jaguar shooterJaguar1, Jaguar shooterJaguar2, Relay frisbeePushRelayMotor) {
        this.aim = aim;
        this.shooterJaguar1 = shooterJaguar1;
        this.shooterJaguar2 = shooterJaguar2;
        this.frisbeePushRelayMotor = frisbeePushRelayMotor;
    }
    
    /**
     * Set the shooter speed.
     * @param speed Shooter speed, from -1.0 to 1.0. (This is a Jaguar speed.)
     * @return None
     */
    public void shooterSet(double speed) { shooterJaguar1.set(speed); shooterJaguar2.set(speed); }
    
    /**
     * Actually shoot the frisbee! This method should not be called 
     * directly, since shoot() aims the shooter before shooting (and 
     * therefore calling this method).
     * @param None
     * @return None
     */
    public void actuallyShoot() {
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Making the shot!");
        if (GOAL_HEIGHT_AFFECTS_SPEED) {
            if (aim.currentRect.goal_type == VisionTarget.RECTANGLE_GOAL_LOW) {
                shooterSet(shooterLowGoalSpeed);
            } else if (aim.currentRect.goal_type == VisionTarget.RECTANGLE_GOAL_MIDDLE) {
                shooterSet(shooterMiddleGoalSpeed);
            } else {
                shooterSet(shooterHighGoalSpeed);
            }
        } else {
            // Just use the fastest speed available.
            shooterSet(shooterHighGoalSpeed);
        }
        // Let the motors spin up!
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Spinning up shooter motors - waiting for " + shooterStartupDelay + "seconds.");
        Timer.delay(shooterStartupDelay);
        
        // Shoot the frisbee!
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Shooting!");
        frisbeePushRelayMotor.set(Relay.Value.kReverse);
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Making sure fisbee has been shot - waiting " + frisbeeEscapeDelay + " seconds.");
        Timer.delay(frisbeeEscapeDelay);
        
        // Shut off the motors.
        shooterSet(0);
        frisbeePushRelayMotor.set(Relay.Value.kOff);
    }
    
    /**
     * Shoot the frisbee! This will align the shooter and shoot the 
     * frisbee. Note that this blocks execution until the shooting 
     * process is complete. If you wish to have it run without the 
     * shooting freezing up the controls for the robot, use shootNB() 
     * instead. This method is ideal for autonomous.
     * @param None
     * @return None
     */
    public void shoot() {
        while (!aim.onTarget()) {
            aim.aimForTarget();
        }
        actuallyShoot();
    }
    
    /**
     * Shoot the frisbee! This will align the shooter and shoot the 
     * frisbee. Note that this is NON-blocking, which means execution 
     * of this method will not freeze the robot. HOWEVER, this method 
     * MUST run in a loop, since it will execute bits of the code at a
     * time.
     * @param None
     * @return Boolean if shooting is complete.
     */
    public boolean shootNB() {
        if (!aim.onTarget()) {
            aim.aimForTarget();
            return false;
        } else {
            actuallyShoot();
            return true;
        }
    }
}
