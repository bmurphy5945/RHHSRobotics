#!/usr/bin/env python
# wxAnimPlus v0.1 Alpha - an extension to the wxPython animation class
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

###### DEPRECATED UNTIL FURTHER NOTICE ######

import random
import wx
from PIL import Image

# NOTE TO SELF: wx.animate is a piece of crap, use PIL frame by frame parsing and
# use http://wiki.wxpython.org/WorkingWithImages to convert to wxImage

class PILGIFImageSequence:
    def __init__(self, im):
        self.im = im
    def __getitem__(self, ix):
        try:
            if ix:
                self.im.seek(ix)
            return self.im
        except EOFError:
            raise IndexError # end of sequence

class AnimPlusCtrl(wx.PyControl):
    def __init__(self, parent, id=wx.ID_ANY, anim="", pos=wx.DefaultPosition, size=wx.DefaultSize, style=wx.NO_BORDER, validator=wx.DefaultValidator, name="AnimPlusCtrl"):
        wx.PyControl.__init__(self, parent, id, pos, size, style, validator, name)
        
        # Control setup
        self.SetInitialSize(size)
        self.SetPosition(pos)
        self.InheritAttributes()       
        print "Loading GIF animation from: "+anim
        self.animation = Image.open(anim)
        #res = self.LoadFile(anim)
        if (self.animation):
            self.frames = 0
            for frame in PILGIFImageSequence(self.animation):
                self.frames += 1 
            #self.frames = len(PILGIFImageSequence(self.animation))
            print "Total GIF frames: "+str(self.frames)
            print "GIF frame size: "+str(self.animation.size)
            if self.frames == 0:
                raise Exception("AnimPlusCtrl: Could not load image! (Empty GIF?)")
            # Reinitialize the image object so that we can seek back to frame 0.
            self.animation = Image.open(anim)
            self.curframe = 0
        else:
            raise Exception("AnimPlusCtrl: Could not load image!")
        self.image = wx.EmptyImage(*self.animation.size)
        self.TIMER_ID = random.randrange(999, 99999)
        self.timer = wx.Timer(self, self.TIMER_ID)
        self.timer.Start(1)
        self.Bind(wx.EVT_TIMER, self.on_timer)
        self.parent = parent
        # Fetch the first image so that we have a wxImage object
        #self.image = self.animation.GetFrame(1)
        self.parent.Bind(wx.EVT_PAINT, self.on_paint)
        self.Bind(wx.EVT_ERASE_BACKGROUND, self.OnEraseBackground)
        #self.on_timer(None)
    def on_timer(self, event):
        print "AnimPlusCtrl: on_timer called!"
        self.timer.Stop()
        print "DEBUG: self.animation: "+str(self.animation)
        if self.curframe <= self.frames:
            print "AnimPlusCtrl: Playing GIF frame "+str(self.curframe)+" out of "+str(self.frames)+" (PIL frame: "+str(self.animation.tell())+")"
            int = self.animation.info["duration"]
            RGBData = self.animation.convert('RGB').tostring()
            self.image.SetData(RGBData)
            RGBAData = self.animation.convert('RGBA').tostring()
            AlphaData = RGBAData[3::4]
            self.image.SetAlphaData(AlphaData)
            #self.image = self.animation.GetFrame(self.curframe);
            self.timer = wx.Timer(self.parent, self.TIMER_ID)
            self.Bind(wx.EVT_TIMER, self.on_timer)
            self.timer.Start(int)
            
            # Add to the frame number and seek to the next frame!
            self.curframe += 1
            self.animation.seek(self.curframe)
            print "END FRAME"
        pass
    def on_paint(self, event):
        bitmap = wx.BitmapFromImage(self.image)
        #dc = wx.PaintDC(self.parent)
        dc = wx.PaintDC(self)
        w, h = dc.GetSize()
        dc.Clear()
        dc.DrawBitmap(bitmap, 0, 0, True)
    def OnEraseBackground(self, event):
        # Anti-flicker :P
        pass
    
        
