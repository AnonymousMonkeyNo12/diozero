package com.diozero.sampleapps;

/*-
 * #%L
 * Organisation: diozero
 * Project:      Device I/O Zero - Sample applications
 * Filename:     ButtonControlledLed.java  
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

import org.tinylog.Logger;

import com.diozero.api.GpioPullUpDown;
import com.diozero.devices.Button;
import com.diozero.devices.LED;
import com.diozero.util.RuntimeIOException;
import com.diozero.util.SleepUtil;

/**
 * Control an LED with a button. To run:
 * <ul>
 * <li>sysfs:<br>
 *  {@code java -cp tinylog-api-$TINYLOG_VERSION.jar:tinylog-impl-$TINYLOG_VERSION.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar com.diozero.sampleapps.ButtonControlledLed 25 12}</li>
 * <li>JDK Device I/O 1.0:<br>
 *  {@code sudo java -cp tinylog-api-$TINYLOG_VERSION.jar:tinylog-impl-$TINYLOG_VERSION.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-jdkdio10-$DIOZERO_VERSION.jar:dio-1.0.1-dev-linux-armv6hf.jar -Djava.library.path=. com.diozero.sampleapps.ButtonControlledLed 25 12}
 * <li>JDK Device I/O 1.1:<br>
 *  {@code sudo java -cp tinylog-api-$TINYLOG_VERSION.jar:tinylog-impl-$TINYLOG_VERSION.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-jdkdio11-$DIOZERO_VERSION.jar:dio-1.1-dev-linux-armv6hf.jar -Djava.library.path=. com.diozero.sampleapps.ButtonControlledLed 25 12}
 * <li>Pi4j:<br>
 *  {@code sudo java -cp tinylog-api-$TINYLOG_VERSION.jar:tinylog-impl-$TINYLOG_VERSION.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-pi4j-$DIOZERO_VERSION.jar:pi4j-core-1.2.jar com.diozero.sampleapps.ButtonControlledLed 25 12}
 * <li>wiringPi:<br>
 *  {@code sudo java -cp tinylog-api-$TINYLOG_VERSION.jar:tinylog-impl-$TINYLOG_VERSION.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-wiringpi-$DIOZERO_VERSION.jar:pi4j-core-1.2.jar com.diozero.sampleapps.ButtonControlledLed 25 12}
 * <li>pigpgioJ:<br>
 *  {@code sudo java -cp tinylog-api-$TINYLOG_VERSION.jar:tinylog-impl-$TINYLOG_VERSION.jar:diozero-core-$DIOZERO_VERSION.jar:diozero-sampleapps-$DIOZERO_VERSION.jar:diozero-provider-pigpio-$DIOZERO_VERSION.jar:pigpioj-java-2.4.jar com.diozero.sampleapps.ButtonControlledLed 25 12}
 * </ul>
 * <p>Note to self - currently using 228 (input) and 219 (output) on Odroid C2.</p>
 */
public class ButtonControlledLed {
	public static void main(String[] args) {
		if (args.length < 2) {
			Logger.error("Usage: {} <button-pin> <led-pin>", ButtonControlledLed.class.getName());
			System.exit(1);
		}
		test(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
	
	public static void test(int buttonPin, int ledPin) {
		try (Button button = new Button(buttonPin, GpioPullUpDown.PULL_UP); LED led = new LED(ledPin)) {
			button.whenPressed(led::on);
			button.whenReleased(led::off);
			
			Logger.info("Waiting for 10s - *** Press the button connected to pin {} ***", Integer.valueOf(buttonPin));
			SleepUtil.sleepSeconds(10);
		} catch (RuntimeIOException e) {
			Logger.error(e, "Error: {}", e);
		}
	}
}
