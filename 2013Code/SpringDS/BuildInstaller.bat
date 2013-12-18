:: SpringDS v1.0 Beta 1 - a (cross-platform?) wrapper for the FRC Driver Station
:: Copyright (C) 2012 River Hill HS Robotics Team (Albert H.)
:: 
:: This program is free software: you can redistribute it and/or modify
:: it under the terms of the GNU General Public License as published by
:: the Free Software Foundation, either version 3 of the License, or
:: (at your option) any later version.
:: 
:: This program is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
:: GNU General Public License for more details.
:: 
:: You should have received a copy of the GNU General Public License
:: along with this program.  If not, see <http://www.gnu.org/licenses/>.
:: 
:: This is the SpringDS installer builder for Windows.
:: 
:: Building will be consolidated to the out/ directory.
:: 

@echo off

:: Variables

:: Where's MakeNSIS?
set MAKENSIS=C:\Program Files\NSIS\makensis.exe

:: ==========================================
:: That's it! Don't change anything below!
:: ==========================================

echo SpringDS Installer Builder
echo ===========================
echo Let's get building!

echo ===================================
echo Building the SpringDS installer...
echo ===================================
"%MAKENSIS%" SpringDSInstaller.nsi
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Building failed! See the above output for details.
    pause
    exit /B 1
)

IF "%1" NEQ "/NOPAUSE" (
    echo ==============================================================
    echo Building complete! Review the output above, then press ENTER.
    echo The installer may be found in out/.
    echo ==============================================================
    pause
)