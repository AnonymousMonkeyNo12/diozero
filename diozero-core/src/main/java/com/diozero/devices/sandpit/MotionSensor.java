package com.diozero.devices.sandpit;

/*
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - Core
 * Filename:     MotionSensor.java
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

import com.diozero.api.GpioPullUpDown;
import com.diozero.api.RuntimeIOException;
import com.diozero.api.SmoothedInputDevice;

/**
 * <p>A Passive Infra-Red (PIR) motion sensor.</p>
 * 
 * <p>A typical PIR device has a small circuit board with three pins: VCC, OUT, and
 * GND. VCC should be connected to the Pi's +5V pin, GND to one of the Pi's
 * ground pins, and finally OUT to the GPIO specified as the value of the 'pin'
 * parameter in the constructor.</p>
 */
public class MotionSensor extends SmoothedInputDevice {
	/**
	 * <p>Defaults 'threshold' to 1, eventAge t0 20ms and eventDetectPeriod to 10ms.</p>
	 * 
	 * <p>If your PIR sensor has a short fall time and is particularly "jittery" you
	 * may wish to set this to a higher value (e.g. 5) to mitigate this.</p>
	 * @param gpio The GPIO which the motion sensor is attached.
	 * @throws RuntimeIOException If an I/O error occurred.
	 */
	public MotionSensor(int gpio) throws RuntimeIOException {
		// Trigger if there is 1 or more events in a 20ms period, check every 10ms
		this(gpio, GpioPullUpDown.NONE, 1, 20, 10);
	}
	
	/**
	 * @param gpio The GPIO to which the motion sensor is attached.
	 * @param threshold The value above which the device will be considered "on".
	 * @param eventAge The time in milliseconds to keep active events in the queue.
	 * @param eventDetectPeriod How frequently to check for events.
	 * @throws RuntimeIOException If an I/O error occurred.
	 */
	public MotionSensor(int gpio, int threshold, int eventAge, int eventDetectPeriod)
			throws RuntimeIOException {
		this(gpio, GpioPullUpDown.NONE, threshold, eventAge, eventDetectPeriod);
	}
	
	/**
	 * @param gpio The GPIO to which the motion sensor is attached.
	 * @param pud Pull up/down configuration
	 * @param threshold The value above which the device will be considered "on".
	 * @param eventAge The time in milliseconds to keep active events in the queue.
	 * @param eventDetectPeriod How frequently to check for events.
	 * @throws RuntimeIOException If an I/O error occurred.
	 */
	public MotionSensor(int gpio, GpioPullUpDown pud, int threshold, int eventAge, int eventDetectPeriod)
			throws RuntimeIOException {
		super(gpio, pud, threshold, eventAge, eventDetectPeriod);
	}
}
