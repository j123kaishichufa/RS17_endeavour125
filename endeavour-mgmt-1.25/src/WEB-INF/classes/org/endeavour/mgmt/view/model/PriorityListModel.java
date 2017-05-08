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
public class PriorityListModel extends GridBoxModel {

	public static final String HIGH = "High";
	public static final String MEDIUM = "Medium";
	public static final String LOW = "Low";
	public static final String ALL = "All";

	public PriorityListModel(boolean aIncludeAll) {
		super();
		super.rows = this.initializePriorities(aIncludeAll);
	}

	public void initializeColumns() {
		super.columns = new String[] { "" };
	}

	public Object getValueAt(int aRowIndex, int aColumnIndex) {

		String theValue = null;
		Priority thePriority = (Priority) this.rows.get(aRowIndex);
		switch (aColumnIndex) {
		case 0:
			theValue = thePriority.getDescription();
			break;
		default:
			theValue = "";
		}
		return theValue;
	}

	public int getRowId(int aRowIndex) {
		Priority theStatus = (Priority) this.rows.get(aRowIndex);
		return theStatus.getId();
	}

	private List<Priority> initializePriorities(boolean aIncludeAll) {
		List<Priority> thePriorities = new ArrayList<Priority>();
		thePriorities.add(new Priority(0, IViewConstants.RB.getString("high.lbl"), HIGH));
		thePriorities.add(new Priority(1, IViewConstants.RB.getString("medium.lbl"), MEDIUM));
		thePriorities.add(new Priority(2, IViewConstants.RB.getString("low.lbl"), LOW));
		if (aIncludeAll) {
			thePriorities.add(new Priority(3, IViewConstants.RB.getString("all.lbl"), ALL));
		}
		return thePriorities;
	}

	public String getValueById(Integer anId) {
		Priority thePriority = (Priority) super.getRowObjectByIndex(anId);
		return thePriority.getValue();
	}

	public String getValueByDescription(String aDescription) {
		String theValue = null;
		for (Priority thePriority : (List<Priority>) super.rows) {
			if (thePriority.getDescription().equals(aDescription)) {
				theValue = thePriority.getValue();
			}
		}
		return theValue;
	}

	public String getDescriptionByValue(String aValue) {
		String theDescription = null;
		for (Priority thePriority : (List<Priority>) super.rows) {
			if (thePriority.getValue().equals(aValue)) {
				theDescription = thePriority.getDescription();
			}
		}
		return theDescription;
	}

	class Priority {
		private Integer id = null;
		private String description = null;
		private String value = null;

		public Priority(Integer anId, String aDescription, String aValue) {
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

	public String toString() {
		String thePriorityList = "";
		Priority thePriority = null;
		int theSize = super.rows.size();
		for (int i = 0; i < theSize; i++) {
			thePriority = (Priority) super.rows.get(i);
			thePriorityList = thePriorityList + thePriority.getValue() + ":String";
			if (i != theSize - 1) {
				thePriorityList = thePriorityList + ",";
			}
		}
		return "[" + thePriorityList + "]";
	}
}
