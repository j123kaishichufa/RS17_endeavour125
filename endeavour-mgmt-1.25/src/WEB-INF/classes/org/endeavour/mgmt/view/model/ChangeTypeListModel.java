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
public class ChangeTypeListModel extends GridBoxModel {

	public ChangeTypeListModel() {
		super();
		super.rows = this.initializeChangeTypes();
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		ChangeType theChangeType = (ChangeType) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = theChangeType.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		ChangeType theStatus = (ChangeType) this.rows.get(aRowIndex);
		return theStatus.getId();
	}

	private List<ChangeType> initializeChangeTypes() {
		List<ChangeType> theChangeTypes = new ArrayList<ChangeType>();
		theChangeTypes.add(new ChangeType(0, IViewConstants.RB.getString("new_requirement.lbl"), "New Requirement"));
		theChangeTypes.add(new ChangeType(1, IViewConstants.RB.getString("requirement_change.lbl"), "Requirement Change"));
		return theChangeTypes;
	}

	public String getDescriptionByValue(String aValue) {
		String theDescription = null;
		for (ChangeType theChangeType : (List<ChangeType>) super.rows) {
			if (theChangeType.getValue().equals(aValue)) {
				theDescription = theChangeType.getDescription();
			}
		}
		return theDescription;
	}

	public String getValueById(Integer anId) {
		ChangeType theChangeType = (ChangeType) super.getRowObjectByIndex(anId);
		return theChangeType.getValue();
	}

	class ChangeType {
		private Integer id = null;
		private String description = null;
		private String value = null;

		public ChangeType(Integer anId, String aDescription, String aValue) {
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
