package com.diozero.sampleapps;

/*-
 * #%L
 * Organisation: diozero
 * Project:      diozero - Sample applications
 * Filename:     ServoTest.java
 * 
 * This file is part of the diozero project. More information about this project
 * can be found at https://www.diozero.com/.
 * %%
 * Copyright (C) 2016 - 2021 diozero
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.tinylog.Logger;

import com.diozero.api.ServoTrim;
import com.diozero.devices.PwmServo;
import com.diozero.util.SleepUtil;

/**
 * Servo test application that uses PWM output device.
 */
public class PwmServoTest {
	public static void main(String[] args) {
		if (args.length < 2) {
			Logger.error("Usage: {} <PWM Frequency> <pin number>", PwmServoTest.class.getName());
			System.exit(1);
		}

		int pwm_freq = Integer.parseInt(args[0]);
		int pin_number = Integer.parseInt(args[1]);

		test(pwm_freq, pin_number);
	}

	public static void test(int frequency, int gpio) {
		ServoTrim trim = ServoTrim.TOWERPRO_SG5010;
		try (PwmServo servo = new PwmServo(gpio, trim.getMidPulseWidthMs(), frequency, trim)) {
			Logger.info("Mid");
			servo.setPulseWidthMs(trim.getMidPulseWidthMs());
			SleepUtil.sleepMillis(1000);

			Logger.info("Min");
			servo.setPulseWidthMs(trim.getMinPulseWidthMs());
			SleepUtil.sleepMillis(1000);

			Logger.info("Mid");
			servo.setPulseWidthMs(trim.getMidPulseWidthMs());
			SleepUtil.sleepMillis(1000);

			Logger.info("Max");
			servo.setPulseWidthMs(trim.getMaxPulseWidthMs());
			SleepUtil.sleepMillis(1000);

			Logger.info("Mid");
			servo.setPulseWidthMs(trim.getMidPulseWidthMs());
			SleepUtil.sleepMillis(1000);

			for (float pulse_ms = trim.getMidPulseWidthMs(); pulse_ms < trim.getMaxPulseWidthMs(); pulse_ms += 0.005) {
				servo.setPulseWidthMs(pulse_ms);
				SleepUtil.sleepMillis(10);
			}
			for (float pulse_ms = trim.getMaxPulseWidthMs(); pulse_ms > trim.getMinPulseWidthMs(); pulse_ms -= 0.005) {
				servo.setPulseWidthMs(pulse_ms);
				SleepUtil.sleepMillis(10);
			}
			for (float pulse_ms = trim.getMinPulseWidthMs(); pulse_ms < trim.getMidPulseWidthMs(); pulse_ms += 0.005) {
				servo.setPulseWidthMs(pulse_ms);
				SleepUtil.sleepMillis(10);
			}
		}
	}
}
