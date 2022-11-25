package org.firstinspires.ftc.teamcode.ftc10650.Auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.Calculators.Interfaces.MoveData.StartData
import org.firstinspires.ftc.teamcode.Calculators.MotionCalcs
import org.firstinspires.ftc.teamcode.Calculators.OrientationCalcs
import org.firstinspires.ftc.teamcode.Calculators.SpeedCalcs
import org.firstinspires.ftc.teamcode.Hardware.RobertoMap.RobotMap
import org.firstinspires.ftc.teamcode.Op.ComplexOp
import org.firstinspires.ftc.teamcode.Utilities.Vector2D

@Autonomous(name = "kotlin auto", group = "ftc10650")
class Meet2Auto : ComplexOp() {

    override fun startPositionAndOrientation(): StartData {
        return StartData(Vector2D(0.0, 0.0), 0.0)
    }

    @Throws(InterruptedException::class)
    override fun body() {
        RobotMap.gyro.resetYaw()
//        saveInitialHeading(true)
        ComplexMove(
                SpeedCalcs.SetSpeed(.2),
                MotionCalcs.AlignPost(),
//                MotionCalcs.PointMotion(0.2,
//                        Vector2D(0.0, 2.0),
//                        Vector2D(-2.0, 2.0),
//                        Vector2D(-2.0, 0.0),
//                        Vector2D(0.0, 0.0)
//                ),
                OrientationCalcs.lookToOrientation(0.0)
        )

    }

}