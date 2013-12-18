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
from PyQt4 import QtCore, QtGui
from PyQt4.QtGui import *
from PyQt4.QtCore import *
from PyQt4.QtNetwork import QHttp
from MainWindow import Ui_MainForm
from InitialNotice import Ui_NoticeForm
from Options import Ui_OptionsForm
from LicenseAgreements import Ui_LicenseAgreementsForm
import sys

FRC2013_ARCHIVE_URL="http://riverhillrobotics.org/Resources/FRC2013/Software/FRC2013.7z"

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

class Main(QMainWindow):
	def __init__(self):
		QDialog.__init__(self)

		# Set up the user interface from Designer.
		self.ui = Ui_MainForm()
		self.ui.setupUi(self)

		# Let's set some attributes!
		# First, make the passwords hidden
		self.ui.downloadPasswordTxt.setEchoMode(QLineEdit.Password)
		self.ui.archivePasswordTxt.setEchoMode(QLineEdit.Password)
		
		# Make the start button default
		self.ui.startBtn.setDefault(True)
		
		# Connect up the buttons.
		self.ui.startBtn.clicked.connect(self.startDownload)
		
		#self.ui.cancelButton.clicked.connect(self.reject)
		
		# Make the window as small as possible!
		self.resize(self.minimumSizeHint())
		self.setFixedSize(self.minimumSizeHint())
	def startDownload(self):
		print "Start download trigger"
		pass
if __name__ == "__main__":
	app = QtGui.QApplication(sys.argv)
	myapp = Main()
	myapp.show()
	#sys.exit(app.exec_())
	try:
		app.exec_()
	except Exception:
		print "An error occured."