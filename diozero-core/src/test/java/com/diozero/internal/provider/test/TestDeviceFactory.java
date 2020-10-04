package com.diozero.internal.provider.test;

/*
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - Core
 * Filename:     TestDeviceFactory.java  
 * 
 * This file is part of the diozero project. More information about this project
 * can be found at http://www.diozero.com/
 * %%
 * Copyright (C) 2016 - 2020 diozero
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
import com.diozero.api.GpioEventTrigger;
import com.diozero.api.GpioPullUpDown;
import com.diozero.api.PinInfo;
import com.diozero.api.SerialDevice;
import com.diozero.api.SpiClockMode;
import com.diozero.internal.DeviceStates;
import com.diozero.internal.provider.AnalogInputDeviceInterface;
import com.diozero.internal.provider.AnalogOutputDeviceInterface;
import com.diozero.internal.provider.BaseNativeDeviceFactory;
import com.diozero.internal.provider.DeviceFactoryInterface;
import com.diozero.internal.provider.GpioDigitalInputDeviceInterface;
import com.diozero.internal.provider.GpioDigitalInputOutputDeviceInterface;
import com.diozero.internal.provider.GpioDigitalOutputDeviceInterface;
import com.diozero.internal.provider.I2CDeviceInterface;
import com.diozero.internal.provider.PwmOutputDeviceInterface;
import com.diozero.internal.provider.SerialDeviceInterface;
import com.diozero.internal.provider.SpiDeviceInterface;
import com.diozero.util.RuntimeIOException;

public class TestDeviceFactory extends BaseNativeDeviceFactory {
	private static Class<? extends AnalogInputDeviceInterface> analogInputDeviceClass;
	private static Class<? extends AnalogOutputDeviceInterface> analogOutputDeviceClass;
	private static Class<? extends GpioDigitalInputDeviceInterface> digitalInputDeviceClass = TestDigitalInputDevice.class;
	private static Class<? extends GpioDigitalOutputDeviceInterface> digitalOutputDeviceClass = TestDigitalOutputDevice.class;
	private static Class<? extends PwmOutputDeviceInterface> pwmOutputDeviceClass;
	private static Class<? extends SpiDeviceInterface> spiDeviceClass;
	private static Class<? extends I2CDeviceInterface> i2cDeviceClass = TestI2CDevice.class;
	private static Class<? extends SerialDeviceInterface> serialDeviceClass;

	public TestDeviceFactory() {
	}

	public static void setAnalogInputDeviceClass(Class<? extends AnalogInputDeviceInterface> clz) {
		analogInputDeviceClass = clz;
	}

	public static void setAnalogOutputDeviceClass(Class<? extends AnalogOutputDeviceInterface> clz) {
		analogOutputDeviceClass = clz;
	}

	public static void setDigitalInputDeviceClass(Class<? extends GpioDigitalInputDeviceInterface> clz) {
		digitalInputDeviceClass = clz;
	}

	public static void setDigitalOutputDeviceClass(Class<? extends GpioDigitalOutputDeviceInterface> clz) {
		digitalOutputDeviceClass = clz;
	}

	public static void setPwmOutputDeviceClass(Class<? extends PwmOutputDeviceInterface> clz) {
		pwmOutputDeviceClass = clz;
	}

	public static void setSpiDeviceClass(Class<? extends SpiDeviceInterface> clz) {
		spiDeviceClass = clz;
	}

	public static void setI2CDeviceClass(Class<? extends I2CDeviceInterface> clz) {
		i2cDeviceClass = clz;
	}

	public static void setSerialDeviceClass(Class<? extends SerialDeviceInterface> clz) {
		serialDeviceClass = clz;
	}

	private static final int DEFAULT_PWM_FREQUENCY = 100;
	private int boardPwmFrequency = DEFAULT_PWM_FREQUENCY;

	// Added for testing purposes only
	public DeviceStates getDeviceStates() {
		return deviceStates;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public GpioDigitalInputDeviceInterface createDigitalInputDevice(String key, PinInfo pinInfo, GpioPullUpDown pud,
			GpioEventTrigger trigger) throws RuntimeIOException {
		if (digitalInputDeviceClass == null) {
			throw new UnsupportedOperationException("Digital input implementation class hasn't been set");
		}

		try {
			return digitalInputDeviceClass
					.getConstructor(String.class, DeviceFactoryInterface.class, int.class, GpioPullUpDown.class,
							GpioEventTrigger.class)
					.newInstance(key, this, Integer.valueOf(pinInfo.getDeviceNumber()), pud, trigger);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GpioDigitalOutputDeviceInterface createDigitalOutputDevice(String key, PinInfo pinInfo, boolean initialValue)
			throws RuntimeIOException {
		if (digitalOutputDeviceClass == null) {
			throw new UnsupportedOperationException("Digital output implementation class hasn't been set");
		}

		try {
			return digitalOutputDeviceClass
					.getConstructor(String.class, DeviceFactoryInterface.class, int.class, boolean.class)
					.newInstance(key, this, Integer.valueOf(pinInfo.getDeviceNumber()), Boolean.valueOf(initialValue));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public GpioDigitalInputOutputDeviceInterface createDigitalInputOutputDevice(String key, PinInfo pinInfo,
			DeviceMode mode) throws RuntimeIOException {
		throw new UnsupportedOperationException("Digital Input / Output devices not yet supported by this provider");
	}

	@Override
	public PwmOutputDeviceInterface createPwmOutputDevice(String key, PinInfo pinInfo, int pwmFrequency,
			float initialValue) throws RuntimeIOException {
		if (pwmOutputDeviceClass == null) {
			throw new IllegalArgumentException("PWM output implementation class hasn't been set");
		}

		try {
			return pwmOutputDeviceClass
					.getConstructor(String.class, DeviceFactoryInterface.class, int.class, float.class)
					.newInstance(key, this, Integer.valueOf(pinInfo.getDeviceNumber()), Float.valueOf(initialValue));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AnalogInputDeviceInterface createAnalogInputDevice(String key, PinInfo pinInfo) throws RuntimeIOException {
		if (analogInputDeviceClass == null) {
			throw new UnsupportedOperationException("Analog input implementation class hasn't been set");
		}

		try {
			return analogInputDeviceClass.getConstructor(String.class, DeviceFactoryInterface.class, int.class)
					.newInstance(key, this, Integer.valueOf(pinInfo.getDeviceNumber()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AnalogOutputDeviceInterface createAnalogOutputDevice(String key, PinInfo pinInfo, float initialValue)
			throws RuntimeIOException {
		if (analogOutputDeviceClass == null) {
			throw new UnsupportedOperationException("Analog output implementation class hasn't been set");
		}

		try {
			return analogOutputDeviceClass.getConstructor(String.class, DeviceFactoryInterface.class, int.class)
					.newInstance(key, this, Integer.valueOf(pinInfo.getDeviceNumber()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SpiDeviceInterface createSpiDevice(String key, int controller, int chipSelect, int frequency,
			SpiClockMode spiClockMode, boolean lsbFirst) throws RuntimeIOException {
		if (spiDeviceClass == null) {
			throw new IllegalArgumentException("SPI Device implementation class hasn't been set");
		}

		try {
			return spiDeviceClass.getConstructor(String.class, DeviceFactoryInterface.class, int.class, int.class,
					int.class, SpiClockMode.class, boolean.class).newInstance(key, this, Integer.valueOf(controller),
							Integer.valueOf(chipSelect), Integer.valueOf(frequency), spiClockMode,
							Boolean.valueOf(lsbFirst));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public I2CDeviceInterface createI2CDevice(String key, int controller, int address, int addressSize,
			int clockFrequency) throws RuntimeIOException {
		if (i2cDeviceClass == null) {
			throw new IllegalArgumentException("I2C Device implementation class hasn't been set");
		}

		try {
			return i2cDeviceClass.getConstructor(String.class, DeviceFactoryInterface.class, int.class, int.class,
					int.class, int.class).newInstance(key, this, Integer.valueOf(controller), Integer.valueOf(address),
							Integer.valueOf(addressSize), Integer.valueOf(clockFrequency));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SerialDeviceInterface createSerialDevice(String key, String tty, int baud, SerialDevice.DataBits dataBits,
			SerialDevice.Parity parity, SerialDevice.StopBits stopBits) throws RuntimeIOException {
		if (serialDeviceClass == null) {
			throw new IllegalArgumentException("Serial Device implementation class hasn't been set");
		}

		try {
			return serialDeviceClass
					.getConstructor(String.class, DeviceFactoryInterface.class, String.class, int.class,
							SerialDevice.DataBits.class, SerialDevice.Parity.class, SerialDevice.StopBits.class)
					.newInstance(key, this, tty, Integer.valueOf(baud), dataBits, parity, stopBits);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getBoardPwmFrequency() {
		return boardPwmFrequency;
	}

	@Override
	public void setBoardPwmFrequency(int pwmFrequency) {
		this.boardPwmFrequency = pwmFrequency;
	}
}
