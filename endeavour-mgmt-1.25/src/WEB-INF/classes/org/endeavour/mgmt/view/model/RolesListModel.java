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

import java.util.ArrayList;
import java.util.List;

import org.endeavour.mgmt.view.IViewConstants;
import org.endeavour.mgmt.view.components.GridBoxModel;

@SuppressWarnings("unchecked")
public class RolesListModel extends GridBoxModel {

	public static final String DEVELOPER = "Developer";
	public static final String PROJECT_MANAGER = "Project Manager";
	public static final String TESTER = "Tester";
	public static final String DATABASE_ADMINISTRATOR = "Database Administrator";
	public static final String STAKEHOLDER = "Stakeholder";
	public static final String BUSINESS_ANALYST = "Business Analyst";
	public static final String ARCHITECT = "Architect";
	public static final String SYSTEM_ADMINISTRATOR = "Architect";

	public RolesListModel() {
		super();
		super.rows = this.initializePriorities();
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Role theRole = (Role) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theRole.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public String getValueByDescription(String aDescription) {
		String theValue = null;
		for (Role theRole : (List<Role>) super.rows) {
			if (theRole.getDescription().equals(aDescription)) {
				theValue = theRole.getValue();
			}
		}
		return theValue;
	}

	public String getDescriptionByValue(String aValue) {
		String theDescription = null;
		for (Role theRole : (List<Role>) super.rows) {
			if (theRole.getValue().equals(aValue)) {
				theDescription = theRole.getDescription();
			}
		}
		return theDescription;
	}

	public int getRowId(int aRowIndex) {
		Role theStatus = (Role) this.rows.get(aRowIndex);
		return theStatus.getId();
	}

	private List<Role> initializePriorities() {
		List<Role> theRole = new ArrayList<Role>();
		theRole.add(new Role(0, IViewConstants.RB.getString("developer.lbl"), DEVELOPER));
		theRole.add(new Role(1, IViewConstants.RB.getString("project_manager.lbl"), PROJECT_MANAGER));
		theRole.add(new Role(2, IViewConstants.RB.getString("tester.lbl"), TESTER));
		theRole.add(new Role(3, IViewConstants.RB.getString("database_administrator.lbl"), DATABASE_ADMINISTRATOR));
		theRole.add(new Role(4, IViewConstants.RB.getString("stakeholder.lbl"), STAKEHOLDER));
		theRole.add(new Role(5, IViewConstants.RB.getString("business_analyst.lbl"), BUSINESS_ANALYST));
		theRole.add(new Role(6, IViewConstants.RB.getString("architect.lbl"), ARCHITECT));
		theRole.add(new Role(7, IViewConstants.RB.getString("system_administrator.lbl"), SYSTEM_ADMINISTRATOR));
		return theRole;
	}

	class Role {
		private Integer id = null;
		private String description = null;
		private String value = null;

		public Role(Integer anId, String aDescription, String aValue) {
			this.id = anId;
			this.description = aDescription;
			this.value = aValue;
		}

		public Integer getId() {
			return this.id;
		}

		public String getDescription() {
			return this.description;
		}

		public String getValue() {
			return this.value;
		}
	}
}
