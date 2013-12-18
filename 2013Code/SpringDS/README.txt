SpringDS v1.0 Beta 1 - a (cross-platform?) wrapper for the FRC Driver Station
==============================================================================
Copyright (C) 2012 River Hill HS Robotics Team (Albert H.)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

What is this?
--------------
This program serves as a wrapper to the FRC Driver Station.
It configures the network for you automatically - before AND after using
the Driver Station.

Currently, there is no configuration tool. Simply modify SpringDS.ini
(in the directory where SpringDS.exe resides) to change settings.

This program is NOT ready for public use. Sometime soon we'll modify the
program to be a bit more flexible and stable.

How do I use it?
-----------------
Connect to the wifi network first, then launch SpringDS. Enjoy! :)

In the future, we will implement wifi connection directly from SpringDS.

Building
---------
** This is only if you got the source distribution of SpringDS.
** If you installed this from the installer, this is not relevant
** for you.

For Windows, you can use the BuildEXE.bat script to perform a build.
You should edit/check the BuildEXE.bat script to make sure it points to the
correct path where Python 2.x is installed.

For other platforms... we haven't implemented the code yet!

To successfully build SpringDS, you need to install the following:
 = Pywin32 (includes module pythoncom)
 = PyWMI
 = psutil
 = wxPython

Currently disabled (but may be put to use again in the future):
 = PIL (Python Imaging Library)

And of course...
 = Python 2.x (I used 2.7)
That should be obvious if you're trying to build this!

To build the SpringDS installer, you need the following:
 = NSIS (Nullsoft Scriptable Install System)
 = ExperienceUI NSIS Plugin
 = A dummy skin to substitute the "Hawks" ExperienceUI skin (see below)

To build the installation/deployment package, use the BuildInstaller.bat
script to build it. You should edit/check the BuildInstaler.bat script to
make sure it points to the correct path where NSIS is installed.
We also use a custom ExperienceUI skin, so you may wish to replace "Hawks"
inside SpringDSInstaller.nsi to a different skin, or omit it altogether if
you don't care. Alternatively, you can copy a skin and rename it as "Hawks",
and develop the installer skin there.

Needless to say, you MUST modify the SpringDSInstaller.nsi file, or else it
WILL NOT BUILD!

To build EVERYTHING (the resulting EXE and the installer), run BuildAll.bat.

All (or at least most) of the files will be stored in the out/ directory,
in an attempt to keep the source tree clean. This isn't always the case
though - the PyInstaller tends to harbor cached compiled Python file
(*.pyc files), as well as a folder bincache01/ (or similar) that holds
the intermediate files when building SpringDS.

Simply said, if you don't mind those intermediate files, you can easily
clean up the build by removing out/. The compiled Python files are OK
to keep between code changes and builds. The intermediate files are also
OK, but if you update UPX, change Python versions, etc. you may desire to
remove the bincache directory as well.

Files
------
** Note: Some of these files may not exist, especially if you are using
** the installer.
BuildEXE.bat           - described above
BuildInstaller.bat     - described above
BuildAll.bat           - described above
LaunchSpringDS.bat     - this is used to launch SpringDS (ironic, eh?) in its
                         script form. Basically, it's only here to allow developers (us)
                         to see the output. Usually, once it's done, Python closes the
                         console window immediately. This allows us to see the output.
LaunchDHCPResetter.bat - this is used to launch the DHCP resetter, in case
                         SpringDS crashes and burns and you're too lazy to open
                         up the network settings. It also functions like its SpringDS
                         counterpart - letting us see the output.
Launch*.bat            - other launching scripts
License.rtf            - the license of this program, GNU GPL v3, in pretty formatted
                         RTF format.
LICENSE.txt            - the license of this program, GNU GPL v3, in plain text. Ugly. :P
loading_images/        - loading images that were going to be used for SpringDS, but
                         were discarded due to the issues with wxAnimPlus (see below)
pyinstaller-1.5.1      - PyInstaller distribution, included for convienence
pyNM.py                - pyNetworkManager library, our in-house (or rather, in-workshop)
                         library that handles the network configuration changing. This is
                         basically the gears of SpringDS.
ResetWirelessDHCP.py   - this is the script that does the DHCP resetting. It also serves as
                         a pyNM demo.
SpringDS.exe.manifest-DISABLED
                       - manifest for forcing elevation of SpringDS. For some reason,
                         PyInstaller considers it invalid and won't take it. Hence,
                         DISABLED.
SpringDS.py            - the actual application itself, in Python script form.
SpringDSConfig.py      - configuration class for SpringDS; handles all config related stuff
SpringDSCrash.py       - crash window class; handles any Pythonic SpringDS crashes
                         (bug report window)
SpringDSPanel.py       - SpringDS panel application, in Python script form
SpringDSInstaller.nsi  - the NSIS install script that is compiled into an installer
SpringDSVersionInfo.versionrc
                       - version resource that is used by PyInstaller to make the EXE.
stripebg.png           - PNG image of stripes that was going to be used for the SpringDS
                         window background... but everything kept failing. So, like our
                         friend wxAnimPlus (below), it's deserted as well, waiting for
                         a day when it can become useful...
*.svg                  - SVG source files for use in header and icon images
*.png/*.ico            - header images and icons
*.xcf                  - GIMP files; analogous to PSDs for Photoshop
*.wxg                  - wxGlade GUI prototype files, used for creating the GUIs with
                         clicks, not typing
wxAnimPlus.py          - wxPython GIF animation playback library. It doesn't work - it just
                         sits there in hoping of becoming useful one day. Currently, it
                         freezes the entire GUI, making this useless. Patches to fix this
                         are definitely welcome!