package com.diozero.devices.sandpit;

/*
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - Core
 * Filename:     DebouncedDigitalInputDevice.java  
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


import com.diozero.api.GpioEventTrigger;
import com.diozero.api.GpioPullUpDown;
import com.diozero.api.RuntimeIOException;
import com.diozero.api.WaitableDigitalInputDevice;

/**
 * Represents a generic input device with typical on/off behaviour.
 * 
 * This class extends 'WaitableInputDevice' with machinery to fire the active
 * and inactive events for devices that operate in a typical digital manner:
 * straight forward on / off states with (reasonably) clean transitions between
 * the two.
 */
public class DebouncedDigitalInputDevice extends WaitableDigitalInputDevice {
	public DebouncedDigitalInputDevice(int gpio) throws RuntimeIOException {
		this(gpio, GpioPullUpDown.NONE, 0, GpioEventTrigger.BOTH);
	}

	/**
	 * 
	 * @param gpio GPIO
	 * @param pud Pull-up/down configuratoin
	 * @param debounceTime
	 *            Specifies the length of time (in seconds) that the component
	 *            will ignore changes in state after an initial change. This
	 *            defaults to 0 which indicates that no bounce compensation will
	 *            be performed.
	 * @throws RuntimeIOException if an I/O error occurs
	 */
	public DebouncedDigitalInputDevice(int gpio, GpioPullUpDown pud, float debounceTime) throws RuntimeIOException {
		this(gpio, pud, debounceTime, GpioEventTrigger.BOTH);
	}
	
	public DebouncedDigitalInputDevice(int gpio, GpioPullUpDown pud, float debounceTime, GpioEventTrigger trigger) throws RuntimeIOException {
		super(gpio, pud, trigger);

		if (debounceTime > 0) {
			delegate.setDebounceTimeMillis(Math.round(debounceTime * 1000));
		}
	}

	// Exposed operations
	public void setDebounceTime(float debounceTime) {
		delegate.setDebounceTimeMillis(Math.round(debounceTime * 1000));
	}
}
