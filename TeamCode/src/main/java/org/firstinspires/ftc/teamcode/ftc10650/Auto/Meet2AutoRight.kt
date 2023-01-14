package org.firstinspires.ftc.teamcode.ftc10650.Auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Calculators.Interfaces.MoveData.StartData
import org.firstinspires.ftc.teamcode.Calculators.MotionCalcs
import org.firstinspires.ftc.teamcode.Calculators.OrientationCalcs
import org.firstinspires.ftc.teamcode.Calculators.OtherCalcs
import org.firstinspires.ftc.teamcode.Calculators.SpeedCalcs
import org.firstinspires.ftc.teamcode.Hardware.RobertoMap.RobotMap
import org.firstinspires.ftc.teamcode.Op.ComplexOp
import org.firstinspires.ftc.teamcode.Utilities.Vector2D
import java.util.Timer

//import kotlin.Math.sign

@Autonomous(name = "meet 3 auto Right", group = "ftc10650")
class Meet2AutoRight : ComplexOp() {

    override fun startPositionAndOrientation(): StartData {
        return StartData(Vector2D(0.0, 0.0), 0.0)
    }

    @Throws(InterruptedException::class)
    override fun body() {
        RobotMap.gyro.resetYaw()

        ComplexMove(null, null,null,OtherCalcs.ShakeClaw(), OtherCalcs.TimeProgress(2000.0));

//        var signalPosition = d.robot.signalPipeline.currentSignal
//        while (signalPosition == -1){
//            signalPosition = d.robot.signalPipeline.currentSignal
//        }
        val signalPosition = d.robot.aprilTagDetectionPipeline.id;

//        saveInitialHeading(true)

        ComplexMove(
                SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                MotionCalcs.PointMotion(0.0,
                        Vector2D(-.1,0.1),
                        Vector2D(-.1, 1.90),
                        Vector2D(-0.50, 1.95)
                ),
                OrientationCalcs.lookToOrientation(0.0)
        )
        ComplexMove(null,null,null, OtherCalcs.Raise())
        ComplexMove(null,null,null,OtherCalcs.TimeProgress(1000.0))
        ComplexMove(null,null,null, OtherCalcs.Lower())
        ComplexMove(null,null,null,OtherCalcs.TimeProgress(1000.0))


        if(signalPosition == 1) { // purple
            ComplexMove(
                    SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                    MotionCalcs.PointMotion(
                            0.0,
                            Vector2D(-1.1, 1.9),
                            Vector2D(-1.1, 1.2),
                    ),
                    OrientationCalcs.lookToOrientation(0.0)
            )
        }
        else if (signalPosition == 2) { // green
            ComplexMove(
                    SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                    MotionCalcs.PointMotion(
                            0.0,
                            Vector2D(-0.1, 1.9),
                            Vector2D(-0.1, 1.2),
                    ),
                    OrientationCalcs.lookToOrientation(0.0)
            )
        } else if (signalPosition == 3){ // orange
            ComplexMove(
                    SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                    MotionCalcs.PointMotion(0.0,
                            Vector2D(0.65, 1.9),
                            Vector2D(0.65, 1.2)
                    ),
                    OrientationCalcs.lookToOrientation(0.0)
            )
        }

    }

}