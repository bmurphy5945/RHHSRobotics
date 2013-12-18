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
 * This class holds the Rectangle class, which is used in the VisionTarget class.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDo;

/**
 * The VisionTarget class is a class designed to handle vision targeting,
 * and ONLY vision targeting!
 * @author Albert Huang (Team 4067), FIRST
 * @param camera The AxisCamera object that VisionTarget will use to fetch images from.
 */

public class Rectangle {
    int vertex_tl_x;
    int vertex_tl_y;
    int vertex_tr_x;
    int vertex_tr_y;
    int vertex_br_x;
    int vertex_br_y;
    int vertex_bl_x;
    int vertex_bl_y;
    
    int center_x;
    int center_y;
    
    double center_norm_x;
    double center_norm_y;
    
    int goal_type;
    
    /**
     * Creates a rectangle object.
     * 
     * @param v_tl_x Top-left vertex of the rectangle, x coordinate
     * @param v_tl_y Top-left vertex of the rectangle, y coordinate
     * @param v_tr_x Top-right vertex of the rectangle, x coordinate
     * @param v_tr_y Top-right vertex of the rectangle, y coordinate
     * @param v_br_x Bottom-right vertex of the rectangle, x coordinate
     * @param v_br_y Bottom-right vertex of the rectangle, y coordinate
     * @param v_bl_x Bottom-left vertex of the rectangle, x coordinate
     * @param v_bl_y Bottom-left vertex of the rectangle, y coordinate
     * @param c_x Center of the rectangle, x coordinate
     * @param c_y Center of the rectangle, y coordinate
     * @param c_n_x Center of the rectangle (normalized to -1.0 < n < 1.0), x coordinate
     * @param c_n_y Center of the rectangle (normalized to -1.0 < n < 1.0), y coordinate
     * @return Rectangle object with entered data
     */
    public Rectangle(int v_tl_x, int v_tl_y, int v_tr_x, int v_tr_y, int v_br_x, int v_br_y, int v_bl_x, int v_bl_y, int c_x, int c_y, double c_n_x, double c_n_y, int gt) {
        vertex_tl_x = v_tl_x;
        vertex_tl_y = v_tl_y;
        vertex_tr_x = v_tr_x;
        vertex_tr_y = v_tr_y;
        vertex_br_x = v_br_x;
        vertex_br_y = v_br_y;
        vertex_bl_x = v_bl_x;
        vertex_bl_y = v_bl_y;
        
        center_x = c_x;
        center_y = c_y;
        
        center_norm_x = c_n_x;
        center_norm_y = c_n_y;
        
        goal_type = gt;
    }
    
    // Overload so that we can allow a no-value initialization
    /**
     * Creates a blank rectangle object.
     * 
     * @param None
     * @return Rectangle object with entered data
     */
    public Rectangle() {
        this(0, 0, 0, 100, 100, 100, 0, 100, 50, 50, 0.0, 0.0, VisionTarget.RECTANGLE_GOAL_LOW);
    }
    
    /**
     * Check if a rectangle is equal to another.
     * 
     * @param org Rectangle
     * @return Boolean true or false if the rectangle is equal or not.
     */
    public boolean equals(Object rect) {
        if((rect == null) || (getClass() != rect.getClass()))
            return false;
        
        Rectangle tmpR = (Rectangle) rect;
        if ( (vertex_bl_x == tmpR.vertex_bl_x) && (vertex_bl_y == tmpR.vertex_bl_y) && (vertex_br_x == tmpR.vertex_br_x)
                && (vertex_br_y == tmpR.vertex_br_y) && (vertex_tl_x == tmpR.vertex_tl_x) && (vertex_tl_y == tmpR.vertex_tl_y)
                && (vertex_tr_x == tmpR.vertex_tr_x) && (vertex_tr_y == tmpR.vertex_tr_y)
                && (center_x == tmpR.center_x) && (center_y == tmpR.center_y)
                && (center_norm_x == tmpR.center_norm_x) && (center_norm_y == tmpR.center_norm_y)
                && (goal_type == tmpR.goal_type) )
            return true;
        else
            return false;
    }
    
    /**
     * Check if the currently instantiated rectangle is null or not.
     * 
     * @param None
     * @return Boolean true or false if the instantiated rectangle is null or not.
     */
    public boolean isNull() {
        Rectangle nullRect = new Rectangle();
        if (this.equals(nullRect))
            return true;
        else
            return false;
    }
    
    /**
     * Check if a rectangle is null or not.
     * 
     * @param org Rectangle
     * @return Boolean true or false if the rectangle is null or not.
     */
    public static boolean isNull(Object rect) {
        Rectangle nullRect = new Rectangle();
        Rectangle checkRect = (Rectangle) rect;
        if (checkRect.equals(nullRect))
            return true;
        else
            return false;
    }
    
    /**
     * Append a Rectangle element to a Rectangle array.
     * This method is not intended for external use, but it is made available
     * for anyone wishing to use it.
     * 
     * @param org Rectangle array
     * @param added Rectangle element to append
     * @return Rectangle array with appended element
     */
    public static Rectangle[] _addElement(Rectangle[] org, Rectangle added) {
        Rectangle[] result = new Rectangle[org.length + 1];
        System.arraycopy(org, 0, result, 0, org.length);
        result[org.length] = added;
        return result;
    }
}

