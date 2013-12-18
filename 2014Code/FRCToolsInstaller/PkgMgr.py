#!/usr/bin/env python
# Team 4067 FRC Tools Installer - easy installer for FRC Tools
# Copyright (C) 2013 River Hill HS Robotics Team (Albert H.)
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

# PackageManager class - initialized from GUI
# Package dictionary format:
#   self.Packages = { "package_name" : {
#                                           "url" : "http://someplaceamazing.com/dir/bla",
#                                           "md5" : "md5_checksum_goes_here",
#                                           "sha512" : "sha512_hash_goes_here",
#                                           "licenses" : {
#                                                           "company1" : "http://url/to/license.rtf",
#                                                           "company2" : "http://url/to/html_license.html"
#                                           },
#                                           "dependencies" : [ "dep1", "dep2" ],
#                                           "install_steps" : [ "extract file.iso", "run file/bla.exe", "copy file dest", "move file dest", "del file" ]
#                   }
DEFAULT_SERVER_URL = "http://riverhillrobotics.org/Resources/FRC2013/Software/"
DEFAULT_PACKAGE_LIST = [ "frc2013tools", "frc2013utilupdate", "frc2013dsupdate" ]
class PackageManagerException(Exception):
	def __init__(self, value):
		self.parameter = value
	def __str__(self):
		return repr(self.parameter)

class PackageManager():
	def __init__():
		self.ServerURL = ""
		self.FetchedPackageList = False
		self.Packages = {}
		self.SelectedPackages = DEFAULT_PACKAGE_LIST
	def fetchPackageList(self):
		if self.ServerURL = "":
			throw PackageManagerException("Tried to fetch the package list without setting a server URL")
			
	def processPackages(self):
		if not self.FetchedPackageList:
			self.fetchPackageList()
		for PACKAGE in self.SelectedPackages:
			if not PACKAGE in self.Packages:
				throw PackageManagerException("Could not find package '"+PACKAGE+"' in the available package list")
			