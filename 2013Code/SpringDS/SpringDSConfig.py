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
# SpringDS Configuration Management Class
# 
# Configuration Documentation
# ============================
# Sample configuration:
################################################################################
# [network]
# staticip = 10.40.67.9
# subnet = 255.0.0.0
# nictype = wireless
# cnicindex = 1
# teamip = 1
# teamid = 4067
# 
# [paths]
# driverstation = C:\Program Files\FRC Driver Station\Driver Station.exe
# dashboard = C:\Program Files\FRC Driver Station\Dashboard.exe
# 
# [springds]
# autoupdate = 1
################################################################################
# Explanation of fields:
#  network:
#   staticip      - the static IP that will be set.
#   subnet        - the subnet that will be set.
#   nictype       - the type of NIC that will be used.
#                   It can be "lan", "wireless", or "nicindex".
#                   The option "nicindex" specifies an index that will be
#                   used to access a certain NIC within the system's list.
#   cnicindex     - the NIC index in the system's list. This is used when
#                   nictype is "nicindex".
#   teamip        - whether to use the team number to automatically figure out
#                   the IP address or not. (1 for yes, 0 for no)
#   teamid        - the FRC team number/ID. This is used when teamip is 1.
#  paths:
#   driverstation - the path to the FRC Driver Station.
#   dashboard     - the path to the FRC Dashboard.
#  springds:
#   autoupdate    - whether to enable autoupdate or not. (1 for yes, 0 for no.)
# 
# These settings can be changed manually or in the SpringDS Settings program.
# 
################################################################################
# TODO:
#  - Implement SpringDSConfigError class and raise them as necessary
#  - Implement team # -> IP conversion
#  - More stringent error checking
# 

import os, sys
import ConfigParser
import re
import traceback

# Set up globals
global configFilePath
configFilePath = ""
global Config
global _hasInited
hasInited = False

# Default values
# WARNING: This is used to write new configs... AND check against old ones.
# Don't edit unless you are changing the config structure!
configData = {
                        "network"  : {
                                        "staticip"      : "10.40.67.9",
                                        "subnet"        : "255.0.0.0",
                                        "nictype"       : "wireless",
                                        "cnicindex"     : "1",
                                        "teamip"        : "1",
                                        "teamid"        : "4067"
                                     },
                        "paths"    : {
                                        "driverstation" : "C:\Program Files\FRC Driver Station\Driver Station.exe",
                                        "dashboard"     : "C:\Program Files\FRC Driver Station\Dashboard.exe"
                                     },
                        "springds" : {
                                        "autoupdate"    : "1"
                                     }
                    }

# SpringDSConfig error class
# TODO: add flag support, error types
class SpringDSConfigError(Exception):
    """Exception raised for errors in the input.
    Attributes:
        errMsg: [ARG - required]
            Explanation of the error.
        exception: [ARG - optional]
            Python exception that caused this error.
    """
    
    def __init__(self, errMsg, exception = ""):
        # Bind the variables (not really, more like copy) to the class
        self.errMsg = errMsg
        self.exception = exception
        
        Exception.__init__(self, errMsg)

def is_valid_ipv4(ip):
    """Validates IPv4 addresses.
    """
    pattern = re.compile(r"""
        ^
        (?:
          # Dotted variants:
          (?:
            # Decimal 1-255 (no leading 0's)
            [3-9]\d?|2(?:5[0-5]|[0-4]?\d)?|1\d{0,2}
          |
            0x0*[0-9a-f]{1,2}  # Hexadecimal 0x0 - 0xFF (possible leading 0's)
          |
            0+[1-3]?[0-7]{0,2} # Octal 0 - 0377 (possible leading 0's)
          )
          (?:                  # Repeat 0-3 times, separated by a dot
            \.
            (?:
              [3-9]\d?|2(?:5[0-5]|[0-4]?\d)?|1\d{0,2}
            |
              0x0*[0-9a-f]{1,2}
            |
              0+[1-3]?[0-7]{0,2}
            )
          ){0,3}
        |
          0x0*[0-9a-f]{1,8}    # Hexadecimal notation, 0x0 - 0xffffffff
        |
          0+[0-3]?[0-7]{0,10}  # Octal notation, 0 - 037777777777
        |
          # Decimal notation, 1-4294967295:
          429496729[0-5]|42949672[0-8]\d|4294967[01]\d\d|429496[0-6]\d{3}|
          42949[0-5]\d{4}|4294[0-8]\d{5}|429[0-3]\d{6}|42[0-8]\d{7}|
          4[01]\d{8}|[1-3]\d{0,9}|[4-9]\d{0,8}
        )
        $
    """, re.VERBOSE | re.IGNORECASE)
    return pattern.match(ip) is not None

