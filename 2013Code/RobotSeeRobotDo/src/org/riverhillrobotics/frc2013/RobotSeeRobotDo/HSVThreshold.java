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
 * This class holds the HSVThreshold class, which is used in the VisionTarget class.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDo;

/**
 * The HSVThreshol class is a storage class for HSV thresholds.
 * @author Albert Huang (Team 4067), FIRST
 */

public class HSVThreshold {
    int hue_low, hue_high, hue_avg;
    int saturation_low, saturation_high, saturation_avg;
    int value_low, value_high, value_avg;
    
    /**
     * Create a HSVThreshold object.
     * 
     * @param hue_low Low end of HSV hue threshold, from 0 to 360.
     * @param saturation_low Low end of HSV saturation threshold, from 0 to 100%.
     * @param value_low Low end of HSV value (or brightness) threshold, from 0 to 100%.
     * @param hue_high High end of HSV hue threshold, from 0 to 360.
     * @param saturation_high High end of HSV saturation threshold, from 0 to 100%.
     * @param value_high High end of HSV value (or brightness) threshold, from 0 to 100%.
     * @return HSVThreshold object
     */
    public HSVThreshold(int hue_low, int saturation_low, int value_low, int hue_high, int saturation_high, int value_high) {
        this.hue_low = hue_low;
        this.saturation_low = saturation_low;
        this.value_low = value_low;
        
        this.hue_high = hue_high;
        this.saturation_high = saturation_high;
        this.value_high = value_high;
        
        this.hue_avg = (int) (hue_low + hue_high) / 2;
        this.saturation_avg = (int) (saturation_low + saturation_high) / 2;
        this.value_avg = (int) (value_low + value_high) / 2;
    }
    
    /**
     * Create a HSVThreshold object. This uses the NIVision's RGBImage style arguments,
     * which pair each value next to each other.
     * 
     * @param hue_low Low end of HSV hue threshold, from 0 to 360.
     * @param hue_high High end of HSV hue threshold, from 0 to 360.
     * @param saturation_low Low end of HSV saturation threshold, from 0 to 100%.
     * @param saturation_high High end of HSV saturation threshold, from 0 to 100%.
     * @param value_low Low end of HSV value (or brightness) threshold, from 0 to 100%.
     * @param value_high High end of HSV value (or brightness) threshold, from 0 to 100%.
     * @param not_used Boolean that you MUST specify to use this overloaded method.
     * @return HSVThreshold object
     */
    public HSVThreshold(int hue_low, int hue_high, int saturation_low, int saturation_high, int value_low, int value_high, boolean not_used) {
        this.hue_low = hue_low;
        this.saturation_low = saturation_low;
        this.value_low = value_low;
        
        this.hue_high = hue_high;
        this.saturation_high = saturation_high;
        this.value_high = value_high;
        
        this.hue_avg = (int) (hue_low + hue_high) / 2;
        this.saturation_avg = (int) (saturation_low + saturation_high) / 2;
        this.value_avg = (int) (value_low + value_high) / 2;
    }
    
    /**
     * Create a HSVThreshold object. This uses averages (or rather, exact)
     * values of the hue, saturation, and value desired, and creates a threshold
     * from them.
     * 
     * The low/high end of the HSV hue threshold is the input average +-20.
     * The low/high end of the HSV saturation threshold is the input average -157 and +158, respectively.
     * The low/high end of the HSV value threshold is the input average -117 and +118, respectively.
     * 
     * @param avg_hue Average (or exact) HSV hue, from 0 to 360.
     * @param avg_saturation Average (or exact) HSV saturation, from 0 to 100%.
     * @param avg_value Average (or exact) HSV value (or brightness), from 0 to 100%.
     * @return HSVThreshold object
     */
    public HSVThreshold(int avg_hue, int avg_saturation, int avg_value) {
        this.hue_low = avg_hue - 20;
        this.saturation_low = avg_saturation - 157;
        this.value_low = avg_value - 117;
        
        this.hue_high = avg_hue + 20;
        this.saturation_high = avg_saturation + 158;
        this.value_high = avg_value + 118;
        
        this.hue_avg = avg_hue;
        this.saturation_avg = avg_saturation;
        this.value_avg = avg_value;
    }
    
