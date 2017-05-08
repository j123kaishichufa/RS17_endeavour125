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

import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

public class ProjectMemberStatusListModel extends GridBoxModel {

	public ProjectMemberStatusListModel() {
		super();
	}

	public ProjectMemberStatusListModel(List<ProjectMember> aProjectMember) {
		super(aProjectMember);
	}

	public void initializeColumns() {
		String theName = IViewConstants.RB.getString("name.lbl");
		String theStatus = IViewConstants.RB.getString("status.lbl");
		String theUpdatedOn = IViewConstants.RB.getString("updated_on.lbl");

		super.columns = new String[] { theName, theStatus, theUpdatedOn };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		ProjectMember theProjectMember = (ProjectMember) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theProjectMember.getFullName();
			break;
		case 1:
			theValue = theProjectMember.getStatus();
			break;
		case 2:
			theValue = new SimpleDateFormat(IViewConstants.DATE_MASK + " hh:mm aaa").format(theProjectMember.getStatusDate());
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		ProjectMember theProjectMember = (ProjectMember) this.rows.get(aRowIndex);
		return theProjectMember.getId();
	}
}
