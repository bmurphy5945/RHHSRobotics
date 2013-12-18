#!/usr/bin/env python
# pyNetworkManager v1.1 - cross-platform Python network management library
# Copyright (C) 2012 River Hill Robotics Team (Albert H.)
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# 
# This is simply a pyNM demo/test. It actually works well, so don't worry
# about the "demo/test" part. :P
# 
import pyNM
print "Loading pyNetworkManager..."
pyNMHandle = pyNM.pyNM()
print "Using pyNetworkManager version "+str(pyNMHandle.getVersion())
print "Initializing pyNetworkManager..."
pyNMHandle.initNM()
print "Setting wireless NIC to DHCP..."
pyNMHandle.setWirelessDHCP()