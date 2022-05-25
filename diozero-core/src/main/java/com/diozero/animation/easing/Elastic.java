package com.diozero.animation.easing;

/*-
 * #%L
 * Organisation: diozero
 * Project:      diozero - Core
 * Filename:     Elastic.java
 * 
 * This file is part of the diozero project. More information about this project
 * can be found at https://www.diozero.com/.
 * %%
 * Copyright (C) 2016 - 2022 diozero
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

public class Elastic {
	public static final String IN = "inElastic";
	public static float easeIn(float t, float b, float c, float d) {
		if (t == 0) {
			return b;
		}
		if ((t /= d) == 1) {
			return b + c;
		}
		float p = d * .3f;
		float a = c;
		float s = p / 4;
		return -(a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)) + b;
	}

	public static float easeIn(float t, float b, float c, float d, float a, float p) {
		float s;
		if (t == 0) {
			return b;
		}
		if ((t /= d) == 1) {
			return b + c;
		}
		if (a < Math.abs(c)) {
			a = c;
			s = p / 4;
		} else {
			s = p / (2 * (float) Math.PI) * (float) Math.asin(c / a);
		}
		return -(a * (float) Math.pow(2, 10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * Math.PI) / p)) + b;
	}

	public static final String OUT = "outElastic";
	public static float easeOut(float t, float b, float c, float d) {
		if (t == 0) {
			return b;
		}
		if ((t /= d) == 1) {
			return b + c;
		}
		float p = d * .3f;
		float a = c;
		float s = p / 4;
		return (a * (float) Math.pow(2, -10 * t) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) + c + b);
	}

	public static float easeOut(float t, float b, float c, float d, float a, float p) {
		float s;
		if (t == 0) {
			return b;
		}
		if ((t /= d) == 1) {
			return b + c;
		}
		if (a < Math.abs(c)) {
			a = c;
			s = p / 4;
		} else {
			s = p / (2 * (float) Math.PI) * (float) Math.asin(c / a);
		}
		return (a * (float) Math.pow(2, -10 * t) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) + c + b);
	}

	public static final String IN_OUT = "inOutElastic";
	public static float easeInOut(float t, float b, float c, float d) {
		if (t == 0) {
			return b;
		}
		if ((t /= d / 2) == 2) {
			return b + c;
		}
		float p = d * (.3f * 1.5f);
		float a = c;
		float s = p / 4;
		if (t < 1) {
			return -.5f * (a * (float) Math.pow(2, 10 * (t -= 1))
					* (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)) + b;
		}
		return a * (float) Math.pow(2, -10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) * .5f
				+ c + b;
	}

	public static float easeInOut(float t, float b, float c, float d, float a, float p) {
		float s;
		if (t == 0) {
			return b;
		}
		if ((t /= d / 2) == 2) {
			return b + c;
		}
		if (a < Math.abs(c)) {
			a = c;
			s = p / 4;
		} else {
			s = p / (2 * (float) Math.PI) * (float) Math.asin(c / a);
		}
		if (t < 1) {
			return -.5f * (a * (float) Math.pow(2, 10 * (t -= 1))
					* (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p)) + b;
		}
		return a * (float) Math.pow(2, -10 * (t -= 1)) * (float) Math.sin((t * d - s) * (2 * (float) Math.PI) / p) * .5f
				+ c + b;
	}
}
