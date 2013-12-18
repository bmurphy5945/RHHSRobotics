#!/usr/bin/env python
# SpringDS v1.0 Beta 1 - a (cross-platform?) wrapper for the FRC Driver Station
# Copyright (C) 2012 River Hill HS Robotics Team (Albert H.)
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

import wx
import os, sys
import traceback
import pyNM
import threading
import time
import subprocess
import psutil

import SpringDSCrash
import SpringDSConfig

#import random

if sys.platform == 'win32':
    import win32api

# Variables

# Version
SPRINGDS_VERSION = "1.0 beta 1"

# Functions

# MsgBox - shows a message box.
# Types: wx.ICON_INFORMATION, wx.ICON_EXCLAMATION, wx.ICON_ERROR, wx.ICON_ERROR
def MsgBox(text, title, type, parent=None):
    dlg = wx.MessageDialog(parent, text, title, 
            wx.OK | type)
    dlg.ShowModal()

# ErrorBox - shows a error box with a Python traceback. Used if something crashed.
def ErrorBox(traceback):
    SDSCrashFrameInstance = SpringDSCrash.SDSCrashFrame(traceback)
    #app.SetTopWindow(SDSCrashFrameInstance)
    SDSCrashFrameInstance.MakeModal()
    SDSCrashFrameInstance.ShowModal()
    SDSCrashFrameInstance.MakeModal(False)
    
# Define notification event for thread completion updates
EVT_UPDATE_STATUS_ID = wx.NewId()
# Define notification event for window termination trigger
EVT_TERMINATE_WINDOW_ID = wx.NewId()
# Define notification event for unhooking the OnQuit window hook
EVT_UNHOOK_ONQUIT_ID = wx.NewId()
# Define notification event for showing an error box
EVT_ERROR_NOTIFY_ID = wx.NewId()

def EVT_UPDATE_STATUS(win, func):
    """Define Result Event."""
    win.Connect(-1, -1, EVT_UPDATE_STATUS_ID, func)

def EVT_TERMINATE_WINDOW(win, func):
    """Define Result Event."""
    win.Connect(-1, -1, EVT_TERMINATE_WINDOW_ID, func)

def EVT_UNHOOK_ONQUIT(win, func):
    """Define Result Event."""
    win.Connect(-1, -1, EVT_UNHOOK_ONQUIT_ID, func)

def EVT_ERROR_NOTIFY(win, func):
    """Define Result Event."""
    win.Connect(-1, -1, EVT_ERROR_NOTIFY_ID, func)
    
class IOStatusParser:
    def __init__(self, updatewin, oldio):
        self.updatewin = updatewin
        self.oldio = oldio
    def write(self, text):
        if text.strip() != "":
            partn = 0
            finaltext = ""
            for part in text.strip().split(" "):
                partn += 1
                if part != "":
                    if (part[0] == '[') and (part[-1] == ']'):
                        # Check
                        if (partn == 2) and (part == "[SpringDSFrame.on_paint]"):
                            finaltext = ""
                            break
                    else:
                        finaltext = " ".join(text.strip().split(" ")[partn-1:])
                        break
            wx.PostEvent(self.updatewin, UpdateStatusEvent(finaltext))
        self.oldio.write(text)
    '''def close(self):
        self.logfile.write(" Log Session End - "+date("%Y-%m-%d %H:%M:%S")+" **\n")
        self.logfile.close()'''
class UpdateStatusEvent(wx.PyEvent):
    """Event that carries the status update data."""
    def __init__(self, data):
        """Init Result Event."""
        wx.PyEvent.__init__(self)
        self.SetEventType(EVT_UPDATE_STATUS_ID)
        self.data = data

class TerminateWindowEvent(wx.PyEvent):
    """Event that tells the window thread to exit."""
    def __init__(self, data):
        """Init Result Event."""
        wx.PyEvent.__init__(self)
        self.SetEventType(EVT_TERMINATE_WINDOW_ID)
        self.data = data

class UnhookOnQuitEvent(wx.PyEvent):
    """Event that tells the window to unhook its OnQuit event."""
    def __init__(self, data):
        """Init Result Event."""
        wx.PyEvent.__init__(self)
        self.SetEventType(EVT_UNHOOK_ONQUIT_ID)
        self.data = data

class ErrorNotifyEvent(wx.PyEvent):
    """Event that tells the window something bad happened."""
    def __init__(self, data):
        """Init Result Event."""
        wx.PyEvent.__init__(self)
        self.SetEventType(EVT_ERROR_NOTIFY_ID)
        self.data = data
        
