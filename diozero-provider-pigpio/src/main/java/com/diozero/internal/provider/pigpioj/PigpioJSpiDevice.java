package com.diozero.internal.provider.pigpioj;

/*-
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - pigpioj provider
 * Filename:     PigpioJSpiDevice.java  
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

import org.tinylog.Logger;

import com.diozero.api.RuntimeIOException;
import com.diozero.api.SpiClockMode;
import com.diozero.internal.spi.AbstractDevice;
import com.diozero.internal.spi.DeviceFactoryInterface;
import com.diozero.internal.spi.InternalSpiDeviceInterface;

import uk.pigpioj.PigpioInterface;

public class PigpioJSpiDevice extends AbstractDevice implements InternalSpiDeviceInterface {
	private static final int CLOSED = -1;
	
	private PigpioInterface pigpioImpl;
	private int handle = CLOSED;
	private int controller;
	private int chipSelect;

	public PigpioJSpiDevice(String key, DeviceFactoryInterface deviceFactory, PigpioInterface pigpioImpl,
			int controller, int chipSelect, int frequency, SpiClockMode spiClockMode, boolean lsbFirst)
			throws RuntimeIOException {
		super(key, deviceFactory);
		
		this.pigpioImpl = pigpioImpl;
		this.controller = controller;
		this.chipSelect = chipSelect;
		
		int flags = createSpiFlags(spiClockMode, controller, lsbFirst);
		int rc = pigpioImpl.spiOpen(chipSelect, frequency, flags);
		if (rc < 0) {
			handle = CLOSED;
			throw new RuntimeIOException(String.format("Error opening SPI device on controller %d, chip-select %d, response: %d",
					Integer.valueOf(controller), Integer.valueOf(chipSelect), Integer.valueOf(rc)));
		}
		handle = rc;
		Logger.trace("SPI device ({}-{}) opened, handle={}", Integer.valueOf(controller),
				Integer.valueOf(chipSelect), Integer.valueOf(handle));
	}
	
	@Override
	public void write(byte... txBuffer) {
		if (! isOpen()) {
			throw new IllegalStateException("SPI Device " + controller + "-" + chipSelect + " is closed");
		}
				
		int rc = pigpioImpl.spiWrite(handle, txBuffer, 0, txBuffer.length);
		if (rc < 0) {
			throw new RuntimeIOException("Error calling pigpioImpl.spiWrite(), response: " + rc);
		}
	}
	
	@Override
	public void write(byte[] txBuffer, int offset, int length) {
		if (! isOpen()) {
			throw new IllegalStateException("SPI Device " + controller + "-" + chipSelect + " is closed");
		}
		
		int rc = pigpioImpl.spiWrite(handle, txBuffer, offset, length);
		if (rc < 0) {
			throw new RuntimeIOException("Error calling pigpioImpl.spiWrite(), response: " + rc);
		}
	}

	@Override
	public byte[] writeAndRead(byte... txBuffer) throws RuntimeIOException {
		if (! isOpen()) {
			throw new IllegalStateException("SPI Device " + controller + "-" + chipSelect + " is closed");
		}
		
		byte[] rx = new byte[txBuffer.length];
		int rc = pigpioImpl.spiXfer(handle, txBuffer, rx, txBuffer.length);
		if (rc < 0) {
			throw new RuntimeIOException("Error calling pigpioImpl.spiXfer(), response: " + rc);
		}
		
		return rx;
	}

	@Override
	public int getController() {
		return controller;
	}

	@Override
	public int getChipSelect() {
		return chipSelect;
	}

	@Override
	public boolean isOpen() {
		return handle >= 0;
	}

	@Override
	protected void closeDevice() throws RuntimeIOException {
		int rc = pigpioImpl.spiClose(handle);
		handle = CLOSED;
		if (rc < 0) {
			throw new RuntimeIOException("Error calling pigpioImpl.spiClose(), response: " + rc);
		}
	}
	
	/**
	 * 21 20 19 18 17 16 15 14 13 12 11 10  9  8  7  6  5  4  3  2  1  0
	 *  b  b  b  b  b  b  R  T  n  n  n  n  W  A u2 u1 u0 p2 p1 p0  m  m
	 * mm defines the SPI mode.
	 * Warning: modes 1 and 3 do not appear to work on the auxiliary device.
	 * Mode POL PHA
	 *  0    0   0
	 *  1    0   1
	 *  2    1   0
	 *  3    1   1
	 * px is 0 if CEx is active low (default) and 1 for active high.
	 * ux is 0 if the CEx gpio is reserved for SPI (default) and 1 otherwise.
	 * A is 0 for the standard SPI device, 1 for the auxiliary SPI. The auxiliary device
	 * is only present on the A+/B+/Pi2/Zero.
	 * W is 0 if the device is not 3-wire, 1 if the device is 3-wire. Standard SPI device only.
	 * nnnn defines the number of bytes (0-15) to write before switching the MOSI line
	 * to MISO to read data. This field is ignored if W is not set. Standard SPI device only.
	 * T is 1 if the least significant bit is transmitted on MOSI first, the default (0)
	 * shifts the most significant bit out first. Auxiliary SPI device only.
	 * R is 1 if the least significant bit is received on MISO first, the default (0) receives
	 * the most significant bit first. Auxiliary SPI device only.
	 * bbbbbb defines the word size in bits (0-32).
	 * The default (0) sets 8 bits per word. Auxiliary SPI device only.
	 * The other bits in flags should be set to zero
	 */
	private static int createSpiFlags(SpiClockMode clockMode, int controller, boolean lsbFirst) {
		int flags = clockMode.getMode();
		
		// CE0 is the standard SPI device, CE1 is auxiliary
		if (controller == 1) {
			flags |= 0x100;
		}
		if (lsbFirst) {
			flags |= 0x8000;
		}
		
		return flags;
	}
}
