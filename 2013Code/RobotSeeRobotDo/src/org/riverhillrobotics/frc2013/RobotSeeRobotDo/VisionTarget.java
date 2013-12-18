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

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.LinearAverages;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * The VisionTarget class is a class designed to handle vision targeting,
 * and ONLY vision targeting!
 * @author Albert Huang (Team 4067), FIRST
 * @param camera The AxisCamera object that VisionTarget will use to fetch images from.
 */

public class VisionTarget {
    /*************************************************************************
     * Vision variables
     *************************************************************************/
    
    // Initialize the camera object
    AxisCamera camera;
    HSVThreshold threshold;
    LED led;
    
    final int XMAXSIZE = 24;
    final int XMINSIZE = 24;
    final int YMAXSIZE = 24;
    final int YMINSIZE = 48;
    final double xMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    final double xMin[] = {.4, .6, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, 0.6, 0};
    final double yMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    final double yMin[] = {.4, .6, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
								.05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
								.05, .05, .6, 0};
    
    final int RECTANGULARITY_LIMIT = 45;
    final int ASPECT_RATIO_LIMIT = 75;
    final int X_EDGE_LIMIT = 40;
    final int Y_EDGE_LIMIT = 60;
    
    final int X_IMAGE_RES = 320;          //X Image resolution in pixels, should be 160, 320 or 640
    final double VIEW_ANGLE = 48;       //Axis M1011 camera - this is our camera!
    
    final static int RECTANGLE_GOAL_LOW = 1;
    final static int RECTANGLE_GOAL_MIDDLE = 2;
    final static int RECTANGLE_GOAL_HIGH = 3;
    
    // Initialize the image processor - create the criteria for the particle filter
    CriteriaCollection cc = new CriteriaCollection();
    
