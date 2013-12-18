/*
 * MadDriving - driving code for insane people!
 * Copyright (C) 2013 Albert Huang
 * Based on the IterativeRobot template by FIRST, (C) 2008, FIRST BSD license
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

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;

public class MadDriving extends IterativeRobot {
    // Initial variables
    RobotDrive drive;
    Joystick leftStick, rightStick;
    
    // The big switch - enable insane driving mode or not?
    boolean insaneDriving = true;
    
    // Use native implementation?
    boolean nativeImplementation = true;
    
    // Dampening value (0.0-1.0) for insaneDriving
    double insaneDrivingDampening = 0.5;
    
    /* IT'S ALIVE!! */
    public void robotInit() {
        // Initialize things
        
        // RobotDrive uses PWM 1, 2, 3, and 4
        drive = new RobotDrive(1, 2, 9, 10); // j/k
        
        // Joystick: leftStick uses Joystick 1
        leftStick = new Joystick(1);
        
        // Joystick: rightStick uses Joystick 2
        rightStick = new Joystick(2);
    }

    public void autonomousPeriodic() {
        // What are we going to do here? Run around? Drive into the audience?
        // Shoot fireballs? For sanity's sake, this has no running code.
    }

    // insaneDrive(double x, double y)
    // x is the forward/backward value from -1.0 - 1.0
    // y is the left/right value from -1.0 - 1.0
    public void insaneDrive(double x, double y) {
        // This is actually implemented in FIRST's API as arcadeDrive.
        // We allow the user to pick between the implementations.
        if (nativeImplementation) {
            if (insaneDrivingDampening > 0.0)
                // Drive with dampening
                drive.arcadeDrive(x, y, true);
            else
                // Drive without dampening
                drive.arcadeDrive(x, y);
        } else {
            // Determine a dampened factor by multiplying the left/right value
            // by the factor.
            double finalFactor = y * insaneDrivingDampening;
            
            // Add the finalFactor to the intensity to determine the left motor's
            // value.
            // Testcase:
            //   L/R -0.5, Intensity 1.0 => left motor 0.5
            //   L/R +0.5, Intensity 1.0 => left motor 1.5
            double finalLeft = x + finalFactor;
            
            // Subtract the finalFactor from the intensity to determine the right motor's
            // value.
            // Testcase:
            //   L/R -0.5, Intensity 1.0 => right motor 1.5
            //   L/R +0.5, Intensity 1.0 => right motor 0.5
            double finalRight = x - finalFactor;
            
            // Cap the intensities!
            if (finalLeft < -1.0)
                finalLeft = -1.0;
            else if (finalLeft > 1.0)
                finalLeft = 1.0;
            
            if (finalRight < -1.0)
                finalRight = -1.0;
            else if (finalRight > 1.0)
                finalRight = 1.0;
            
            // Drive!
            drive.tankDrive(finalLeft, finalRight);
        }
    }
    
    public void teleopPeriodic() {
        // The FUN begins here!
        
        // We also enable symmetrical driving, e.g. just driving with one stick
        // to only go backward and forward.
        // We trigger this mode with the left joystick's trigger (no pun intended)
        if (leftStick.getTrigger())
            // Just pass leftStick.getY() to both arguments so that both sides get
            // the same value, hence forward/backward with one stick.
            // Technically, you could do:
            // drive.tankDrive(leftStick, leftStick);
            // ...but this makes more sense. :P DEAL WITH IT!!!!1!
            drive.tankDrive(leftStick.getY(), leftStick.getY());
        else
            // Do regular/insane driving!
            if (insaneDriving)
                // Insane driving
                insaneDrive(leftStick.getX(), leftStick.getY());
            else
                // Regular tankDrive driving
                drive.tankDrive(leftStick, rightStick);
    }
    
    // Testing method
    public void testPeriodic() {
        // Test using the insaneDrive method
        // This should drive at half speed to the right.
        insaneDrive(0.5, 0.5);
    }
    
}
