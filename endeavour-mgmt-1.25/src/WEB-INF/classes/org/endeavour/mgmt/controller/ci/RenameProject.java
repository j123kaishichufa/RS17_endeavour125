/***************************************************************************************
 *Endeavour Agile ALM
 *Copyright (C) 2009  Ezequiel Cuellar
 *
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ***************************************************************************************/
package org.endeavour.mgmt.controller.ci;

public class RenameProject extends CIMaintenance implements Runnable {

	private String oldProjectName = null;
	private String newProjectName = null;

	public RenameProject(String aOldProjectName, String aNewProjectName) {
		this.oldProjectName = aOldProjectName;
		this.newProjectName = aNewProjectName;
	}

	public void run() {
		super.renameProject(oldProjectName, newProjectName);
	}
}
