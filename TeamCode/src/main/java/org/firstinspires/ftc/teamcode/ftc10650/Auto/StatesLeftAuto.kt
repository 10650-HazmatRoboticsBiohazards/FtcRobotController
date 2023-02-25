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

//        ComplexMove(null, null,null,OtherCalcs.ShakeClaw(), OtherCalcs.TimeProgress(2000.0));

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
            SpeedCalcs.StandardRampUpDown(0.1, 0.35, 0.5),
            MotionCalcs.pointSplineMotion(0.95,
                Vector2D(-.2,.1),
                Vector2D(-.2, 2.1),
                Vector2D(0.35, 2.1)
            ),
            OrientationCalcs.lookToOrientation(0.0)
        )

        ComplexMove(null,null,OrientationCalcs.lookToOrientation(0.0), OtherCalcs.Raise(), OtherCalcs.AutoAlignPost())


        for (i in 0.. 1){

            ComplexMove(
                SpeedCalcs.StandardRampUpDown(0.15, .25, .5),
                MotionCalcs.pointSplineMotion(.95,
                    Vector2D(-0.7,2.2),
                    Vector2D(-1.05,2.1)
                ),
                OrientationCalcs.spinToProgress(OrientationCalcs.spinProgress(0.1, 0.5, 90.0), ),
                OtherCalcs.LiftToPos(270 - 80 * i),
                OtherCalcs.OtherLambda { d: Interfaces.MoveData ->
                    if (d.progress > .5) {
                        RobotMap.pitch.position = .46
                    } else {
                        RobotMap.pitch.position = .75
                    } },
            )
            ComplexMove(null, null, null,
                OtherCalcs.TimeProgress(600.0),

                OtherCalcs.OtherLambda { RobotMap.claw.position = .23 })

            ComplexMove(
                SpeedCalcs.SetSpeed(.1),
                MotionCalcs.MotionLambda { Vector2D(1.0, 0.0) },
                OrientationCalcs.lookToOrientation(90.0),
                OtherCalcs.TimeProgress(300.0),
                OtherCalcs.OtherLambda {
                    if(it.progress>.2) {
                        RobotMap.lift.targetPosition = 550 - 80*i
                        RobotMap.liftEx.velocity = 1500.0
                        RobotMap.pitch.position = .55
                    }
                }

            )

            ComplexMove(
                SpeedCalcs.StandardRampUpDown(0.15, 0.25, 0.5),
                //                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(0.95,
                    Vector2D(-0.95, 2.20),
                    Vector2D(0.4, 2.20)
                ),
                OrientationCalcs.spinToProgress(OrientationCalcs.spinProgress(0.5, 0.9, 0.0)),
                OtherCalcs.OtherLambda { RobotMap.pitch.position = 0.9 }
            )
            ComplexMove(null,null,OrientationCalcs.lookToOrientation(0.0), OtherCalcs.Raise(), OtherCalcs.AutoAlignPost())

            //fudge
            d.wPos = d.wPos + Vector2D(0.08, 0.0)
        }

        if(signalPosition == 0) {
            ComplexMove(
                SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(
                    0.95,
                    Vector2D(-0.9, 2.1),
                ),
                OrientationCalcs.lookToOrientation(0.0),
                OtherCalcs.LiftToPos(5),
                OtherCalcs.OtherLambda { RobotMap.pitch.position = .8
                    RobotMap.claw.position = .3}
            )
        }
        else if (abs(signalPosition) == 1) {
            ComplexMove(
                SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(0.95,
                    Vector2D(-0.2, 2.1),
                ),
                OrientationCalcs.lookToOrientation(0.0),
                OtherCalcs.LiftToPos(5),
                OtherCalcs.OtherLambda { RobotMap.pitch.position = .8
                    RobotMap.claw.position = .3}
            )
        } else if (signalPosition == 2){
            ComplexMove(
                SpeedCalcs.SetSpeed(.2),
//                MotionCalcs.AlignPost(),
                MotionCalcs.pointSplineMotion(0.95,
                    Vector2D(0.9, 2.1),
                ),
                OrientationCalcs.lookToOrientation(0.0),
                OtherCalcs.LiftToPos(5),
                OtherCalcs.OtherLambda { RobotMap.pitch.position = .8
                    RobotMap.claw.position = .3}
            )
        }

    }
}