class SpringDS_NM(threading.Thread):
    def __init__(self, updatewin):
        threading.Thread.__init__(self)
        self.updatewin = updatewin
        self.stopNow = False
        self.oldstdout = sys.stdout
        self.pyNMHandle = None
        self.errBoxOpen = True
    def killSpringDS_NM(self):
        # Terminate this thread
        self.stopNow = True
    def checkKill(self):
        # Check for flag
        if self.stopNow == True:
            self.doKill()
    def setErrBoxClose(self):
        self.errBoxOpen = False
    def doKill(self):
        # Shut down pyNM
        print "[SpringDS] Terminating SpringDS and the Driver Station..."
        for proc in psutil.process_iter():
            if sys.platform == 'win32':
                if proc.name == "Dashboard.exe":
                    proc.terminate()
                if proc.name == "Driver Station.exe":
                    proc.terminate()
        
        #print "[SpringDS] Pausing for 3 seconds..."
        time.sleep(1)
        try:
            if self.pyNMHandle != None:
                self.pyNMHandle._disableRefresh(False)
                # By now, we've refreshed everything by force. Disable it again for a speedy exit.
                self.pyNMHandle._disableRefresh(True)
                if SpringDSConfig.getCfgNICType() == "wireless":
                    print "[SpringDS] Resetting wireless NIC to DHCP..."
                    self.pyNMHandle.setWirelessDHCP()
                elif SpringDSConfig.getCfgNICType() == "lan":
                    print "[SpringDS] Resetting LAN NIC to DHCP..."
                    self.pyNMHandle.setLANDHCP()
        except:
            # Allow termination
            wx.PostEvent(self.updatewin, UnhookOnQuitEvent(None))
        print "[SpringDS] Restoring I/O..."
        print "[SpringDS] Exiting..."
        sys.stdout = self.oldstdout
        wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
        exit()
    def run(self):
        out2status = IOStatusParser(self.updatewin, sys.stdout)
        sys.stdout = out2status
        self.checkKill()
        try:
            SpringDSConfig.initConfig()
        except:
            MsgBox("Couldn't load SpringDS configuration!", "Error - SpringDS", wx.ICON_ERROR)
            import traceback
            wx.PostEvent(self.updatewin, ErrorNotifyEvent("".join(traceback.format_exc())))
            while self.errBoxOpen:
                time.sleep(1)
            wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
            exit()
            #wx.PostEvent(self.updatewin, UnhookOnQuitEvent(None))
            #print "[SpringDS] Couldn't load configuration! Press ALT-F4 to exit."
        # Wrap all of this in a try/catch so that we can watch for errors and handle
        # them nicely!
        try:
            print "[SpringDS] Loading pyNetworkManager..."
            self.pyNMHandle = pyNM.pyNM()
            self.checkKill()
            print "[SpringDS] Using pyNetworkManager version "+str(self.pyNMHandle.getVersion())
            print "[SpringDS] Initializing pyNetworkManager..."
            #raise pyNM.pyNMError("test", pyNM.pyNM_ERROR_SYSTEM, -123)
            self.pyNMHandle.initNM()
            self.checkKill()
            self.pyNMHandle._disableRefresh()
            self.checkKill()
            
            if SpringDSConfig.getCfgNICType() == 'wireless':
                print "[SpringDS] Setting required FRC static IP and subnet on wireless NIC: "+SpringDSConfig.getCfgStaticIP()+" and "+SpringDSConfig.getCfgSubnet()
                self.pyNMHandle.setWirelessStaticIPAndSubnet(SpringDSConfig.getCfgStaticIP(), SpringDSConfig.getCfgSubnet())
            elif SpringDSConfig.getCfgNICType() == 'lan':
                print "[SpringDS] Setting required FRC static IP and subnet on LAN NIC: "+SpringDSConfig.getCfgStaticIP()+" and "+SpringDSConfig.getCfgSubnet()
                self.pyNMHandle.setLANStaticIPAndSubnet(SpringDSConfig.getCfgStaticIP(), SpringDSConfig.getCfgSubnet())
            self.checkKill()
            
            # Now launch the Driver Station (and wait)!
            print "[SpringDS] Launching FRC Driver Station..."
            subprocess.call(SpringDSConfig.getCfgDriverStationPath())
            self.checkKill()
            
            print "[SpringDS] Ensuring that everything has closed (and if not, terminating them)..."
            for proc in psutil.process_iter():
                if sys.platform == 'win32':
                    if proc.name == "Dashboard.exe":
                        proc.terminate()
                self.checkKill()
            self.checkKill()
            #print "[SpringDS] Pausing for 3 seconds..."
            #time.sleep(3)
            self.pyNMHandle._disableRefresh(False)
            # By now, we've refreshed everything by force. Disable it again for a speedy exit.
            self.pyNMHandle._disableRefresh(True)
            if SpringDSConfig.getCfgNICType() == 'wireless':
                print "[SpringDS] Resetting wireless NIC to DHCP..."
                self.pyNMHandle.setWirelessDHCP()
            elif SpringDSConfig.getCfgNICType() == 'lan':
                print "[SpringDS] Resetting LAN NIC to DHCP..."
                self.pyNMHandle.setLANDHCP()
            self.checkKill()
            print "[SpringDS] Exiting..."
            sys.stdout = self.oldstdout
            time.sleep(2)
            self.checkKill()
            wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
        except pyNM.pyNMError, err:
            # TODO: add more cases
            # Is this a permission error?
            if err.IsSystemError:
                # Is this a permission error? (This is a subset of the system error)
                if err.IsPermissionError:
                    # Are we on Windows?
                    if sys.platform == 'win32':
                        wx.PostEvent(self.updatewin, UnhookOnQuitEvent(None))
                        # Check args - this should be fixed later to actually check for elevation.
                        ELEVATED = 0
                        if len(sys.argv) > 1:
                            if sys.argv[1] == '--FLAG-SPRINGDS-INTERNAL-ELEVATION-LAUNCH':
                                # We tried elevating, it failed.
                                ELEVATED = 1
                                MsgBox("Could not set network config, even with elevation!", "Error - SpringDS", wx.ICON_ERROR)
                                self.doKill()
                                #wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                                #print "[SpringDS] Could not set network config, even with elevation. Press ALT-F4 to exit."
                        try:
                            if ELEVATED == 0:
                                #print "DEBUG: sys.argv is "+str(sys.argv)
                                #print "DEBUG: __file__ is "+str(__file__)
                                if getattr(sys, 'frozen', False):
                                    print "[SpringDS] Detected regular old script execution, so re-executing accordingly with elevation."
                                    #print "DEBUG: executing this:"
                                    print "'"+sys.executable+" "+" ".join(sys.argv[1:])+"'"
                                    win32api.ShellExecute(0, "runas", sys.executable, "'"+" ".join(sys.argv[1:])+"' --FLAG-SPRINGDS-INTERNAL-ELEVATION-LAUNCH", None, 1)
                                    time.sleep(3)
                                    wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                                elif sys.argv[0][len(sys.argv[0])-3:] == '.py':
                                    print "[SpringDS] Detected regular old script execution (2), so re-executing accordingly with elevation."
                                    #print "DEBUG: executing this:"
                                    print "'"+sys.executable+" '"+os.path.abspath(__file__)+"''"
                                    win32api.ShellExecute(0, "runas", sys.executable, '"'+os.path.abspath(__file__)+'"'+" --FLAG-SPRINGDS-INTERNAL-ELEVATION-LAUNCH", None, 1)
                                    time.sleep(3)
                                    wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                                elif __file__:
                                    print "[SpringDS] Detected that we're in a EXE, so executing accordingly with elevation."
                                    print "'"+__file__+" "+" '".join(sys.argv[1:])+"''"
                                    win32api.ShellExecute(0, "runas", __file__, "' ".join(sys.argv[1:])+"' --FLAG-SPRINGDS-INTERNAL-ELEVATION-LAUNCH", None, 1)
                                    time.sleep(3)
                                    wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                                else:
                                    MsgBox("Failed to determine script execution environment!", "Error - SpringDS", wx.ICON_ERROR)
                                    self.doKill()
                                    #wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                                    #print "[SpringDS] Failed to determine script execution environment! Press ALT-F4 to exit."
                                    
                        except:
                            MsgBox("Couldn't launch SpringDS in elevated mode!", "Error - SpringDS", wx.ICON_ERROR)
                            self.doKill()
                            #wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                            #print "[SpringDS] Couldn't launch SpringDS in elevated mode! Press ALT-F4 to exit."
            else:
                import traceback
                if err.exception != "":
                    wx.PostEvent(self.updatewin, ErrorNotifyEvent(err.exception+"\n\n"+"".join(traceback.format_exc())))
                else:
                    wx.PostEvent(self.updatewin, ErrorNotifyEvent("".join(traceback.format_exc())))
                while self.errBoxOpen:
                    time.sleep(1)
                self.doKill()
                #wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
                #print "[SpringDS] Code failed! Press ALT-F4 to exit. [Inner loop]"
                #wx.PostEvent(self.updatewin, UnhookOnQuitEvent(None))
                raise
        except SystemExit:
            pass
        except:
            import traceback
            print "GAHAHHHHHHHHH FAILURE!!!!"
            traceback.print_exc()
            wx.PostEvent(self.updatewin, ErrorNotifyEvent("".join(traceback.format_exc())))
            while self.errBoxOpen:
                time.sleep(1)
            self.doKill()
            #wx.PostEvent(self.updatewin, TerminateWindowEvent(None))
            #print "[SpringDS] Code failed! Press ALT-F4 to exit."
            #wx.PostEvent(self.updatewin, UnhookOnQuitEvent(None))
            raise
        

