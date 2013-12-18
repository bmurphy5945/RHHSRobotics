#!/usr/bin/env python
# Team 4067 FRC Tools Installer - easy installer for FRC Tools
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
class DownloadProgress(QtCore.QThread):
    signalUpdateProcess = QtCore.pyqtSignal(int)
    signalDone = QtCore.pyqtSignal(bool)

    def __init__(self, parent = None):
        QThread.__init__(self, parent)
        self.exiting = False
        self.doneDownload = False
    def run(self):
        while not self.exiting and not self.doneDownload:
            bla
    def __del__(self):
        self.exiting = True
        self.wait()

class VerificationProgress(QtCore.QThread):
    signalUpdateProcess = QtCore.pyqtSignal(int)
    signalDone = QtCore.pyqtSignal(bool)

    def __init__(self, parent = None):
        QThread.__init__(self, parent)
        self.exiting = False
        self.doneVerify = False
    def run(self):
        while not self.exiting and not self.doneVerify:
            bla
    def __del__(self):
        self.exiting = True
        self.wait()

class ExtractProgress(QtCore.QThread):
    signalUpdateProcess = QtCore.pyqtSignal(int)
    signalDone = QtCore.pyqtSignal(bool)

    def __init__(self, parent = None):
        QThread.__init__(self, parent)
        self.exiting = False
        self.doneExtract = False
    def run(self):
        while not self.exiting and not self.doneExtract:
            bla
    def __del__(self):
        self.exiting = True
        self.wait()