# Internal function
# Thanks to the Python guys for this code snipplet!
# http://wiki.python.org/moin/ConfigParserExamples
def ConfigSectionMap(Config, section):
    dict1 = {}
    options = Config.options(section)
    for option in options:
        try:
            dict1[option] = Config.get(section, option)
            if dict1[option] == -1:
                DebugPrint("skip: %s" % option)
        except:
            print("exception on %s!" % option)
            dict1[option] = None
            raise SpringDSConfigError("Failed to create configuration map!", "".join(traceback.format_exc()))
    return dict1

# Write out configuration!
def writeConfig():
    global configFilePath
    global Config
    if configFilePath != "":
        try:
            cfg = open(configFilePath, "w")
            Config.write(cfg)
            cfg.close()
        except:
            raise SpringDSConfigError("Failed to save configuration!", "".join(traceback.format_exc()))
        # Reread config
        Config.read(configFilePath)
    else:
        print "[SpringDSConfig] W: Attempted to write config when configFilePath isn't defined yet."

# Finds and checks config, and then enables this module.
def initConfig():
    global configFilePath
    global Config
    global _hasInited
    Config = ConfigParser.ConfigParser()
    
    configFilePath = ""
    print "[SpringDSConfig] Trying to find and open config file... (path "+sys.path[0]+"\SpringDS.ini)"
    if sys.path[0] == "":
        if sys.argv[0] == "":
            print "[SpringDSConfig] Can't find absolute path - will try using the config file in current directory!"
            configFilePath = ".\SpringDS.ini"
            whichPath = "current_dir"
        else:
            print "[SpringDSConfig] Selecting config file in argv directory: "+sys.argv[0]+"\SpringDS.ini"
            whichPath = "argv"
            configFilePath = sys.argv[0]+"\SpringDS.ini"
    else:
        print "[SpringDSConfig] Selecting config file in path directory: "+sys.path[0]+"\SpringDS.ini"
        whichPath = "path"
        configFilePath = sys.path[0]+"\SpringDS.ini"
    
    if (os.path.isfile(configFilePath)):
        print "[SpringDSConfig] Found config file: "+configFilePath
    else:
        # Write some default values.
        print "[SpringDSConfig] No config file found, so writing a new one."
        for rsect in configData:
            Config.add_section(rsect)
            for rkey, value in rsect.items():
                Config.set(rsect, rkey, value)
        writeConfig()
    try:
        Config.read(configFilePath)
    except:
        raise SpringDSConfigError("Failed to read configuration!", "".join(traceback.format_exc()))
    
    # Now check the sections
    #print "DEBUG: cfg_sections set to "+str(Config.sections())
    cfg_sections = Config.sections()
    
    cur_sections = Config.sections()
    track_rsections = {}
    for rsect in configData:
        track_rsections[rsect] = 0
    for sect in cfg_sections:
        secisvalid = False
        #print "DEBUG: parsing sect "+sect
        for rsect in configData:
            #print "DEBUG:        rsect "+rsect
            if rsect == sect:
                #print "DEBUG:          Valid match!"
                # Increment counter
                track_rsections[sect] += 1
                # Remove the first match - this allows us to detect any duplicate sections!
                cur_sections.pop(cur_sections.index(sect))
                secisvalid = True
                break
        if secisvalid != True:
            print "[SpringDSConfig] W: Detected invalid section "+sect+", removing."
            Config.remove_section(sect)
            writeConfig()
    #print "DEBUG: cfg_sections is "+str(cfg_sections)
    # Check counter
    for rsect, count in track_rsections.items():
        if count > 1:
            print "[SpringDSConfig] W: Detected duplicate sections of "+rsect+", consolidating."
            config_backup = ConfigSectionMap(Config, rsect)
            while 1:
                try:
                    Config.remove_section(rsect)
                except:
                    break
            Config.add_section(rsect)
            for key, value in config_backup.items():
                Config.set(rsect, key, value)
            writeConfig()
        if count == 0:
            print "[SpringDSConfig] W: Detected missing section "+rsect+", writing defaults. (Count: "+str(count)+")"
            Config.add_section(rsect)
            
            for key, value in configData[rsect].items():
                Config.set(rsect, key, value)
            writeConfig()
        if count < 0:
            print "[SpringDSConfig] W: Negative count for section: "+rsect
    
    #####################
    # Check key validity
    #####################
    
    for rsect in configData:
        req_keys = configData[rsect]
        track_rkeys = {}
        for reqkey in req_keys:
            track_rkeys[reqkey] = 0
        
        cur_keys = []
        for key, value in ConfigSectionMap(Config, rsect).items():
            cur_keys.append(key)
        
        for key in ConfigSectionMap(Config, rsect):
            keyisvalid = False
            for rkey in req_keys:
                if rkey == key:
                    # Increment counter
                    track_rkeys[key] += 1
                    # Remove the first match - this allows us to detect any duplicate sections!
                    cur_keys.pop(cur_keys.index(key))
                    keyisvalid = True
            if keyisvalid != True:
                print "[SpringDSConfig] W: Detected invalid key "+key+" in section "+rsect+", removing."
                Config.remove_option(rsect, key)
                writeConfig()
        
        # Check counter
        for rkey, count in track_rkeys.items():
            if count > 1:
                print "[SpringDSConfig] W: Detected duplicate key "+rkey+" in section "+rsect+", consolidating."
                key_backup = ConfigSectionMap(Config, rsect)[key]
                while 1:
                    try:
                        Config.remove_option(rsect, rkey)
                    except:
                        break
                Config.set(rsect, rkey, key_backup)
                writeConfig()
            if count == 0:
                print "[SpringDSConfig] W: Detected missing key "+rkey+" in section "+rsect+", writing defaults."
                Config.set(rsect, rkey, configData[rsect][rkey])
                writeConfig()
            if count < 0:
                print "[SpringDSConfig] W: Negative count for key: "+rsect
    # We're ready!
    _hasInited = True
    print "[SpringDSConfig] Configuration initialized!"

