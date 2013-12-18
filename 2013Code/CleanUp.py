#!/usr/bin/env python
# Team 4067 Java Cleanup - cleanup any old build files
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
import shutil
def listDir(directory):
    if os.path.isdir(directory):
        return [ name for name in os.listdir(directory) if os.path.isdir(os.path.join(directory, name)) ]
    else:
        return []

for srcDir in listDir("."):
    if os.path.isdir(os.path.join(srcDir, "nbproject")):
        # It's a Netbeans project, so let's clean it up!
        print "Examining "+srcDir+" for any old build files..."
        if os.path.isdir(os.path.join(srcDir, "j2meclasses")):
            print " = Removing j2meclasses directory..."
            shutil.rmtree(os.path.join(srcDir, "j2meclasses"))
        if os.path.isdir(os.path.join(srcDir, "build")):
            print " = Removing build directory..."
            shutil.rmtree(os.path.join(srcDir, "build"))
        if os.path.isdir(os.path.join(srcDir, "suite")):
            print " = Removing suite directory..."
            shutil.rmtree(os.path.join(srcDir, "suite"))
    else:
        print "Skipping "+srcDir+" as it is not a Netbeans project."
for srcDir in listDir("FRCEventCode"):
    if os.path.isdir(os.path.join("FRCEventCode", srcDir, "nbproject")):
        # It's a Netbeans project, so let's clean it up!
        print "Examining "+srcDir+" for any old build files..."
        if os.path.isdir(os.path.join("FRCEventCode", srcDir, "j2meclasses")):
            print " = Removing j2meclasses directory..."
            shutil.rmtree(os.path.join("FRCEventCode", srcDir, "j2meclasses"))
        if os.path.isdir(os.path.join("FRCEventCode", srcDir, "build")):
            print " = Removing build directory..."
            shutil.rmtree(os.path.join("FRCEventCode", srcDir, "build"))
        if os.path.isdir(os.path.join("FRCEventCode", srcDir, "suite")):
            print " = Removing suite directory..."
            shutil.rmtree(os.path.join("FRCEventCode", srcDir, "suite"))
    else:
        print "Skipping "+srcDir+" as it is not a Netbeans project. "
print "Cleanup complete!"
raw_input()