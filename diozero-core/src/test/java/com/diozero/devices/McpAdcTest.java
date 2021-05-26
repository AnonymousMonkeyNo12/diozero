package com.diozero.devices;

/*-
 * #%L
 * Organisation: diozero
 * Project:      diozero - Core
 * Filename:     McpAdcTest.java
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import com.diozero.api.AnalogInputDevice;
import com.diozero.internal.provider.test.TestDeviceFactory;
import com.diozero.internal.provider.test.TestMcpAdcSpiDevice;
import com.diozero.util.SleepUtil;

/**
 * Base class for testing the various MCP ADC types
 */
public abstract class McpAdcTest {
	protected McpAdc.Type type;

	@BeforeAll
	public static void beforeAll() {
		TestDeviceFactory.setSpiDeviceClass(TestMcpAdcSpiDevice.class);
	}
	
	public McpAdcTest(McpAdc.Type type) {
		this.type = type;
	}

	@BeforeEach
	public void setup() {
		Logger.info("setup(), type=" + type);
		TestMcpAdcSpiDevice.setType(type);
	}
	
	@Test
	public void test() {
		int spi_chip_select = 0;
		int pin_number = 1;
		int iterations = 20;
		float voltage = 3.3f;

		try (McpAdc adc = new McpAdc(type, spi_chip_select, voltage);
				AnalogInputDevice device = new AnalogInputDevice(adc, pin_number)) {
			for (int i=0; i<iterations; i++) {
				float unscaled_val = adc.getValue(pin_number);
				Logger.info("Value: {}", String.format("%.2f", Float.valueOf(unscaled_val)));
				if (type.isSigned()) {
					Assertions.assertTrue(unscaled_val >= -1 && unscaled_val < 1, "Unscaled range");
				} else {
					Assertions.assertTrue(unscaled_val >= 0 && unscaled_val < 1, "Unscaled range");
				}
				
				unscaled_val = device.getUnscaledValue();
				Logger.info("Unscaled value: {}", String.format("%.2f", Float.valueOf(unscaled_val)));
				if (type.isSigned()) {
					Assertions.assertTrue(unscaled_val >= -1 && unscaled_val < 1, "Unscaled range");
				} else {
					Assertions.assertTrue(unscaled_val >= 0 && unscaled_val < 1, "Unscaled range");
				}
				
				float scaled_val = device.getScaledValue();
				Logger.info("Scaled value: {}", String.format("%.2f", Float.valueOf(scaled_val)));
				if (type.isSigned()) {
					Assertions.assertTrue(scaled_val >= -voltage && scaled_val < voltage, "Scaled range");
				} else {
					Assertions.assertTrue(scaled_val >= 0 && scaled_val < voltage, "Scaled range");
				}
				
				SleepUtil.sleepMillis(100);
			}
		}
	}
}
