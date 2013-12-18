# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'LicenseAgreements.ui'
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

class Ui_LicenseAgreementsForm(object):
    def setupUi(self, LicenseAgreementsForm):
        LicenseAgreementsForm.setObjectName(_fromUtf8("LicenseAgreementsForm"))
        LicenseAgreementsForm.resize(427, 312)
        self.verticalLayout_2 = QtGui.QVBoxLayout(LicenseAgreementsForm)
        self.verticalLayout_2.setObjectName(_fromUtf8("verticalLayout_2"))
        self.licenseAgreementPromptLbl = QtGui.QLabel(LicenseAgreementsForm)
        self.licenseAgreementPromptLbl.setObjectName(_fromUtf8("licenseAgreementPromptLbl"))
        self.verticalLayout_2.addWidget(self.licenseAgreementPromptLbl)
        self.licenseTabs = QtGui.QTabWidget(LicenseAgreementsForm)
        self.licenseTabs.setObjectName(_fromUtf8("licenseTabs"))
        self.installerLicenseTab = QtGui.QWidget()
        sizePolicy = QtGui.QSizePolicy(QtGui.QSizePolicy.Expanding, QtGui.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.installerLicenseTab.sizePolicy().hasHeightForWidth())
        self.installerLicenseTab.setSizePolicy(sizePolicy)
        self.installerLicenseTab.setObjectName(_fromUtf8("installerLicenseTab"))
        self.horizontalLayout_2 = QtGui.QHBoxLayout(self.installerLicenseTab)
        self.horizontalLayout_2.setMargin(2)
        self.horizontalLayout_2.setObjectName(_fromUtf8("horizontalLayout_2"))
        self.installerLicenseLayout = QtGui.QVBoxLayout()
        self.installerLicenseLayout.setObjectName(_fromUtf8("installerLicenseLayout"))
        self.installerLicenseTxt = QtGui.QTextEdit(self.installerLicenseTab)
        self.installerLicenseTxt.setObjectName(_fromUtf8("installerLicenseTxt"))
        self.installerLicenseLayout.addWidget(self.installerLicenseTxt)
        self.horizontalLayout_2.addLayout(self.installerLicenseLayout)
        self.licenseTabs.addTab(self.installerLicenseTab, _fromUtf8(""))
        self.otherLicenseTab = QtGui.QWidget()
        self.otherLicenseTab.setObjectName(_fromUtf8("otherLicenseTab"))
        self.verticalLayout_5 = QtGui.QVBoxLayout(self.otherLicenseTab)
        self.verticalLayout_5.setMargin(2)
        self.verticalLayout_5.setObjectName(_fromUtf8("verticalLayout_5"))
        self.otherLicenseLayout = QtGui.QVBoxLayout()
        self.otherLicenseLayout.setObjectName(_fromUtf8("otherLicenseLayout"))
        self.otherLicenseTxt = QtGui.QTextEdit(self.otherLicenseTab)
        self.otherLicenseTxt.setObjectName(_fromUtf8("otherLicenseTxt"))
        self.otherLicenseLayout.addWidget(self.otherLicenseTxt)
        self.verticalLayout_5.addLayout(self.otherLicenseLayout)
        self.licenseTabs.addTab(self.otherLicenseTab, _fromUtf8(""))
        self.verticalLayout_2.addWidget(self.licenseTabs)
        self.radioBtnLayout = QtGui.QVBoxLayout()
        self.radioBtnLayout.setObjectName(_fromUtf8("radioBtnLayout"))
        self.acceptRadioBtn = QtGui.QRadioButton(LicenseAgreementsForm)
        self.acceptRadioBtn.setEnabled(False)
        self.acceptRadioBtn.setObjectName(_fromUtf8("acceptRadioBtn"))
        self.radioBtnLayout.addWidget(self.acceptRadioBtn)
        self.declineRadioBtn = QtGui.QRadioButton(LicenseAgreementsForm)
        self.declineRadioBtn.setChecked(True)
        self.declineRadioBtn.setObjectName(_fromUtf8("declineRadioBtn"))
        self.radioBtnLayout.addWidget(self.declineRadioBtn)
        self.verticalLayout_2.addLayout(self.radioBtnLayout)
        self.buttonLayout = QtGui.QHBoxLayout()
        self.buttonLayout.setObjectName(_fromUtf8("buttonLayout"))
        self.continueBtn = QtGui.QPushButton(LicenseAgreementsForm)
        self.continueBtn.setEnabled(False)
        self.continueBtn.setObjectName(_fromUtf8("continueBtn"))
        self.buttonLayout.addWidget(self.continueBtn)
        self.declineExitBtn = QtGui.QPushButton(LicenseAgreementsForm)
        self.declineExitBtn.setObjectName(_fromUtf8("declineExitBtn"))
        self.buttonLayout.addWidget(self.declineExitBtn)
        self.verticalLayout_2.addLayout(self.buttonLayout)

        self.retranslateUi(LicenseAgreementsForm)
        self.licenseTabs.setCurrentIndex(0)
        QtCore.QMetaObject.connectSlotsByName(LicenseAgreementsForm)

    def retranslateUi(self, LicenseAgreementsForm):
        LicenseAgreementsForm.setWindowTitle(_translate("LicenseAgreementsForm", "Team 4067 - 2013 FRC Software Installer - License Agreements", None))
        self.licenseAgreementPromptLbl.setText(_translate("LicenseAgreementsForm", "<html><head/><body><p><span style=\" font-weight:600;\">License Agreements</span></p></body></html>", None))
        self.installerLicenseTxt.setHtml(_translate("LicenseAgreementsForm", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:\'MS Shell Dlg 2\'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n"
"<p align=\"center\" style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:16pt; font-weight:600;\">Sample License Agreement</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">This is a License Agreement of intellectual property between:</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Toy Company Ltd, an USA Corporation doing business as The Toy Company Ltd, Their Address here, hereafter referred to as (the &quot;COMPANY&quot;);</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">And</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">[Inventor],</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">(the “LICENSOR”)</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Grant:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The LICENSOR hereby grants to the COMPANY the exclusive license for the invented product</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">described in Schedule A (the &quot;Licensed Item&quot;) and to use the know how to: manufacture, sell, market and distribute the Licensed Item. For consideration of the grant, COMPANY shall pay royalties of 5% of the sales price of the item. Conditions for renewal are timely payment of royalties in excess of $1.00 per quarter.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Payment of Royalties:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">Company will pay royalties within 31 days of the end of each quarter covering goods / each Licensed Item shipped during the quarter. Accompanying each royalty payment will be a list of accounts, which purchased the Licensed Item during that quarter and the amount of each sale. In the event of late payment, the COMPANY will pay interest on such delinquent amount at the rate per annum equal to 20%. Royalties will not be due for accounts, which have filed for bankruptcy or are insolvent or have not paid outstanding invoices for over 120 days and adjustments for these amounts may be made on subsequent royalty payments.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Termination:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">In the event that COMPANY does not ship this item before Dec 31, 2999, all rights conveyed from this license to COMPANY are null and void and all rights revert back to OWNER. If the COMPANY should discontinue the manufacturing of the Licensed Item, or should the COMPANY terminate its business or enter into liquidation, then all rights to said Licensed Item shall revert back to the OWNER forthwith.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Warranties:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Company represents and warrants to the Licensor that: (i) this Agreement constitutes the legal, valid and binding obligation of the Company enforceable against the Company in accordance with its terms and (ii) products based upon the Licensed Item will be of good quality in design material and workmanship and will be manufactured, sold and distributed in accordance with applicable laws and regulations.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Inventor/Licensor warrants that the subject of the license is original work and is wholely owned concept by the inventor and indemnifies licensee against claims from competing claims of ownership to the intellectual property, which is the subject of this license.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Indemnity:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Company shall indemnify and hold the Licensor, Inventor and Owner harmless from any claim, action, proceeding or judgment and all costs associated with same.</span></p></body></html>", None))
        self.licenseTabs.setTabText(self.licenseTabs.indexOf(self.installerLicenseTab), _translate("LicenseAgreementsForm", "2013 FRC Software Installer License", None))
        self.otherLicenseTxt.setHtml(_translate("LicenseAgreementsForm", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:\'MS Shell Dlg 2\'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n"
"<p align=\"center\" style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:16pt; font-weight:600;\">Sample License Agreement 2</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">This is a License Agreement of intellectual property between:</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Toy Company Ltd, an USA Corporation doing business as The Toy Company Ltd, Their Address here, hereafter referred to as (the &quot;COMPANY&quot;);</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">And</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">[Inventor],</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">(the “LICENSOR”)</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Grant:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The LICENSOR hereby grants to the COMPANY the exclusive license for the invented product</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">described in Schedule A (the &quot;Licensed Item&quot;) and to use the know how to: manufacture, sell, market and distribute the Licensed Item. For consideration of the grant, COMPANY shall pay royalties of 5% of the sales price of the item. Conditions for renewal are timely payment of royalties in excess of $1.00 per quarter.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Payment of Royalties:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">Company will pay royalties within 31 days of the end of each quarter covering goods / each Licensed Item shipped during the quarter. Accompanying each royalty payment will be a list of accounts, which purchased the Licensed Item during that quarter and the amount of each sale. In the event of late payment, the COMPANY will pay interest on such delinquent amount at the rate per annum equal to 20%. Royalties will not be due for accounts, which have filed for bankruptcy or are insolvent or have not paid outstanding invoices for over 120 days and adjustments for these amounts may be made on subsequent royalty payments.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Termination:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">In the event that COMPANY does not ship this item before Dec 31, 2999, all rights conveyed from this license to COMPANY are null and void and all rights revert back to OWNER. If the COMPANY should discontinue the manufacturing of the Licensed Item, or should the COMPANY terminate its business or enter into liquidation, then all rights to said Licensed Item shall revert back to the OWNER forthwith.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Warranties:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Company represents and warrants to the Licensor that: (i) this Agreement constitutes the legal, valid and binding obligation of the Company enforceable against the Company in accordance with its terms and (ii) products based upon the Licensed Item will be of good quality in design material and workmanship and will be manufactured, sold and distributed in accordance with applicable laws and regulations.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Inventor/Licensor warrants that the subject of the license is original work and is wholely owned concept by the inventor and indemnifies licensee against claims from competing claims of ownership to the intellectual property, which is the subject of this license.</span></p>\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px; font-size:8pt;\"><br /></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt; font-weight:600;\">Indemnity:</span></p>\n"
"<p style=\" margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><span style=\" font-size:8pt;\">The Company shall indemnify and hold the Licensor, Inventor and Owner harmless from any claim, action, proceeding or judgment and all costs associated with same.</span></p></body></html>", None))
        self.licenseTabs.setTabText(self.licenseTabs.indexOf(self.otherLicenseTab), _translate("LicenseAgreementsForm", "National Instruments™ License", None))
        self.acceptRadioBtn.setText(_translate("LicenseAgreementsForm", "I accept the terms in all 2 license agreements (20)", None))
        self.declineRadioBtn.setText(_translate("LicenseAgreementsForm", "I do not accept the terms in the license agreements", None))
        self.continueBtn.setText(_translate("LicenseAgreementsForm", "&Continue (20)", None))
        self.declineExitBtn.setText(_translate("LicenseAgreementsForm", "Decline and E&xit", None))

