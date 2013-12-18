#!/usr/bin/env python
# pyNetworkManager v1.1 - cross-platform Python network management library
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
# Special thanks to these wonderful websites:
# http://www.python-forum.org/pythonforum/viewtopic.php?f=5&t=10133
# http://pypi.python.org/pypi/WMI/#downloads
# http://timgolden.me.uk/python/wmi/tutorial.html
# http://timgolden.me.uk/python/wmi/cookbook.html
# http://msdn.microsoft.com/en-us/library/windows/desktop/aa394216%28v=vs.85%29.aspx
# http://msdn.microsoft.com/en-us/library/windows/desktop/aa394217%28v=vs.85%29.aspx
# http://www.google.com/#sclient=psy-ab&hl=en&safe=active&site=&source=hp&q=wmi+network+adapter+python&pbx=1&oq=wmi+network+adapter+python&aq=f&aqi=&aql=&gs_sm=e&gs_upl=252l5148l0l5366l26l19l0l7l7l2l369l4105l1.7.7.4l26l0&bav=on.2,or.r_gc.r_pw.,cf.osb&fp=ad65ee382c54199f&biw=1024&bih=516
# http://stackoverflow.com/questions/83756/how-to-programmatically-enable-disable-network-interfaces-windows-xp
# http://stackoverflow.com/questions/7580834/script-to-change-ip-address-on-windows
# http://us.generation-nt.com/answer/win32-networkadapter-wireless-help-25835992.html
# http://weblogs.sqlteam.com/mladenp/archive/2010/11/04/find-only-physical-network-adapters-with-wmi-win32_networkadapter-class.aspx
# http://www.google.com/search?q=python%20change%20network%20settings&ie=utf-8&oe=utf-8&aq=t&rls=org.mozilla:en-US:official&client=firefox-a&source=hp&channel=np
# 
# API:
# pyNM.initNM()
#   This initializes pyNM. Initialization includes probing for the network devices and sorting them.
# pyNM.setWirelessSubnet(subnet_as_string)
#   This sets the subnet for the wireless NIC.
# pyNM.setLANSubnet(subnet_as_string)
#   This sets the subnet for the Ethernet NIC.
# pyNM.setWirelessStaticIP(static_ip_as_string)
#   This sets the static IP for the wireless NIC.
# pyNM.setLANStaticIP(static_ip_as_string)
#   This sets the static IP for the Ethernet NIC.
# pyNM.setWirelessDHCP()
#   This enables DHCP for the wireless NIC.
# pyNM.setLANDHCP()
#   This enables DHCP for the Ethernet NIC.
# pyNM.setNICSubnet(pyNM_NIC, subnet_as_string)
#   This sets the subnet for a NIC specified in the arguments.
# pyNM.setNICStaticIP(pyNM_NIC, static_ip_as_string)
#   This sets the static IP for a NIC specifid in the arguments.
# pyNM.setNICDHCP(pyNM_NIC)
#   This enables DHCP for a NIC specified in the arguments.
# pyNM.getNICs()
#   This returns an array of NICs detected by pyNM.
# 
# Types: (not really)
# pyNM_NetworkDevice (actual type: class with read-only properties)
#   The type for a network device detected by pyNM.
#   This type is used to identify NICs passed through the setNIC* method.
#   Includes:
#      "name"
#         The NIC name. This is the manufacturer's name for its NIC.
#         This is read-only, obviously. You'll have to hack the kernel
#         if you want to change the NIC name... ;)
#      "control"
#         Platform dependent handle for the device.
#         This handle can be used if you wish for a more fine tuned control
#         over the NIC. The methods for controlling this handle are NOT
#         cross-platform. For instance, on Windows, the handle is
#         controlled with the Python WMI API. This is read-only, since
#         pyNM depends on this to run, and it would be kinda odd to mess
#         around with a handle...
#      "sysaddr"
#         Platform dependent system address for the device.
#         This can be used if you wish to know the direct address to access
#         the NIC. This is NOT cross-platform. You must have your own code
#         to handle the address. For instance, on Windows, this uses
#         PNPDeviceID, which looks like PCI\VEN_NNEC&DEV_NNNN&SUBSYS_NNLNNNNN&RE
#         (where N is a number, L is a letter). On Linux, it might be a /dev path.
#         This is obviously read only, but pyNM will not be using this value anyway
#         for its operation.
#      "type"
#         The type of the NIC. Can either be "wireless" or "ethernet."
#         This is obviously read only, but pyNM will not be using this value anyway
#         for its operation.
#   Methods:
#      getName()
#        Returns the name of the NIC.
#      getControlHandle()
#        Returns the platform dependent handle for the device.
#        This is the method equivalent to the read-only type element "control."
#        See the above documentation for details.
#      getSysAddr()
#        Returns the platform dependent system address for the device.
#        This is the method equivalent to the read-only type element "sysaddr."
#        See the above documentation for details.
#      getType()
#        Returns the type of the NIC. Can either be "wireless" or "ethernet."
#        This is the method equivalent to the read-only type element "type."
# ## READ-ONLY PYTHON ATTRIBUTES:
# ## http://bright-green.com/blog/2004_06_03/read_only_python_attributes.html
# ## http://devinfee.com/blog/2010/11/07/so-you-want-some-read-only-python-vars/
# 

