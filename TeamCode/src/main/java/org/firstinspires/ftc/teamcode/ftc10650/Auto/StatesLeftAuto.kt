package org.firstinspires.ftc.teamcode.ftc10650.Auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Calculators.*
import org.firstinspires.ftc.teamcode.Op.ComplexOp
import org.firstinspires.ftc.teamcode.Utilities.Vector2D
import org.firstinspires.ftc.teamcode.hardware.RobertoMap.RobotMap
import kotlin.math.abs

@Autonomous(name = "Left Auto States", group = "ftc10650")
class StatesLeftAuto: ComplexOp(){

    override fun startPositionAndOrientation(): Interfaces.MoveData.StartData {
        return Interfaces.MoveData.StartData(Vector2D(0.0, 0.0), 0.0)//back right corner    // origin is against center of wall
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

        RobotMap.leftCamera.setPipeline(d.robot.leftPoleAlignPipeline)


//        saveInitialHeading(true)


        ComplexMove(
            SpeedCalcs.SetSpeed(.1),
            MotionCalcs.pointSplineMotion(0.95,
                Vector2D(-.2,.1),
                Vector2D(-.2, 1.95),
                Vector2D(0.35, 1.98)
            ),
            OrientationCalcs.lookToOrientation(0.0)
        )

        ComplexMove(null,null,null, OtherCalcs.Raise(), OtherCalcs.AutoAlignPost())
        ComplexMove(null,null,null,OtherCalcs.TimeProgress(1000.0), OtherCalcs.AutoAlignPost())
        ComplexMove(null,null,null, OtherCalcs.Lower())
        ComplexMove(null,null,null,OtherCalcs.TimeProgress(1000.0))


        if(signalPosition == 0) { // purple
            ComplexMove(
                SpeedCalcs.SetSpeed(.1),
//                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(
                    0.95,
                    Vector2D(-0.9, 2.1),
                    Vector2D(-0.9, 1.5),
                ),
                OrientationCalcs.lookToOrientation(0.0)
            )
        }
        else if (abs(signalPosition) == 1) { // green
            ComplexMove(
                SpeedCalcs.SetSpeed(.1),
//                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(0.95,
                    Vector2D(-0.2, 2.1),
                    Vector2D(-0.2, 1.5),
                ),
                OrientationCalcs.lookToOrientation(0.0)
            )
        } else if (signalPosition == 2){ // orange
            ComplexMove(
                SpeedCalcs.SetSpeed(.1),
//                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(0.95,
                    Vector2D(0.75, 2.1),
                    Vector2D(0.75, 1.5)
                ),
                OrientationCalcs.lookToOrientation(0.0)
            )
        }

    }
}