class SpringDSFrame(wx.Frame):
    """
    class Panel1 creates a panel for the tile image
    fw and fh are the width and height of the base frame
    """
    def __init__(self, parent, id, size):
        wx.Frame.__init__(self, parent, id, "SpringDS", size)
        
        # Create panel        
        self.panel = wx.Panel(self)
        self.panel.SetBackgroundColour((0, 0, 0))
        
        # Background setup
        print "[SpringDS] Loading image from: "+os.path.join(os.getcwd(), "stripebg.png")
        self.bmp1 = wx.Bitmap(os.path.join(os.getcwd(), "stripebg.png"))
        #self.panel.Bind(wx.EVT_ERASE_BACKGROUND, self.on_paint)
        
        self.SetIcon(wx.Icon("SpringDSIcon.ico", wx.BITMAP_TYPE_ICO, 16, 16))
        
        self.invalidated = True
        #wx.EVT_SIZE(self, self.on_size)
        # now put a button on the panel, on top of wallpaper
        #self.button1 = wx.Button(self.panel, -1, label='Button1', pos=(10, 5))
        self.logo = wx.StaticText(self.panel, -1, '', pos=(5,5))
        self.logo.SetBackgroundColour((0, 0, 0))
        self.logo.SetForegroundColour((255, 255, 255))
        font2 = wx.Font(16, wx.MODERN, wx.NORMAL, wx.BOLD)#, False, u'Comic Sans MS')
        self.logo.SetFont(font2)
        self.logo.SetLabel("SpringDS v"+SPRINGDS_VERSION+" - Team #4067")
        
        self.status = wx.StaticText(self.panel, -1, '', pos=(10,5))
        self.status.SetBackgroundColour((0, 0, 0))
        self.status.SetForegroundColour((255, 255, 255))
        font = wx.Font(16, wx.MODERN, wx.NORMAL, wx.BOLD)#, False, u'Comic Sans MS')
        self.status.SetFont(font)
        
        self.status2 = wx.StaticText(self.panel, -1, '', pos=(10,5))
        self.status2.SetBackgroundColour((0, 0, 0))
        self.status2.SetForegroundColour((255, 255, 255))
        font = wx.Font(16, wx.MODERN, wx.NORMAL, wx.BOLD)#, False, u'Comic Sans MS')
        self.status2.SetFont(font)
        #loading_gif_file = os.path.join(os.getcwd(), "loading_images", "loading" + str(random.randrange(1,6)) + ".gif")
        
        # DEPRECATED - doesn't work at all!
        #loading_widget = wxAnimPlus.AnimPlusCtrl(self, wx.ID_ANY, loading_gif_file, pos=(100,100))
        
        # BROKEN - buggy background
        #self.ag = wx.animate.GIFAnimationCtrl(self, id, loading_gif_file, pos=(100, 100))
        # clears the background
        #self.ag.GetPlayer().UseBackgroundColour(True)
        # continuously loop through the frames of the gif file (default)
        #self.ag.Play()
        
        # BROKEN FOR NOW
        #self.loading = wxAnimPlus.AnimPlusCtrl(self, id, loading_gif_file, pos=(100, 100))
        
        self.Show()
        self.Bind(wx.EVT_SIZE, self.on_size)
        self.Bind(wx.EVT_CLOSE, self.on_quit)
        self.SetSize(size)
        self.ShowFullScreen(True)
        
        # Connect the status update event to a callback
        EVT_UPDATE_STATUS(self, self.OnUpdateStatus)
        EVT_TERMINATE_WINDOW(self, self.OnTerminateWindow)
        EVT_UNHOOK_ONQUIT(self, self.OnUnhookOnQuit)
        EVT_ERROR_NOTIFY(self, self.OnErrorNotify)
        
        self.SpringDS_NM_Instance = SpringDS_NM(self)
        self.SpringDS_NM_Instance.start()
        
        self.quitTriggered = False
        self.hasTerminated = False
    def OnErrorNotify(self, event):
        # Show the error
        #print "CALLED: showing error!"
        ErrorBox('%s' % event.data)
        self.SpringDS_NM_Instance.setErrBoxClose()
        self.SetFocus()
    def OnUnhookOnQuit(self, event):
        self.Unbind(wx.EVT_CLOSE)
    def OnTerminateWindow(self, event):
        self.hasTerminated = True
        self.status2.SetLabel("SpringDS has terminated, closing this window...")
        self.Refresh()
        self.Update()
        time.sleep(2)
        self.Destroy()
    def on_quit(self, event):
        if (self.quitTriggered == False) and (self.hasTerminated == False):
            self.status2.SetLabel("Attempting to terminate SpringDS, please wait...")
            self.SpringDS_NM_Instance.killSpringDS_NM()
            self.quitTriggered = True
            self.status2.SetLabel("SpringDS is terminating, please wait...")
            for proc in psutil.process_iter():
                if sys.platform == 'win32':
                    if proc.name == "Dashboard.exe":
                        proc.terminate()
                    if proc.name == "Driver Station.exe":
                        proc.terminate()
        else:
            self.status2.SetLabel("SpringDS is already terminating, please wait...")
    def OnUpdateStatus(self, event):
        # Update the status text!
        if event.data != None:
            # Process data and set the label
            self.status.SetLabel('%s' % event.data)
    def on_paint(self, event):
        '''if self.invalidated == True:
            self.invalidated = False
        else:
            event.Skip()
            return'''
        print "[SpringDS] [SpringDSFrame.on_paint] Function called for painting!"
        # do the actual drawing on the memory dc here
        #dc = wx.PaintDC(self.panel)
        dc = event.GetDC()
 
        if not dc:
            dc = wx.ClientDC(self)
            rect = self.GetUpdateRegion().GetBox()
            dc.SetClippingRect(rect) 
        #dc.Clear()
        w, h = dc.GetSize()
        # Create BufferBmp and set the same size as the drawing area.
        #self.BufferBmp = wx.EmptyBitmap(w, h)
        #memdc = wx.MemoryDC()
        #memdc.Clear()
        #memdc.SelectObject(self.BufferBmp)
        #memdc.BeginDrawing()
        #dc.SetBackgroundMode(wx.TRANSPARENT)
        # get image width and height
        iw = self.bmp1.GetWidth()
        ih = self.bmp1.GetHeight()
        # tile/wallpaper the image across the canvas
        # Draw once to ensure it's done, even if it doesn't trigger from the for loop
        #dc.DrawBitmap(self.bmp1, 0, 0, True)
        for x in range(0, self.panel.GetSize()[0], iw):
            for y in range(0, self.panel.GetSize()[1], ih):
                dc.DrawBitmap(self.bmp1, x, y, True)
        #dc.Clear()
        #dc.EndDrawing()
        #dc.DrawBitmap(self.BufferBmp, 0, 0, True)
        #self.panel.Refresh()
    def on_size(self, event):
        print "[on_size] Invalidating draw state..."
        if (self.status != None):
            self.status.SetPosition((5, self.GetSize()[1] - 20 - 5))
        if (self.status2 != None):
            self.status2.SetPosition((5, self.GetSize()[1] - 20 - 20 - 5))
        #if (self.ag != None):
        #    self.ag.SetPosition((5, self.GetSize()[1] - 60 - 5))
        self.invalidated = True
        
        event.Skip()
print "SpringDS v"+str(SPRINGDS_VERSION)
bar = ""
for i in range(0, len("SpringDS v"+str(SPRINGDS_VERSION)) + 1):
    bar = bar + "="
print bar
print "Copyright (C) 2012 River Hill Robotics Team (Albert H.)\n"
app = wx.PySimpleApp()

# create a window/frame instance, no parent, -1 is default ID
fw = 320
fh = 240
try:
    SpringDSFrameInstance = SpringDSFrame(None, -1, size=(fw, fh))
    app.SetTopWindow(SpringDSFrameInstance)
    app.MainLoop()
except:
    print "[SpringDS] Crashed..."
    traceback.print_exc()
    if sys.platform == 'win32':
        raw_input("[SpringDS] Press ENTER to exit.")