import sys

# TODO: Only load when class loads
if sys.platform == 'win32':
    # Load the Python modules
    import wmi
    import pythoncom

# Variables

# Version - don't change.
pyNM_VERSION = "1.1"

# Error flags

# Error in the pyNM library code
pyNM_ERROR_BUG = 1

# Error in the pyNM client's code
pyNM_ERROR_CLIENT = 2

# Error in the system (i.e. connection fails, system problems, etc.)
pyNM_ERROR_SYSTEM = 3

# That's... probably all of the errors for now. :P
# When more features are added later, more error flags will suffice.

# pyNetworkManager error class
# TODO: multiple flag support
class pyNMError(Exception):
    """Exception raised for errors in the input.
    Attributes:
        errMsg: [ARG - required]
            Explanation of the error.
        errFlag: [ARG - required]
            A flag indicating what kind of pyNM error this is.
        sysErrNum: [ARG - required]
            The platform dependent error number. Platform specific error numbers
            are parsed and translated into more portable error flags.
        exception: [ARG - optional]
            Python exception that caused this error.
        IsSystemError:
            Boolean that determines whether the error was a system related error or
            not.
        IsPermissionError:
            Boolean that determines whether the error was a permission error or not.
            This is a subset of the system error.
        IsClientError:
            Boolean that determines whether the error was a developer (client coding)
            error or not.
        IsLibraryError:
            Boolean that determines whether the error was a library error or not
            (library runtime error unrelated to the user/client).
        IsUnknownError:
            Boolean that determines whether the error was an unknown error or not.
    """
    
    # By default, sysErrNum is 0 unless there's a use for it, in which it will be set.
    def __init__(self, errMsg, errFlag, sysErrNum = 0, exception = ""):
        # Bind the variables (not really, more like copy) to the class
        self.errMsg = errMsg
        self.errFlag = errFlag
        self.sysErrNum = sysErrNum
        self.exception = exception
        
        # Detailed error processing
        if self.errFlag == pyNM_ERROR_SYSTEM:
            if sys.platform == 'win32':
                # Check to see if this is a WMI permission error or not
                # The error code is actually -2147217405, but...
                if self.sysErrNum < 0:
                    self.IsPermissionError = True
                else:
                    self.IsPermissionError = False
            else:
                # No other platforms implemented yet, so just assume that it isn't special.
                self.IsPermissionError = False
            self.IsSystemError = True
            self.IsUnknownError = False
            self.IsClientError = False
            self.IsLibraryError = False
        elif self.errFlag == pyNM_ERROR_CLIENT:
            # Software developer's error (the client of this library)
            self.IsUnknownError = False
            self.IsClientError = True
            self.IsPermissionError = False
            self.IsSystemError = False
            self.IsLibraryError = False
        elif self.errFlag == pyNM_ERROR_BUG:
            # This code's error (library)
            self.IsUnknownError = False
            self.IsClientError = False
            self.IsPermissionError = False
            self.IsSystemError = False
            self.IsLibraryError = True
        else:
            # Oh my! An UNKNOWN error! GAHHHHHHHHHHHH
            self.IsUnknownError = True
            self.IsClientError = False
            self.IsPermissionError = False
            self.IsSystemError = False
            # This error boolean may change in the future - does an unknown error count
            # as a bug?
            self.IsLibraryError = False
        Exception.__init__(self, errMsg)

