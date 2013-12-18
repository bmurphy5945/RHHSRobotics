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
 * This class holds the LED class, which handles lighting up LEDs for vision tracking.
 * It is used in the VisionTarget class.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDo;

/**
 * The VisionTarget class is a class designed to handle vision targeting,
 * and ONLY vision targeting!
 * @author Albert Huang (Team 4067), FIRST
 * @param camera The AxisCamera object that VisionTarget will use to fetch images from.
 */

public class LED {
    int red, green, blue;
    private boolean onOffState;
    
    /**
     * Create a LED object.
     * 
     * @param r Red color amount, from 0 to 255.
     * @param g Green color amount, from 0 to 255.
     * @param b Blue color amount, from 0 to 255.
     * @return LED object
     */
    public LED(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }
    
    /**
     * Create a LED object using a HSVThreshold object.
     * This will use the average to light up the LED.
     * 
     * @param threshold HSVThreshold object
     * @return LED object
     */
    public LED(HSVThreshold threshold) {
        int[] rgb = threshold.toRGB();
        this.red = rgb[0];
        this.green = rgb[1];
        this.blue = rgb[2];
    }
    
    /**
     * Turn on the LED.
     * 
     * @param None
     * @return None
     */
    public void turnOn() {
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] [LED] [turnOn] WARNING: Not implemented!");
        onOffState = true;
    }
    
    /**
     * Turn off the LED.
     * 
     * @param None
     * @return None
     */
    public void turnOff() {
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] [LED] [turnOff] WARNING: Not implemented!");
        onOffState = false;
    }
    
    /**
     * Toggle the LED.
     * 
     * @param None
     * @return None
     */
    public void toggleOnOff() {
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] [LED] [toggleOnOff] WARNING: Not implemented!");
        if (onOffState) turnOff(); else turnOn();
    }
    
    /**
     * Get the current LED state - is it on or off?
     * 
     * @param None
     * @return Boolean true if on, false if off
     */
    public boolean getOnOff() {
        return onOffState;
    }

}
    
