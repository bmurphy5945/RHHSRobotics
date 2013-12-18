# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'MainWindow.ui'
#
# Created: Sat Jan 19 21:47:33 2013
#      by: PyQt4 UI code generator 4.9.6
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

try:
    _fromUtf8 = QtCore.QString.fromUtf8
except AttributeError:
    def _fromUtf8(s):
        return s

try:
    _encoding = QtGui.QApplication.UnicodeUTF8
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig, _encoding)
except AttributeError:
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig)

class Ui_MainForm(object):
    def setupUi(self, MainForm):
        MainForm.setObjectName(_fromUtf8("MainForm"))
        MainForm.resize(460, 270)
        MainForm.setAutoFillBackground(False)
        self.MainWidget = QtGui.QWidget(MainForm)
        self.MainWidget.setObjectName(_fromUtf8("MainWidget"))
        self.verticalLayout = QtGui.QVBoxLayout(self.MainWidget)
        self.verticalLayout.setSpacing(0)
        self.verticalLayout.setMargin(0)
        self.verticalLayout.setObjectName(_fromUtf8("verticalLayout"))
        self.headerIMG = QtGui.QLabel(self.MainWidget)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Fixed, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.headerIMG.sizePolicy().hasHeightForWidth())
        self.headerIMG.setSizePolicy(sizePolicy)
        self.headerIMG.setText(_fromUtf8(""))
        self.headerIMG.setPixmap(QtGui.QPixmap(_fromUtf8(":/Images/FRCToolsInstallerHeader.png")))
        self.headerIMG.setObjectName(_fromUtf8("headerIMG"))
        self.verticalLayout.addWidget(self.headerIMG)
        self.MainLayout = QtGui.QVBoxLayout()
        self.MainLayout.setSpacing(6)
        self.MainLayout.setMargin(9)
        self.MainLayout.setObjectName(_fromUtf8("MainLayout"))
        self.installerMessageLabel = QtGui.QLabel(self.MainWidget)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Preferred, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.installerMessageLabel.sizePolicy().hasHeightForWidth())
        self.installerMessageLabel.setSizePolicy(sizePolicy)
        self.installerMessageLabel.setObjectName(_fromUtf8("installerMessageLabel"))
        self.MainLayout.addWidget(self.installerMessageLabel)
        self.passwordOptionsLayout = QtGui.QHBoxLayout()
        self.passwordOptionsLayout.setSpacing(6)
        self.passwordOptionsLayout.setMargin(0)
        self.passwordOptionsLayout.setObjectName(_fromUtf8("passwordOptionsLayout"))
        self.downloadPasswordLayout = QtGui.QVBoxLayout()
        self.downloadPasswordLayout.setObjectName(_fromUtf8("downloadPasswordLayout"))
        self.downloadPasswordLabel = QtGui.QLabel(self.MainWidget)
        self.downloadPasswordLabel.setObjectName(_fromUtf8("downloadPasswordLabel"))
        self.downloadPasswordLayout.addWidget(self.downloadPasswordLabel)
        self.downloadPasswordTxt = QtGui.QLineEdit(self.MainWidget)
        self.downloadPasswordTxt.setText(_fromUtf8(""))
        self.downloadPasswordTxt.setObjectName(_fromUtf8("downloadPasswordTxt"))
        self.downloadPasswordLayout.addWidget(self.downloadPasswordTxt)
        self.passwordOptionsLayout.addLayout(self.downloadPasswordLayout)
        self.archivePasswordLayout = QtGui.QVBoxLayout()
        self.archivePasswordLayout.setObjectName(_fromUtf8("archivePasswordLayout"))
        self.archivePasswordLabel = QtGui.QLabel(self.MainWidget)
        self.archivePasswordLabel.setObjectName(_fromUtf8("archivePasswordLabel"))
        self.archivePasswordLayout.addWidget(self.archivePasswordLabel)
        self.archivePasswordTxt = QtGui.QLineEdit(self.MainWidget)
        self.archivePasswordTxt.setObjectName(_fromUtf8("archivePasswordTxt"))
        self.archivePasswordLayout.addWidget(self.archivePasswordTxt)
        self.passwordOptionsLayout.addLayout(self.archivePasswordLayout)
        self.advOptionsBtn = QtGui.QPushButton(self.MainWidget)
        self.advOptionsBtn.setObjectName(_fromUtf8("advOptionsBtn"))
        self.passwordOptionsLayout.addWidget(self.advOptionsBtn)
        self.MainLayout.addLayout(self.passwordOptionsLayout)
        self.progressBar = QtGui.QProgressBar(self.MainWidget)
        self.progressBar.setProperty("value", 0)
        self.progressBar.setTextVisible(False)
        self.progressBar.setObjectName(_fromUtf8("progressBar"))
        self.MainLayout.addWidget(self.progressBar)
        self.buttonLayout = QtGui.QHBoxLayout()
        self.buttonLayout.setSpacing(80)
        self.buttonLayout.setContentsMargins(0, 0, -1, -1)
        self.buttonLayout.setObjectName(_fromUtf8("buttonLayout"))
        self.startBtn = QtGui.QPushButton(self.MainWidget)
        self.startBtn.setObjectName(_fromUtf8("startBtn"))
        self.buttonLayout.addWidget(self.startBtn)
        self.aboutBtn = QtGui.QPushButton(self.MainWidget)
        self.aboutBtn.setObjectName(_fromUtf8("aboutBtn"))
        self.buttonLayout.addWidget(self.aboutBtn)
        self.exitBtn = QtGui.QPushButton(self.MainWidget)
        self.exitBtn.setObjectName(_fromUtf8("exitBtn"))
        self.buttonLayout.addWidget(self.exitBtn)
        self.MainLayout.addLayout(self.buttonLayout)
        self.verticalLayout.addLayout(self.MainLayout)
        self.verticalLayout.setStretch(1, 1)
        MainForm.setCentralWidget(self.MainWidget)

        self.retranslateUi(MainForm)
        QtCore.QMetaObject.connectSlotsByName(MainForm)

    def retranslateUi(self, MainForm):
        MainForm.setWindowTitle(_translate("MainForm", "Team 4067 - 2013 FRC Software Installer", None))
        self.installerMessageLabel.setText(_translate("MainForm", "<html><head/><body><p>This installer will download and install the <span style=\" font-weight:600;\">2013 FRC Tools and Driver Station</span>.</p><p>Enter any passwords given to you by the team, and then click <span style=\" font-weight:600;\">Start</span>.</p><p>This tool will also install the updates as well.</p><p><span style=\" font-weight:600;\">Note that you must be connected to the internet for the tool to run.</span></p></body></html>", None))
        self.downloadPasswordLabel.setText(_translate("MainForm", "Download Password:", None))
        self.archivePasswordLabel.setText(_translate("MainForm", "Archive Password:", None))
        self.advOptionsBtn.setText(_translate("MainForm", "Advanced &Options...", None))
        self.startBtn.setText(_translate("MainForm", "&Start", None))
        self.aboutBtn.setText(_translate("MainForm", "&About...", None))
        self.exitBtn.setText(_translate("MainForm", "E&xit", None))

import MainWindows_rc
