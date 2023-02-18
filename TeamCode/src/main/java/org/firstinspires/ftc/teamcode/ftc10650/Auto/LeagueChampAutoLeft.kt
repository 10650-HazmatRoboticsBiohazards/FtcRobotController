package org.firstinspires.ftc.teamcode.ftc10650.Auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Calculators.Interfaces.MoveData.StartData
import org.firstinspires.ftc.teamcode.Calculators.MotionCalcs
import org.firstinspires.ftc.teamcode.Calculators.OrientationCalcs
import org.firstinspires.ftc.teamcode.Calculators.OtherCalcs
import org.firstinspires.ftc.teamcode.Calculators.SpeedCalcs
import org.firstinspires.ftc.teamcode.Op.ComplexOp
import org.firstinspires.ftc.teamcode.Utilities.Vector2D
import org.firstinspires.ftc.teamcode.hardware.RobertoMap.RobotMap
import kotlin.math.abs

//import kotlin.Math.sign

@Autonomous(name = "auto left", group = "ftc10650")
class LeagueChampAutoLeft : ComplexOp() {

    override fun startPositionAndOrientation(): StartData {
        return StartData(Vector2D(0.0, 0.0), 0.0)
    }

    @Throws(InterruptedException::class)
    override fun body() {
        RobotMap.gyro.resetYaw()

        ComplexMove(null, null,null,OtherCalcs.ShakeClaw(), OtherCalcs.TimeProgress(2000.0));

//        var signalPosition = d.robot.signalPipeline.currentSignal
//        var count = 0
//        while (signalPosition == -1 && count < 500){
//            signalPosition = d.robot.signalPipeline.currentSignal
//            count++
//        }
        val signalPosition = d.robot.aprilTagDetectionPipeline.id

        RobotMap.leftCamera.setPipeline(d.robot.signalPipeline)//leftPoleAlignPipeline)


//        saveInitialHeading(true)

        ComplexMove(
                SpeedCalcs.SetSpeed(.2),
                MotionCalcs.pointSplineMotion(0.95,
                        Vector2D(-.1,.1),
                        Vector2D(-.1, 1.90),
                        Vector2D(0.35, 1.95)
                ),
                OrientationCalcs.lookToOrientation(0.0)
        )

        ComplexMove(null,null,null, OtherCalcs.Raise(), OtherCalcs.AutoAlignPost())
        ComplexMove(null,null,null,OtherCalcs.TimeProgress(1000.0), OtherCalcs.AutoAlignPost())
        ComplexMove(null,null,null, OtherCalcs.Lower())
        ComplexMove(null,null,null,OtherCalcs.TimeProgress(1000.0))


        if(signalPosition == 0) { // purple
            ComplexMove(
                    SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                    MotionCalcs.pointSplineMotion(
                            0.95,
                            Vector2D(-0.9, 1.9),
                            Vector2D(-0.9, 1.2),
                    ),
                    OrientationCalcs.lookToOrientation(0.0)
            )
        }
        else if (abs(signalPosition) == 1) { // green
            ComplexMove(
                    SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                    MotionCalcs.pointSplineMotion(
                            0.95,
                            Vector2D(-0.1, 1.9),
                            Vector2D(-0.1, 1.2),
                    ),
                    OrientationCalcs.lookToOrientation(0.0)
            )
        } else if (signalPosition == 2){ // orange
            ComplexMove(
                    SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                    MotionCalcs.pointSplineMotion(0.95,
                            Vector2D(0.75, 1.9),
                            Vector2D(0.75, 1.2)
                    ),
                    OrientationCalcs.lookToOrientation(0.0)
            )
        }

    }

}