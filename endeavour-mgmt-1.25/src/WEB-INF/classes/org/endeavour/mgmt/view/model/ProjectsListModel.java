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

package org.endeavour.mgmt.view.model;

import java.text.SimpleDateFormat;
import java.util.List;

import org.endeavour.mgmt.model.Project;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class ProjectsListModel extends GridBoxModel {

	private StageStatusListModel statusListModel = null;

	public ProjectsListModel(List<Project> aProjects) {
		super(aProjects);
		this.statusListModel = new StageStatusListModel();
	}

	public void initializeColumns() {

		String theProjectName = IViewConstants.RB.getString("project_name.lbl");
		String theStartDate = IViewConstants.RB.getString("start_date.lbl");
		String theEndDate = IViewConstants.RB.getString("end_date.lbl");
		String theStatus = IViewConstants.RB.getString("status.lbl");

		super.columns = new String[] { theProjectName, theStartDate, theEndDate, theStatus };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Project theProject = (Project) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theProject.getName();
			break;
		case 1:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theProject.getStartDate());
			break;
		case 2:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK).format(theProject.getEndDate());
			break;
		case 3:
			theValue = this.statusListModel.getDescriptionByValue(theProject.getStatus());
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Project theProject = (Project) this.rows.get(aRowIndex);
		return theProject.getId();
	}
}