# pyNetworkManager main class
# This class serves as a wrapper/bootstapper for the
# platform specific pyNM classes.
class pyNM:
    def __init__(self, platform = None):
        # Some self-advertisement
        print "[pyNM] "+"pyNetworkManager v"+str(pyNM_VERSION)
        bar = ""
        for i in range(0, len("pyNetworkManager v"+str(pyNM_VERSION)) + 1):
            bar = bar + "="
        print "[pyNM] "+bar
        print "[pyNM] "+"Copyright (C) 2012 River Hill Robotics Team (Albert H.)\n"+"[pyNM] "
        # Give this class a version variable
        self.version = pyNM_VERSION
        if platform is not None:
            if platform not in ['win32', 'linux2', 'darwin']:
                raise pyNMError("Either the platform specified is invalid or it isn't supported at this time.", pyNM_ERROR_CLIENT)
            print "[pyNM] WARNING: Platform choice forced to "+platform+"."
        else:
            platform = sys.platform
        print "[pyNM] Platform: "+platform
        if platform == 'win32': # Windows! Yay!
            pyNM_Win32_Handle = pyNM_Win32()
            self.initNM = pyNM_Win32_Handle.initNM
            self.setWirelessSubnet = pyNM_Win32_Handle.setWirelessSubnet
            self.setLANSubnet = pyNM_Win32_Handle.setLANSubnet
            self.setWirelessStaticIP = pyNM_Win32_Handle.setWirelessStaticIP
            self.setLANStaticIP = pyNM_Win32_Handle.setLANStaticIP
            self.setWirelessDHCP = pyNM_Win32_Handle.setWirelessDHCP
            self.setLANDHCP = pyNM_Win32_Handle.setLANDHCP
            self.setNICSubnet = pyNM_Win32_Handle.setNICSubnet
            self.setNICStaticIP = pyNM_Win32_Handle.setNICStaticIP
            self.setNICDHCP = pyNM_Win32_Handle.setNICDHCP
            self.getNICs = pyNM_Win32_Handle.getNICs
            self.setWirelessStaticIPAndSubnet = pyNM_Win32_Handle.setWirelessStaticIPAndSubnet
            self.setLANStaticIPAndSubnet = pyNM_Win32_Handle.setLANStaticIPAndSubnet
            self.setNICStaticIPAndSubnet = pyNM_Win32_Handle.setNICStaticIPAndSubnet
            self._disableRefresh = pyNM_Win32_Handle._disableRefresh
        elif platform == 'linux2': # Linux! Yay!
            pyNM_Linux2_Handle = pyNM_Linux2()
            self.initNM = pyNM_Linux2_Handle.initNM
        elif platform == 'darwin': # Mac OS X... eww. (Also OpenDarwin, if anyone uses that.)
            pyNM_Darwin_Handle = pyNM_Darwin()
            self.initNM = pyNM_Darwin_Handle.initNM
        print "[pyNM] Platform wrapper setup complete."
    def getVersion(self):
        return pyNM_VERSION 
