package com.diozero;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

import com.diozero.devices.HCSR04;
import com.diozero.internal.provider.test.HCSR04EchoPin;
import com.diozero.internal.provider.test.HCSR04TriggerPin;
import com.diozero.internal.provider.test.TestDeviceFactory;
import com.diozero.util.SleepUtil;

@SuppressWarnings("static-method")
public class HCSR04Test {
	@BeforeAll
	public static void beforeAll() {
		TestDeviceFactory.setDigitalInputDeviceClass(HCSR04EchoPin.class);
		TestDeviceFactory.setDigitalOutputDeviceClass(HCSR04TriggerPin.class);
	}
	
	@Test
	public void test() {
		try (HCSR04 hcsr04 = new HCSR04(26, 4)) {
			for (int i=0; i<6; i++) {
				float distance = hcsr04.getDistanceCm();
				Logger.info("Distance={}", Float.valueOf(distance));
				SleepUtil.sleepSeconds(0.5);
			}
		}
	}
}
