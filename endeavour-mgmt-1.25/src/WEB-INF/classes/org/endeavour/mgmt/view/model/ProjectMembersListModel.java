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

import java.util.List;

import org.endeavour.mgmt.model.ProjectMember;
import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

@SuppressWarnings("unchecked")
public class ProjectMembersListModel extends GridBoxModel {

	public static final String ALL = "All";
	private RolesListModel rolesListModel = null;

	public ProjectMembersListModel() {
		super();
		this.rolesListModel = new RolesListModel();
	}

	public ProjectMembersListModel(List<ProjectMember> aProjectMembers, boolean aIncludeAll) {
		super(aProjectMembers);
		this.rolesListModel = new RolesListModel();
		if (aIncludeAll) {
			ProjectMember theAllMember = new ProjectMember();
			theAllMember.setId(-1);
			theAllMember.setUserId(ALL);
			super.rows.add(theAllMember);
		}
	}

	public void initializeColumns() {

		String theUserId = IViewConstants.RB.getString("user_id.lbl");
		String theName = IViewConstants.RB.getString("name.lbl");
		String theRole = IViewConstants.RB.getString("role.lbl");

		super.columns = new String[] { theUserId, theName, theRole };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		ProjectMember theProjectMember = (ProjectMember) super.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theProjectMember.getUserId();
			break;
		case 1:
			theValue = theProjectMember.getFullName();
			break;
		case 2:
			theValue = this.rolesListModel.getDescriptionByValue(theProjectMember.getRole());
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		ProjectMember theProjectMember = (ProjectMember) super.rows.get(aRowIndex);
		return theProjectMember.getId();
	}
}