class pyNM_Win32:
    # NOTE:
    # When we print about errors, we are using str(res), even though the actual error number
    # is stored in res[0]. Why? We don't know Microsoft's WMI API well enough yet to know
    # if the WMI API returns multiple error codes or not. We hope this isn't the case,
    # but we're printing the values to be sure. Please report any multiple value error codes
    # if you find one!
    def __init__(self):
        # Do some initialization!
        print "[pyNM] [pyNM_Win32] Initializing..."
        # Run this for threading
        pythoncom.CoInitialize()
        self.disableRefresh = False
        print "[pyNM] [pyNM_Win32] Welcome to pyNM for the win32 platform!"
    def _disableRefresh(self, disabled = True):
        if disabled == True:
            self.disableRefresh = True
        else:
            self.disableRefresh = False
            # Process queue
            print "[pyNM] [pyNM_Win32] Refreshing all NIC network configurations..."
            # DIRTY HACK - TODO: replace by using existing handles and then running refresh
            self.initNM()
            
    def _getWin32NetAdapConfig(self, nic_handle):
        # Kinda odd that PNPDeviceID isn't shared... but MACAddress is :P
        nacs = nic_handle.associators(wmi_result_class="Win32_NetworkAdapterConfiguration")
        if len(nacs) == 0:
            print "[pyNM] [pyNM_Win32._getWin32NetAdapConfig] Failed to find Win32_NetworkAdapterConfiguration equivalent to NIC handle"
            raise pyNMError("Failed to find Win32_NetworkAdapterConfiguration equivalent to NIC handle", pyNM_ERROR_SYSTEM)
        if len(nacs) > 1:
            print "[pyNM] [pyNM_Win32._getWin32NetAdapConfig] Failed to find exact Win32_NetworkAdapterConfiguration equivalent to NIC handle (got "+str(len(nacs))+" entries)"
            raise pyNMError("Failed to find exact Win32_NetworkAdapterConfiguration equivalent to NIC handle (got "+str(len(nacs))+" entries)", pyNM_ERROR_SYSTEM)
        # Everything's fine if there's no errors raised, so return the only entry in the array
        return nacs[0]
    def _refreshWin32NetAdapConfig(self, nicac_handle):
        # This refreshes the Win32_NetworkAdapterConfiguration class to reflect any changes
        # Very hacky - we're basically getting the associator Win32_NetworkAdapter, then getting 
        # the associator of that class to hop back to Win32_NetworkAdapterConfig.
        # There's probably a better way to do this, but...
        print "[pyNM] [pyNM_Win32._refreshWin32NetAdapConfig] Refreshing network configuration class..."
        if self.disableRefresh == True:
            print "[pyNM] [pyNM_Win32._refreshWin32NetAdapConfig] Refreshing disabled by client, queueing for later."
            return
        nacs = nicac_handle.associators(wmi_result_class="Win32_NetworkAdapter")
        if len(nacs) == 0:
            print "[pyNM] [pyNM_Win32._refreshWin32NetAdapConfig] Failed to find Win32_NetworkAdapterConfiguration equivalent to NIC handle"
            raise pyNMError("Failed to find Win32_NetworkAdapterConfiguration equivalent to NIC handle", pyNM_ERROR_SYSTEM)
        if len(nacs) > 1:
            print "[pyNM] [pyNM_Win32._refreshWin32NetAdapConfig] Failed to find exact Win32_NetworkAdapterConfiguration equivalent to NIC handle (got "+str(len(nacs))+" entries)"
            raise pyNMError("Failed to find exact Win32_NetworkAdapterConfiguration equivalent to NIC handle (got "+str(len(nacs))+" entries)", pyNM_ERROR_SYSTEM)
        
        # Everything's fine if there's no errors raised, so find the associator from the only entry in the array
        nacacs = nacs[0].associators(wmi_result_class="Win32_NetworkAdapterConfiguration")
        if len(nacacs) == 0:
            print "[pyNM] [pyNM_Win32._refreshWin32NetAdapConfig] Failed to find Win32_NetworkAdapterConfiguration equivalent to NIC handle"
            raise pyNMError("Failed to find Win32_NetworkAdapterConfiguration equivalent to NIC handle", pyNM_ERROR_SYSTEM)
        if len(nacacs) > 1:
            print "[pyNM] [pyNM_Win32._refreshWin32NetAdapConfig] Failed to find exact Win32_NetworkAdapterConfiguration equivalent to NIC handle (got "+str(len(nacs))+" entries)"
            raise pyNMError("Failed to find exact Win32_NetworkAdapterConfiguration equivalent to NIC handle (got "+str(len(nacs))+" entries)", pyNM_ERROR_SYSTEM)

        # Everything's fine if there's no errors raised, so return the only entry in the array
        return nacacs[0]
    def _getWirelessNICs(self):
        return self.nic_wlan_array
    def _getLANNICs(self):
        return self.nic_lan_array
    def initNM(self):
        print "[pyNM] [pyNM_Win32.initNM] Initializing pyNM for the win32 platform..."
        wmi_instance = wmi.WMI()
        self.wmi_instance = wmi_instance
        network_adapters = wmi_instance.Win32_NetworkAdapter()
        if len(network_adapters) == 0:
            raise pyNMError("No network adapters found! (Maybe you don't have enough permissions?)", pyNM_ERROR_SYSTEM)
        
        physical_adapters=[]
        try:
            for adapter in network_adapters:
                if (adapter.Manufacturer != 'Microsoft' and adapter.PNPDeviceID != None and len(adapter.PNPDeviceID) >= 4 and adapter.PNPDeviceID[:4] != 'ROOT'):
                    physical_adapters.append(adapter)
                    # NetConnectionID is unreliable, BUT we can use it as reinforcement. (or not :P)
                    # Best to use the Name
                    print "[pyNM] [pyNM_Win32.initNM] HW Network Adapter: name="+str(adapter.Name)+", type="+str(adapter.AdapterType)+", PNPDeviceID="+str(adapter.PNPDeviceID)+", NetConnectionID="+str(adapter.NetConnectionID)
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.initNM] Failed to filter network devices"
            traceback.print_exc()
            raise pyNMError("Failed to filter network devices", pyNM_ERROR_SYSTEM)
        
        # Now categorize into wireless and LAN (Ethernet) NICs
        wireless_adapters=[]
        lan_adapters=[]
        
        try:
            for adapter in physical_adapters:
                # Do a case-insensitive search!
                if (str(adapter.Name).lower().find('wireless') != -1) or (str(adapter.Name).lower().find('wifi') != -1) or (str(adapter.Name).lower().find('wi-fi') != -1):
                    print "[pyNM] [pyNM_Win32.initNM] Identified '"+str(adapter.Name)+"' as a wireless NIC"
                    wireless_adapters.append(adapter)
                elif (str(adapter.Name).lower().find('ethernet') != -1):
                    print "[pyNM] [pyNM_Win32.initNM] Identified '"+str(adapter.Name)+"' as a Ethernet NIC"
                    lan_adapters.append(adapter)
                else:
                    # Assume it's LAN
                    print "[pyNM] [pyNM_Win32.initNM] Identified '"+str(adapter.Name)+"' as a Ethernet NIC"
                    print "[pyNM] [pyNM_Win32.initNM]  ** WARNING: This may not be accurate."
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.initNM] Failed to categorize network devices"
            traceback.print_exc()
            raise pyNMError("Failed to filter categorize devices", pyNM_ERROR_SYSTEM)
        
        nic_array = []
        nic_wlan_array = []
        nic_lan_array = []
        
        try:
            for wireless_adapter in wireless_adapters:
                nic_handle = pyNM_NetworkDevice(str(wireless_adapter.Name), [wireless_adapter, self._getWin32NetAdapConfig(wireless_adapter)], wireless_adapter.PNPDeviceID, "wireless")
                nic_array.append(nic_handle)
                nic_wlan_array.append(nic_handle)
            for lan_adapter in lan_adapters:
                nic_handle = pyNM_NetworkDevice(str(lan_adapter.Name), [lan_adapter, self._getWin32NetAdapConfig(lan_adapter)], lan_adapter.PNPDeviceID, "ethernet")
                nic_array.append(nic_handle)
                nic_lan_array.append(nic_handle)
            self.nic_array = nic_array
            self.nic_wlan_array = nic_wlan_array
            self.nic_lan_array = nic_lan_array
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.initNM] Failed to create device array"
            traceback.print_exc()
            raise pyNMError("Failed to create device array", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
        
        print "[pyNM] [pyNM_Win32.initNM] Initilization complete!"
    def setWirelessSubnet(self, subnet):
        try:
            for NIC in self.nic_wlan_array:
                res = NIC.control[1].EnableStatic([unicode(NIC.control[1].IPAddress[0])], [unicode(subnet)])
                print "[pyNM] [pyNM_Win32.setWirelessSubnet] ** Result code: "+str(res)
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setWirelessSubnet] Failed to set wireless subnet (status code "+str(res)+")"
                    raise pyNMError("Failed to set wireless subnet (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setWirelessSubnet] Failed to set wireless subnet"
            traceback.print_exc()
            raise pyNMError("Failed to set wireless subnet", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setLANSubnet(self, subnet):
        try:
            for NIC in self.nic_lan_array:
                res = NIC.control[1].EnableStatic([unicode(NIC.control[1].IPAddress[0])], [unicode(subnet)])
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setLANSubnet] Failed to set LAN subnet (status code "+str(res)+")"
                    raise pyNMError("Failed to set LAN subnet (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setLANSubnet] Failed to set LAN subnet"
            traceback.print_exc()
            raise pyNMError("Failed to set LAN subnet", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setNICSubnet(self, nic, subnet):
        try:
            res = nic.control[1].EnableStatic([unicode(nic.control[1].IPAddress[0])], [unicode(subnet)])
            if res[0] != 0:
                print "[pyNM] [pyNM_Win32.setNICSubnet] Failed to set NIC subnet (status code "+str(res)+")"
                raise pyNMError("Failed to set NIC subnet (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
            nic.control[1] = self._refreshWin32NetAdapConfig(nic.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setNICSubnet] Failed to set NIC subnet"
            traceback.print_exc()
            raise pyNMError("Failed to set NIC subnet", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setWirelessStaticIP(self, staticip):
        try:
            for NIC in self.nic_wlan_array:
                #print "[pyNM] [pyNM_Win32.setWirelessStaticIP] DEBUG | Dev control contents: "+str(NIC.control[0])
                #print "[pyNM] [pyNM_Win32.setWirelessStaticIP] DEBUG | Dev control type: "+str(type(NIC.control[0]))
                #print "[pyNM] [pyNM_Win32.setWirelessStaticIP] DEBUG | Cfg control contents: "+str(NIC.control[1])
                #print "[pyNM] [pyNM_Win32.setWirelessStaticIP] DEBUG | Cfg control type: "+str(type(NIC.control[1]))
                # Windows is really stupid... they specify strings as args, but instead ask for string *arrays*
                # ARRRRRRRRRRRGGHHHHHHHH !@#$%^&*
                print "[pyNM] [pyNM_Win32.setWirelessStaticIP] Setting static IP to "+str([unicode(staticip)])+" and IPSubnet to "+str(NIC.control[1].IPSubnet)
                res = NIC.control[1].EnableStatic([unicode(staticip)], [unicode(NIC.control[1].IPSubnet[0])])
                print "[pyNM] [pyNM_Win32.setWirelessStaticIP] ** Result code: "+str(res)
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setNICWirelessIP] Failed to set wireless static IP (status code "+str(res)+")"
                    raise pyNMError("Failed to set wireless static IP (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setWirelessStaticIP] Failed to set wireless static IP"
            traceback.print_exc()
            raise pyNMError("Failed to set wireless static IP", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setLANStaticIP(self, staticip):
        try:
            for NIC in self.nic_lan_array:
                NIC.control[1].EnableStatic([unicode(staticip)], [unicode(NIC.control[1].IPSubnet[0])])
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setLANStaticIP] Failed to set LAN static IP (status code "+str(res)+")"
                    raise pyNMError("Failed to set LAN static IP (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setLANStaticIP] Failed to set LAN static IP"
            traceback.print_exc()
            raise pyNMError("Failed to set LAN static IP", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setNICStaticIP(self, nic, staticip):
        try:
            nic.control[1].EnableStatic([unicode(staticip)], [unicode(NIC.control[1].IPSubnet[0])])
            if res[0] != 0:
                print "[pyNM] [pyNM_Win32.setNICStaticIP] Failed to set NIC static IP (status code "+str(res)+")"
                raise pyNMError("Failed to set NIC static IP (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
            nic.control[1] = self._refreshWin32NetAdapConfig(nic.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setNICStaticIP] Failed to set NIC static IP"
            traceback.print_exc()
            raise pyNMError("Failed to set NIC static IP", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setWirelessStaticIPAndSubnet(self, staticip, subnet):
        try:
            for NIC in self.nic_wlan_array:
                res = NIC.control[1].EnableStatic([unicode(staticip)], [unicode(subnet)])
                print "[pyNM] [pyNM_Win32.setWirelessStaticIPAndSubnet] ** Result code: "+str(res)
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setWirelessStaticIPAndSubnet] Failed to set wireless static IP and subnet (status code "+str(res)+")"
                    raise pyNMError("Failed to set wireless static IP and subnet (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setWirelessStaticIPAndSubnet] Failed to set wireless static IP and subnet"
            traceback.print_exc()
            raise pyNMError("Failed to set wireless static IP and subnet", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setLANStaticIPAndSubnet(self, staticip, subnet):
        try:
            for NIC in self.nic_lan_array:
                res = NIC.control[1].EnableStatic([unicode(staticip)], [unicode(subnet)])
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setLANStaticIPAndSubnet] Failed to set LAN static IP and subnet (status code "+str(res)+")"
                    raise pyNMError("Failed to set LAN static IP and subnet (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setLANStaticIPAndSubnet] Failed to set LAN static IP and subnet"
            traceback.print_exc()
            raise pyNMError("Failed to set LAN static IP and subnet", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setNICStaticIPAndSubnet(self, nic, staticip, subnet):
        try:
            res = nic.control[1].EnableStatic([unicode(staticip)], [unicode(subnet)])
            if res[0] != 0:
                print "[pyNM] [pyNM_Win32.setNICStaticIPAndSubnet] Failed to set NIC static IP and subnet (status code "+str(res)+")"
                raise pyNMError("Failed to set NIC static IP and subnet (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
            nic.control[1] = self._refreshWin32NetAdapConfig(nic.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setNICStaticIPAndSubnet] Failed to set NIC static IP and subnet"
            traceback.print_exc()
            raise pyNMError("Failed to set NIC static IP and subnet", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setWirelessDHCP(self):
        try:
            for NIC in self.nic_wlan_array:
                res = NIC.control[1].EnableDHCP()
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setWirelessDHCP] Failed to set wireless DHCP (status code "+str(res)+")"
                    raise pyNMError("Failed to set wireless DHCP (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setWirelessDHCP] Failed to set wireless DHCP"
            traceback.print_exc()
            raise pyNMError("Failed to set wireless DHCP", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setLANDHCP(self):
        try:
            for NIC in self.nic_lan_array:
                res = NIC.control[1].EnableDHCP()
                if res[0] != 0:
                    print "[pyNM] [pyNM_Win32.setLANDHCP] Failed to set LAN DHCP (status code "+str(res)+")"
                    raise pyNMError("Failed to set LAN DHCP (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
                NIC.control[1] = self._refreshWin32NetAdapConfig(NIC.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setLANDHCP] Failed to set LAN DHCP"
            traceback.print_exc()
            raise pyNMError("Failed to set LAN DHCP", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def setNICDHCP(self, nic):
        try:
            res = nic.control[1].EnableDHCP()
            if res[0] != 0:
                print "[pyNM] [pyNM_Win32.setNICDHCP] Failed to set NIC DHCP (status code "+str(res)+")"
                raise pyNMError("Failed to set NIC DHCP (status code "+str(res)+")", pyNM_ERROR_SYSTEM, res[0])
            nic.control[1] = self._refreshWin32NetAdapConfig(nic.control[1])
        except pyNMError:
            raise
        except:
            import traceback
            print "[pyNM] [pyNM_Win32.setNICDHCP] Failed to set NIC DHCP"
            traceback.print_exc()
            raise pyNMError("Failed to set NIC DHCP", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def getNICs(self):
        return self.nic_array
        
class pyNM_Linux2:
    def __init__(self):
        # Nothing
        print "[pyNM] [pyNM_Linux2] Welcome to pyNM for the Linux (linux2) platform!"
        raise pyNMError("Linux2 platform not implemented.", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def initNM(self):
        print "[pyNM] [pyNM_Linux2.initNM] Initializing pyNM for the Linux (linux2) platform..."
        
class pyNM_Darwin:
    def __init__(self):
        # Nothing
        print "[pyNM] [pyNM_Darwin] Welcome to pyNM for the Mac (darwin) platform!"
        raise pyNMError("Darwin platform not implemented.", pyNM_ERROR_BUG, 0, "".join(traceback.format_exc()))
    def initNM(self):
        print "[pyNM] [pyNM_Darwin.initNM] Initializing pyNM for the Mac (darwin) platform..."

class pyNM_NetworkDevice:
    def __init__(self, name, control, sysaddr, type):
        # Set up class struct
        self.name = name
        self.control = control
        self.sysaddr = sysaddr
        self.type = type
        #print "[pyNM] pyNM_NetworkDevice type initialized with name="+str(name)+", control="+str(control)+", sysaddr="+str(sysaddr)+", type="+str(type)
    def getName(self):
        return self.name
    def getControlHandle(self):
        return self.control
    def getSysAddr(self):
        return self.sysaddr
    def getType(self):
        return self.type

if __name__ == "__main__":
    wmi_instance = wmi.WMI()
    
    network_adapters = wmi_instance.Win32_NetworkAdapter()
    index = 0
    if len(network_adapters) == 0:
        print "ERROR: No network adapters found! (Maybe you don't have enough permissions?)"
        sys.exit(1)
    '''for adapter in network_adapters:
        print "Network adapter "+str(index)+" type: "+str(adapter.AdapterType)
        print "Network adapter "+str(index)+" name: "+str(adapter.Name)
        print "Network adapter "+str(index)+" mfr: "+str(adapter.Manufacturer)
        index += 1'''

    physical_adapters=[]
    try:
        for adapter in network_adapters:
            if (adapter.Manufacturer != 'Microsoft' and adapter.PNPDeviceID != None and len(adapter.PNPDeviceID) >= 4 and adapter.PNPDeviceID[:4] != 'ROOT'):
                physical_adapters.append(adapter)
                # NetConnectionID is unreliable, BUT we can use it as reinforcement. (or not :P)
                # Best to use the Name
                print "HW Network Adapter: name="+str(adapter.Name)+", type="+str(adapter.AdapterType)+", PNPDeviceID="+str(adapter.PNPDeviceID)+", NetConnectionID="+str(adapter.NetConnectionID)
    except:
        import traceback
        print "Failed to filter network devices"
        traceback.print_exc()

    raw_input()