def getCfgStaticIP():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgStaticIP called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if getCfgTeamIP() == "1":
        # Translate team number into an IP address!
        team = getCfgTeamID()
        if (len(team) > 4) or (len(team) < 1):
            raise SpringDSConfigError("Couldn't get static IP: Invalid team number specified! (TeamIP is enabled)", "".join(traceback.format_exc()))
        elif len(team) == 4:
            return "10."+team[:2]+"."+team[2:]+".6"
        elif len(team) == 3:
            return "10."+team[:1]+"."+team[1:]+".6"
        elif len(team) <= 2:
            return "10.0."+team+".6"
    if is_valid_ipv4(ConfigSectionMap(Config, "network")["staticip"]):
        return ConfigSectionMap(Config, "network")["staticip"]
    else:
        raise SpringDSConfigError("Couldn't get static IP: Static IP specified is invalid!", "".join(traceback.format_exc()))
        #return configData["network"]["staticip"]
# NOTE THE abs ADDITION
def setCfgAbsStaticIP(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgStaticAbsIP called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if is_valid_ipv4(val):
        return Config.set("network", "staticip", val)
    else:
        raise SpringDSConfigError("Couldn't set static IP: Static IP specified is invalid!", "".join(traceback.format_exc()))
# NOTE THE abs ADDITION
def getCfgAbsStaticIP():
    global _hasInited
    global Config
    if is_valid_ipv4(ConfigSectionMap(Config, "network")["staticip"]):
        return ConfigSectionMap(Config, "network")["staticip"]
    else:
        raise SpringDSConfigError("Couldn't get absolute static IP: Static IP specified is invalid!", "".join(traceback.format_exc()))
        #return configData["network"]["staticip"]
def getCfgSubnet():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgSubnet called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if is_valid_ipv4(ConfigSectionMap(Config, "network")["staticip"]):
        return ConfigSectionMap(Config, "network")["subnet"]
    else:
        # TODO: error/fix IP
        raise SpringDSConfigError("Couldn't get subnet: Subnet specified is invalid!", "".join(traceback.format_exc()))
        #return configData["network"]["subnet"]
def setCfgSubnet(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgSubnet called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if is_valid_ipv4(val):
        return Config.set("network", "subnet", val)
    else:
        # TODO: error
        raise SpringDSConfigError("Couldn't set subnet: Subnet specified is invalid!", "".join(traceback.format_exc()))
def getCfgNICType():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgNICType called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if (ConfigSectionMap(Config, "network")["nictype"] == "wireless") or (ConfigSectionMap(Config, "network")["nictype"] == "lan") or (ConfigSectionMap(Config, "network")["nictype"] == "nic"):
        return ConfigSectionMap(Config, "network")["nictype"]
    else:
        # TODO: error/fix
        raise SpringDSConfigError("Couldn't get NIC type: NIC type specified is invalid!", "".join(traceback.format_exc()))
        #return "lan"
def setCfgNICType(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgNICType called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if (val == "wireless") or (val == "lan") or (val == "nic"):
        return Config.set("network", "nictype", val)
    else:
        raise SpringDSConfigError("Couldn't set NIC type: NIC type specified is invalid!", "".join(traceback.format_exc()))
def getCfgTeamIP():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgTeamIP called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if (ConfigSectionMap(Config, "network")["teamip"] == "0") or (ConfigSectionMap(Config, "network")["teamip"] == "1"):
        return ConfigSectionMap(Config, "network")["teamip"]
    else:
        #TODO: fix it?
        raise SpringDSConfigError("Couldn't get team number to IP use option (TeamIP): invalid value!", "".join(traceback.format_exc()))
def setCfgTeamIP(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgTeamIP called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if (val == "0") or (val == "1"):
        return Config.set("network", "teamip", val)
    else:
        raise SpringDSConfigError("Couldn't set team number to IP use option (TeamIP): invalid value!", "".join(traceback.format_exc()))
def getCfgTeamID():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgTeamID called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    return ConfigSectionMap(Config, "network")["teamid"]
def setCfgTeamID(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgTeamID called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    return Config.set("network", "teamid", val)
def getCfgDriverStationPath():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgDriverStationPath called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    return ConfigSectionMap(Config, "paths")["driverstation"]
def setCfgDriverStationPath(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgDriverStationPath called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    return Config.set("paths", "driverstation", val)
def getCfgDashboardPath():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgDashboardPath called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    return ConfigSectionMap(Config, "paths")["dashboard"]
def setCfgDashboardPath(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgDashboardPath called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    return Config.set("paths", "dashboard", val)
def getCfgAutoUpdateOpt():
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: getCfgAutoUpdateOpt called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if (ConfigSectionMap(Config, "springds")["autoupdate"] == "0") or (ConfigSectionMap(Config, "springds")["autoupdate"] == "1"):
        return ConfigSectionMap(Config, "springds")["autoupdate"]
    else:
        raise SpringDSConfigError("Couldn't get SpringDS auto update option (autoupdate): invalid value!", "".join(traceback.format_exc()))
def setCfgAutoUpdateOpt(val):
    global _hasInited
    global Config
    if _hasInited == False:
        print "[SpringDSConfig] E: setCfgAutoUpdateOpt called without initing!"
        raise SpringDSConfigError("SpringDSConfig hasn't initialized yet!", "".join(traceback.format_exc()))
    if (val == "0") or (val == "1"):
        return Config.set("springds", "autoupdate", val)
    else:
        raise SpringDSConfigError("Couldn't get SpringDS auto update option (autoupdate): invalid value!", "".join(traceback.format_exc()))