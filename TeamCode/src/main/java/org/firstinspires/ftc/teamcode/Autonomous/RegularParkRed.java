package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
@Autonomous (name = "WarehouseRedPark", group = "RedAutos")
public class RegularParkRed extends LinearOpMode {
        Robot bsgRobot = new Robot();

        /*
       *
       VARIABLES FOR ENCODERS
       *
       */
        private ElapsedTime runtime = new ElapsedTime();

        static final double COUNTS_PER_MOTOR_REV = 537.7;    // gobilda 435
        static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
        static final double WHEEL_DIAMETER_INCHES = 3.78;     // For figuring circumference
        static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
        static final double DRIVE_SPEED = 0.6;
        static final double TURN_SPEED = 0.5;

        //For turning with encoders
        Integer cpr = 28; //counts per rotation originally 28
        Double gearratio = 13.7; //IDK IT WAS ORIGINALLY 40
        Double diameter = 4.0;
        Double cpi = (cpr * gearratio) / (Math.PI * diameter); //counts per inch, 28cpr * gear ratio / (2 * pi * diameter (in inches, in the center))
        Double bias = 0.8;//default 0.8
        Double meccyBias = 0.9;//change to adjust only strafing movement (was .9)
        //
        Double conversion = cpi * bias;
        Boolean exit = false;

        @Override
        public void runOpMode() throws InterruptedException {
            bsgRobot.init(hardwareMap);

            // Send telemetry message to signify robot waiting;
            telemetry.addData("Status", "Resetting Encoders");    //
            telemetry.update();

            bsgRobot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bsgRobot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bsgRobot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bsgRobot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            waitForStart();

            strafeToPosition(-40,DRIVE_SPEED);

            bsgRobot.stopWheels();

            encoderDrive(DRIVE_SPEED, 30, 22, 2);




        }
        /*
           *
           *
           *
           FUNCTIONS FOR ENCODERS
           *
           *
           *
           *
            */
        public void encoderDrive(double speed,
                                 double leftInches, double rightInches,
                                 double timeoutS) {
            int newFrontLeftTarget;
            int newFrontRightTarget;
            int newBackLeftTarget;
            int newBackRightTarget;

            // Ensure that the opmode is still active
            if (opModeIsActive()) {

                // Determine new target position, and pass to motor controller
                newFrontLeftTarget = bsgRobot.frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
                newBackLeftTarget = bsgRobot.backLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
                newFrontRightTarget = bsgRobot.frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
                newBackRightTarget = bsgRobot.backRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

                bsgRobot.frontLeft.setTargetPosition(newFrontLeftTarget);
                bsgRobot.backLeft.setTargetPosition(newBackLeftTarget);
                bsgRobot.frontRight.setTargetPosition(newFrontRightTarget);
                bsgRobot.backRight.setTargetPosition(newBackRightTarget);


                // Turn On RUN_TO_POSITION
                bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                // reset the timeout time and start motion.
                runtime.reset();

                bsgRobot.frontLeft.setPower(Math.abs(speed));
                bsgRobot.backLeft.setPower(Math.abs(speed));
                bsgRobot.frontRight.setPower(Math.abs(speed));
                bsgRobot.backRight.setPower(Math.abs(speed));

                // keep looping while we are still active, and there is time left, and both motors are running.
                // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
                // its target position, the motion will stop.  This is "safer" in the event that the robot will
                // always end the motion as soon as possible.
                // However, if you require that BOTH motors have finished their moves before the robot continues
                // onto the next step, use (isBusy() || isBusy()) in the loop test.
                while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        (bsgRobot.frontLeft.isBusy() && bsgRobot.frontRight.isBusy() &&
                                bsgRobot.backLeft.isBusy() && bsgRobot.backRight.isBusy()))

                    // Stop all motion;
                    bsgRobot.stopWheels();

                // Turn off RUN_TO_POSITION
                bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                //  sleep(250);   // optional pause after each move
            }
        }

        //strafing with encoders
        public void strafeToPosition(double inches, double speed) {
            //
            int move = (int) (Math.round(inches * cpi * meccyBias * 1.265));
            //
            bsgRobot.backLeft.setTargetPosition(bsgRobot.backLeft.getCurrentPosition() - move);
            bsgRobot.frontLeft.setTargetPosition(bsgRobot.frontLeft.getCurrentPosition() + move);
            bsgRobot.backRight.setTargetPosition(bsgRobot.backRight.getCurrentPosition() + move);
            bsgRobot.frontRight.setTargetPosition(bsgRobot.frontRight.getCurrentPosition() - move);
            //
            bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //
            bsgRobot.frontLeft.setPower(speed);
            bsgRobot.backLeft.setPower(speed);
            bsgRobot.frontRight.setPower(speed);
            bsgRobot.backRight.setPower(speed);
            //
            while (bsgRobot.frontLeft.isBusy() && bsgRobot.frontRight.isBusy() &&
                    bsgRobot.backLeft.isBusy() && bsgRobot.backRight.isBusy()) {
            }
            bsgRobot.frontRight.setPower(0);
            bsgRobot.frontLeft.setPower(0);
            bsgRobot.backRight.setPower(0);
            bsgRobot.backLeft.setPower(0);
            return;
        }
    }

