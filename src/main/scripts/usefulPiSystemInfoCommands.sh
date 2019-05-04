###
# #%L
# Organisation: mattjlewis
# Project:      Device I/O Zero
# Filename:     usefulPiSystemInfoCommands.sh  
# 
# This file is part of the diozero project. More information about this project
# can be found at http://www.diozero.com/
# %%
# Copyright (C) 2016 - 2019 mattjlewis
# %%
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
# #L%
###
vcgencmd version

for var in arm gpu ; do \
  echo -e "$var:\t$(vcgencmd get_mem $var)"; \
done

for var in arm core h264 isp v3d uart pwm emmc pixel vec hdmi dpi ; do \
  echo -e "$var:\t$(vcgencmd measure_clock $var)"; \
done

for var in core sdram_c sdram_i sdram_p ; do \
  echo -e "$var:\t$(vcgencmd measure_volts $var)" ; \
done

for var in H264 MPG2 WVC1 MPG4 MJPG WMV9 ; do \
  echo -e "$var:\t$(vcgencmd codec_enabled $var)" ; \
done

for var in config int str ; do \
  echo -e "$var:\t$(vcgencmd get_config $var)" ; \
done

vcgencmd measure_temp
