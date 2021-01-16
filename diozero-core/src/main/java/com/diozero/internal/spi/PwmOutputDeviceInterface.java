package com.diozero.internal.spi;

/*
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - Core
 * Filename:     PwmOutputDeviceInterface.java  
 * 
 * This file is part of the diozero project. More information about this project
 * can be found at http://www.diozero.com/
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


import com.diozero.api.DeviceMode;
import com.diozero.api.RuntimeIOException;

public interface PwmOutputDeviceInterface extends GpioDeviceInterface {
	/**
	 * Get the device PWM output device number
	 * @return Device native PWM output
	 */
	int getPwmNum();
	/**
	 * Get the current PWM output value (0..1)
	 * @return Range is 0..1
	 * @throws RuntimeIOException if an I/O error occurs
	 */
	float getValue() throws RuntimeIOException;
	/**
	 * Set the PWM output value (0..1)
	 * @param value Relative value in the range 0..1
	 * @throws RuntimeIOException if an I/O error occurs
	 */
	void setValue(float value) throws RuntimeIOException;
	
	@Override
	default DeviceMode getMode() {
		return DeviceMode.PWM_OUTPUT;
	}
}
