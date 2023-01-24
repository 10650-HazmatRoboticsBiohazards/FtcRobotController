package org.firstinspires.ftc.teamcode.ftc10650.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Calculators.Interfaces.MoveData.StartData;
import org.firstinspires.ftc.teamcode.hardware.RobertoMap.RobotMap;
import org.firstinspires.ftc.teamcode.Op.ComplexOp;
import org.firstinspires.ftc.teamcode.Utilities.Vector2D;

//import kotlin.Math.sign
@TeleOp(name = "vision test", group = "ftc10650")
public class VisionTest extends ComplexOp {


    @Override
    public StartData startPositionAndOrientation() {
        return new StartData(new Vector2D(), 0);
    }

    @Override
    public void body() throws InterruptedException {
        RobotMap.leftCamera.setPipeline(d.robot.aprilTagDetectionPipeline);
        ComplexMove(null, null, null);
    }
}