    // Analogous to __init__ in Python - this is run to set the variables when this
    // class is initialized.
    /**
     * Create a VisionTarget object.
     * 
     * @param cam AxisCamera object to use for tracking.
     * @return VisionTarget object
     */
    VisionTarget(AxisCamera cam) {
        camera = cam;
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, 500, 65535, false);
        // This uses the NIVision syntax, exactly from the example.
        threshold = new HSVThreshold(60, 100, 90, 255, 20, 255, true);
        led = new LED(threshold);
        led.turnOn();
    }
    
    /**
     * Create a VisionTarget object with a custom color threshold.
     * 
     * @param cam AxisCamera object to use for tracking.
     * @param thres HSVThreshold color object to use for tracking.
     * @return VisionTarget object
     */
    VisionTarget(AxisCamera cam, HSVThreshold thres) {
        camera = cam;
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, 500, 65535, false);
        threshold = thres;
        led = new LED(threshold);
        led.turnOn();
    }
    
    // Scores class - sort of a C struct for storing data (in this case, Scores) in!
    public static class Scores {
        double rectangularity;
        double aspectRatioInner;
        double aspectRatioOuter;
        double xEdge;
        double yEdge;
    }
    
    /**
     * Change the threshold used by the target tracker.
     * The threshold used is a HSVThreshold object.
     * 
     * @param thres HSVThreshold object.
     * @return None
     */
    public void changeThreshold(HSVThreshold thres) {
        threshold = thres;
    }
    
    /**
     * Find rectangles from the camera image.
     * findBestRectangle() uses this method to gather rectangles and
     * find the best one.
     * 
     * @param None
     * @return Rectangle array with detected rectangles in image
     */
    public Rectangle[] findRectangles() {
        // Initalize an empty Rectangle array
        Rectangle[] rects = new Rectangle[0];
        
        try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "testImage.jpg"
             * 
             */
            
            // Fetch an image from the camera.
            ColorImage image = camera.getImage();
            
            // Keep only green objects. This can be changed later.
            //BinaryImage thresholdImage = image.thresholdHSV(60, 100, 90, 255, 20, 255);
            BinaryImage thresholdImage = image.thresholdHSV(threshold.hue_low, threshold.hue_high, threshold.saturation_low, threshold.saturation_high, threshold.value_low, threshold.value_high);
            
            // Fill in occluded rectangles
            BinaryImage convexHullImage = thresholdImage.convexHull(false);
            
            // Filter out small particles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);

            // Go and iterate through each particle and score to see if it is a target
            Scores scores[] = new Scores[filteredImage.getNumberParticles()];
            
            for (int i = 0; i < scores.length; i++) {
                // Analyze the "particles" found via a report.
                ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                
                // Make a new Scores class to store data in.
                scores[i] = new Scores();
                
                // Score rectangularity, from 0 - 100, and store it
                scores[i].rectangularity = scoreRectangularity(report);
                
                // Score the outer aspect ratio, 0 - 100, and store it
                scores[i].aspectRatioOuter = scoreAspectRatio(filteredImage, report, i, true);
                
                // Score the inner aspect ratio, 0 - 100, and store it
                scores[i].aspectRatioInner = scoreAspectRatio(filteredImage, report, i, false);
                
                // Score the X edge, 0 - 100, and store it
                scores[i].xEdge = scoreXEdge(thresholdImage, report);
                
                // Score the X edge, 0 - 100, and store it
                scores[i].yEdge = scoreYEdge(thresholdImage, report);
                
                // Check if the rectangle target analysis meets requirements.
                // The false boolean indicates that the target is in the middle.
                    if(scoreCompare(scores[i], false))
                    {
                        // Store rectangle information for a high goal rectangle!
                        
                        // Variable naming scheme:
                        // tmp_v_pos_xy
                        // tmp - temporary variable (erased upon method completion)
                        // v - vertice
                        // pos - two letter position (top-left is tl, bottom-right is br, etc.)
                        // xy - x or y coordinate
                        int tmp_v_tl_x = report.boundingRectLeft;
                        int tmp_v_tl_y = report.boundingRectTop;
                        int tmp_v_tr_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_tr_y = report.boundingRectTop;

                        int tmp_v_bl_x = report.boundingRectLeft;
                        int tmp_v_bl_y = report.boundingRectTop + report.boundingRectHeight;
                        int tmp_v_br_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_br_y = report.boundingRectTop + report.boundingRectHeight;
                        
                        // Variable naming scheme: tmp_center_xy
                        int tmp_center_x = report.center_mass_x;
                        int tmp_center_y = report.center_mass_y;
                        
                        // Variable naming scheme: tmp_center_norm_xy
                        double tmp_center_norm_x = report.center_mass_x_normalized;
                        double tmp_center_norm_y = report.center_mass_y_normalized;

                        // Append a new Rectangle to the Rectangle array
                        rects = Rectangle._addElement(rects, new Rectangle(tmp_v_tl_x, tmp_v_tl_y, tmp_v_tr_x, tmp_v_tr_y, tmp_v_bl_x, tmp_v_bl_y, tmp_v_br_x, tmp_v_br_y, tmp_center_x, tmp_center_y, tmp_center_norm_x, tmp_center_norm_y, RECTANGLE_GOAL_HIGH));

                        System.out.println("particle: " + i + "is a High Goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                        System.out.println("Distance: " + computeDistance(thresholdImage, report, i, false));
                        
                // Check if the rectangle target analysis meets requirements.
                // The true boolean indicates that the target is on the outside.
                    } else if (scoreCompare(scores[i], true)) {
                        // Store rectangle information for a middle goal rectangle!
                        
                        // Variable naming scheme:
                        // tmp_v_pos_xy
                        // tmp - temporary variable (erased upon method completion)
                        // v - vertice
                        // pos - two letter position (top-left is tl, bottom-right is br, etc.)
                        // xy - x or y coordinate
                        int tmp_v_tl_x = report.boundingRectLeft;
                        int tmp_v_tl_y = report.boundingRectTop;
                        int tmp_v_tr_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_tr_y = report.boundingRectTop;

                        int tmp_v_bl_x = report.boundingRectLeft;
                        int tmp_v_bl_y = report.boundingRectTop + report.boundingRectHeight;
                        int tmp_v_br_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_br_y = report.boundingRectTop + report.boundingRectHeight;
                        
                        // Variable naming scheme: tmp_center_xy
                        int tmp_center_x = report.center_mass_x;
                        int tmp_center_y = report.center_mass_y;
                        
                        // Variable naming scheme: tmp_center_norm_xy
                        double tmp_center_norm_x = report.center_mass_x_normalized;
                        double tmp_center_norm_y = report.center_mass_y_normalized;

                        // Append a new Rectangle to the Rectangle array
                        rects = Rectangle._addElement(rects, new Rectangle(tmp_v_tl_x, tmp_v_tl_y, tmp_v_tr_x, tmp_v_tr_y, tmp_v_bl_x, tmp_v_bl_y, tmp_v_br_x, tmp_v_br_y, tmp_center_x, tmp_center_y, tmp_center_norm_x, tmp_center_norm_y, RECTANGLE_GOAL_MIDDLE));
                        
                        System.out.println("particle: " + i + "is a Middle Goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                        System.out.println("Distance: " + computeDistance(thresholdImage, report, i, true));
                    } else {
                        System.out.println("particle: " + i + "is not a goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                    }
                    System.out.println("rect: " + scores[i].rectangularity + "ARinner: " + scores[i].aspectRatioInner);
                    System.out.println("ARouter: " + scores[i].aspectRatioOuter + "xEdge: " + scores[i].xEdge + "yEdge: " + scores[i].yEdge);	
            }

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */
            filteredImage.free();
            convexHullImage.free();
            thresholdImage.free();
            image.free();

        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
        
        // Return found Rectangle array
        return rects;
    }
    
    /**
     * Find the best rectangle in the camera image.
     * This method is not intended for external use, but it is made available
     * for anyone wishing to use it.
     * 
     * @param None
     * @return Rectangle with best rectangle in image
     */
    public Rectangle findBestRectangle() {
        // Find all the rectangles in the image
        Rectangle[] foundRectangles = findRectangles();
        
        // Set some variables
        // We set impossible values here so that we can verify that we have working code
        int bestGoalHeight = -1;
        double bestGoalCenterXNorm = -2;
        double bestGoalCenterYNorm = -2;
        
        Rectangle bestRectangle = new Rectangle();
        Rectangle nullRectangle = new Rectangle();
        
        boolean BUG_CHECK_MULTIPLE_HIGH_GOALS = false;
        double BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_X = -2.0;
        double BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_Y = -2.0;
        int BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_COUNTER = -1;
        
        // Iterate the Rectangles to find the best one to shoot for.
        // We prefer high goals first, then middle goals, then low goals.
        // For the middle and low goals, we prefer the one that is closest to the center.
        // 
        for (int i = 0; i < foundRectangles.length; i++) {            
            // Get the highest goal type
            if (foundRectangles[i].goal_type >= bestGoalHeight) {
                // Sanity check
                if (foundRectangles[i].goal_type == RECTANGLE_GOAL_HIGH) {
                    // That means we found two high goals... that's not good.
                    System.out.println("[RobotSeeRobotDo] [AUTONOMUS] WARNING: Found multiple high goals! This means that the vision tracking code is buggy! Will average the normals to determine turret positioning.");
                    
                    if (!BUG_CHECK_MULTIPLE_HIGH_GOALS) {
                        BUG_CHECK_MULTIPLE_HIGH_GOALS = true;
                        BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_X = bestGoalCenterXNorm;
                        BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_Y = bestGoalCenterYNorm;
                        BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_COUNTER = 1;
                    } else {
                        BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_X += foundRectangles[i].center_norm_x;
                        BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_Y += foundRectangles[i].center_norm_y;
                        BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_COUNTER++;
                    }
                    bestGoalHeight = foundRectangles[i].goal_type;
                    bestGoalCenterXNorm = foundRectangles[i].center_norm_x;
                    bestGoalCenterYNorm = foundRectangles[i].center_norm_y;
                    bestRectangle = foundRectangles[i];
                } else
                // Check for multiple middle/low goals
                if ((foundRectangles[i].goal_type != RECTANGLE_GOAL_HIGH) && (foundRectangles[i].goal_type == bestGoalHeight)) {
                    System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Found multiple middle/low goals!");
                    
                    // This presents a little bit of a dilemma.
                    // If we check for the lowest X norm, we will get the best target to rotate to.
                    // But what about the lowest Y norm? Wouldn't it be faster to raise or lower the shooter
                    // to 0.0 if the Y norm happens to be the lowest?
                    // Technically, we could average the two norms and pick a Rectangle based on that.
                    // In my opinion, I would stick to just comparing the X norm, since rotation does
                    // take a bit of time, and we want to get rid of the frisbee ASAP anyway.
                    // Nevertheless, code for using the average is available, but commented out.
                    if ((Math.abs(foundRectangles[i].center_norm_x) < Math.abs(bestGoalCenterXNorm))) {
                        // This is a better goal to aim for!
                        bestGoalHeight = foundRectangles[i].goal_type;
                        bestGoalCenterXNorm = foundRectangles[i].center_norm_x;
                        bestGoalCenterYNorm = foundRectangles[i].center_norm_y;
                        bestRectangle = foundRectangles[i];
                    }
                    // if ((Math.abs(foundRectangles[i].center_norm_x + foundRectangles[i].center_norm_y) < Math.abs(bestGoalCenterXNorm + bestGoalCenterYNorm)));
                } else {
                    // High is high, update accordingly!
                    bestGoalHeight = foundRectangles[i].goal_type;
                    bestGoalCenterXNorm = foundRectangles[i].center_norm_x;
                    bestGoalCenterYNorm = foundRectangles[i].center_norm_y;
                    bestRectangle = foundRectangles[i];
                }
                // End if conditional checks for special cases
            }
            // End if conditional check for best goal height
        }
        // End for loop iterating the Rectangles
        
        // Sanity check - did we update anything at all? (Better yet, were there any rectangles to be had?)
        if ( (bestGoalHeight == -1) || (bestGoalCenterXNorm == -2) || (bestGoalCenterYNorm == -2) ) {
            System.out.println("[RobotSeeRobotDo] [AUTONOMUS] WARNING: No rectangles found for targetting, so halting.");
            return nullRectangle;
        }
        
        // Check for BUG_CHECKS
        // This checks to see if we had multiple high goals. If we did, we average the
        // center normal values and use that instead.
        if (BUG_CHECK_MULTIPLE_HIGH_GOALS) {
            // Sanity check
            if ( (BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_X != -2.0) && (BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_Y != -2.0) && (BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_COUNTER != -1) ) {
                // Average and use the new values instead
                System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Bug check for multiple high goals was triggered, using averaged center normal coordinates.");
                double BUG_CHECK_MULTIPLE_HIGH_GOALS_CN_X_AVG = BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_X / BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_COUNTER;
                double BUG_CHECK_MULTIPLE_HIGH_GOALS_CN_Y_AVG = BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_CN_Y / BUG_CHECK_MULTIPLE_HIGH_GOALS_TOTAL_COUNTER;
                bestGoalCenterXNorm = BUG_CHECK_MULTIPLE_HIGH_GOALS_CN_X_AVG;
                bestGoalCenterYNorm = BUG_CHECK_MULTIPLE_HIGH_GOALS_CN_Y_AVG;
                bestRectangle.center_norm_x = bestGoalCenterXNorm;
                bestRectangle.center_norm_y = bestGoalCenterYNorm;
            } else {
                // Code is exploding all over the place, warn driver
                System.out.println("[RobotSeeRobotDo] [AUTONOMUS] WARNING: Bug check for multiple high goals triggered, but variables are not set correctly! Will use the most recent best rectangle.");
            }
        }
        System.out.println("[RobotSeeRobotDo] [AUTONOMUS] NOTICE: Best goal center norm value: (" + bestGoalCenterXNorm + ", " + bestGoalCenterYNorm + ")");
        return bestRectangle;
    }
    
    /**
     * Computes the estimated distance to a target using the height of the particle in the image. For more information and graphics
     * showing the math behind this approach see the Vision Processing section of the ScreenStepsLive documentation.
     * 
     * @param image The image to use for measuring the particle estimated rectangle
     * @param report The Particle Analysis Report for the particle
     * @param outer True if the particle should be treated as an outer target, false to treat it as a center target
     * @return The estimated distance to the target in Inches.
     */
    public double computeDistance (BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean outer) throws NIVisionException {
            double rectShort, height;
            int targetHeight;

            rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
            //using the smaller of the estimated rectangle short side and the bounding rectangle height results in better performance
            //on skewed rectangles
            height = Math.min(report.boundingRectHeight, rectShort);
            targetHeight = outer ? 29 : 21;

            return X_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
    }
    
    /**
     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect ratio for the target. This method uses
     * the equivalent rectangle sides to determine aspect ratio as it performs better as the target gets skewed by moving
     * to the left or right. The equivalent rectangle is the rectangle with sides x and y where particle area= x*y
     * and particle perimeter= 2x+2y
     * 
     * @param image The image containing the particle to score, needed to performa additional measurements
     * @param report The Particle Analysis Report for the particle, used for the width, height, and particle number
     * @param outer	Indicates whether the particle aspect ratio should be compared to the ratio for the inner target or the outer
     * @return The aspect ratio score (0-100)
     */
    public double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean outer) throws NIVisionException
    {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        idealAspectRatio = outer ? (62/29) : (62/20);	//Dimensions of goal opening + 4 inches on all 4 sides for reflective tape
	
        //Divide width by height to measure aspect ratio
        if(report.boundingRectWidth > report.boundingRectHeight){
            //particle is wider than it is tall, divide long by short
            aspectRatio = 100*(1-Math.abs((1-((rectLong/rectShort)/idealAspectRatio))));
        } else {
            //particle is taller than it is wide, divide short by long
                aspectRatio = 100*(1-Math.abs((1-((rectShort/rectLong)/idealAspectRatio))));
        }
	return (Math.max(0, Math.min(aspectRatio, 100.0)));		//force to be in range 0-100
    }
    
    /**
     * Compares scores to defined limits and returns true if the particle appears to be a target
     * 
     * @param scores The structure containing the scores to compare
     * @param outer True if the particle should be treated as an outer target, false to treat it as a center target
     * 
     * @return True if the particle meets all limits, false otherwise
     */
    public boolean scoreCompare(Scores scores, boolean outer){
            boolean isTarget = true;

            isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT;
            if(outer){
                    isTarget &= scores.aspectRatioOuter > ASPECT_RATIO_LIMIT;
            } else {
                    isTarget &= scores.aspectRatioInner > ASPECT_RATIO_LIMIT;
            }
            isTarget &= scores.xEdge > X_EDGE_LIMIT;
            isTarget &= scores.yEdge > Y_EDGE_LIMIT;

            return isTarget;
    }
    
    /**
     * Computes a score (0-100) estimating how rectangular the particle is by comparing the area of the particle
     * to the area of the bounding box surrounding it. A perfect rectangle would cover the entire bounding box.
     * 
     * @param report The Particle Analysis Report for the particle to score
     * @return The rectangularity score (0-100)
     */
    public double scoreRectangularity(ParticleAnalysisReport report){
            if(report.boundingRectWidth*report.boundingRectHeight !=0){
                    return 100*report.particleArea/(report.boundingRectWidth*report.boundingRectHeight);
            } else {
                    return 0;
            }	
    }
    
    /**
     * Computes a score based on the match between a template profile and the particle profile in the X direction. This method uses the
     * the column averages and the profile defined at the top of the sample to look for the solid vertical edges with
     * a hollow center.
     * 
     * @param image The image to use, should be the image before the convex hull is performed
     * @param report The Particle Analysis Report for the particle
     * 
     * @return The X Edge Score (0-100)
     */
    public double scoreXEdge(BinaryImage image, ParticleAnalysisReport report) throws NIVisionException
    {
        double total = 0;
        LinearAverages averages;
        
        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(image.image, LinearAverages.LinearAveragesMode.IMAQ_COLUMN_AVERAGES, rect);
        float columnAverages[] = averages.getColumnAverages();
        for(int i=0; i < (columnAverages.length); i++){
                if(xMin[(i*(XMINSIZE-1)/columnAverages.length)] < columnAverages[i] 
                   && columnAverages[i] < xMax[i*(XMAXSIZE-1)/columnAverages.length]){
                        total++;
                }
        }
        total = 100*total/(columnAverages.length);
        return total;
    }
    
    /**
	 * Computes a score based on the match between a template profile and the particle profile in the Y direction. This method uses the
	 * the row averages and the profile defined at the top of the sample to look for the solid horizontal edges with
	 * a hollow center
	 * 
	 * @param image The image to use, should be the image before the convex hull is performed
	 * @param report The Particle Analysis Report for the particle
	 * 
	 * @return The Y Edge score (0-100)
	 *
    */
    public double scoreYEdge(BinaryImage image, ParticleAnalysisReport report) throws NIVisionException
    {
        double total = 0;
        LinearAverages averages;
        
        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(image.image, LinearAverages.LinearAveragesMode.IMAQ_ROW_AVERAGES, rect);
        float rowAverages[] = averages.getRowAverages();
        for(int i=0; i < (rowAverages.length); i++){
                if(yMin[(i*(YMINSIZE-1)/rowAverages.length)] < rowAverages[i] 
                   && rowAverages[i] < yMax[i*(YMAXSIZE-1)/rowAverages.length]){
                        total++;
                }
        }
        total = 100*total/(rowAverages.length);
        return total;
    }
}
