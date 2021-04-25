package com.diozero.internal.provider.firmata;

/*
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - Firmata
 * Filename:     FirmataDigitalOutputDevice.java
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


import java.io.IOException;

import org.firmata4j.Pin;
import org.firmata4j.Pin.Mode;

import com.diozero.api.RuntimeIOException;
import com.diozero.internal.spi.AbstractDevice;
import com.diozero.internal.spi.GpioDigitalOutputDeviceInterface;

public class FirmataDigitalOutputDevice extends AbstractDevice implements GpioDigitalOutputDeviceInterface {
	private Pin pin;

	public FirmataDigitalOutputDevice(FirmataDeviceFactory deviceFactory, String key, int deviceNumber,
			boolean initialValue) {
		super(key, deviceFactory);
		
		pin = deviceFactory.getIoDevice().getPin(deviceNumber);
		try {
			pin.setMode(Mode.OUTPUT);
		} catch (IOException e) {
			throw new RuntimeIOException("Error setting pin mode to output for pin " + deviceNumber);
		}
		setValue(initialValue);
	}

	@Override
	public boolean getValue() throws RuntimeIOException {
		return pin.getValue() != 0;
	}

	@Override
	public int getGpio() {
		return pin.getIndex();
	}

	@Override
	public void setValue(boolean value) throws RuntimeIOException {
		try {
			pin.setValue(value ? 1 : 0);
		} catch (IOException e) {
			throw new RuntimeIOException("Error setting output value for pin " + pin.getIndex());
		}
	}

	@Override
	protected void closeDevice() throws RuntimeIOException {
		setValue(false);
	}
}
