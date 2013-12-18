/*
 * RobotSeeRobotDoLite - code for adjusting the turret to align with the targets, "lite"
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

package org.riverhillrobotics.frc2013.RobotSeeRobotDoLite;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;

public class RobotSeeRobotDoLite extends IterativeRobot {
    AxisCamera camera;
    RobotDrive drive;
    Joystick leftStick, rightStick, turretStick;
    Relay turret, shooterElevator, frisbeePush;
    Jaguar shooterM1, shooterM2;
    VisionTarget vision;
    VisionAim aim;
    FrisbeeShooter frisbeeShooter;
    
    public void robotInit() 
    {
        turretStick = new Joystick(3);
        turret = new Relay(1);
        shooterElevator = new Relay(2);
        frisbeePush = new Relay(3);
        shooterM1 = new Jaguar(5);
        shooterM2 = new Jaguar(6);
        camera = AxisCamera.getInstance();
        vision = new VisionTarget(camera);
        aim = new VisionAim(vision, turret, shooterElevator);
        frisbeeShooter = new FrisbeeShooter(aim, shooterM1, shooterM2, frisbeePush);
    }
    
    public void testPeriodic() {
        frisbeeShooter.shoot();
    }   
}
