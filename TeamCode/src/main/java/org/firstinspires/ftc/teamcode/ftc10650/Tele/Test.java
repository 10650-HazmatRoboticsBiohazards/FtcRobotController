package org.firstinspires.ftc.teamcode.ftc10650.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Calculators.Interfaces;
import org.firstinspires.ftc.teamcode.Calculators.MotionCalcs;
import org.firstinspires.ftc.teamcode.Calculators.OrientationCalcs;
import org.firstinspires.ftc.teamcode.Calculators.OtherCalcs;
import org.firstinspires.ftc.teamcode.Calculators.SpeedCalcs;
import org.firstinspires.ftc.teamcode.Op.ComplexOp;
import org.firstinspires.ftc.teamcode.Utilities.Vector2D;

@TeleOp(name = "Test")
public class Test extends ComplexOp {
    @Override
    public Interfaces.MoveData.StartData startPositionAndOrientation() {
        return new Interfaces.MoveData.StartData(new Vector2D(), 0);
    }

    @Override
    public void body() throws InterruptedException {
        ComplexMove(
                SpeedCalcs.JoystickSpeed(),
                MotionCalcs.ObjectCentricJoystick(),
                OrientationCalcs.turnWithJoystick()
        );
    }
}
