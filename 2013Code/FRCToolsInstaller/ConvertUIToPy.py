#!/usr/bin/env python
# Team 4067 Qt UI to Python Batch Converter - convert all .ui files to .py!
# Copyright (C) 2013 River Hill HS Robotics Team (Albert H.)
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
import os
import subprocess

print "Starting conversions..."
for uifile in os.listdir("."):
	if uifile.endswith(".ui"):
		print "Converting "+uifile+" to "+uifile.replace(".ui", ".py")+"..."
		pyfile = open(uifile.replace(".ui", ".py"), "w")
		subprocess.call([r"C:\Python27\Lib\site-packages\PyQt4\pyuic4.bat", uifile], stdout = pyfile)
	elif uifile.endswith(".qrc"):
		print "Converting "+uifile+" to "+uifile.replace(".qrc", "_rc.py")+"..."
		pyfile = open(uifile.replace(".qrc", "_rc.py"), "w")
		subprocess.call([r"C:\Python27\Lib\site-packages\PyQt4\pyrcc4.exe", "-py2", uifile], stdout = pyfile)
	elif uifile.endswith(".pyc"):
		print "Removing precompiled Python file "+uifile+"..."
		os.remove(uifile)
print "Conversions complete."
raw_input()