    /**
     * Create a HSVThreshold object. This uses the average (or rather, exact)
     * value of the hue, and creates a HSV hue threshold from it.
     * It then assumes a default range for saturation and value to complete the
     * threshold.
     * 
     * The low/high end of the HSV hue threshold is the input average +-20, respectively.
     * The low/high end of the HSV saturation threshold is 90 and 255, respectively.
     * The low/high end of the HSV value threshold is 20 and 255, respectively.
     * 
     * @param avg_hue Average (or exact) HSV hue, from 0 to 360.
     * @return HSVThreshold object
     */
    public HSVThreshold(int avg_hue) {
        this.hue_low = avg_hue - 20;
        this.saturation_low = 90;
        this.value_low = 20;
        
        this.hue_high = avg_hue + 20;
        this.saturation_high = 255;
        this.value_high = 255;
        
        this.hue_avg = avg_hue;
        this.saturation_avg = (int) (this.saturation_low + this.saturation_high) / 2;
        this.value_avg = (int) (this.value_low + this.value_high) / 2;
    }
    
    /**
     * Convert the HSV average to RGB.
     * 
     * @param None
     * @return int array (int[]) with RGB values of the average threshold
     */
    public int[] toRGB() {
        return _HSVToRGB(hue_avg, saturation_avg, value_avg);
    }
    
    /**
     * Convert the low end HSV components to RGB.
     * 
     * @param None
     * @return int array (int[]) with RGB values of the low end HSV components
     */
    public int[] lowToRGB() {
        return _HSVToRGB(hue_low, saturation_low, value_low);
    }
    
    /**
     * Convert the high end HSV components to RGB.
     * 
     * @param None
     * @return int array (int[]) with RGB values of the high end HSV components
     */
    public int[] highToRGB() {
        return _HSVToRGB(hue_high, saturation_high, value_high);
    }
    
    // Adapted from http://stackoverflow.com/a/7901693, which was adapted from
    // http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
    /**
     * Convert a HSV value to RGB.
     * This method is not intended for external use, but it is made available
     * for anyone wishing to use it.
     * 
     * @param hue Hue part of HSV, from 0 to 360.
     * @param saturation Saturation part of HSV, from 0 to 100%.
     * @param value Value (or brightness) part of HSV, from 0 to 100%.
     * @return int array (int[]) with RGB values
     */
    public static int[] _HSVToRGB(int hue, int saturation, int value) {
        float c_hue, c_saturation, c_value;
        c_hue = hue / 360;
        c_saturation = saturation / 100;
        c_value = value / 100;
        
        float r, g, b;

        int h = (int)(c_hue * 6);
        float f = c_hue * 6 - h;
        float p = c_value * (1 - c_saturation);
        float q = c_value * (1 - f * c_saturation);
        float t = c_value * (1 - (1 - f) * c_saturation);
        
        if (h == 0) {
            r = c_value;
            g = t;
            b = p;
        } else if (h == 1) {
            r = q;
            g = c_value;
            b = p;
        } else if (h == 2) {
            r = p;
            g = c_value;
            b = t;
        } else if (h == 3) {
            r = p;
            g = q;
            b = c_value;
        } else if (h == 4) {
            r = t;
            g = p;
            b = c_value;
        } else if (h == 5) {
            r = c_value;
            g = p;
            b = q;
        } else {
            throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
        
        int[] rgb = new int[3];
        rgb[0] = (int)(r * 256);
        rgb[1] = (int)(g * 256);
        rgb[2] = (int)(b * 256);
        
        return rgb;
    }    
}
    
