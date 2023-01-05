package org.firstinspires.ftc.teamcode.Hardware.Sensors.pipelines

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline

class LeftCameraStackAlignPipeline : OpenCvPipeline() {

    private val rLower = Scalar(0.0, 30.0, 50.0) //150, 90
    private val rUpper = Scalar(15.0, 255.0, 250.0) // 230, 255

    private val bLower = Scalar(100.0, 30.0, 50.0) //150, 90
    private val bUpper = Scalar(120.0, 255.0, 250.0) // 230, 255

    private val yLower = Scalar(20.0, 50.0, 50.0) //150, 90
    private val yUpper = Scalar(30.0, 255.0, 250.0) // 230, 255


    private var hsv = Mat()
    private var mask = Mat()
    private var maskR = Mat()
    private var maskB = Mat()
    private var maskY = Mat()
    private var hierarchy = Mat()
    private var contours: List<MatOfPoint> = java.util.ArrayList()

    private var horizontalPosition = 0;


    override fun processFrame(input: Mat): Mat {

        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV)

        Core.inRange(hsv, rLower, rUpper, maskR)
        Core.inRange(hsv, bLower, bUpper, maskB)
        Core.inRange(hsv, yLower, yUpper, maskY)
        Core.add(maskR, maskB, mask)
        Core.add(mask, maskY, mask)



        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(10.0, 30.0))
        Imgproc.erode(mask, mask, kernel)
        Imgproc.dilate(mask, mask, kernel)

        val cannyEdges = Mat()
        Imgproc.Canny(mask, cannyEdges, 10.0, 100.0)

//        MatOfPoint totalContours = new MatOfPoint();

//        MatOfPoint totalContours = new MatOfPoint();
        val tempContours: List<MatOfPoint> = ArrayList()
        Imgproc.findContours(cannyEdges, tempContours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)
        contours = tempContours
        var maxArea = 0.0
//        var largestContour: MatOfPoint? = MatOfPoint()
        var largestBox : Rect = Rect()
        for (contour in contours) {
//            val area = Imgproc.contourArea(contour)
            val boundingBox = Imgproc.boundingRect(contour)
            val area = boundingBox.area()
            if (area > maxArea) {
                maxArea = area
                largestBox = boundingBox
//                largestContour = contour
            }
        }
//        val boundingRect = Imgproc.boundingRect(largestContour)

        Imgproc.rectangle(input, largestBox, Scalar(0.0, 255.0, 0.0), 2)

        for (i in contours.indices) {
            Imgproc.drawContours(input, contours, i, Scalar(255.0, 0.0, 0.0), 2)
        }

        horizontalPosition = largestBox.x + largestBox.width/2

        //Cleanup
        kernel.release()
        cannyEdges.release()
        return input
    }

    fun distanceFromCenter() : Int {
        return horizontalPosition - 320
    }
}