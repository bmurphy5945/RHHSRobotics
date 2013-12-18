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
:: This is the SpringDS EXE builder for Windows.
:: 
:: We tend to make use of UPX, since it's a very nice compressor.
:: There should be a upx.exe binary inside the pyinstaller-1.5.1 folder.
:: If not, or if you would like to update it:
::   Download it from here:
::   http://upx.sourceforge.net/::downloadupx
::   Extract it to the pyinstaller-1.5.1 directory.
:: 
:: Building will be consolidated to the out/ directory.
:: 

@echo off

:: Variables

:: Where's Python? Specify the executable path here!
set PYTHON=C:\Python27\python.exe

:: ==========================================
:: That's it! Don't change anything below!
:: ==========================================

echo SpringDS EXE Builder
echo ========================
echo Let's get building!
rmdir /S /Q out 2> nullout.txt
mkdir out
del nullout.txt
echo ==============================
echo Configuring... (system check)
echo ==============================
%PYTHON% pyinstaller-1.5.1\Configure.py -C out\config.dat --upx-dir=pyinstaller-1.5.1\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Configuring failed! See the above output for details.
    pause
    exit /B 1
)
echo ==============================
echo Configuring SpringDS build...
echo ==============================
%PYTHON% pyinstaller-1.5.1\MakeSpec.py -C out\config.dat -o out\ -D -X -w --icon SpringDSIcon.ico -v SpringDSVersionInfo.versionrc SpringDS.py
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build configuration failed! See the above output for details.
    pause
    exit /B 1
)
echo ==============================
echo Building SpringDS...
echo ==============================
%PYTHON% pyinstaller-1.5.1\Build.py -C out\config.dat -y out\SpringDS.spec
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Building failed! See the above output for details.
    pause
    exit /B 1
)

echo =======================================
echo Configuring SpringDS Settings build...
echo =======================================
%PYTHON% pyinstaller-1.5.1\MakeSpec.py -C out\config.dat -o out\ -D -X -w --icon SpringDSSettingsIcon.ico -v SpringDSSettingsVersionInfo.versionrc SpringDSSettings.py
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build configuration failed! See the above output for details.
    pause
    exit /B 1
)
echo =======================================
echo Building SpringDS Settings...
echo =======================================
%PYTHON% pyinstaller-1.5.1\Build.py -C out\config.dat -y out\SpringDSSettings.spec
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Building failed! See the above output for details.
    pause
    exit /B 1
)

echo ====================================
echo Configuring SpringDS Panel build...
echo ====================================
%PYTHON% pyinstaller-1.5.1\MakeSpec.py -C out\config.dat -o out\ -D -X -w --icon SpringDSPanelIcon.ico -v SpringDSPanelVersionInfo.versionrc SpringDSPanel.py
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build configuration failed! See the above output for details.
    pause
    exit /B 1
)
echo ===========================
echo Building SpringDS Panel...
echo ===========================
%PYTHON% pyinstaller-1.5.1\Build.py -C out\config.dat -y out\SpringDSPanel.spec
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Building failed! See the above output for details.
    pause
    exit /B 1
)

echo ===================================
echo Consolidating files...
echo ===================================

move /Y out\dist\SpringDSSettings\* out\dist\SpringDS\
move /Y out\dist\SpringDSPanel\* out\dist\SpringDS\

echo ===================================
echo Copying additional files...
echo ===================================
copy License.rtf out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy LICENSE.txt out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy README.txt out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy stripebg.png out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy SpringDSSettingsHeader.png out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy SpringDSPanelHeader.png out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy SpringDSIcon.ico out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy SpringDSSettingsIcon.ico out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy SpringDSPanelIcon.ico out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)
copy SpringDS.ini out\dist\SpringDS\
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Copying failed! See the above output for details.
    pause
    exit /B 1
)

:: This doesn't work yet!
IF "%1" NEQ "/NOPAUSE" (
    echo ==============================================================
    echo Building complete! Review the output above, then press ENTER.
    echo The installation directory may be found at out/dist/SpringDS.
    echo ==============================================================
    pause
)
