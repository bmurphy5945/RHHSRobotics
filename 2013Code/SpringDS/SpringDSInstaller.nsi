; SpringDS v1.0 Beta 1 - a (cross-platform?) wrapper for the FRC Driver Station
; Copyright (C) 2012 River Hill HS Robotics Team (Albert H.)
; 
; This program is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
; 
; This program is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
; 
; You should have received a copy of the GNU General Public License
; along with this program.  If not, see <http://www.gnu.org/licenses/>.
; 
; This is the SpringDS NSIS installation script, which will be compiled
; into an installer by NSIS.
;
 
; Some variables and stuff
Name "SpringDS"

SetCompressor /final /solid lzma

!define XPUI_ABORTWARNING
;--------------------------------
;Include ExperienceUI

  !ifdef XPUI_SYSDIR
    !include "${XPUI_SYSDIR}\XPUI.nsh"
  !else
    ; just hope NSIS has XPUI.nsh installed
    !include "XPUI.nsh"
  !endif

!define XPUI_SKIN "Hawks"
# No need to be too pretty...
#!define       XPUI_WANSIS
#!define XPUI_WANSIS_SKIN Bliss
;--------------------------------

!define XPUI_FINISHPAGE_RUN
!define XPUI_FINISHPAGE_RUN_TEXT "Launch SpringDS"
!define XPUI_FINISHPAGE_RUN_FUNCTION "LaunchSpringDS"
!define XPUI_FINISHPAGE_RUN_CHECKED
!define XPUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!define XPUI_FINISHPAGE_SHOWREADME $INSTDIR\README.txt

Function LaunchSpringDS
  ExecShell "" "$SMPROGRAMS\$(^Name)\$(^Name).lnk"
FunctionEnd

Caption "$(^Name) v1.0 Setup"
OutFile "out\SpringDS-installer-1.0b1.exe"
InstallDir "$PROGRAMFILES\SpringDS"
InstallDirRegKey HKLM "Software\SpringDS" ""
RequestExecutionLevel admin

${Page} Welcome
${LicensePage} "License.rtf"
${Page} Directory
${Page} InstConfirm
${Page} InstFiles
${Page} Finish
${Page} Abort

${UnPage} Welcome
${UnPage} UnConfirm
${UnPage} InstFiles
${UnPage} Finish

${Language} English

!define APPNAME "SpringDS"
!define COMPANYNAME "River Hill HS Robotics Team (FRC #4067) / Albert H."
!define DESCRIPTION "SpringDS FRC Driver Station Launcher"
# These three must be integers
!define VERSIONMAJOR 1
!define VERSIONMINOR 0
!define VERSIONBUILD 1
# These will be displayed by the "Click here for support information" link in "Add/Remove Programs"
!define HELPURL "http://code.google.com/p/frcbot4067/" # "Support Information" link
!define UPDATEURL "http://code.google.com/p/frcbot4067/" # "Product Updates" link
!define ABOUTURL "http://code.google.com/p/frcbot4067/" # "Publisher" link
# This is the size (in kB) of all the files copied into "Program Files"
!define INSTALLSIZE 14947

Section
  SetOutPath $INSTDIR
  File /r out\dist\SpringDS\*
  
  ; Write install dir
  WriteRegStr HKLM "Software\SpringDS" "" "$INSTDIR"
  
  # Start Menu
  CreateDirectory "$SMPROGRAMS\$(^Name)"
  CreateShortCut "$SMPROGRAMS\$(^Name)\$(^Name).lnk" "$INSTDIR\$(^Name).exe" "" "$INSTDIR\$(^Name).exe"
  CreateShortCut "$SMPROGRAMS\$(^Name)\$(^Name) Settings.lnk" "$INSTDIR\$(^Name)Settings.exe" "" "$INSTDIR\$(^Name)Settings.exe"
  CreateShortCut "$SMPROGRAMS\$(^Name)\$(^Name) Panel.lnk" "$INSTDIR\$(^Name)Panel.exe" "" "$INSTDIR\$(^Name)Panel.exe"
  CreateShortCut "$SMPROGRAMS\$(^Name)\Uninstall $(^Name).lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe"
  
  # Desktop shortcut!
  CreateShortCut "$DESKTOP\$(^Name).lnk" "$INSTDIR\$(^Name).exe" "" "$INSTDIR\$(^Name).exe"
  
  ; Write uninstaller
  WriteUninstaller "$INSTDIR\uninstall.exe"
  
  ; Registry stuff
  # Registry information for add/remove programs
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "DisplayName" "${APPNAME}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "InstallLocation" "$\"$INSTDIR$\""
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "DisplayIcon" "$\"$INSTDIR\$(^Name).exe$\""
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "Publisher" "${COMPANYNAME}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "HelpLink" "${HELPURL}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "URLUpdateInfo" "${UPDATEURL}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "URLInfoAbout" "${ABOUTURL}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "DisplayVersion" "${VERSIONMAJOR}.${VERSIONMINOR}.${VERSIONBUILD}"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "VersionMajor" ${VERSIONMAJOR}
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "VersionMinor" ${VERSIONMINOR}
  # There is no option for modifying or repairing the install
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "NoRepair" 1
  # Set the INSTALLSIZE constant (!defined at the top of this script) so Add/Remove Programs can accurately report the size
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "EstimatedSize" ${INSTALLSIZE}

SectionEnd

Section "Uninstall"
  Delete "$INSTDIR\uninstall.exe"
  Delete "$SMPROGRAMS\$(^Name)\$(^Name).lnk"
  Delete "$SMPROGRAMS\$(^Name)\$(^Name) Settings.lnk"
  Delete "$SMPROGRAMS\$(^Name)\$(^Name) Panel.lnk"
  Delete "$SMPROGRAMS\$(^Name)\Uninstall $(^Name).lnk"
  Delete "$DESKTOP\$(^Name).lnk"
  # Try to remove the Start Menu folder - this will only happen if it is empty
  RmDir /r "$SMPROGRAMS\$(^Name)"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}"
  DeleteRegKey HKLM "Software\SpringDS"
  
  ; RmDir /r removes directories with files.
  
  RmDir /r "$INSTDIR\"
  
SectionEnd