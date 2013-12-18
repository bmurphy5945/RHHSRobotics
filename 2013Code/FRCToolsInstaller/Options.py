# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'Options.ui'
#
# Created: Sat Jan 19 21:47:35 2013
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

class Ui_OptionsForm(object):
    def setupUi(self, OptionsForm):
        OptionsForm.setObjectName(_fromUtf8("OptionsForm"))
        OptionsForm.resize(612, 226)
        self.horizontalLayout_2 = QtGui.QHBoxLayout(OptionsForm)
        self.horizontalLayout_2.setObjectName(_fromUtf8("horizontalLayout_2"))
        self.logoGraphicsView = QtGui.QGraphicsView(OptionsForm)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Fixed, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.logoGraphicsView.sizePolicy().hasHeightForWidth())
        self.logoGraphicsView.setSizePolicy(sizePolicy)
        self.logoGraphicsView.setObjectName(_fromUtf8("logoGraphicsView"))
        self.horizontalLayout_2.addWidget(self.logoGraphicsView)
        self.tabLayout = QtGui.QVBoxLayout()
        self.tabLayout.setObjectName(_fromUtf8("tabLayout"))
        self.optionTabs = QtGui.QTabWidget(OptionsForm)
        self.optionTabs.setObjectName(_fromUtf8("optionTabs"))
        self.networkTab = QtGui.QWidget()
        self.networkTab.setObjectName(_fromUtf8("networkTab"))
        self.notImplementedLbl = QtGui.QLabel(self.networkTab)
        self.notImplementedLbl.setGeometry(QtCore.QRect(10, 10, 101, 16))
        self.notImplementedLbl.setObjectName(_fromUtf8("notImplementedLbl"))
        self.optionTabs.addTab(self.networkTab, _fromUtf8(""))
        self.serverTab = QtGui.QWidget()
        self.serverTab.setObjectName(_fromUtf8("serverTab"))
        self.verticalLayoutWidget_2 = QtGui.QWidget(self.serverTab)
        self.verticalLayoutWidget_2.setGeometry(QtCore.QRect(9, 9, 379, 141))
        self.verticalLayoutWidget_2.setObjectName(_fromUtf8("verticalLayoutWidget_2"))
        self.serverOptionsLayout = QtGui.QVBoxLayout(self.verticalLayoutWidget_2)
        self.serverOptionsLayout.setMargin(0)
        self.serverOptionsLayout.setObjectName(_fromUtf8("serverOptionsLayout"))
        self.serverPromptLbl = QtGui.QLabel(self.verticalLayoutWidget_2)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Fixed, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.serverPromptLbl.sizePolicy().hasHeightForWidth())
        self.serverPromptLbl.setSizePolicy(sizePolicy)
        self.serverPromptLbl.setWordWrap(True)
        self.serverPromptLbl.setObjectName(_fromUtf8("serverPromptLbl"))
        self.serverOptionsLayout.addWidget(self.serverPromptLbl)
        self.urlLayout = QtGui.QHBoxLayout()
        self.urlLayout.setObjectName(_fromUtf8("urlLayout"))
        self.urlLbl = QtGui.QLabel(self.verticalLayoutWidget_2)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Preferred, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.urlLbl.sizePolicy().hasHeightForWidth())
        self.urlLbl.setSizePolicy(sizePolicy)
        self.urlLbl.setObjectName(_fromUtf8("urlLbl"))
        self.urlLayout.addWidget(self.urlLbl)
        self.urlTxt = QtGui.QLineEdit(self.verticalLayoutWidget_2)
        self.urlTxt.setObjectName(_fromUtf8("urlTxt"))
        self.urlLayout.addWidget(self.urlTxt)
        self.serverOptionsLayout.addLayout(self.urlLayout)
        self.optionTabs.addTab(self.serverTab, _fromUtf8(""))
        self.packagesTab = QtGui.QWidget()
        self.packagesTab.setObjectName(_fromUtf8("packagesTab"))
        self.verticalLayoutWidget_3 = QtGui.QWidget(self.packagesTab)
        self.verticalLayoutWidget_3.setGeometry(QtCore.QRect(9, 9, 381, 141))
        self.verticalLayoutWidget_3.setObjectName(_fromUtf8("verticalLayoutWidget_3"))
        self.packagesOptionsLayout = QtGui.QVBoxLayout(self.verticalLayoutWidget_3)
        self.packagesOptionsLayout.setMargin(0)
        self.packagesOptionsLayout.setObjectName(_fromUtf8("packagesOptionsLayout"))
        self.packagesPromptLbl = QtGui.QLabel(self.verticalLayoutWidget_3)
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Fixed, QtGui.QSizePolicy.Fixed)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.packagesPromptLbl.sizePolicy().hasHeightForWidth())
        self.packagesPromptLbl.setSizePolicy(sizePolicy)
        self.packagesPromptLbl.setObjectName(_fromUtf8("packagesPromptLbl"))
        self.packagesOptionsLayout.addWidget(self.packagesPromptLbl)
        self.FRCToolsDriverStationCheckbox = QtGui.QCheckBox(self.verticalLayoutWidget_3)
        self.FRCToolsDriverStationCheckbox.setObjectName(_fromUtf8("FRCToolsDriverStationCheckbox"))
        self.packagesOptionsLayout.addWidget(self.FRCToolsDriverStationCheckbox)
        self.FRCUtilitiesUpdateCheckbox = QtGui.QCheckBox(self.verticalLayoutWidget_3)
        self.FRCUtilitiesUpdateCheckbox.setObjectName(_fromUtf8("FRCUtilitiesUpdateCheckbox"))
        self.packagesOptionsLayout.addWidget(self.FRCUtilitiesUpdateCheckbox)
        self.FRCDriverStationUpdateCheckbox = QtGui.QCheckBox(self.verticalLayoutWidget_3)
        self.FRCDriverStationUpdateCheckbox.setObjectName(_fromUtf8("FRCDriverStationUpdateCheckbox"))
        self.packagesOptionsLayout.addWidget(self.FRCDriverStationUpdateCheckbox)
        self.optionTabs.addTab(self.packagesTab, _fromUtf8(""))
        self.tabLayout.addWidget(self.optionTabs)
        self.buttonLayout = QtGui.QHBoxLayout()
        self.buttonLayout.setObjectName(_fromUtf8("buttonLayout"))
        self.btnOK = QtGui.QPushButton(OptionsForm)
        self.btnOK.setObjectName(_fromUtf8("btnOK"))
        self.buttonLayout.addWidget(self.btnOK)
        self.btnCancel = QtGui.QPushButton(OptionsForm)
        self.btnCancel.setObjectName(_fromUtf8("btnCancel"))
        self.buttonLayout.addWidget(self.btnCancel)
        self.tabLayout.addLayout(self.buttonLayout)
        self.horizontalLayout_2.addLayout(self.tabLayout)
        self.horizontalLayout_2.setStretch(1, 1)

        self.retranslateUi(OptionsForm)
        self.optionTabs.setCurrentIndex(2)
        QtCore.QMetaObject.connectSlotsByName(OptionsForm)

    def retranslateUi(self, OptionsForm):
        OptionsForm.setWindowTitle(_translate("OptionsForm", "Team 4067 - 2013 FRC Software Installer - Options", None))
        self.notImplementedLbl.setText(_translate("OptionsForm", "Not implemented...", None))
        self.optionTabs.setTabText(self.optionTabs.indexOf(self.networkTab), _translate("OptionsForm", "&Network", None))
        self.serverPromptLbl.setText(_translate("OptionsForm", "You may specify a different server URL to download and install packages from.", None))
        self.urlLbl.setText(_translate("OptionsForm", "<html><head/><body><p><span style=\" font-weight:600;\">URL:</span></p></body></html>", None))
        self.optionTabs.setTabText(self.optionTabs.indexOf(self.serverTab), _translate("OptionsForm", "&Server", None))
        self.packagesPromptLbl.setText(_translate("OptionsForm", "You may select which packages you wish to download and install.", None))
        self.FRCToolsDriverStationCheckbox.setText(_translate("OptionsForm", "2013 FRC Tools and Driver Station", None))
        self.FRCUtilitiesUpdateCheckbox.setText(_translate("OptionsForm", "2013 FRC Utilities Update", None))
        self.FRCDriverStationUpdateCheckbox.setText(_translate("OptionsForm", "2013 FRC Driver Station Update", None))
        self.optionTabs.setTabText(self.optionTabs.indexOf(self.packagesTab), _translate("OptionsForm", "&Packages", None))
        self.btnOK.setText(_translate("OptionsForm", "&OK", None))
        self.btnCancel.setText(_translate("OptionsForm", "&Cancel", None))

import MainWindows_rc
