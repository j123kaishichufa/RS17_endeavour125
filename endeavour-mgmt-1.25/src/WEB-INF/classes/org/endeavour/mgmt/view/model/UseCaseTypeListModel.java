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
public class UseCaseTypeListModel extends GridBoxModel {

	public UseCaseTypeListModel() {
		super();
		super.rows = this.initializeTypes();
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		UseCaseType theUseCaseType = (UseCaseType) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theUseCaseType.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public String getDescriptionByType(String aType) {
		String theDescription = null;
		for (UseCaseType theUseCaseType : (List<UseCaseType>) super.rows) {
			if (theUseCaseType.getType().equals(aType)) {
				theDescription = theUseCaseType.getDescription();
			}
		}
		return theDescription;
	}

	public String getTypeByDescription(String aDescription) {
		String theType = null;
		for (UseCaseType theUseCaseType : (List<UseCaseType>) super.rows) {
			if (theUseCaseType.getDescription().equals(aDescription)) {
				theType = theUseCaseType.getType();
			}
		}
		return theType;
	}

	public int getRowId(int aRowIndex) {
		UseCaseType theType = (UseCaseType) this.rows.get(aRowIndex);
		return theType.getId();
	}

	private List<UseCaseType> initializeTypes() {
		List<UseCaseType> theStatuses = new ArrayList<UseCaseType>();
		theStatuses.add(new UseCaseType(0, IViewConstants.RB.getString("business_use_case.lbl"), "Business Use Case"));
		theStatuses.add(new UseCaseType(1, IViewConstants.RB.getString("system_use_case.lbl"), "System Use Case"));
		return theStatuses;
	}

	class UseCaseType {
		private Integer id = null;
		private String description = null;
		private String type = null;

		public UseCaseType(Integer anId, String aDescription, String aType) {
			this.id = anId;
			this.description = aDescription;
			this.type = aType;
		}

		public Integer getId() {
			return this.id;
		}

		public String getDescription() {
			return this.description;
		}

		public String getType() {
			return this.type;
		}
	}
}
