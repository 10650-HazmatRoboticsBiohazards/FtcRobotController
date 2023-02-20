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

//import kotlin.Math.sign

@Autonomous(name = "TESTS", group = "ftc10650")
class StatesTESTv0 : ComplexOp() {

    override fun startPositionAndOrientation(): StartData {
        return StartData(Vector2D(0.0, 0.0), 0.0)
    }

    @Throws(InterruptedException::class)
    override fun body() {
        RobotMap.gyro.resetYaw()
        ComplexMove(
            SpeedCalcs.StandardRampUpDown(.1, .1, .2),
//                MotionCalcs.AlignPost(),
            MotionCalcs.pointSplineMotion(0.8,
                Vector2D(0.0, 2.0),
                Vector2D(-2.0,2.0),
                Vector2D(-2.0,0.0),
                Vector2D(0.0,0.0),
            ),
            OrientationCalcs.lookToOrientation(0.0)
        )

//        ComplexMove(
//            SpeedCalcs.StandardRampUpDown(.1, .5, .2),
////                MotionCalcs.AlignPost(),
//            MotionCalcs.pointSplineMotion(0.9,
//                Vector2D(2.0, 0.0),
//                Vector2D(2.0, 1.0),
//                Vector2D(0.0, 1.0),
//                Vector2D(0.0, 0.0)
//            ),
//            OrientationCalcs.lookToOrientation(0.0)
//        )

    }

}