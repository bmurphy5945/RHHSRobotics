# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'InitialNotice.ui'
#
# Created: Sat Jan 19 21:47:32 2013
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

class Ui_NoticeForm(object):
    def setupUi(self, NoticeForm):
        NoticeForm.setObjectName(_fromUtf8("NoticeForm"))
        NoticeForm.resize(405, 282)
        self.verticalLayout = QtGui.QVBoxLayout(NoticeForm)
        self.verticalLayout.setObjectName(_fromUtf8("verticalLayout"))
        self.noticeLbl = QtGui.QLabel(NoticeForm)
        self.noticeLbl.setWordWrap(True)
        self.noticeLbl.setObjectName(_fromUtf8("noticeLbl"))
        self.verticalLayout.addWidget(self.noticeLbl)
        self.buttonLayout = QtGui.QHBoxLayout()
        self.buttonLayout.setObjectName(_fromUtf8("buttonLayout"))
        self.acceptBtn = QtGui.QPushButton(NoticeForm)
        self.acceptBtn.setEnabled(False)
        self.acceptBtn.setObjectName(_fromUtf8("acceptBtn"))
        self.buttonLayout.addWidget(self.acceptBtn)
        self.declineExitBtn = QtGui.QPushButton(NoticeForm)
        self.declineExitBtn.setObjectName(_fromUtf8("declineExitBtn"))
        self.buttonLayout.addWidget(self.declineExitBtn)
        self.verticalLayout.addLayout(self.buttonLayout)
        self.verticalLayout.setStretch(0, 1)

        self.retranslateUi(NoticeForm)
        QtCore.QMetaObject.connectSlotsByName(NoticeForm)

    def retranslateUi(self, NoticeForm):
        NoticeForm.setWindowTitle(_translate("NoticeForm", "Team 4067 - 2013 FRC Software Installer - NOTICE", None))
        self.noticeLbl.setText(_translate("NoticeForm", "<html><head/><body><p><span style=\" font-weight:600; text-decoration: underline;\">2013 FRC Software Installer by Team 4067 - SOFTWARE USE NOTICE</span></p><p>You may only use this software IF AND ONLY IF you are an active teacher, mentor, or participant of the River Hill Robotics Team (The Incredible Hawk, FRC Team 4067). If not, you can NOT use this software and must exit this installer now. Even if you are an active participant of FIRST Robotics™, the licensing of the software distributed by this installer permits only one team to download, install, and use. If you wish to use this software for your team, you must recreate this application and the appropriate archive(s) for your team.<br/><br/>If you have left the team and/or have terminated participation in FIRST Robotics via the River Hill Robotics Team, you can NOT use this software and must exit this installer now.</p><p>This installer is bound to the licensing agreements of FIRST Robotics™, National Instruments™, and any other software licensing agreements that are a part of the FRC competiton software.</p></body></html>", None))
        self.acceptBtn.setText(_translate("NoticeForm", "&Accept (10)", None))
        self.declineExitBtn.setText(_translate("NoticeForm", "Decline and E&xit", None))

