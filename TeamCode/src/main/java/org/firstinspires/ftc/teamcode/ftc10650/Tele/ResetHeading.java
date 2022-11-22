package org.firstinspires.ftc.teamcode.ftc10650.Tele;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Calculators.Interfaces;
import org.firstinspires.ftc.teamcode.Op.ComplexOp;


@TeleOp(name = "reset heading")

public class ResetHeading extends ComplexOp {
    @Override
    public Interfaces.MoveData.StartData startPositionAndOrientation() {
        return null;
    }

    @Override
    public void body() throws InterruptedException {
        saveInitialHeading(true);
    }
}
