package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.VisionConstants.cameraPitch;
import static org.firstinspires.ftc.teamcode.constants.VisionConstants.goalHeight;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import java.util.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.VisionConstants;

public class Vision extends SubsystemBase
{
    public Limelight3A cam;
    private List<LLResultTypes.FiducialResult> tagList;
    private String Alliance;
    private Telemetry telemetry;
    private double targetx;
    private double distance;

    public Vision(HardwareMap hMap, Telemetry telemetry, String Alliance)
    {
        this.Alliance = Alliance;
        this.telemetry=telemetry;
        cam= hMap.get(Limelight3A.class,"limelight");

        // CRITICAL: Start the Limelight before trying to get results
        cam.start();

        cam.setPollRateHz(50);

        // Initialize tagList as empty - it will be populated in periodic()
        tagList = new ArrayList<>();

        Alliance.toUpperCase();
    }

    @Override
    public void periodic()
    {
        // Always update tagList from the latest result, but check if result is valid first
        LLResult result = cam.getLatestResult();
        if (result != null && result.isValid()) {
            tagList = result.getFiducialResults();

            // Update targetx if we have a valid tag
            LLResultTypes.FiducialResult tag = getTag();
            if (tag != null) {
                targetx = tag.getTargetXDegrees();
            }
        } else {
            // If result is invalid, clear the tag list
            tagList = new ArrayList<>();
        }

        telemetry.addData("Target X: ", targetx);
        telemetry.addData("Target Present: ", targetPresent());
        telemetry.addData("Fiducial Count: ", tagList != null ? tagList.size() : 0);
        telemetry.addData("Distance: ",calcDistance());
    }
    public LLResultTypes.FiducialResult getTag()
    {
        LLResultTypes.FiducialResult target= null;
        if(Alliance.equals("BLUE"))
        {
            if (tagList!=null)
            {
                for(LLResultTypes.FiducialResult tar:tagList)
                {
                    if(tar!=null && tar.getFiducialId() == 20)
                    {
                        target=tar;
                        break;
                    }
                }
            }
        }
        else if(Alliance.equals("RED"))
        {
            if(tagList!=null)
            {
                for(LLResultTypes.FiducialResult tar:tagList)
                {
                    if(tar!=null && tar.getFiducialId() == 24)
                    {
                        target=tar;
                        break;
                    }
                }
            }
        }
        return target;
    }

    public double getTargetX()
    {
        if(getTag()!=null) {
            return getTag().getTargetXDegrees();
        }
        return 0;
    }

    public double getTy() {
        LLResult result = cam.getLatestResult();
        tagList = result.getFiducialResults();

        LLResultTypes.FiducialResult tag = getTag();
        return (tag != null) ? tag.getTargetYDegrees() : 0.0;
    }


    public boolean hasTarget() {
        LLResult result = cam.getLatestResult();
        tagList = result.getFiducialResults();

        return (tagList.isEmpty()) ? false : true;
    }




    public boolean targetPresent()
    {
        return getTag()!=null;
    }

    public double getTargetYDegrees()
    {
        if(getTag()!=null)
        {
            return getTag().getTargetYDegrees();
        }
        return 0;
    }

    public double calcDistance()
    {
        if(getTag()!=null)
        {
            distance=goalHeight/(Math.tan(cameraPitch+getTargetYDegrees()));
            return distance;
        }
        return 0;
    }

    public double getCamDistance() {
        double LLAngle = 15;
        double LLHeight = 13.5;
        double aprilTagHeight = 29.5;

        double angleToGoalDegrees = LLAngle + cam.getLatestResult().getTy();
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        return (aprilTagHeight - LLHeight) / Math.tan(angleToGoalRadians);
    }
}