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
:: This is the SpringDS complete builder for Windows. It simply runs both
:: batch scripts.
:: 
:: Thanks to these websites for some helpful resources!
:: http://blog.borngeek.com/2008/05/22/exiting-batch-file-contexts/
:: http://www.windowsitpro.com/article/server-management/how-do-i-call-a-batch-file-from-within-another-batch-file-
:: 
:: Building will be consolidated to the out/ directory.
:: 

@echo off

:: Variables... none.

:: ==========================================
:: That's it! Don't change anything below!
:: ==========================================

echo SpringDS Builder (All)
echo =======================
echo Let's get building!

echo ==========================================
echo Launching the SpringDS builder...
echo ==========================================
call BuildEXE.bat /NOPAUSE
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Building failed! See the above output for details.
    pause
    exit 1
)

echo ==========================================
echo Launching the SpringDS install builder...
echo ==========================================
call BuildInstaller.bat /NOPAUSE
IF %ERRORLEVEL% NEQ 0 (
    echo ERROR: Building failed! See the above output for details.
    pause
    exit 1
)

echo ==============================================================
echo Building complete! Review the output above, then press ENTER.
echo The installer may be found in out/.
echo ==============================================================
pause
